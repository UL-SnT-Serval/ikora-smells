package tech.ikora.smells.checks;

import org.junit.jupiter.api.Test;
import tech.ikora.builder.BuildResult;
import tech.ikora.builder.Builder;
import tech.ikora.model.Project;
import tech.ikora.model.TestCase;
import tech.ikora.smells.SmellCheck;
import tech.ikora.smells.SmellConfiguration;
import tech.ikora.smells.SmellResult;

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

        assertEquals(0., metric.getValue(), 0.0001);
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

        assertEquals(0.16666, metric.getValue(), 0.0001);
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

        assertEquals(0.285714, metric.getValue(), 0.0001);
    }
}