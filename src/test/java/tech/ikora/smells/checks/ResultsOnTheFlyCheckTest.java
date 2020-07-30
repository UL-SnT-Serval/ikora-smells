package tech.ikora.smells.checks;

import org.junit.jupiter.api.Test;
import tech.ikora.builder.BuildResult;
import tech.ikora.builder.Builder;
import tech.ikora.model.Project;
import tech.ikora.model.TestCase;
import tech.ikora.smells.SmellResult;

import static org.junit.jupiter.api.Assertions.*;

class ResultsOnTheFlyCheckTest {
    @Test
    void testWithNoResultsOnTheFly(){
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

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final ResultsOnTheFlyCheck check = new ResultsOnTheFlyCheck();
        final SmellResult metric = check.computeMetric(testCase, null);

        assertEquals(0., metric.getValue(), 0.0001);
    }

    @Test
    void testWithResultsOnTheFlyFromVariableAssignment(){
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
                "    Title Should Be    ${LOGIN PAGE}\n" +
                "\n" +
                "*** Variables ***\n" +
                "${DELAY}      0\n" +
                "${LOGIN PAGE}    Login Page";


        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final ResultsOnTheFlyCheck check = new ResultsOnTheFlyCheck();
        final SmellResult metric = check.computeMetric(testCase, null);

        assertEquals(0., metric.getValue(), 0.0001);
    }

    @Test
    void testWithResultsOnTheFlyFromKeywordAssignment(){
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
                "    ${LOGIN PAGE}=    Get Element Attribute    title_page\n" +
                "    Title Should Be    ${LOGIN PAGE}\n" +
                "\n" +
                "*** Variables ***\n" +
                "${DELAY}      0\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final ResultsOnTheFlyCheck check = new ResultsOnTheFlyCheck();
        final SmellResult metric = check.computeMetric(testCase, null);

        assertEquals(1., metric.getValue(), 0.0001);
    }

    @Test
    void testWithResultsOnTheFlyKeywordAssignment(){
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

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final ResultsOnTheFlyCheck check = new ResultsOnTheFlyCheck();
        final SmellResult metric = check.computeMetric(testCase, null);

        assertEquals(1., metric.getValue(), 0.0001);
    }
}