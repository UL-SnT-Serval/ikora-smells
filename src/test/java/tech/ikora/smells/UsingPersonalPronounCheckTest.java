package tech.ikora.smells;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tech.ikora.model.Project;
import tech.ikora.model.TestCase;
import tech.ikora.smells.checks.UsingPersonalPronounCheck;

import static org.junit.jupiter.api.Assertions.*;

public class UsingPersonalPronounCheckTest {
    private static Project project;

    @BeforeAll
    static void loadProject(){
        project = Helpers.compileProject("usingPersonalPronoun.robot");
    }

    @Test
    void testWithNoPronoun(){
        final TestCase testCase = project.findTestCase("usingPersonalPronoun", "Test case without personal pronoun").iterator().next();
        final UsingPersonalPronounCheck check = new UsingPersonalPronounCheck();
        final SmellMetric metric = check.computeMetric(testCase, null);

        assertEquals(metric.getValue(), 0.0);
    }

    @Test
    void testWithAllPronoun(){
        final TestCase testCase = project.findTestCase("usingPersonalPronoun", "Test case with all personal pronoun").iterator().next();
        final UsingPersonalPronounCheck check = new UsingPersonalPronounCheck();
        final SmellMetric metric = check.computeMetric(testCase, null);

        assertEquals(metric.getValue(), 1.0);
    }

    @Test
    void testWithSomePronoun(){
        final TestCase testCase = project.findTestCase("usingPersonalPronoun", "Test case with some personal pronoun").iterator().next();
        final UsingPersonalPronounCheck check = new UsingPersonalPronounCheck();
        final SmellMetric metric = check.computeMetric(testCase, null);

        assertEquals(metric.getValue(), 0.6666, 0.0001);
    }
}
