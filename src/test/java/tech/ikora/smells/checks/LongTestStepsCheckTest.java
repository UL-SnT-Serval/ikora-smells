package tech.ikora.smells.checks;

import org.junit.jupiter.api.Test;
import tech.ikora.builder.BuildResult;
import tech.ikora.builder.Builder;
import tech.ikora.model.Project;
import tech.ikora.model.TestCase;
import tech.ikora.smells.SmellCheck;
import tech.ikora.smells.SmellResult;

import static org.junit.jupiter.api.Assertions.*;

class LongTestStepsCheckTest {
    @Test
    void testShortStep(){
        final String code =
                "*** Test Cases ***\n" +
                "Valid Login\n" +
                "    User \"demo\" logs in with password \"mode\"\n" +
                "\n" +
                "*** Keywords ***\n" +
                "User \"${username}\" logs in with password \"${password}\"\n" +
                "    Input Text    username_field    ${username}\n" +
                "    Input Text    login_button    ${password}\n" +
                "    Click Button    login_button\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final SmellCheck check = new LongTestStepsCheck();
        final SmellResult metric = check.computeMetric(testCase, null);

        assertEquals(0., metric.getValue(), 0.0001);
    }

    @Test
    void testLongStep(){
        final String code =
                "*** Test Cases ***\n" +
                "Valid Login\n" +
                "    User \"demo\" logs in with password \"mode\"\n" +
                "\n" +
                "*** Keywords ***\n" +
                "\n" +
                "User \"${username}\" logs in with password \"${password}\"\n" +
                "    Open Browser    http://localhost/    chrome\n" +
                "    Should Be Equal    ${Delay}    0\n" +
                "    Should Be Equal As Numbers    ${Delay}    0\n" +
                "    Set Selenium Speed    ${DELAY}\n" +
                "    Maximize Browser Window\n" +
                "    Title Should Be    Login Page\n" +
                "    Page Should Not Contain Image    http://localhost/\n" +
                "    Input Text    username_field    ${username}\n" +
                "    Input Text    login_button    ${password}\n" +
                "    Click Button    login_button\n" +
                "    Title Should Be    Login Page\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final SmellCheck check = new LongTestStepsCheck();
        final SmellResult metric = check.computeMetric(testCase, null);

        assertEquals(1., metric.getValue(), 0.0001);
    }
}