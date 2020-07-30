package tech.ikora.smells.checks;

import org.junit.jupiter.api.Test;
import tech.ikora.builder.BuildResult;
import tech.ikora.builder.Builder;
import tech.ikora.model.Project;
import tech.ikora.model.TestCase;
import tech.ikora.smells.SmellConfiguration;
import tech.ikora.smells.SmellResult;

import static org.junit.jupiter.api.Assertions.*;

class OverCheckingCheckTest {
    @Test
    void testWithNoAssertion(){
        final String code =
                "*** Test Cases ***\n" +
                        "Valid Login\n" +
                        "    Open Browser To Login Page\n" +
                        "\n" +
                        "*** Keywords ***\n" +
                        "Open Browser To Login Page\n" +
                        "    Open Browser    http://localhost/    chrome\n" +
                        "    Set Selenium Speed    ${DELAY}\n" +
                        "    Maximize Browser Window\n" +
                        "\n" +
                        "*** Variables ***\n" +
                        "${DELAY}      0\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final OverCheckingCheck check = new OverCheckingCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(0., metric.getValue(), 0.0001);
    }

    @Test
    void testWithOneAssertion(){
        final String code =
                "*** Test Cases ***\n" +
                        "Valid Login\n" +
                        "    Open Browser To Login Page\n" +
                        "\n" +
                        "*** Keywords ***\n" +
                        "Open Browser To Login Page\n" +
                        "    Open Browser    http://localhost/    chrome\n" +
                        "    Set Selenium Speed    ${DELAY}\n" +
                        "    Maximize Browser Window\n" +
                        "    Title Should Be    Login Page\n" +
                        "\n" +
                        "*** Variables ***\n" +
                        "${DELAY}      0\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final OverCheckingCheck check = new OverCheckingCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(0.2, metric.getValue(), 0.0001);
    }

    @Test
    void testWithManyAssertions(){
        final String code =
                "*** Test Cases ***\n" +
                        "Valid Login\n" +
                        "    Open Browser To Login Page\n" +
                        "\n" +
                        "*** Keywords ***\n" +
                        "Open Browser To Login Page\n" +
                        "    Open Browser    http://localhost/    chrome\n" +
                        "    Should Be Equal    ${Delay}    0\n" +
                        "    Should Be Equal As Numbers    ${Delay}    0\n" +
                        "    Set Selenium Speed    ${DELAY}\n" +
                        "    Maximize Browser Window\n" +
                        "    Title Should Be    Login Page\n" +
                        "    Page Should Not Contain Image    http://localhost/\n" +
                        "\n" +
                        "*** Variables ***\n" +
                        "${DELAY}      0\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();
        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final OverCheckingCheck check = new OverCheckingCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(0.5, metric.getValue(), 0.0001);
    }
}