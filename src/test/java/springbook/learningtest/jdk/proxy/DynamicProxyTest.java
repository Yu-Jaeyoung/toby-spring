package springbook.learningtest.jdk.proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import springbook.proxy.Hello;
import springbook.proxy.HelloTarget;
import springbook.proxy.UppercaseHandler;

import java.lang.reflect.Proxy;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DynamicProxyTest {
    @Test
    void simpleProxy() {
        Hello proxiedHello = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{ Hello.class },
                new UppercaseHandler(new HelloTarget())
        );

        assertThat(proxiedHello.sayHello("Toby"), is("HELLO TOBY"));
        assertThat(proxiedHello.sayHi("Toby"), is("HI TOBY"));
        assertThat(proxiedHello.sayThankYou("Toby"), is("THANK YOU TOBY"));
    }

    @Test
    void proxyFactoryBean() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());
        pfBean.addAdvice(new UppercaseAdvice()); // 부가 기능을 담은 어드바이스를 추가 (여러 개 가능)

        Hello proxiedHello = (Hello) pfBean.getObject();
        assertThat(proxiedHello.sayHello("Toby"), is("HELLO TOBY"));
        assertThat(proxiedHello.sayHi("Toby"), is("HI TOBY"));
        assertThat(proxiedHello.sayThankYou("Toby"), is("THANK YOU TOBY"));
    }

    static class UppercaseAdvice implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            // 리플렉션의 Method와 달리 메소드 실행 시 타깃 오브젝트를 전달할 필요가 없음
            // MethodInvocation 은 메소드 정보와 함께 타깃 오브젝트를 알고 있기 때문
            String ret = (String) invocation.proceed();
            return ret.toUpperCase();
        }
    }

    @Test
    void pointcutAdvisor() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayH*");

        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));

        Hello proxiedHello = (Hello) pfBean.getObject();

        assertThat(proxiedHello.sayHello("Toby"), is("HELLO TOBY"));
        assertThat(proxiedHello.sayHi("Toby"), is("HI TOBY"));
        assertThat(proxiedHello.sayThankYou("Toby"), is("Thank you Toby"));
    }
}
