package micro.atoms;

import micro.Check;
import micro.Names;
import micro.Value;

import java.util.Map;
import java.util.Objects;

public class Eq implements Primitive {

    @Override
    public Object execute(Map<String, Value> values) {
        Check.notNull(values);
        return Objects.equals(values.get(Names.left).get(), values.get(Names.right).get());
    }

    public static Eq eq (){
        return new Eq();
    }
}
