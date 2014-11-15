package ru.cscenter.practice.recsys;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import ru.cscenter.practice.recsys.exceptions.NoAreasException;
import ru.cscenter.practice.recsys.exceptions.TooManyAreasException;

import java.util.ArrayList;
import java.util.List;


public class UserParser extends Parser {
    private static final String ID_EXPRESSION = "//div[@class='user-profile-symbol']";
    private static final String LANGUAGE_EXPRESSION = "//div[@class='panel row-space-4']//dl/*";
    private static final String USERHOST_IDS_EXPRESSION = "//div[@class='reviews_section as_guest row-space-top-3']/" +
            "div[@class='reviews']/div[@class = 'row']/div[@class = 'col-2']/div[@class = 'pull-right']/a";

    private static final String FLAT_IDS_EXPRESSION = "//div[@class = 'listings row-space-2 row-space-top-4']/" +
            "ul[@class = 'hostings-list list-layout']/li[@class='row-space-2']/a";

    private static final String COUNT_REVIEWS_FROM_HOST_EXPRESSION = "//div[@id = 'reviews']/h2/small";
    private static final String LOAD_MORE_COMMENTS =
            "//div[@class = 'reviews_section as_guest row-space-top-3']//a[@class = 'load_more']";

    private static final Logger logger = Logger.getLogger(UserParser.class.getName());

    public User parse(final WebDriver htmlPage) {
        if (htmlPage == null) {
            throw new IllegalArgumentException("html page can't be null");
        }
        return new User(getId(htmlPage), getLanguages(htmlPage), getCountReviewsFromHost(htmlPage));
    }

    public static int getId(final WebDriver htmlPage) {

        String id = null;
        try {
            id = getFeature(htmlPage, ID_EXPRESSION, "data-user-id");
        } catch (TooManyAreasException | NoAreasException e) {
            logger.debug(e.getMessage() + " user id");
        }


        return getNumber(id);
    }

    public static List<Language> getLanguages(final WebDriver htmlPage) {
        final List<String> pretendersToBeLanguage = getFeatures(htmlPage, LANGUAGE_EXPRESSION);
        final ArrayList<Language> resultLanguages = new ArrayList<>();

        int numberOfStringWithLanguages = -1;

        for (int i = 0; i < pretendersToBeLanguage.size(); ++i) {
            if (pretendersToBeLanguage.get(i).contains("Languages")) {
                numberOfStringWithLanguages = i + 1;
                break;
            }
        }

        if (numberOfStringWithLanguages != -1) {

            final String[] languages = pretendersToBeLanguage.get(numberOfStringWithLanguages).split(",");
            for (String currLanguage : languages)
                resultLanguages.add(Language.getInstance(removeSkipSymbols(currLanguage)));
        }

        return resultLanguages;
    }

    public static int getCountReviewsFromHost(final WebDriver htmlPage) {
        String review = null;

        try {
            review = getFeature(htmlPage, COUNT_REVIEWS_FROM_HOST_EXPRESSION);
        } catch (TooManyAreasException e) {
            logger.debug(e.getMessage() + " countReviewsfromHost");
        } catch (NoAreasException e) {
            // ignore, because exist users without reviews from host
        }

        return getNumber(review);
    }

    public static List<Integer> getUserHostIdsFromComments(final WebDriver htmlPage) {

        final ArrayList<Integer> result = new ArrayList<>();

        List<String> userIds;
        while (Parser.clickAndWait(htmlPage, LOAD_MORE_COMMENTS)) ;

        userIds = getFeatures(htmlPage, USERHOST_IDS_EXPRESSION, "href");
        for (String currentUserId : userIds)
            result.add(getNumber(currentUserId));

        return result;

    }

    public static boolean hasFlats(final WebDriver htmlPage) {
        return getFeatures(htmlPage, FLAT_IDS_EXPRESSION, "href").size() > 0;
    }
}
