package ru.cscenter.practice.recsys;

import org.apache.log4j.Logger;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class WebSpider {
    private static final String FLAT_ADDRESS = "https://www.airbnb.com/rooms/";
    private static final String USER_ADDRESS = "https://www.airbnb.com/users/show/";
    private static final HtmlCleaner cleaner = new HtmlCleaner();

    private static final Logger logger = Logger.getLogger(WebSpider.class.getName());

    public static TagNode downloadHtmlPage(String address) throws IOException {
        return cleaner.clean(new URL(address));
    }

    public void DiscoverHostUsersThenFlats(int globalHostUserId, final int quantityFlats, final int quantityUsers)
            throws IOException {
        if (globalHostUserId <= 0 || quantityFlats < 0 || quantityUsers < 0) {
            throw new IllegalArgumentException("parameters have illegal values");
        }

        LinkedList<Pair<Integer, Integer>> usersQueue = new LinkedList<>();

        FlatParser flatParser;
        UserParser userParser;

        int countFlats = 0, countUsers = 0;

        try (DatabaseConnect connection = new DatabaseConnect()) {
            while (countFlats < quantityFlats || countUsers < quantityUsers) {
                if (usersQueue.size() == 0) {
                    usersQueue.push(Pair.of(globalHostUserId++, -1));
                }

                Pair<Integer, Integer> currentHostUser = usersQueue.getFirst();
                usersQueue.removeFirst();

                if (currentHostUser.snd() != -1) {
                    connection.putVisitedFlat(currentHostUser.fst(), currentHostUser.snd());
                }

                if (connection.containsUser(currentHostUser.fst())) {
                    continue;
                }

                logger.debug("processing user " + currentHostUser.fst);
                userParser = new UserParser(downloadHtmlPage(USER_ADDRESS + currentHostUser.fst()));
                User newUser = userParser.parse();

                if (currentHostUser.fst() != newUser.getId() || newUser.getId() == 0) {
                    continue;
                }

                if (currentHostUser.snd() != -1) {
                    newUser.addVisitedFlat(currentHostUser.snd());

                }

                connection.putUserIntoDatabase(newUser);
                ++countUsers;

                ArrayList<Integer> listiningsOfHostUser = userParser.getFlatIds();
                ArrayList<Integer> usersFromComments = userParser.getUserHostIdsFromComments();

                usersQueue.addAll(makeArrayOfPair(usersFromComments, -1));

                for (Integer currentFlatId : listiningsOfHostUser) {
                    if (connection.containsFlat(currentFlatId)) {
                        continue;
                    }

                    logger.debug("processing flat " + currentFlatId);

                    flatParser = new FlatParser(downloadHtmlPage(FLAT_ADDRESS + currentFlatId));
                    Flat flat = flatParser.parse();

                    if (flat.getId() != currentFlatId || flat.getId() == 0) {
                        continue;
                    }

                    connection.putFlatIntoDatabase(flat);
                    ++countFlats;

                    ArrayList<Integer> usersFromFlatComments = flatParser.getUserIdsFromComments();
                    usersQueue.addAll(makeArrayOfPair(usersFromFlatComments, currentFlatId));
                }

            }
        } catch (SQLException e) {
            logger.debug(e.getMessage());
        }

        logger.debug("users : " + countUsers + " flats: " + countFlats);

    }

    private ArrayList<Pair<Integer, Integer>> makeArrayOfPair(ArrayList<Integer> arrayOfUsers, Integer flatId) {
        ArrayList<Pair<Integer, Integer>> result = new ArrayList<>();

        for (Integer currentUserIdFromComment : arrayOfUsers)
            result.add(Pair.of(currentUserIdFromComment, flatId));
        return result;
    }

    private static class Pair<A, B> {

        private A fst;
        private B snd;

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
