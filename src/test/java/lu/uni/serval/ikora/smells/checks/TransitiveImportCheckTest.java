package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.core.builder.BuildResult;
import lu.uni.serval.ikora.core.builder.Builder;
import lu.uni.serval.ikora.core.model.Project;
import lu.uni.serval.ikora.core.model.TestCase;
import lu.uni.serval.ikora.smells.SmellCheck;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransitiveImportCheckTest {
    @Test
    void testBuiltIn(){
        final String code =
            "*** Test Cases ***\n" +
            "Test with BuiltIn call\n" +
            "\tLog\tHello there!\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Test with BuiltIn call").iterator().next();
        final SmellCheck check = new TransitiveImportCheck();
        final SmellResult metric = check.computeMetric(testCase, new SmellConfiguration());

        assertEquals(0, metric.getRawValue());
        assertEquals(0, metric.getNormalizedValue());
        assertEquals(0, metric.getNodes().size());
    }
}