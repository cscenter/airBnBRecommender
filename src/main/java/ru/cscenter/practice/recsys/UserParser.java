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

    public UserParser(WebDriver htmlPage) {
        super(htmlPage);
    }

    public User parse() {
        return new User(getId(), getLanguages(), getCountReviewsFromHost());
    }

    public int getId() {

        String id = null;
        try {
            id = getFeature(ID_EXPRESSION, "data-user-id");
            logger.debug("got user id: " + id);
        } catch (TooManyAreasException | NoAreasException e) {
            logger.debug(e.getMessage() + " user id");
        }


        return getNumber(id);
    }

    public List<Language> getLanguages() {
        final List<String> pretendersToBeLanguage = getFeatures(LANGUAGE_EXPRESSION);
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

        logger.debug("got languages: " + resultLanguages.toString());

        return resultLanguages;
    }

    public int getCountReviewsFromHost() {
        String review = null;

        try {
            review = getFeature(COUNT_REVIEWS_FROM_HOST_EXPRESSION);
        } catch (TooManyAreasException e) {
            logger.debug(e.getMessage() + " countReviewsfromHost");
        } catch (NoAreasException e) {
            // ignore, because exist users without reviews from host
        }

        logger.debug("got count reviews: " + review);

        return getNumber(review);
    }

    public List<Integer> getUserHostIdsFromComments() {

        final ArrayList<Integer> result = new ArrayList<>();

        List<String> userIds;
        while (clickAndWait(LOAD_MORE_COMMENTS)) ;

        userIds = getFeatures(USERHOST_IDS_EXPRESSION, "href");
        for (String currentUserId : userIds)
            result.add(getNumber(currentUserId));

        logger.debug("got userhostidsfromcomment: " + result.size() + (result.size() > 0 ? " [" + result.get(0) + ", ...]" : ""));

        return result;

    }

    public boolean hasFlats() {
        boolean result =  getFeatures(FLAT_IDS_EXPRESSION, "href").size() > 0;
        logger.debug("got hasFlatvalue: " + result);
        return result;
    }
}
