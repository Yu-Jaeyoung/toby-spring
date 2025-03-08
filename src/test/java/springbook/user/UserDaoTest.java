package springbook.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import springbook.user.dao.UserDao;
import springbook.user.domain.User;

import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserDaoTest {

    @Autowired
    private UserDao dao;

    @Test
    void addAndGet() throws SQLException {
        User user1 = new User("park", "박", "password");
        User user2 = new User("Han", "한", "password");

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user1);
        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        User userGet1 = dao.get(user1.getId());
        assertThat(userGet1.getName(), is(user1.getName()));
        assertThat(userGet1.getPassword(), is(user1.getPassword()));

        User userGet2 = dao.get(user1.getId());
        assertThat(userGet2.getName(), is(user1.getName()));
        assertThat(userGet2.getPassword(), is(user1.getPassword()));
    }

    @Test
    void count() throws SQLException {
        User user1 = new User("tom", "톰", "password");
        User user2 = new User("Tim", "팀", "password");
        User user3 = new User("Jay", "제이", "password");

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user1);
        assertThat(dao.getCount(), is(1));

        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        dao.add(user3);
        assertThat(dao.getCount(), is(3));
    }

    @Test
    public void getUserFailure() throws SQLException {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        assertThrows(EmptyResultDataAccessException.class, () -> {
            dao.get("unknown_id");
        });
    }
}
