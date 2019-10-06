package nano.ingredients;

import akka.actor.ActorRef;
import nano.ingredients.infrastructure.TraceMessage;
import nano.ingredients.infrastructure.Tracer;
import nano.misc.Adresses;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static nano.ingredients.Ensemble.getStackTrace;
import static nano.ingredients.Ensemble.resolve;
import static nano.ingredients.RunMode.RUN;
import static nano.ingredients.RunProperty.*;
import static nano.ingredients.infrastructure.TraceMessage.traced;

public abstract class Actress implements Serializable {
    private static final long serialVersionUID = 0L;
    private static Logger logger = Logger.getRootLogger();
    ActorRef aref;
    private Set<RunProperty> runProperties = new HashSet<>();

    RunMode runMode = RUN;

    private boolean stop = false;

    private boolean stopped = false;

    protected transient Tracer tracer = resolveTracer();

    protected ActorRef aRef() {
        return aref;
    }

    void setAref(ActorRef aref) {
        this.aref = aref;
    }

    protected Tracer resolveTracer() {
        return (Tracer) resolve(Adresses.trace);
    }

    public final Address address;


    void setRunMode(RunMode runMode) {
        this.runMode = runMode;
    }

    public void stop() {
        stop = true;
    }

    public boolean isStopped() {
        return stopped;
    }

    protected Actress(Address address) {
        this.address = address;
    }

    Actress() {
        address = createAddress();
        if (!(this instanceof Tracer)) {
            tracer = resolveTracer();
        }
    }

    Address createAddress() {
        return new Address(this.getClass().getSimpleName(), Ensemble.nextId());
    }

    public void label(String sticker) {
        address.setLabel(sticker);
    }

    void debug(Message m, Origin o, String rel) {
        if (!(m instanceof TraceMessage))
            debug(String.format("%s (%d/%d)%s " + rel + " %s %s", m.origin.getComputationPath().toString(), o.getExecutionId(), o.getComputationPath().size(), this.address.toString(), o.getSender().address.toString(), m.toString()));
    }

    public abstract void process(Message message);

    public void tell(Message m) {
        if (runMode == RUN) {
            aRef().tell(m, m.origin.getSender().aref);
        }
    }

    public void receive(Message m) {
        try {
            debug(m, m.origin, "!!");
            if (hasRunProperty(SHOW_STACKS)) {
                System.out.println(m.origin.getComputationPath().getStack().stackPoints());
            }
            if (m.key.equals(Name.ack)) {
                onAck((Message) m.getValue());
            } else {
                process(m);
            }
            if (m.ack == Acknowledge.Y) {
                m.origin.getSender().aRef().tell(ackMsg(m), this.aRef());
            }

        } catch (Exception e) {
            Ensemble.instance().abort(e, this);
        }
        stopped = stop;
    }

    String getExceptionMessage(Message m, Exception e) {
        return this.address + " " + m + "\n" + getStackTrace(e);
    }

    private Message ackMsg(Message m) {
        return new Message(Name.ack, m, m.origin.sender((Function) this));
    }

    protected void onAck(Message value) {
    }

    protected void trace(Message message) {
        if (hasRunProperty(TRACE)) {
            tracer.aRef().tell(traced(message, this), this.aRef());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Actress actress = (Actress) o;
        return Objects.equals(address, actress.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }

    void debug(String s) {
        if (hasRunProperty(DEBUG)) {
            logger.debug(s);
        }
    }

    public void checkSanityOnStop() {
    }

    void setRunProperties(Set<RunProperty> value) {
        this.runProperties = value;
    }

    @Override
    public String toString() {
        return address.toString();
    }

    public void receiveRecover(Message m) {
    }

    public boolean shouldPersist(Message m){
        return false;
    };

    private boolean hasRunProperty(RunProperty p) {
        return runProperties.contains(p);
    }
}
