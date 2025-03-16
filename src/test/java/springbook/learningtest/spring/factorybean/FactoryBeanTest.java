package springbook.learningtest.spring.factorybean;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@ContextConfiguration(locations = "/FactoryBeanTest-context.xml")
class FactoryBeanTest {
    @Autowired
    ApplicationContext context;

    @Test
    void getMessageFromFactoryBean() {
        Object message = context.getBean("message");
        assertThat(message.getClass(), is(Message.class));
        assertThat(((Message) message).getText(), is("Factory Bean"));
    }

    @Test
    void getFactoryBean() throws Exception {
        Object factory = context.getBean("&message");
        assertThat(factory.getClass(), is(MessageFactoryBean.class));
    }
}