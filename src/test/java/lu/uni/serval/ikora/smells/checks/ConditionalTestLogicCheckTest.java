package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.smells.SmellCheck;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellResult;
import org.junit.jupiter.api.Test;
import lu.uni.serval.ikora.builder.BuildResult;
import lu.uni.serval.ikora.builder.Builder;
import lu.uni.serval.ikora.model.Project;
import lu.uni.serval.ikora.model.TestCase;

import static org.junit.jupiter.api.Assertions.*;

class ConditionalTestLogicCheckTest {
    @Test
    void testWithNoCondition(){
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

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final SmellCheck check = new ConditionalTestLogicCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(0., metric.getNormalizedValue(), 0.0001);
    }

    @Test
    void testWithOneCondition(){
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
                        "    Run Keyword If    ${True}    Maximize Browser Window\n" +
                        "    Title Should Be    ${Login Page}\n" +
                        "\n" +
                        "*** Variables ***\n" +
                        "${DELAY}      0\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final SmellCheck check = new ConditionalTestLogicCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(0.16666, metric.getNormalizedValue(), 0.0001);
    }

    @Test
    void testWithTwoConditionsOnSameLine(){
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
                        "    Run Keyword If    ${True}    Run Keyword If    ${True}    Maximize Browser Window\n" +
                        "    Title Should Be    ${Login Page}\n" +
                        "\n" +
                        "*** Variables ***\n" +
                        "${DELAY}      0\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final SmellCheck check = new ConditionalTestLogicCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(0.285714, metric.getNormalizedValue(), 0.0001);
    }
}