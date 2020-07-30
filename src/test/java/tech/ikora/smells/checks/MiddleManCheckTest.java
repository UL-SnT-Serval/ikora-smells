package tech.ikora.smells.checks;

import org.junit.jupiter.api.Test;
import tech.ikora.builder.BuildResult;
import tech.ikora.builder.Builder;
import tech.ikora.model.Project;
import tech.ikora.model.TestCase;
import tech.ikora.smells.SmellConfiguration;
import tech.ikora.smells.SmellResult;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MiddleManCheckTest {
    @Test
    void testWithOneIndirection(){
        final String code =
                "*** Test Cases ***\n" +
                "Valid Login\n" +
                "    User \"demo\" logs in with password \"mode\"\n" +
                "\n" +
                "*** Keywords ***\n" +
                "\n" +
                "User \"${username}\" logs in with password \"${password}\"\n" +
                "    Input username    ${username}\n" +
                "\n" +
                "Input Username\n" +
                "    [Arguments]    ${username}\n" +
                "    Input Text    username_field    ${username}\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final MiddleManCheck check = new MiddleManCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(0.5, metric.getValue(), 0.0001);
    }

    @Test
    void testWithNoIndirection(){
        final String code =
                "*** Test Cases ***\n" +
                        "Valid Login\n" +
                        "    User \"demo\" logs in with password \"mode\"\n" +
                        "\n" +
                        "*** Keywords ***\n" +
                        "\n" +
                        "User \"${username}\" logs in with password \"${password}\"\n" +
                        "    [Arguments]    ${username}\n" +
                        "    Input Text    username_field    ${username}\n";


        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final MiddleManCheck check = new MiddleManCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(metric.getValue(), 0., 0.0001);
    }
}
