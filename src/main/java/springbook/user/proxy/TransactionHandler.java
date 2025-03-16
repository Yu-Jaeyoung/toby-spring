package springbook.user.proxy;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TransactionHandler implements InvocationHandler {

    // 부가 기능을 제공할 타깃 오브젝트. 어떤 타입의 오브젝트에도 적용 가능
    private Object target;

    // 트랜잭션 기능을 제공하는 데 필요한 트랜잭션 매니저
    private PlatformTransactionManager transactionManager;

    // 트랜잭션을 적용할 메소드 이름 패턴
    private String pattern;

    public void setTarget(Object target) {
        this.target = target;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 트랜잭션 적용 대상 메소드를 선별해서 트랜잭션 경계설정 기능을 부여
        if (method.getName().startsWith(pattern)) {
            return invokeInTransaction(method, args);
        }

        return method.invoke(target, args);
    }

    private Object invokeInTransaction(Method method, Object[] args) throws Throwable {
        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            // 트랜잭션을 시작하고 타깃 오브젝트의 메소드를 호출.
            // 예외가 발생하지 않았다면 commit
            Object ret = method.invoke(target, args);
            this.transactionManager.commit(status);

            return ret;
        } catch (InvocationTargetException e) {
            // 예외가 발생하면 트랜잭션을 롤백
            this.transactionManager.rollback(status);

            throw e.getTargetException();
        }
    }
}
