package nano.ingredients.infrastructure;

import akka.actor.AbstractActor;
import akka.actor.DeadLetter;

public class DeadLetterActor extends AbstractActor {
  @Override
  public Receive createReceive() {
    return receiveBuilder()
        .match(
            DeadLetter.class,
            msg -> {
              System.out.println("dead letter: " + msg);
            })
        .build();
  }
}