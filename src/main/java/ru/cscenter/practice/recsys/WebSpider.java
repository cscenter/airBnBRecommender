package ru.cscenter.practice.recsys;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.cscenter.practice.recsys.database.DataBaseQueue;
import ru.cscenter.practice.recsys.database.FlatDao;
import ru.cscenter.practice.recsys.database.Pair;
import ru.cscenter.practice.recsys.database.UserDao;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class WebSpider {
    private static final String FLAT_ADDRESS = "https://www.airbnb.com/rooms/";
    private static final String USER_ADDRESS = "https://www.airbnb.com/users/show/";
    private static final String FLATS_OF_USER_EXPRESSION = "https://www.airbnb.com/s?host_id=";
    private static final int QUANTITY_USERS_IN_AIRBNB = 30_000_000;
    private final Logger logger = Logger.getLogger(WebSpider.class.getName());
    private final WebDriver webDriver;

    public WebSpider(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void discoverHostUsersThenFlats(int globalHostUserId) {
        if (globalHostUserId <= 0) {
            throw new IllegalArgumentException("parameters have illegal values");
        }

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("config.xml");
        final DataBaseQueue usersQueue = (DataBaseQueue) applicationContext.getBean("dataBaseQueue", DataBaseQueue.class);
        final FlatDao flatDao = (FlatDao) applicationContext.getBean("flatDao", FlatDao.class);
        final UserDao userDao = (UserDao) applicationContext.getBean("userDao", UserDao.class);

        final FlatParser flatParser = new FlatParser(webDriver);
        final UserParser userParser = new UserParser(webDriver);
        final FlatPageParser flatPageParser = new FlatPageParser(webDriver);

        List<Integer> usersFromComments;
        List<Integer> listiningsOfHostUser;
        List<Integer> usersFromFlatComments;

        boolean hasFlats;


        while (globalHostUserId < QUANTITY_USERS_IN_AIRBNB) {

            if (usersQueue.size() == 0) {
                usersQueue.push(Pair.of(globalHostUserId++, -1));
            }

            final Pair<Integer, Integer> currentHostUser = usersQueue.pop();

            if (currentHostUser.snd() != -1) {
                userDao.putVisitedFlat(currentHostUser.fst(), currentHostUser.snd());
            }

            if (userDao.contains(currentHostUser.fst())) {
                continue;
            }

            logger.info("processing user " + currentHostUser.fst());

            downloadNewPage(USER_ADDRESS + currentHostUser.fst());

            final User newUser = userParser.parse();
            usersFromComments = userParser.getUserHostIdsFromComments();
            hasFlats = userParser.hasFlats();


            if ((newUser.getId() == 0) ||
                    ((usersFromComments.size() == 0) && !hasFlats && (currentHostUser.snd() == -1))) {
                logger.info("skip this user " + currentHostUser.fst());
                continue;
            }

            userDao.put(newUser);

            if(currentHostUser.snd() != -1)
                userDao.putVisitedFlat(currentHostUser.snd(), currentHostUser.fst());
            usersQueue.addAll(Pair.makeArrayOfPair(usersFromComments, -1));

            logger.info("user is put into db");

            if (hasFlats) {
                downloadNewPage(FLATS_OF_USER_EXPRESSION + currentHostUser.fst());
                listiningsOfHostUser = flatPageParser.parse();

                for (Integer currentFlatId : listiningsOfHostUser) {
                    if (flatDao.contains(currentFlatId)) {
                        continue;
                    }

                    logger.info("processing flat " + currentFlatId);

                    downloadNewPage(FLAT_ADDRESS + currentFlatId);
                    final Flat flat = flatParser.parse();

                    if (flat.getId() == 0) {
                        continue;
                    }

                    flatDao.put(flat);

                    logger.info("flat is put into db");

                    usersFromFlatComments = flatParser.getUserIdsFromComments();
                    usersQueue.addAll(Pair.makeArrayOfPair(usersFromFlatComments, currentFlatId));
                }
            }
        }
    }

    private void downloadNewPage(String address) {
        webDriver.get(address);
        webDriver.manage().timeouts().setScriptTimeout(5, TimeUnit.SECONDS);
        webDriver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
        webDriver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
    }
}
