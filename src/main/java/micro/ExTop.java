package micro;

import micro.event.ExEvent;

import java.util.List;

public class ExTop implements  _Ex, Id {

    public final static long TOP_ID = 0;

    private final _F template;
    private final Address address;

    public ExTop(Address address) {
        this.address = address;
        this.template = createTemplate();
    }

    private _F createTemplate() {
        return new _F(){

            @Override
            public long getId() {
                return TOP_ID;
            }

            @Override
            public void setId(long value) {
                throw new IllegalStateException();
            }

            @Override
            public void addPropagation(PropagationType type, String nameExpected, String namePropagated, _F to) {
                throw new IllegalStateException();
            }

            @Override
            public List<FPropagation> getPropagations() {
                return null;
            }

            @Override
            public _Ex createExecution(long id, _Ex returnTo) {
                throw new IllegalStateException();
            }

            @Override
            public Address getAddress() {
                return Address.localhost;
            }

            @Override
            public boolean isTailRecursive() {
                return false;
            }

        };
    }

    @Override
    public String toString() {
        return "TOP";
    }

    @Override
    public _Ex returnTo() {
        return this;
    }

    @Override
    public _F getTemplate() {
        return template;
    }

    @Override
    public void receive(Value v) {

    }

    @Override
    public void recover(ExEvent e) {

    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public String getLabel() {
        return "TOP";
    }

    @Override
    public long getId() {
        return TOP_ID;
    }

    @Override
    public void setId(long value) {

    }

    @Override
    public boolean isMoreToDoRightNow() {
        return false;
    }

    @Override
    public boolean isDone() {
        return true;
    }

    @Override
    public void crank() {

    }
}
