package org.kie.junit.tests;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.model.InitializationError;
import org.kie.api.KieBase;
import org.kie.api.definition.type.FactType;
import org.kie.junit.KieTestHelper;
import org.kie.junit.KieUnitTest;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ScorecardTesterTest {

    protected static KieTestHelper statelessKieSessionTest;
    protected static KieTestHelper statefulKieSessionTest;

    @BeforeClass
    public static void init() throws InitializationError {
        InputStream resourceAsStream = ScorecardTesterTest.class.getResourceAsStream("/org/kie/junit/examples/scorecards/scoremodel_c.sxls");
        assertNotNull("Unable to find scorecardModel to initialize Test!",resourceAsStream);

        statelessKieSessionTest = KieTestHelper.newStatelessSession()
                .addSCard(resourceAsStream)
                .build();
        assertNotNull("Failed to create StatelessKieSession!", statelessKieSessionTest);

        statefulKieSessionTest = KieTestHelper.newStatefulSession()
                .addSCard(resourceAsStream)
                .build();
        assertNotNull("Failed to create KieSession!", statefulKieSessionTest);
    }

    @Test
    public void testRuleFired_StatelessSession() throws Exception {
        KieBase kieBase = statelessKieSessionTest.getKieBase();
        testRuleFiredFromKBase(kieBase);
    }

    @Ignore("All Stateful Session Tests are Failing")
    @Test
    public void testRuleFired_Session() throws Exception {
        KieBase kieBase = statefulKieSessionTest.getKieBase();
        testRuleFiredFromKBase(kieBase);
    }

    private void testRuleFiredFromKBase(KieBase kieBase) throws Exception {
        FactType scorecardType = kieBase.getFactType("org.kie.junit.examples.scorecards", "SampleScore");
        Object scorecard = scorecardType.newInstance();
        scorecardType.set(scorecard, "age", 10);

        KieUnitTest.withTester(statelessKieSessionTest)
                .addObject(scorecard)
                .verifyRuleFired("Create SampleScore Output Bean")
                .runTest();

        assertEquals(29.0, scorecardType.get(scorecard, "scorecard__calculatedScore"));
    }

}
