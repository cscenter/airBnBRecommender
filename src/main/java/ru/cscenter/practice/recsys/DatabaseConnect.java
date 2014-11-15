package ru.cscenter.practice.recsys;


import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.sql.*;

class DatabaseConnect implements AutoCloseable {

    private static final String DB_URL = "jdbc:mysql://localhost/recommendersystemdb";
    private static final String USER = "hostuser";
    private static final String PASS = "systemuser";

    private Connection connection;

    private final Logger logger = Logger.getLogger(DatabaseConnect.class);

    public DatabaseConnect() throws SQLException {

        try {
            this.connection = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new SQLException(e.getMessage());
        }
    }


    public boolean putFlatIntoDatabase(final Flat flat) {

        final StringBuilder builder = new StringBuilder("insert into flats value(");

        for (int i = 0; i < flat.getClass().getDeclaredFields().length; ++i)
            builder.append("?,");
        builder.deleteCharAt(builder.length() - 1);
        builder.append(")");

        boolean result = false;

        try (PreparedStatement request = connection.prepareStatement(builder.toString())) {

            Field[] fieldsOfFlat = flat.getClass().getDeclaredFields();

            for (int i = 0; i < fieldsOfFlat.length; ++i) {
                try {
                    fieldsOfFlat[i].setAccessible(true);

                    Object valueOfField = fieldsOfFlat[i].get(flat);
                    if (valueOfField == null)
                        valueOfField = "null";

                    switch (valueOfField.toString()) {
                        case "true":
                            request.setObject(i + 1, 1);
                            break;
                        case "false":
                            request.setObject(i + 1, 0);
                            break;
                        default:
                            request.setObject(i + 1, valueOfField.toString());
                            break;
                    }

                } catch (IllegalAccessException e) {
                    e.printStackTrace();

                }
            }

            result = request.execute();

        } catch (SQLException e) {
            logger.error(e.getMessage() + ", flat id" + flat.getId());

        }
        return result;
    }

    public boolean putUserIntoDatabase(final User user) {

        boolean result = putLanguagesOfUser(user);

        if (!result)
            return false;

        try (PreparedStatement request = connection.prepareStatement("insert into users value(?,?)")) {

            request.setObject(1, user.getId());
            request.setObject(2, user.getCountReviewsFromHosts());
            result = request.execute();

        } catch (SQLException e) {
            logger.debug(e.getMessage() + ", user id " + user.getId());
        }

        return result;
    }

    public boolean putVisitedFlat(final int userId, final int flatId) {

        boolean result = false;

        try (PreparedStatement request = connection.prepareStatement("insert into visitedflats value(?,?)")) {
            request.setInt(1, userId);
            request.setInt(2, flatId);
            result = request.execute();
        } catch (SQLException e) {
            logger.debug(e.getMessage() + " userId: " + userId + " flatId: " + flatId);
        }
        return result;
    }

    private boolean putLanguagesOfUser(final User user) {

        int result = 0;

        try (PreparedStatement request = connection.prepareStatement("insert into languagesOfUser value(?,?)")) {
            request.setInt(1, user.getId());
            for (Language language : user.getLanguages()) {

                int languageId = getLanguageId(language.getLanguage());
                if (languageId == -1) continue;

                request.setInt(2, languageId);
                result += request.execute() ? 1 : 0;
            }
        } catch (SQLException e) {
            logger.debug(e.getMessage() + " userId: " + user.getId());
        }

        return result == user.getLanguages().size();
    }

    private int getLanguageId(final String language) {

        try (PreparedStatement request = connection.prepareStatement("select id from language where value = ?")) {
            request.setString(1, language);
            ResultSet result = request.executeQuery();

            if (!result.next())
                putLanguage(language);
            else
                return result.getInt("id");

            result = request.executeQuery();
            return result.next() ? result.getInt("id") : -1;
        } catch (SQLException e) {
            logger.debug(e.getMessage() + language);
        }

        return -1;
    }

    private boolean putLanguage(final String language) {

        try (PreparedStatement request = connection.prepareStatement("insert into language(value) value (?)")) {
            request.setString(1, language);
            return request.execute();
        } catch (SQLException e) {
            logger.debug(e.getMessage() + language);
        }
        return false;
    }

    private boolean contains(final String table, final int id) {

        boolean result = false;

        try (PreparedStatement request = connection.prepareStatement("select id from " + table + " where id = ?")) {
            request.setInt(1, id);
            ResultSet resultSet = request.executeQuery();
            result = resultSet.next();
        } catch (SQLException e) {
            logger.debug(e.getMessage() + " " + table + " Id: " + id);
        }
        return result;
    }

    public boolean containsFlat(final int id) {
        return contains("flats", id);
    }

    public boolean containsUser(final int id) {
        return contains("users", id);
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }
}
