package springbook.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class UppercaseHandler implements InvocationHandler {

    Hello target;

    // 다이내믹 프록시로부터 전달받은 요청을 다시 타깃 오브젝트에
    // 위임해야 하기 때문에 타깃 오브젝트를 주입받아 둠
    public UppercaseHandler(Hello target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String ret = (String) method.invoke(target, args); // 타깃으로 위임. 인터페이스 메소드 호출에 모두 적용됨
        return ret.toUpperCase(); // 부가기능 제공
    }
}
