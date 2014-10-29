package ru.cscenter.practice.recsys;


import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.sql.*;

public class DatabaseConnect implements AutoCloseable {

    private static final String DB_URL = "jdbc:mysql://localhost/recommendersystemdb";
    private static final String USER = "hostuser";
    private static final String PASS = "systemuser";

    private Connection connection;

    private Logger logger = Logger.getLogger(DatabaseConnect.class);

    public DatabaseConnect() throws SQLException {

        try {
            this.connection = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new SQLException(e.getMessage());
        }
    }


    public boolean putFlatIntoDatabase(Flat flat) {

        final StringBuilder builder = new StringBuilder("insert into flats value(");

        for (int i = 0; i < flat.getClass().getDeclaredFields().length; ++i)
            builder.append("?,");
        builder.deleteCharAt(builder.length() - 1);
        builder.append(")");

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

            return request.execute();

        } catch (SQLException e) {
            logger.error(e.getMessage() + ", flat id" + flat.getId());

        }
        return false;
    }

    public boolean putUserIntoDatabase(User user) {
        try (PreparedStatement request = connection.prepareStatement("insert into users value(?,?)")) {

            request.setObject(1, user.getId());
            request.setObject(2, user.getCountReviewsFromHosts());
            return request.execute();

        } catch (SQLException e) {
            logger.debug(e.getMessage() + ", user id " + user.getId());
        }

        return false;
    }

    public boolean putVisitedFlat(int userId, int flatId) {
        try (PreparedStatement request = connection.prepareStatement("insert into visitedflats value(?,?)")) {
            request.setInt(1, userId);
            request.setInt(2, flatId);
            return request.execute();
        } catch (SQLException e) {
            logger.debug(e.getMessage() + " userId: " + userId + " flatId: " + flatId);
        }
        return false;
    }

    private boolean contains(String table, int id) {
        try (PreparedStatement request = connection.prepareStatement("select id from " + table + " where id = ?")) {
            request.setInt(1, id);
            ResultSet result = request.executeQuery();
            return result.next();
        } catch (SQLException e) {
            logger.debug(e.getMessage() + " " + table + " Id: " + id);
        }
        return false;
    }

    public boolean containsFlat(int id) {
        return contains("flats", id);
    }

    public boolean containsUser(int id) {
        return contains("users", id);
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }
}
