package org.kie.junit.tests;

import org.kie.junit.examples.decisiontable.Driver;
import org.kie.junit.examples.decisiontable.Policy;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.model.InitializationError;
import org.kie.junit.KieTestHelper;
import org.kie.junit.KieUnitTest;

import java.util.Arrays;

public class DTableTesterTest {

    KieTestHelper statelessKieSessionTest;

    @Before
    public void init() throws InitializationError {
        statelessKieSessionTest = KieTestHelper.newStatelessSession()
                .addDTable(DTableTesterTest.class.getResourceAsStream("/org/kie/junit/examples/decisiontable/ExamplePolicyPricing.xls"))
                .build();
    }

    @Test
    public void testRuleFired() throws Exception {
        //now create some test data
        Driver driver = new Driver();
        Policy policy = new Policy();

        KieUnitTest.withTester(statelessKieSessionTest)
                .addObjects(Arrays.asList(new Object[]{driver, policy}))
                .verifyRuleFired("Pricing bracket_18")
                .verifyRuleFired("Discounts_34")
                .runTest();

        Assert.assertEquals(120, policy.getBasePrice());
        Assert.assertEquals(20, policy.getDiscountPercent());
    }

}
