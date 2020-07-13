package tech.ikora.smells.checks;

import org.junit.jupiter.api.Test;
import tech.ikora.builder.BuildResult;
import tech.ikora.builder.Builder;
import tech.ikora.model.Project;
import tech.ikora.model.TestCase;
import tech.ikora.smells.SmellResult;

import static org.junit.jupiter.api.Assertions.*;

class ConditionalAssertionCheckTest {
    @Test
    void testWithOneDirectConditionalAssertion(){
        String code =
                "*** Test Cases ***\n" +
                "Some very interesting test\n" +
                "    Make some clever assertion\n" +
                "\n" +
                "*** Keywords ***\n" +
                "Make some clever assertion\n" +
                "    Run Keyword If  ${True}  Should be empty  ${Container}\n" +
                "\n" +
                "*** Variables ***\n" +
                "${Container}  Not Empty";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Some very interesting test").iterator().next();
        final ConditionalAssertionCheck check = new ConditionalAssertionCheck();
        final SmellResult metric = check.computeMetric(testCase, null);

        assertEquals(1., metric.getValue(), 0.0001);
    }
}