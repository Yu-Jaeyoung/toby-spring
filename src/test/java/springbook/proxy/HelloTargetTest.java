package springbook.proxy;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class HelloTargetTest {
    @Test
    public void simpleProxy() {
        Hello hello = new HelloTarget(); // 타깃은 인터페이스를 통해 접근하는 습관을 들여야함

        assertThat(hello.sayHello("Toby"), is("Hello Toby"));
        assertThat(hello.sayHi("Toby"), is("Hi Toby"));
        assertThat(hello.sayThankYou("Toby"), is("Thank you Toby"));

    }

    @Test
    public void upperProxy() {
        Hello proxiedHello = new HelloUppercase(new HelloTarget());

        assertThat(proxiedHello.sayHello("Toby"), is("HELLO TOBY"));
        assertThat(proxiedHello.sayHi("Toby"), is("HI TOBY"));
        assertThat(proxiedHello.sayThankYou("Toby"), is("THANK YOU TOBY"));
    }

    @Test
    public void dynamicProxy() {
        // 생성된 다이나믹 프록시 오브젝트는 Hello 인터페이스를 구현하고 있으므로
        // Hello 타입으로 타입 캐스팅해도 안전
        Hello proxiedHello = (Hello) Proxy.newProxyInstance(
                // 동적으로 생성되는 다이내믹 프록시 클래스의 로딩에 사용할 클래스 로더
                getClass().getClassLoader(),

                // 구현할 인터페이스
                new Class[]{Hello.class},

                // 부가기능과 위임 코드를 담은 InvocationHandler
                new UppercaseHandler(new HelloTarget())
        );

        assertThat(proxiedHello.sayHello("Toby"), is("HELLO TOBY"));
        assertThat(proxiedHello.sayHi("Toby"), is("HI TOBY"));
        assertThat(proxiedHello.sayThankYou("Toby"), is("THANK YOU TOBY"));
    }
}