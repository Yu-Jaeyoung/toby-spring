package springbook.learningtest.jdk;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ReflectionTest {
    @Test
    public void invokeMethod() throws Exception {
        String name = "Spring";

        // length()
        assertThat(name.length(), is(6));

        Method lengthMethod = String.class.getMethod("length");
        assertThat((Integer) lengthMethod.invoke(name), is(6));

        // charAt()
        assertThat(name.charAt(0), is('S'));

        Method charAtMethod = String.class.getMethod("charAt", int.class);
        assertThat((Character) charAtMethod.invoke(name, 0), is('S'));
    }
}
