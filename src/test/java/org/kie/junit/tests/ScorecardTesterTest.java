package org.kie.junit.tests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runners.model.InitializationError;
import org.kie.api.definition.type.FactType;
import org.kie.junit.KieTestHelper;
import org.kie.junit.KieUnitTest;

import static org.junit.Assert.assertEquals;

public class ScorecardTesterTest {

    KieTestHelper statelessKieSessionTest;

    @Before
    public void init() throws InitializationError {
        statelessKieSessionTest = KieTestHelper.newStatelessSession()
                .addSCard(ScorecardTesterTest.class.getResourceAsStream("/org/kie/junit/examples/scorecards/scoremodel_c.sxls"))
                .build();
    }

    @Test
    public void testRuleFired() throws Exception {
        FactType scorecardType = statelessKieSessionTest.getKieBase().getFactType("org.kie.junit.examples.scorecards", "SampleScore");
        Object scorecard = scorecardType.newInstance();
        scorecardType.set(scorecard, "age", 10);

        KieUnitTest.withTester(statelessKieSessionTest)
                .addObject(scorecard)
                .verifyRuleFired("PartialScore_SampleScore_ValidLicenseScore_8")
                .verifyRuleFired("PartialScore_SampleScore_AgeScore_10")
                .runTest();

        assertEquals(29.0, scorecardType.get(scorecard, "scorecard__calculatedScore"));
    }

}
