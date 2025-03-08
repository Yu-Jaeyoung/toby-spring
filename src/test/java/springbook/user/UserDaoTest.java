package springbook.user;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import springbook.user.dao.UserDao;
import springbook.user.domain.User;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserDaoTest {

    @Test
    void addAndGet() throws SQLException {
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");

        UserDao dao = context.getBean("userDao", UserDao.class);

        User user = new User();
        user.setId("jack");
        user.setName("jaeyoung");
        user.setPassword("password");

        dao.add(user);

        User user2 = dao.get(user.getId());

        assertEquals(user.getName(), user2.getName(), "이름이 일치해야 합니다.");
        assertEquals(user.getPassword(), user2.getPassword(), "패스워드가 일치해야 합니다.");
    }
}
