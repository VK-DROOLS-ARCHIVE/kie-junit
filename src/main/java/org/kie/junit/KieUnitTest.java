package org.kie.junit;

import org.junit.runners.model.InitializationError;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class KieUnitTest {

    private Object object;
    private Map<String, Integer> rulesToBeFired = new HashMap<String, Integer>();
    private Map<String, Integer> rulesShouldNotBeFired = new HashMap<String, Integer>();
    private KieTestHelper kieTestHelper;

    private KieUnitTest(){}

    public KieUnitTest(KieTestHelper kieTestHelper) {
        this.kieTestHelper = kieTestHelper;
    }

    public static KieUnitTest withTester(KieTestHelper kieTestHelper){
        return new KieUnitTest(kieTestHelper);
    }

    public KieUnitTest addObject(Object object){
        this.object = object;
        return this;
    }

    public KieUnitTest addObjects(Iterable<?> object){
        this.object = object;
        return this;
    }

    public KieUnitTest verifyRuleFired(String ruleName){
        rulesToBeFired.put(ruleName, 1);
        return this;
    }

    public KieUnitTest verifyRuleFired(String ruleName, int count){
        rulesToBeFired.put(ruleName, count);
        return this;
    }
    public KieUnitTest verifyRuleNotFired(String ruleName){
        rulesShouldNotBeFired.put(ruleName, 1);
        return this;
    }

    public void runTest() throws InitializationError {
        assertNotNull(kieTestHelper);
        assertNotNull(object);

        Map<String, Integer> rulesFiredMap = kieTestHelper.execute(object);
        checkForRulesFired(rulesFiredMap);
        checkForRulesNotFired(rulesFiredMap);
    }

    private void checkForRulesFired(Map<String, Integer> rulesFiredMap) {
        for (String rule: rulesToBeFired.keySet()) {
            Integer count = rulesFiredMap.get(rule);
            assertNotNull("Rule ('" + rule + "') did not fire!", count);
            assertEquals("Rule (" + rule + ") did not fire expected times.", count, rulesToBeFired.get(rule));
        }
    }

    private void checkForRulesNotFired(Map<String, Integer> rulesFiredMap) {
        for (String rule: rulesShouldNotBeFired.keySet()) {
            Integer count = rulesFiredMap.get(rule);
            assertNull("Rule ("+rule+") fired unexpectedly!", count);
        }
    }

}
