package ru.cscenter.practice.recsys.database;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.cscenter.practice.recsys.Language;

import java.sql.Types;

public class LanguageDao implements IAirBnbObjectDao<Language> {

    private final JdbcTemplate dbAccess;

    public LanguageDao(JdbcTemplate template) {
        dbAccess = template;
    }

    public int put(final Language language) {
        final String query = "insert into language(value) values (?)";
        Object[] params = new Object[]{language.getLanguage()};
        int[] types = new int[]{Types.VARCHAR};
        return dbAccess.update(query, params, types);
    }

    public Language get(int id) {
        final String query = "select value from language where id = ?";
        Object[] params = new Object[]{id};
        int[] types = new int[]{Types.INTEGER};
        SqlRowSet result = dbAccess.queryForRowSet(query, params, types);
        return result.next() ? Language.getInstance(result.getString("value")) : null;
    }

    public int getId(final Language language) {
        final String query = "select id from language where value = ?";
        Object[] params = new Object[]{language.getLanguage()};
        int[] types = new int[]{Types.VARCHAR};
        SqlRowSet result = dbAccess.queryForRowSet(query, params, types);
        return result.next() ? result.getInt("id") : -1;
    }
}