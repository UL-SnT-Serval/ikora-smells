package lu.uni.serval.ikora.smells;

import lu.uni.serval.ikora.core.model.TestCase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SmellDetectorTest {
    private static final int NUMBER_METRICS = 20;
    @Test
    void testSizeAll(){
        assertEquals(NUMBER_METRICS, SmellDetector.all().getNumberMetrics());
    }

    @Test
    void testSizeComputeAllMetrics(){
        final String code =
                "*** Test Cases ***\n" +
                "Valid Login\n" +
                "    Open Browser To   Login Page\n" +
                "\n" +
                "*** Keywords ***\n" +
                "Open Browser To\n" +
                "    [Arguments]    ${Login Page}\n" +
                "    Open Browser    http://localhost/    chrome\n" +
                "    Set Selenium Speed    ${DELAY}\n" +
                "    Maximize Browser Window\n" +
                "    Title Should Be    ${Login Page}\n" +
                "\n" +
                "*** Variables ***\n" +
                "${DELAY}      0\n";

        final TestCase testCase = Helpers.getTestCase(code, "Valid Login");
        final SmellResults smellResults = SmellDetector.all().computeMetrics(testCase, new SmellConfiguration());

        assertEquals(NUMBER_METRICS, smellResults.getNumberMetrics());
    }
}