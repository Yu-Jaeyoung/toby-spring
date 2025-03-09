package springbook.user.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

@SpringBootTest
@ContextConfiguration(locations = "/applicationContext.xml")
class UserServiceTest {
    @Autowired
    UserService userService;

    @Test
    void bean() {
        assertThat(this.userService, is(notNullValue()));
    }
}