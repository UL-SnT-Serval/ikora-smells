package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellResult;

import lu.uni.serval.ikora.core.builder.BuildResult;
import lu.uni.serval.ikora.core.builder.Builder;
import lu.uni.serval.ikora.core.model.Project;
import lu.uni.serval.ikora.core.model.TestCase;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ComplicatedSetupCheckTest {
    @Test
    void testRatioWithSimpleSetup(){
        final String code =
                "*** Settings ***\n" +
                "Library    Selenium2Library\n" +
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

        final SmellConfiguration configuration = new SmellConfiguration();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final ComplicatedSetupCheck check = new ComplicatedSetupCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(0.4, metric.getNormalizedValue(), 0.0001);
    }

    @Test
    void testRatioWithNoSetup(){
        final String code =
                "*** Settings ***\n" +
                "Library    Selenium2Library\n" +
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

        final SmellConfiguration configuration = new SmellConfiguration();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final ComplicatedSetupCheck check = new ComplicatedSetupCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(Double.NaN, metric.getNormalizedValue(), 0.0001);
    }
}
