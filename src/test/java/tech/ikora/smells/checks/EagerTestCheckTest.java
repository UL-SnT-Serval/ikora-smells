package tech.ikora.smells.checks;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tech.ikora.builder.BuildResult;
import tech.ikora.builder.Builder;
import tech.ikora.model.Project;
import tech.ikora.model.TestCase;
import tech.ikora.smells.SmellResult;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class EagerTestCheckTest {
    private static String codeLogin;

    @BeforeAll
    static void loadProject(){
        codeLogin =
                "*** Settings ***\n" +
                "Documentation     A test suite with a single Gherkin style test.\n" +
                "...\n" +
                "...               This test is functionally identical to the example in\n" +
                "...               valid_login.robot file.\n" +
                "Library           SeleniumLibrary\n" +
                "Test Teardown     Close Browser\n" +
                "\n" +
                "*** Test Cases ***\n" +
                "Valid Login\n" +
                "    Given browser is opened to login page\n" +
                "    When user \"demo\" logs in with password \"mode\"\n" +
                "    Then welcome page should be open\n" +
                "\n" +
                "*** Keywords ***\n" +
                "Browser is opened to login page\n" +
                "    Open browser to login page\n" +
                "\n" +
                "User \"${username}\" logs in with password \"${password}\"\n" +
                "    Input username    ${username}\n" +
                "    Input password    ${password}\n" +
                "    Submit credentials\n" +
                "\n" +
                "Open Browser To Login Page\n" +
                "    Open Browser    ${LOGIN URL}    ${BROWSER}\n" +
                "    Maximize Browser Window\n" +
                "    Sleep 5\n" +
                "    Login Page Should Be Open\n" +
                "\n" +
                "Login Page Should Be Open\n" +
                "    Title Should Be    Login Page\n" +
                "\n" +
                "Go To Login Page\n" +
                "    Go To    ${LOGIN URL}\n" +
                "    Login Page Should Be Open\n" +
                "\n" +
                "Input Username\n" +
                "    [Arguments]    ${username}\n" +
                "    Input Text    username_field    ${username}\n" +
                "\n" +
                "Input Password\n" +
                "    [Arguments]    ${password}\n" +
                "    Input Text    ${PASSWORD_FIELD}    ${password}\n" +
                "\n" +
                "Submit Credentials\n" +
                "    Click Button    login_button\n" +
                "\n" +
                "Welcome Page Should Be Open\n" +
                "    Location Should Be    ${WELCOME URL}\n" +
                "    Title Should Be    Welcome Page\n" +
                "\n" +
                "*** Variables ***\n" +
                "${SERVER}         localhost:7272\n" +
                "${BROWSER}        Firefox\n" +
                "${DELAY}          0\n" +
                "${VALID USER}     demo\n" +
                "${VALID PASSWORD}    mode\n" +
                "${LOGIN URL}      http://${SERVER}/\n" +
                "${WELCOME URL}    http://${SERVER}/welcome.html\n" +
                "${ERROR URL}      http://${SERVER}/error.html\n" +
                "${PASSWORD_FIELD}      password_field\n";
    }

    @Test
    void testDetectionWithExampleProject(){
        final BuildResult build = Builder.build(codeLogin, true);
        final Project project = build.getProjects().iterator().next();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final EagerTestCheck check = new EagerTestCheck();
        final SmellResult metric = check.computeMetric(testCase, null);

        assertThat(metric.getValue(), allOf(greaterThan(0.0), lessThan(1.0)));
    }
}
