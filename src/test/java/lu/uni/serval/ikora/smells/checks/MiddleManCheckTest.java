package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellResult;
import org.junit.jupiter.api.Test;
import lu.uni.serval.ikora.builder.BuildResult;
import lu.uni.serval.ikora.builder.Builder;
import lu.uni.serval.ikora.model.Project;
import lu.uni.serval.ikora.model.TestCase;

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

        assertEquals(0.5, metric.getNormalizedValue(), 0.0001);
        assertEquals(1., metric.getRawValue(), 0.0001);
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

        assertEquals(metric.getNormalizedValue(), 0., 0.0001);
        assertEquals(0., metric.getRawValue(), 0.0001);
    }
}
