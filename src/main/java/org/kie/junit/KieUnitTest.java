package org.kie.junit;

import org.apache.commons.lang3.StringUtils;
import org.junit.runners.model.InitializationError;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class KieUnitTest {

    private Object object;
    private Map<RuleNameMatcher, Integer> rulesToBeFired = new HashMap<RuleNameMatcher, Integer>();
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
        rulesToBeFired.put(RuleNameMatcher.newMatcher().exactMatch(ruleName), 1);
        return this;
    }

    public KieUnitTest verifyRuleFired(RuleNameMatcher matcher){
        rulesToBeFired.put(matcher, 1);
        return this;
    }

    public KieUnitTest verifyRuleFiredMultiple(String ruleName, int expectedCount){
        rulesToBeFired.put(RuleNameMatcher.newMatcher().exactMatch(ruleName), expectedCount);
        return this;
    }

    public KieUnitTest verifyRuleFiredMultiple(RuleNameMatcher matcher, int expectedCount){
        rulesToBeFired.put(matcher, expectedCount);
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
        for (String firedRule: rulesFiredMap.keySet()) {
            RuleNameMatcher matcher = checkForMatch(rulesFiredMap, firedRule);
            if (matcher != null){
                rulesToBeFired.remove(matcher);
            }
        }
        for (RuleNameMatcher matcher : rulesToBeFired.keySet()) {
            fail("Rule ('" + matcher.getRuleName() + "') did not fire!. Was expecting to fire '"+rulesToBeFired.get(matcher)+"' times.'");
        }
    }

    private RuleNameMatcher checkForMatch(Map<String, Integer> rulesFiredMap, String firedRule) {
        for (RuleNameMatcher matcher : rulesToBeFired.keySet()){
            boolean matched = false;
            switch (matcher.compareType){
                case STARTS_WITH:
                    matched = matcher.shouldIgnoreCase? StringUtils.startsWithIgnoreCase(firedRule, matcher.ruleName):StringUtils.startsWith(firedRule, matcher.ruleName);
                    break;
                case ENDS_WITH:
                    matched = matcher.shouldIgnoreCase?StringUtils.endsWithIgnoreCase(firedRule, matcher.ruleName):StringUtils.endsWith(firedRule, matcher.ruleName);
                    break;
                case EXACT_MATCH:
                    matched = matcher.shouldIgnoreCase?StringUtils.equalsIgnoreCase(firedRule, matcher.ruleName):StringUtils.equals(firedRule, matcher.ruleName);
                    break;
                case CONTAINS:
                    matched = matcher.shouldIgnoreCase?StringUtils.containsIgnoreCase(firedRule, matcher.ruleName):StringUtils.contains(firedRule, matcher.ruleName);
                    break;
            }
            Integer count = rulesFiredMap.get(firedRule);

            if ( matched ){
                assertEquals("Rule (" + firedRule + ") did not fire expected times.", count, rulesToBeFired.get(matcher));
                return matcher;
            }
        }
        return null;
    }

    private void checkForRulesNotFired(Map<String, Integer> rulesFiredMap) {
        for (String rule: rulesShouldNotBeFired.keySet()) {
            Integer count = rulesFiredMap.get(rule);
            assertNull("Rule ("+rule+") fired unexpectedly!", count);
        }
    }

}
