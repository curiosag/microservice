package micro;

public class ExF extends Ex {

    public ExF(Node node, long id, F template, _Ex returnTo) {
        super(node, id, template, returnTo);
    }

    @Override
    public void processValueDownstream(Value v) {
        Check.preCondition(isLegitDownstreamValue(v));
        Check.invariant(!(template.hasFunctionAtom() && Names.result.equals(v.getName())), "no result as input expected for function atom");

        propagate(v);

        if (template.hasAtom() && paramsReceived.size() == template.numParams()) {
            if (template.getPrimitive().isSideEffect()) {
                applySideEffect();
            } else {
                applyFunction();
            }
            resultOrExceptionFromPrimitive = true;
        }
    }

    private void applySideEffect() {
        try {
            template.getPrimitive().execute(paramsReceived);
        } catch (Exception e) {
            deliverResult(new Value(Names.exception, e, this));
        }
    }

    private void applyFunction() {
        try {
            Object value = template.getPrimitive().execute(paramsReceived);
            deliverResult(new Value(template.returnAs, value, this));
        } catch (Exception e) {
            deliverResult(new Value(Names.exception, e, this));
        }
    }

}
