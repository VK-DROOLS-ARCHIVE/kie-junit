package org.kie.junit;

import org.junit.runners.model.InitializationError;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.junit.support.RuleFiredListener;

import java.util.Map;

public final class StatelessSessionTestHelper extends KieTestHelper {

    protected StatelessSessionTestHelper(){
        super();
    }

    @Override
    public Map<String, Integer> execute(Object object) throws InitializationError {

        StatelessKieSession statelessKieSession = kieBase.newStatelessKieSession();
        RuleFiredListener ruleFiredListener = new RuleFiredListener();
        statelessKieSession.addEventListener(ruleFiredListener);
        if (object instanceof Iterable) {
            statelessKieSession.execute((Iterable)object);
        } else {
            statelessKieSession.execute(object);
        }
        return ruleFiredListener.getRulesFiredMap();
    }
}
