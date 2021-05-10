package micro;

import micro.event.ExEvent;
import nano.ingredients.Name;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CallAsync<T extends Serializable> implements _Ex {

    private final F f;
    private final Class<T> resultType;
    Consumer<T> consumer;
    private final Node node;

    private final Map<String, Integer> params = new HashMap<>();

    private CallAsync(Class<T> resultType, F f, Consumer<T> consumer, Node node) {
        this.f = f;
        this.resultType = resultType;
        this.consumer = consumer;
        this.node = node;
    }

    public static <T extends Serializable> CallAsync<T> of(Class<T> resultType, F f, Consumer<T> consumer, Node node) {
        return new CallAsync<>(resultType, f, consumer, node);
    }

    public CallAsync<T> param(String key, Integer value) {
        params.put(key, value);
        return this;
    }

    public void call() {
        _Ex ex = node.createExecution(f,this);

        if (params.size() == 0) {
            ex.receive(Value.of(Name.kickOff, 0, this));
        } else {
            params.forEach((k, v) -> ex.receive(Value.of(k, v, this)));
        }
    }

    @Override
    public _Ex returnTo() {
        throw new RuntimeException("no call expected");
    }

    @Override
    public _F getTemplate() {
        throw new RuntimeException("no call expected");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void receive(Value v) {
        if (!(resultType.isAssignableFrom(v.get().getClass()))) {
            throw new RuntimeException("inconsistent result type");
        }
        consumer.accept((T) v.get());
    }

    @Override
    public void recover(ExEvent e) {

    }

    @Override
    public Address getAddress() {
        throw new RuntimeException("no call expected");
    }

    @Override
    public long getId() {
        return -1;
    }

    @Override
    public void setId(long value) {
        throw new RuntimeException("no call expected");
    }

    @Override
    public String toString() {
        return "AsyncCall{f:" + f.getLabel() + "}";
    }

    @Override
    public boolean isMoreToDo() {
        return false;
    }

    @Override
    public void crank() {

    }
}
