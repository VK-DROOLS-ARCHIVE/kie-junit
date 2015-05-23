package org.kie.junit.support;

import org.junit.runners.model.InitializationError;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.BeforeMatchFiredEvent;
import org.kie.api.event.rule.DefaultAgendaEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


public class RuleFiredListener extends DefaultAgendaEventListener {

    Map<String, Integer> rulesFiredMap;

    public static final Logger logger = LoggerFactory.getLogger(RuleFiredListener.class);

    public RuleFiredListener() throws InitializationError
    {
        super();
        rulesFiredMap = new HashMap<String, Integer>();
    }

    @Override
    public void afterMatchFired(AfterMatchFiredEvent event) {
        String ruleName = event.getMatch().getRule().getName();
//        System.out.println("ruleName == "+ruleName);
        Integer count = rulesFiredMap.get(ruleName);
        if ( count == null) {
            count = 0;
        }
        count++;
        rulesFiredMap.put(ruleName, count);
    }

    @Override
    public void beforeMatchFired(BeforeMatchFiredEvent event) {
        super.beforeMatchFired(event);
    }

    public Map<String, Integer> getRulesFiredMap() {
        return rulesFiredMap;
    }
}
