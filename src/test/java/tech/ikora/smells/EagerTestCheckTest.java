package tech.ikora.smells;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tech.ikora.model.Project;
import tech.ikora.model.TestCase;
import tech.ikora.smells.checks.EagerTestCheck;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class EagerTestCheckTest {
    private static Project project;

    @BeforeAll
    static void loadProject(){
        project = Helpers.compileProjects("web-demo");
    }

    @Test
    void testCheck(){
        final TestCase testCase = Helpers.getTestCase(project, "Valid Login");
        final EagerTestCheck check = new EagerTestCheck();
        final SmellMetric metric = check.computeMetric(testCase);

        assertThat(metric.getValue(), allOf(greaterThan(0.0), lessThan(1.0)));
    }
}
