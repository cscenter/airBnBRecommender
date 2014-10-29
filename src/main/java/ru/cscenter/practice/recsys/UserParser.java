package ru.cscenter.practice.recsys;

import org.apache.log4j.Logger;
import org.htmlcleaner.TagNode;
import ru.cscenter.practice.recsys.Enums.Language;
import ru.cscenter.practice.recsys.Exceptions.NoAreasException;
import ru.cscenter.practice.recsys.Exceptions.TooManyAreasException;

import java.util.ArrayList;


public class UserParser extends Parser {
    private final String ID_EXPRESSION = "//div[@class='user-profile-symbol loading']/@data-user-id";
    private final String LANGUAGE_EXPRESSION = "//div[@class='panel row-space-4']/dl/dd";

    private final String USER_IDS_EXPRESSION = "//div[@class='reviews_section as_guest row-space-top-3']/" +
            "div[@class='reviews']/div[@class = 'row']/div[@class = 'col-2']/div[@class = 'pull-right']/a/@href";

    private final String FLAT_IDS_EXPRESSION = "//div[@class = 'listings row-space-2 row-space-top-4']/" +
            "ul[@class = 'hostings-list list-layout']/li[@class='row-space-2']/a/@href";

    private final String COUNT_REVIEWS_FROM_HOST = "//div[@id='reviews']/h2/small";

    private final Logger logger = Logger.getLogger(UserParser.class.getName());


    public UserParser(TagNode htmlPage) {
        super(htmlPage);
    }

    public User parse() {
        return new User(getId(), getLanguages(), getCountReviewsFromHost());
    }

    private int getId() {

        String id = null;
        try {
            id = getFeature(ID_EXPRESSION);
        } catch (TooManyAreasException | NoAreasException e) {
            logger.debug(e.getMessage() + " user id");
        }


        return getNumber(id);
    }

    private Language[] getLanguages() {
        final String[] pretendersToBeLanguage = getFeatures(LANGUAGE_EXPRESSION);
        ArrayList<Language> languages = new ArrayList<>();

        for (String currentPretender : pretendersToBeLanguage) {
            Language currentLanguage = Language.getLanguage(currentPretender);
            if (currentLanguage != null)
                languages.add(currentLanguage);
        }

        return languages.toArray(new Language[languages.size()]);
    }

    private int getCountReviewsFromHost() {
        String review = null;

        try {
            review = getFeature(COUNT_REVIEWS_FROM_HOST);
        } catch (TooManyAreasException e) {
            logger.debug(e.getMessage() + " countReviewsfromHost");
        } catch (NoAreasException e) {
            // ignore, because exists user without riviews from host
        }

        return getNumber(review);
    }

    public ArrayList<Integer> getUserHostIdsFromComments() {
        return getIds(USER_IDS_EXPRESSION);
    }

    public ArrayList<Integer> getFlatIds() {
        return getIds(FLAT_IDS_EXPRESSION);
    }
}
