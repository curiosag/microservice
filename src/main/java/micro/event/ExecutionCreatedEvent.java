package micro.event;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import micro.Ex;
import micro.Hydrator;

public class ExecutionCreatedEvent extends ExEvent {

    private Long FId;

    public ExecutionCreatedEvent(Ex ex) {
        super(ex);
        this.FId = ex.template.getId();
    }

    public ExecutionCreatedEvent() {
    }

    public Long getFId() {
        return FId;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        super.write(kryo, output);
        output.writeVarLong(FId, true);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        super.read(kryo, input);
        FId = input.readVarLong(true);
    }

    @Override
    public void hydrate(Hydrator h) {

    }
}
