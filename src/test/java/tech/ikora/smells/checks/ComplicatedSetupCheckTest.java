package tech.ikora.smells.checks;

import org.junit.jupiter.api.Test;
import tech.ikora.builder.BuildResult;
import tech.ikora.builder.Builder;
import tech.ikora.model.Project;
import tech.ikora.model.TestCase;
import tech.ikora.smells.SmellResult;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComplicatedSetupCheckTest {
    @Test
    void testRatioWithSimpleSetup(){
        final String code =
                "*** Test Cases ***\n" +
                "Valid Login\n" +
                "    [Setup]    Open Browser To Login Page\n" +
                "    User \"demo\" logs in with password \"mode\"\n" +
                "\n" +
                "*** Keywords ***\n" +
                "Open Browser To Login Page\n" +
                "    Open Browser    http://localhost/    chrome\n" +
                "    Maximize Browser Window\n" +
                "\n" +
                "User \"${username}\" logs in with password \"${password}\"\n" +
                "    Input Text    username_field    ${username}\n" +
                "    Input Text    login_button    ${password}\n" +
                "    Click Button    login_button\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final ComplicatedSetupCheck check = new ComplicatedSetupCheck();
        final SmellResult metric = check.computeMetric(testCase, null);

        assertEquals(0.4, metric.getValue(), 0.0001);
    }

    @Test
    void testRatioWithNoSetup(){
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
        final ComplicatedSetupCheck check = new ComplicatedSetupCheck();
        final SmellResult metric = check.computeMetric(testCase, null);

        assertEquals(Double.NaN, metric.getValue(), 0.0001);
    }
}
