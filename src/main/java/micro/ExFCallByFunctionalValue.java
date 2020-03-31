package micro;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import micro.event.ValueReceivedEvent;

import java.util.ArrayList;
import java.util.List;

import static micro.Check.notNull;

public class ExFCallByFunctionalValue extends Ex {

    private FCallByFunctionalValue f;
    private F baseFunction;
    private _Ex beingCalled;
    private List<Value> pendingValues = new ArrayList<>();

    ExFCallByFunctionalValue(Node node, FCallByFunctionalValue f, _Ex returnTo) {
        super(node, f, returnTo);
        this.f = f;
    }

    @Override
    protected void alterStateFor(ValueReceivedEvent e) {
        super.alterStateFor(e);
        Value v = e.value;
        if (isFunctionInputValue(v)) {
            if (v.getName().equals(f.getFunctionalValueParam())) {
                Check.invariant(v.get() instanceof PartiallyAppliedFunction, "that wasn't expected: " + v.toString());
                PartiallyAppliedFunction p = ((PartiallyAppliedFunction) v.get());
                baseFunction = p.baseFunction;
                pendingValues.addAll(p.partialValues);
            } else {
                if (baseFunction == null) {
                    pendingValues.add(v);
                }
            }
        }
    }

    @Override
    protected void performValueReceived(Value v) {
        if (v.getName().equals(f.getFunctionalValueParam())) {
            pendingValues.forEach(pv -> getFunctionBeingCalled().receive(pv.withSender(this)));
        } else {
            Check.isFunctionInputValue(v);
            if (baseFunction != null) {
                getFunctionBeingCalled().receive(v.withSender(this));
            }
        }
    }

    @Override
    void clear() {
        beingCalled = null;
        baseFunction = null;
        f = null;
        pendingValues.clear();
        super.clear();
    }

    @Override
    public String getLabel() {
        return f.getLabel();
    }

    String getNameForReturnValue() {
        return f.returnAs;
    }

    private _Ex getFunctionBeingCalled() {
        if (beingCalled == null) {
            beingCalled = node.getExecution(notNull(baseFunction), this);
        }
        return beingCalled;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        super.write(kryo, output);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        super.read(kryo, input);
    }
}
