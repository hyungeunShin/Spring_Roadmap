package hello.proxy.pureproxy.concreteproxy.code;

public class ConcreteClient {
    private ConcreteLogic target;

    public ConcreteClient(ConcreteLogic target) {
        this.target = target;
    }

    public void execute() {
        target.operation();
    }
}
