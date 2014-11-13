package ru.cscenter.practice.recsys;

import org.apache.log4j.Logger;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class WebSpider {
    private static final String FLAT_ADDRESS = "https://www.airbnb.com/rooms/";
    private static final String USER_ADDRESS = "https://www.airbnb.com/users/show/";
    private static final String FLATS_OF_USER_EXPRESSION = "https://www.airbnb.com/s?host_id=";
    private final Logger logger = Logger.getLogger(WebSpider.class.getName());
    private final WebDriver webDriver;

    public WebSpider(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void DiscoverHostUsersThenFlats(int globalHostUserId, final int quantityFlats, final int quantityUsers)
            throws IOException {
        if (globalHostUserId <= 0 || quantityFlats < 0 || quantityUsers < 0) {
            throw new IllegalArgumentException("parameters have illegal values");
        }

        final LinkedList<Pair<Integer, Integer>> usersQueue = new LinkedList<>();

        final FlatParser flatParser = new FlatParser();
        final UserParser userParser = new UserParser();
        final FlatPageParser flatPageParser = new FlatPageParser();
        List<Integer> usersFromComments;
        List<Integer> listiningsOfHostUser;
        List<Integer> usersFromFlatComments;

        int countFlats = 0, countUsers = 0;
        boolean hasFlats;

        try (DatabaseConnect connection = new DatabaseConnect()) {
            while (countFlats < quantityFlats || countUsers < quantityUsers) {
                if (usersQueue.size() == 0) {
                    usersQueue.push(Pair.of(globalHostUserId++, -1));
                }

                final Pair<Integer, Integer> currentHostUser = usersQueue.pop();

                if (currentHostUser.snd() != -1) {
                    connection.putVisitedFlat(currentHostUser.fst(), currentHostUser.snd());
                }

                if (connection.containsUser(currentHostUser.fst())) {
                    continue;
                }

                logger.debug("processing user " + currentHostUser.fst);

                webDriver.get(USER_ADDRESS + currentHostUser.fst());
                final User newUser = userParser.parse(webDriver);
                usersFromComments = UserParser.getUserHostIdsFromComments(webDriver);
                hasFlats = UserParser.hasFlats(webDriver);


                if ((newUser.getId() == 0) ||
                        ((usersFromComments.size() == 0) && !hasFlats && (currentHostUser.snd == -1))) {
                    continue;
                }

                connection.putUserIntoDatabase(newUser);
                ++countUsers;
                usersQueue.addAll(makeArrayOfPair(usersFromComments, -1));



                if(hasFlats) {
                    webDriver.get(FLATS_OF_USER_EXPRESSION + currentHostUser.fst);
                    listiningsOfHostUser = flatPageParser.parse(webDriver);

                    for (Integer currentFlatId : listiningsOfHostUser) {
                        if (connection.containsFlat(currentFlatId)) {
                            continue;
                        }

                        logger.debug("processing flat " + currentFlatId);

                        webDriver.get(FLAT_ADDRESS + currentFlatId);
                        final Flat flat = flatParser.parse(webDriver);

                        if (flat.getId() == 0) {
                            continue;
                        }

                        connection.putFlatIntoDatabase(flat);
                        ++countFlats;

                        usersFromFlatComments = flatParser.getUserIdsFromComments(webDriver);
                        usersQueue.addAll(makeArrayOfPair(usersFromFlatComments, currentFlatId));
                    }
                }
            }
        } catch (SQLException e) {
            logger.debug(e.getMessage());
        }

        logger.debug("users : " + countUsers + " flats: " + countFlats);

    }

    private List<Pair<Integer, Integer>> makeArrayOfPair(final List<Integer> arrayOfUsers, final Integer flatId) {
        final List<Pair<Integer, Integer>> result = new ArrayList<>();

        for (Integer currentUserIdFromComment : arrayOfUsers)
            result.add(Pair.of(currentUserIdFromComment, flatId));
        return result;
    }

    private static class Pair<A, B> {

        private final A fst;
        private final B snd;

        private Pair(A a, B b) {
            this.fst = a;
            this.snd = b;
        }


        public A fst() {
            return fst;
        }

        public B snd() {
            return snd;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Pair pair = (Pair) o;

            if (fst != null ? !fst.equals(pair.fst) : pair.fst != null) return false;
            return !(snd != null ? !snd.equals(pair.snd) : pair.snd != null);

        }

        @Override
        public int hashCode() {
            int result = fst != null ? fst.hashCode() : 0;
            result = 31 * result + (snd != null ? snd.hashCode() : 0);
            return result;
        }

        public static <A, B> Pair<A, B> of(A a, B b) {
            return new Pair(a, b);
        }

    }

}
