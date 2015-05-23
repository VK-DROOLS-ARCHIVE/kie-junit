package org.kie.junit;

import org.junit.runners.model.InitializationError;
import org.kie.api.runtime.KieSession;
import org.kie.junit.support.RuleFiredListener;

import java.util.Map;

public final class StatefulSessionTestHelper extends KieTestHelper {

    protected StatefulSessionTestHelper(){
        super();
    }

    @Override
    public Map<String, Integer> execute(Object object) throws InitializationError {

        KieSession kieSession = kieBase.newKieSession();
        RuleFiredListener ruleFiredListener = new RuleFiredListener();
        kieSession.addEventListener(ruleFiredListener);
        if (object instanceof Iterable) {
            for (Object obj: (Iterable)object) {
                kieSession.insert(obj);
            }
        } else {
            kieSession.insert(object);
        }
//        System.out.println("kieSession.getFactCount() ::" + kieSession.getFactCount());
//        Collection<FactHandle> factHandles = kieSession.getFactHandles();
//        for (FactHandle factHandle : factHandles){
//            Object obj = kieSession.getObject(factHandle);
//            System.out.println(obj.getClass()+"  "+obj);
//        }
        kieSession.fireAllRules();
        kieSession.dispose();
        return ruleFiredListener.getRulesFiredMap();
    }
}
