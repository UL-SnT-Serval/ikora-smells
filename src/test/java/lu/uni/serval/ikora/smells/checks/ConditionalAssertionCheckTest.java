package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellResult;
import org.junit.jupiter.api.Test;
import lu.uni.serval.ikora.builder.BuildResult;
import lu.uni.serval.ikora.builder.Builder;
import lu.uni.serval.ikora.model.Project;
import lu.uni.serval.ikora.model.TestCase;

import static org.junit.jupiter.api.Assertions.*;

class ConditionalAssertionCheckTest {
    @Test
    void testWithOneDirectConditionalAssertion(){
        final String code =
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

        final SmellConfiguration configuration = new SmellConfiguration();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Some very interesting test").iterator().next();
        final ConditionalAssertionCheck check = new ConditionalAssertionCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(1., metric.getNormalizedValue(), 0.0001);
    }
}