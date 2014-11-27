package ru.cscenter.practice.recsys.database;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.cscenter.practice.recsys.Language;
import ru.cscenter.practice.recsys.User;

import java.sql.Types;
import java.util.ArrayList;


public class UserDao implements IAirBnbObjectDao<User> {
    private final JdbcTemplate dbAccess;
    private LanguageDao languageDao;

    public UserDao(JdbcTemplate jdbcTemplate) {
        dbAccess = jdbcTemplate;
    }

    public void setLanguageDao(LanguageDao languageDao) {
        this.languageDao = languageDao;
    }

    public int put(final User user) {

        int countRowsAffected = putLanguagesOfUser(user);

        final String query = "insert into users value(?,?)";
        Object[] params = new Object[]{user.getId(), user.getCountReviewsFromHosts()};
        int[] types = new int[]{Types.INTEGER, Types.INTEGER};
        return countRowsAffected + dbAccess.update(query, params, types);
    }

    private int putLanguagesOfUser(final User user) {

        int countRowsAffected = 0;

        for (Language currentLanguage : user.getLanguages()) {
            int currentLanguageId = languageDao.getId(currentLanguage);
            if (currentLanguageId == -1) {
                languageDao.put(currentLanguage);
                currentLanguageId = languageDao.getId(currentLanguage);
            }
            final String query = "insert into languagesOfUser value(?,?)";
            Object[] params = new Object[]{user.getId(), currentLanguageId};
            int[] types = new int[]{Types.INTEGER, Types.INTEGER};
            countRowsAffected += dbAccess.update(query, params, types);
        }

        return countRowsAffected;
    }

    public int putVisitedFlat(int flatId, Integer userId) {
        final String query = "insert into visitedflats value(?,?)";
        Object[] params = new Object[]{userId, flatId};
        int[] types = new int[]{Types.INTEGER, Types.INTEGER};
        return dbAccess.update(query, params, types);
    }

    public boolean contains(int id) {
        final String query = "select count(*) from users where id = ?";
        Object[] params = new Object[]{id};
        return  dbAccess.queryForObject(query, params, Integer.class) == 1;
    }

    public User get(int id) {
        return new User(1, new ArrayList<Language>(), 1);
    }

}
