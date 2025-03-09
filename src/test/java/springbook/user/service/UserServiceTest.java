package springbook.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static springbook.user.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static springbook.user.service.UserService.MIN_RECCOMEND_FOR_GOLD;

@SpringBootTest
@ContextConfiguration(locations = "/applicationContext.xml")
class UserServiceTest {
    @Autowired
    UserService userService;

    @Autowired
    private UserDao userDao;

    List<User> users;

    static class TestUserService extends UserService {
        private String id;

        private TestUserService(String id) {
            this.id = id;
        }

        @Override
        protected void upgradeLevel(User user) {
            if (user.getId().equals(this.id)) throw new TestUserServiceException();
            super.upgradeLevel(user);
        }
    }

    static class TestUserServiceException extends RuntimeException {

    }

    @BeforeEach
    void setUp() {
        users = Arrays.asList(
                new User("A", "에이", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0),
                new User("B", "비", "p2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
                new User("C", "씨", "p3", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD - 1),
                new User("D", "디", "p4", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD),
                new User("E", "이", "p5", Level.GOLD, 100, Integer.MAX_VALUE)
        );
    }

    @Test
    void bean() {
        assertThat(this.userService, is(notNullValue()));
    }

    @Test
    void upgradeLevels() {
        userDao.deleteAll();

        for (User user : users) userDao.add(user);

        userService.upgradeLevels();

        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);
    }

    @Test
    public void add() {
        userDao.deleteAll();

        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        assertThat(userWithLevelRead.getLevel(), is(userWithLevel.getLevel()));
        assertThat(userWithoutLevelRead.getLevel(), is(Level.BASIC));
    }

    @Test
    public void upgradeAllOrNothing() {
        UserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(this.userDao);

        userDao.deleteAll();
        for(User user : users) userDao.add(user);

        try {
            testUserService.upgradeLevels();
        } catch (TestUserServiceException e) {

        }

        checkLevelUpgraded(users.get(1), false);
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());

        if (upgraded) {
            assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
        } else {
            assertThat(userUpdate.getLevel(), is(user.getLevel()));
        }
    }
}