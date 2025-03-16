package springbook.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class UppercaseHandler implements InvocationHandler {

    Object target;

    // 어떤 종류의 인터페이스를 구현한 타깃에도 적용 가능하도록 Object 타입으로 수정
    public UppercaseHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object ret = method.invoke(target, args);

        // 호출된 메소드의 리턴 타입이 String 인 경우만 대문자 변경 기능을 적용하도록 수정
        if (ret instanceof String) {
            return ((String) ret).toUpperCase();
        }

        return ret;
    }
}
