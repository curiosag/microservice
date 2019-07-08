package miso.ingredients;

import miso.Actress;
import miso.message.Message;

import java.util.function.Consumer;

public class Action extends Actress {

    Consumer<Message> action;

    public Action(Consumer<Message> action) {
        this.action = action;
    }

    @Override
    public void recieve(Message message) {
        System.out.println("Action received " + message.toString());
        action.accept(message);
    }

    @Override
    protected Message getNext() {
        return null;
    }

    public static Action action (Consumer<Message> action){
        return new Action(action);
    }
}
