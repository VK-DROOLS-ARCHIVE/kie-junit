package org.kie.junit.tests;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.model.InitializationError;
import org.kie.junit.KieTestHelper;
import org.kie.junit.KieUnitTest;
import org.kie.junit.examples.decisiontable.Driver;
import org.kie.junit.examples.decisiontable.Policy;

import java.io.InputStream;
import java.util.Arrays;

import static org.junit.Assert.assertNotNull;

public class DTableTesterTest {

    protected static KieTestHelper statelessKieSessionTest;
    protected static KieTestHelper statefulKieSessionTest;

    @BeforeClass
    public static void init() throws InitializationError {
        InputStream resourceAsStream = DTableTesterTest.class.getResourceAsStream("/org/kie/junit/examples/decisiontable/ExamplePolicyPricing.xls");
        assertNotNull("Unable to find DTable Model to initialize Test!",resourceAsStream);

        statelessKieSessionTest = KieTestHelper.newStatelessSession()
                .addDTable(resourceAsStream)
                .build();
        assertNotNull("Failed to create StatelessKieSession!", statelessKieSessionTest);

        statefulKieSessionTest = KieTestHelper.newStatefulSession()
                .addDTable(resourceAsStream)
                .build();
        assertNotNull("Failed to create StatefulKieSession!", statefulKieSessionTest);
    }

    @Test
    public void testRuleFired_StatelessSession() throws Exception {
        internalTestMethod(statelessKieSessionTest);
    }

    @Ignore("All Stateful Session Tests are Failing")
    @Test
    public void testRuleFired_Session() throws Exception {
        internalTestMethod(statefulKieSessionTest);
    }

    private void internalTestMethod(KieTestHelper kieTestHelper) throws InitializationError {
        //now create some test data
        Driver driver = new Driver();
        Policy policy = new Policy();

        KieUnitTest.withTester(kieTestHelper)
                .addObjects(Arrays.asList(new Object[]{driver, policy}))
                .verifyRuleFired("Pricing bracket_18")
                .verifyRuleFired("Discounts_34")
                .runTest();

        Assert.assertEquals(120, policy.getBasePrice());
        Assert.assertEquals(20, policy.getDiscountPercent());
    }
}
