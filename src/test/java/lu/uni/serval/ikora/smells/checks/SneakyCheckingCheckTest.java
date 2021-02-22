package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellResult;
import org.junit.jupiter.api.Test;
import lu.uni.serval.ikora.builder.BuildResult;
import lu.uni.serval.ikora.builder.Builder;
import lu.uni.serval.ikora.model.Project;
import lu.uni.serval.ikora.model.TestCase;

import static org.junit.jupiter.api.Assertions.*;

class SneakyCheckingCheckTest {
    @Test
    void testWithBlockOfOneSneakyAssertion(){
        final String code =
                "*** Test Cases ***\n" +
                "Valid Login\n" +
                "    Input username    user\n" +
                "    Input password    password\n" +
                "    Submit Credentials\n" +
                "    Welcome Page Should Be Open\n" +
                "\n" +
                "*** Keywords ***\n" +
                "Input Username\n" +
                "    [Arguments]    ${username}\n" +
                "    Input Text    username_field    ${username}\n" +
                "\n" +
                "Input Password\n" +
                "    [Arguments]    ${password}\n" +
                "    Input Text    password_field    ${password}\n" +
                "\n" +
                "Submit Credentials\n" +
                "    Click Button    login_button\n" +
                "\n" +
                "Welcome Page Should Be Open\n" +
                "    Location Should Be    http://example.com/welcome.html\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final SneakyCheckingCheck check = new SneakyCheckingCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(0.25, metric.getValue(), 0.0001);
    }

    @Test
    void testWithBlockOfOneSneakyAssertionWithLogging(){
        final String code =
                "*** Test Cases ***\n" +
                        "Valid Login\n" +
                        "    Input username    user\n" +
                        "    Input password    password\n" +
                        "    Submit Credentials\n" +
                        "    Welcome Page Should Be Open\n" +
                        "\n" +
                        "*** Keywords ***\n" +
                        "Input Username\n" +
                        "    [Arguments]    ${username}\n" +
                        "    Input Text    username_field    ${username}\n" +
                        "\n" +
                        "Input Password\n" +
                        "    [Arguments]    ${password}\n" +
                        "    Input Text    password_field    ${password}\n" +
                        "\n" +
                        "Submit Credentials\n" +
                        "    Click Button    login_button\n" +
                        "\n" +
                        "Welcome Page Should Be Open\n" +
                        "    Log    Asserting that page has the correct location\n" +
                        "    Location Should Be    http://example.com/welcome.html\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final SneakyCheckingCheck check = new SneakyCheckingCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(0.25, metric.getValue(), 0.0001);
    }

    @Test
    void testWithBlockOfTwoSneakyAssertion(){
        final String code =
                "*** Test Cases ***\n" +
                        "Valid Login\n" +
                        "    Input username    user\n" +
                        "    Input password    password\n" +
                        "    Submit Credentials\n" +
                        "    Welcome Page Should Be Open\n" +
                        "\n" +
                        "*** Keywords ***\n" +
                        "Input Username\n" +
                        "    [Arguments]    ${username}\n" +
                        "    Input Text    username_field    ${username}\n" +
                        "\n" +
                        "Input Password\n" +
                        "    [Arguments]    ${password}\n" +
                        "    Input Text    password_field    ${password}\n" +
                        "\n" +
                        "Submit Credentials\n" +
                        "    Click Button    login_button\n" +
                        "\n" +
                        "Welcome Page Should Be Open\n" +
                        "    Location Should Be    http://example.com/welcome.html\n" +
                        "    Title Should Be    Welcome Page\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final SneakyCheckingCheck check = new SneakyCheckingCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(0.25, metric.getValue(), 0.0001);
    }

    @Test
    void testWithNoSneakyAssertion(){
        final String code =
                "*** Test Cases ***\n" +
                        "Valid Login\n" +
                        "    Input username    user\n" +
                        "    Input password    password\n" +
                        "    Submit Credentials\n" +
                        "    Welcome Page Should Be Open\n" +
                        "    Location Should Be    http://example.com/welcome.html\n" +
                        "    Title Should Be    Welcome Page\n" +
                        "\n" +
                        "*** Keywords ***\n" +
                        "Input Username\n" +
                        "    [Arguments]    ${username}\n" +
                        "    Input Text    username_field    ${username}\n" +
                        "\n" +
                        "Input Password\n" +
                        "    [Arguments]    ${password}\n" +
                        "    Input Text    password_field    ${password}\n" +
                        "\n" +
                        "Submit Credentials\n" +
                        "    Click Button    login_button\n" +
                        "\n" +
                        "Welcome Page Should Be Open\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final SneakyCheckingCheck check = new SneakyCheckingCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(0., metric.getValue(), 0.0001);
    }
}