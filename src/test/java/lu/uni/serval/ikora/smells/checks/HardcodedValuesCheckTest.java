package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellResult;
import org.junit.jupiter.api.Test;
import lu.uni.serval.ikora.builder.BuildResult;
import lu.uni.serval.ikora.builder.Builder;
import lu.uni.serval.ikora.model.Project;
import lu.uni.serval.ikora.model.TestCase;

import static org.junit.jupiter.api.Assertions.*;

public class HardcodedValuesCheckTest {
    @Test
    void testWithOneHardcodedValue(){
        final String code =
                "*** Test Cases ***\n" +
                "Valid Login\n" +
                "    User \\\"demo\\\" logs in with password \\\"mode\\\"\n" +
                "\n" +
                "*** Keywords ***\n" +
                "\n" +
                "User \\\"${username}\\\" logs in with password \\\"${password}\\\"\n" +
                "    Input username    ${username}\n" +
                "    Input password    ${password}\n" +
                "    Submit credentials\n" +
                "\n" +
                "Input Username\n" +
                "    [Arguments]    ${username}\n" +
                "    Input Text    ${USERNAME_FIELD}    ${username}\n" +
                "\n" +
                "Input Password\n" +
                "    [Arguments]    ${password}\n" +
                "    Input Text    password_field    ${password}\n" +
                "\n" +
                "Submit Credentials\n" +
                "    Click Button    ${BOTTON_FIELD}\n" +
                "\n" +
                "*** Variables ***\n" +
                "${USERNAME_FIELD}      username_field\n" +
                "${BOTTON_FIELD}        login_button";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final HardcodedValuesCheck check = new HardcodedValuesCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(0.14285, metric.getValue(), 0.0001);
    }
}
