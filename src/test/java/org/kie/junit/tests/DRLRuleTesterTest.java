package org.kie.junit.tests;


import org.junit.Before;
import org.junit.runners.model.InitializationError;
import org.kie.api.io.ResourceType;
import org.kie.junit.KieTestHelper;
import org.kie.junit.KieUnitTest;
import org.junit.Test;
import org.kie.junit.examples.drl.Person;

import java.util.Arrays;

public class DRLRuleTesterTest {

    KieTestHelper statelessKieSessionTest;

    @Before
    public void init() throws InitializationError {
        statelessKieSessionTest = KieTestHelper.newStatelessSession()
                .addKieResource(ResourceType.DRL, DRLRuleTesterTest.class.getResourceAsStream("/org/kie/junit/examples/drl/Hal1.drl"))
                .addKieResource(ResourceType.DRL, DRLRuleTesterTest.class.getResourceAsStream("/org/kie/junit/examples/drl/Hal2.drl"))
                .build();
    }

    @Test
    public void testRuleFired() throws Exception {
        Person personObject = new Person();
        personObject.setName("HAL2");

        KieUnitTest.withTester(statelessKieSessionTest)
                .addObject(personObject)
                .verifyRuleFired("rule 1")
                .verifyRuleFired("rule 3")
                .runTest();
    }

    @Test (expected = AssertionError.class)
    public void testRuleFiredFail() throws Exception {
        Person personObject = new Person();
        personObject.setName("HAL2");

        KieUnitTest.withTester(statelessKieSessionTest)
                .addObject(personObject)
                .verifyRuleNotFired("rule 1")
                .runTest();
    }

    @Test
    public void testRuleNotFired() throws Exception {
        Person personObject = new Person();
        personObject.setName("HAL");

        KieUnitTest.withTester(statelessKieSessionTest)
                .addObject(personObject)
                .verifyRuleNotFired("rule 1")
                .runTest();
    }

    @Test (expected = AssertionError.class)
    public void testRuleNotFiredFail() throws Exception {
        Person personObject = new Person();
        personObject.setName("HAL");

        KieUnitTest.withTester(statelessKieSessionTest)
                .addObject(personObject)
                .verifyRuleFired("rule 1")
                .runTest();
    }

    @Test
    public void testRuleFiredCount() throws Exception {
        Person personObject = new Person();
        personObject.setName("HAL");
        Person personObject2 = new Person();
        personObject2.setName("HAL");

        KieUnitTest.withTester(statelessKieSessionTest)
                .addObjects(Arrays.asList(new Object[]{personObject, personObject2}))
                .verifyRuleFired("rule 2", 2)
                .runTest();
    }

    @Test (expected = AssertionError.class)
    public void testRuleFiredCountFail() throws Exception {
        Person personObject = new Person();
        personObject.setName("HAL");

        KieUnitTest.withTester(statelessKieSessionTest)
                .addObjects(Arrays.asList(new Object[]{personObject}))
                .verifyRuleFired("rule 2", 2)
                .runTest();
    }

    @Test
    public void testAll() throws Exception {
        Person personObject = new Person();

        KieUnitTest.withTester(statelessKieSessionTest)
                .addObject(personObject)
                .verifyRuleNotFired("rule 1")
                .verifyRuleNotFired("rule 2")
                .verifyRuleNotFired("rule 4")
                .verifyRuleFired("rule 3")
                .runTest();
    }
}
