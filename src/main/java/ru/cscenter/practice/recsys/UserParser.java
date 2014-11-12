package ru.cscenter.practice.recsys;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import ru.cscenter.practice.recsys.exceptions.NoAreasException;
import ru.cscenter.practice.recsys.exceptions.TooManyAreasException;

import java.util.ArrayList;
import java.util.List;


public class UserParser extends Parser {
    private static final String ID_EXPRESSION = "//div[@class='user-profile-symbol loading']";
    private static final String LANGUAGE_EXPRESSION = "//div[@class='panel row-space-4']//dl/*";
    private static final String USER_IDS_EXPRESSION = "//div[@class='reviews_section as_guest row-space-top-3']/" +
            "div[@class='reviews']/div[@class = 'row']/div[@class = 'col-2']/div[@class = 'pull-right']/a";

    private static final String FLAT_IDS_EXPRESSION = "//div[@class = 'listings row-space-2 row-space-top-4']/" +
            "ul[@class = 'hostings-list list-layout']/li[@class='row-space-2']/a";

    private static final String COUNT_REVIEWS_FROM_HOST = "//div[@id='reviews']/h2/small";

    private static final Logger logger = Logger.getLogger(UserParser.class.getName());

    public User parse(final WebDriver htmlPage) {
        if (htmlPage == null) {
            throw new IllegalArgumentException("html page can't be null");
        }
        return new User(getId(htmlPage), getLanguages(htmlPage), getCountReviewsFromHost(htmlPage));
    }

    public  static int getId(final WebDriver htmlPage) {

        String id = null;
        try {
            id = getFeature(htmlPage, ID_EXPRESSION, "@data-user-id");
        } catch (TooManyAreasException | NoAreasException e) {
            logger.debug(e.getMessage() + " user id");
        }


        return getNumber(id);
    }

    public static Language[] getLanguages(final WebDriver htmlPage) {
        final String[] pretendersToBeLanguage = getFeatures(htmlPage, LANGUAGE_EXPRESSION);
        final ArrayList<Language> resultLanguages = new ArrayList<>();

        int numberOfStringWithLanguages = -1;

        for (int i = 0; i < pretendersToBeLanguage.length; ++i) {
            if(pretendersToBeLanguage[i].contains("Languages")) {
                numberOfStringWithLanguages = i + 1;
                break;
            }
        }

        if(numberOfStringWithLanguages != -1) {

            final String[] languages = pretendersToBeLanguage[numberOfStringWithLanguages].split(",");
            for(String currLanguage : languages)
                resultLanguages.add(Language.getInstance(removeSkipSymbols(currLanguage)));
        }

        return resultLanguages.toArray(new Language[resultLanguages.size()]);
    }

    public static int getCountReviewsFromHost(final WebDriver htmlPage) {
        String review = null;

        try {
            review = getFeature(htmlPage, COUNT_REVIEWS_FROM_HOST);
        } catch (TooManyAreasException e) {
            logger.debug(e.getMessage() + " countReviewsfromHost");
        } catch (NoAreasException e) {
            // ignore, because exist users without riviews from host
        }

        return getNumber(review);
    }

    public static List<Integer> getUserHostIdsFromComments(final WebDriver htmlPage) {
        return getIds(htmlPage, USER_IDS_EXPRESSION, "@href");
    }

    public static boolean hasFlats(final WebDriver htmlPage) {
        return getIds(htmlPage, FLAT_IDS_EXPRESSION, "@href").size() > 0;
    }
}
