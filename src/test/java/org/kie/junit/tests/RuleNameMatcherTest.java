package org.kie.junit.tests;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runners.model.InitializationError;
import org.kie.api.io.ResourceType;
import org.kie.junit.KieTestHelper;
import org.kie.junit.KieUnitTest;
import org.kie.junit.RuleNameMatcher;
import org.kie.junit.examples.drl.Person;

import java.io.InputStream;
import java.util.Arrays;

import static org.junit.Assert.assertNotNull;

public class RuleNameMatcherTest {

    protected static KieTestHelper statelessKieSessionTest;

    @BeforeClass
    public static void init() throws InitializationError {
        InputStream resourceAsStream = RuleNameMatcherTest.class.getResourceAsStream("/org/kie/junit/examples/drl/Hal1.drl");
        assertNotNull("Unable to find DRL File to initialize Test!",resourceAsStream);

        InputStream resourceAsStream2 = RuleNameMatcherTest.class.getResourceAsStream("/org/kie/junit/examples/drl/Hal2.drl");
        assertNotNull("Unable to find DRL File to initialize Test!",resourceAsStream2);

        statelessKieSessionTest = KieTestHelper.newStatelessSession()
                .addKieResource(ResourceType.DRL, resourceAsStream)
                .addDRL(resourceAsStream2)
                .build();
        assertNotNull("Failed to create StatelessKieSession!", statelessKieSessionTest);
    }

    @Test
    public void testRuleFiredStartsWith_StatelessSession() throws Exception {
        Person personObject = new Person();
        personObject.setName("HAL2");

        RuleNameMatcher ruleNameMatcher = RuleNameMatcher.newMatcher().startsWith("rule 1");

        KieUnitTest.withTester(statelessKieSessionTest)
                .addObject(personObject)
                .verifyRuleFired(ruleNameMatcher)
                .runTest();
    }

    @Test
    public void testRuleFiredEndsWith_StatelessSession() throws Exception {
        Person personObject = new Person();
        personObject.setName("HAL2");

        RuleNameMatcher ruleNameMatcher = RuleNameMatcher.newMatcher().endsWith("3");

        KieUnitTest.withTester(statelessKieSessionTest)
                .addObject(personObject)
                .verifyRuleFired(ruleNameMatcher)
                .runTest();
    }

    @Test
    public void testRuleFiredExactMatch_StatelessSession() throws Exception {
        Person personObject = new Person();
        personObject.setName("HAL2");

        RuleNameMatcher ruleNameMatcher = RuleNameMatcher.newMatcher().exactMatch("rule 1");

        KieUnitTest.withTester(statelessKieSessionTest)
                .addObject(personObject)
                .verifyRuleFired(ruleNameMatcher)
                .runTest();
    }

    @Test
    public void testRuleFiredContains_StatelessSession() throws Exception {
        Person personObject = new Person();
        personObject.setName("HAL2");

        RuleNameMatcher ruleNameMatcher = RuleNameMatcher.newMatcher().contains("1");

        KieUnitTest.withTester(statelessKieSessionTest)
                .addObject(personObject)
                .verifyRuleFired(ruleNameMatcher)
                .runTest();
    }

    @Test
    public void testRuleFiredExactIgnoreCase_StatelessSession() throws Exception {
        Person personObject = new Person();
        personObject.setName("HAL2");

        RuleNameMatcher ruleNameMatcher = RuleNameMatcher.newIgnoreCaseMatcher().exactMatch("RULE 1");

        KieUnitTest.withTester(statelessKieSessionTest)
                .addObject(personObject)
                .verifyRuleFired(ruleNameMatcher)
                .runTest();
    }

    @Test (expected = java.lang.AssertionError.class)
    public void testRuleNotFiredFail_StatelessSession() throws Exception {
        Person personObject = new Person();
        personObject.setName("HAL");
        //should fail due to mismatch in case of the fired Rule Name
        RuleNameMatcher ruleNameMatcher = RuleNameMatcher.newMatcher().exactMatch("RULE 2");

        KieUnitTest.withTester(statelessKieSessionTest)
                .addObject(personObject)
                .verifyRuleFired(ruleNameMatcher)
                .runTest();
    }

    @Test
    public void testRuleFiredCount_StatelessSession() throws Exception {
        Person personObject = new Person();
        personObject.setName("HAL");
        Person personObject2 = new Person();
        personObject2.setName("HAL");

        RuleNameMatcher ruleNameMatcher = RuleNameMatcher.newMatcher().exactMatch("Rule 3");

        KieUnitTest.withTester(statelessKieSessionTest)
                .addObjects(Arrays.asList(new Object[]{personObject, personObject2}))
                .verifyRuleFiredMultiple(ruleNameMatcher, 2)
                .runTest();
    }
}
