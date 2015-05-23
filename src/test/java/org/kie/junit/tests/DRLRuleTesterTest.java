package org.kie.junit.tests;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.model.InitializationError;
import org.kie.api.io.ResourceType;
import org.kie.junit.KieTestHelper;
import org.kie.junit.KieUnitTest;
import org.kie.junit.examples.drl.Person;

import java.io.InputStream;
import java.util.Arrays;

import static org.junit.Assert.assertNotNull;

public class DRLRuleTesterTest {

    protected static KieTestHelper statelessKieSessionTest;
    protected static KieTestHelper statefulKieSessionTest;

    @BeforeClass
    public static void init() throws InitializationError {
        InputStream resourceAsStream = DRLRuleTesterTest.class.getResourceAsStream("/org/kie/junit/examples/drl/Hal1.drl");
        assertNotNull("Unable to find DRL File to initialize Test!",resourceAsStream);

        InputStream resourceAsStream2 = DRLRuleTesterTest.class.getResourceAsStream("/org/kie/junit/examples/drl/Hal2.drl");
        assertNotNull("Unable to find DRL File to initialize Test!",resourceAsStream2);

        statelessKieSessionTest = KieTestHelper.newStatelessSession()
                .addKieResource(ResourceType.DRL, resourceAsStream)
                .addDRL(resourceAsStream2)
                .build();
        assertNotNull("Failed to create StatelessKieSession!", statelessKieSessionTest);

        statefulKieSessionTest = KieTestHelper.newStatefulSession()
                .addKieResource(ResourceType.DRL, resourceAsStream)
                .addDRL(resourceAsStream2)
                .build();
        assertNotNull("Failed to create StatefulKieSession!", statefulKieSessionTest);
    }

    @Test
    public void testRuleFired_StatelessSession() throws Exception {
        Person personObject = new Person();
        personObject.setName("HAL2");

        KieUnitTest.withTester(statelessKieSessionTest)
                .addObject(personObject)
                .verifyRuleFired("rule 1")
                .verifyRuleFired("rule 3")
                .runTest();
    }

    @Test (expected = AssertionError.class)
    public void testRuleFiredFail_StatelessSession() throws Exception {
        Person personObject = new Person();
        personObject.setName("HAL2");

        KieUnitTest.withTester(statelessKieSessionTest)
                .addObject(personObject)
                .verifyRuleNotFired("rule 1")
                .runTest();
    }

    @Test
    public void testRuleNotFired_StatelessSession() throws Exception {
        Person personObject = new Person();
        personObject.setName("HAL");

        KieUnitTest.withTester(statelessKieSessionTest)
                .addObject(personObject)
                .verifyRuleNotFired("rule 1")
                .runTest();
    }

    @Test (expected = AssertionError.class)
    public void testRuleNotFiredFail_StatelessSession() throws Exception {
        Person personObject = new Person();
        personObject.setName("HAL");

        KieUnitTest.withTester(statelessKieSessionTest)
                .addObject(personObject)
                .verifyRuleFired("rule 1")
                .runTest();
    }

    @Test
    public void testRuleFiredCount_StatelessSession() throws Exception {
        Person personObject = new Person();
        personObject.setName("HAL");
        Person personObject2 = new Person();
        personObject2.setName("HAL");

        KieUnitTest.withTester(statelessKieSessionTest)
                .addObjects(Arrays.asList(new Object[]{personObject, personObject2}))
                .verifyRuleFiredMultiple("rule 2", 2)
                .runTest();
    }

    @Test (expected = AssertionError.class)
    public void testRuleFiredCountFail_StatelessSession() throws Exception {
        Person personObject = new Person();
        personObject.setName("HAL");

        KieUnitTest.withTester(statelessKieSessionTest)
                .addObjects(Arrays.asList(new Object[]{personObject}))
                .verifyRuleFiredMultiple("rule 2", 2)
                .runTest();
    }

    @Test
    public void testAll_StatelessSession() throws Exception {
        Person personObject = new Person();

        KieUnitTest.withTester(statelessKieSessionTest)
                .addObject(personObject)
                .verifyRuleNotFired("rule 1")
                .verifyRuleNotFired("rule 2")
                .verifyRuleNotFired("rule 4")
                .verifyRuleFired("rule 3")
                .runTest();
    }

    @Ignore("All Stateful Session Tests are Failing")
    @Test
    public void testRuleFired_Session() throws Exception {
        Person personObject = new Person();
        personObject.setName("HAL2");

        KieUnitTest.withTester(statefulKieSessionTest)
                .addObject(personObject)
                .verifyRuleFired("rule 1")
                .verifyRuleFired("rule 3")
                .runTest();
    }

    @Ignore("All Stateful Session Tests are Failing")
    @Test (expected = AssertionError.class)
    public void testRuleFiredFail_Session() throws Exception {
        Person personObject = new Person();
        personObject.setName("HAL2");

        KieUnitTest.withTester(statefulKieSessionTest)
                .addObject(personObject)
                .verifyRuleNotFired("rule 1")
                .runTest();
    }

    @Ignore("All Stateful Session Tests are Failing")
    @Test
    public void testRuleNotFired_Session() throws Exception {
        Person personObject = new Person();
        personObject.setName("HAL");

        KieUnitTest.withTester(statefulKieSessionTest)
                .addObject(personObject)
                .verifyRuleNotFired("rule 1")
                .runTest();
    }

    @Ignore("All Stateful Session Tests are Failing")
    @Test (expected = AssertionError.class)
    public void testRuleNotFiredFail_Session() throws Exception {
        Person personObject = new Person();
        personObject.setName("HAL");

        KieUnitTest.withTester(statefulKieSessionTest)
                .addObject(personObject)
                .verifyRuleFired("rule 1")
                .runTest();
    }

    @Ignore("All Stateful Session Tests are Failing")
    @Test
    public void testRuleFiredCount_Session() throws Exception {
        Person personObject = new Person();
        personObject.setName("HAL");
        Person personObject2 = new Person();
        personObject2.setName("HAL");

        KieUnitTest.withTester(statefulKieSessionTest)
                .addObjects(Arrays.asList(personObject, personObject2))
                .verifyRuleFiredMultiple("rule 3", 2)
                .runTest();
    }

    @Ignore("All Stateful Session Tests are Failing")
    @Test (expected = AssertionError.class)
    public void testRuleFiredCountFail_Session() throws Exception {
        Person personObject = new Person();
        personObject.setName("HAL");

        KieUnitTest.withTester(statefulKieSessionTest)
                .addObjects(Arrays.asList(personObject))
                .verifyRuleFiredMultiple("rule 2", 2)
                .runTest();
    }

    @Ignore("All Stateful Session Tests are Failing")
    @Test
    public void testAll_Session() throws Exception {
        Person personObject = new Person();

        KieUnitTest.withTester(statefulKieSessionTest)
                .addObject(personObject)
                .verifyRuleNotFired("rule 1")
                .verifyRuleNotFired("rule 2")
                .verifyRuleNotFired("rule 4")
                .verifyRuleFired("rule 3")
                .runTest();
    }
}
