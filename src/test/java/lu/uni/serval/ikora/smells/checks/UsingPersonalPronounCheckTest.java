package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellResult;
import org.junit.jupiter.api.Test;
import lu.uni.serval.ikora.builder.BuildResult;
import lu.uni.serval.ikora.builder.Builder;
import lu.uni.serval.ikora.model.Project;
import lu.uni.serval.ikora.model.TestCase;

import static org.junit.jupiter.api.Assertions.*;

public class UsingPersonalPronounCheckTest {
    @Test
    void testWithNoPronoun(){
        final String code =
                "*** Test Cases ***\n" +
                "\n" +
                "Test case without personal pronoun\n" +
                "    Given browser is opened to login page\n" +
                "    When user \"demo\" logs in with password \"mode\"\n" +
                "    Then welcome page should be open\n" +
                "\n" +
                "*** Keywords ***\n" +
                "Browser is opened to login page\n" +
                "    Log    Browser is opened to login page\n" +
                "\n" +
                "User \"${username}\" logs in with password \"${password}\"\n" +
                "    Log    ${username}\n" +
                "    Log    ${password}\n" +
                "\n" +
                "Welcome page should be open\n" +
                "    Log    Welcome page should be open\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Test case without personal pronoun").iterator().next();
        final UsingPersonalPronounCheck check = new UsingPersonalPronounCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(0., metric.getValue(), 0.0001);
    }

    @Test
    void testWithAllPronoun(){
        final String code =
                "*** Test Cases ***\n" +
                "\n" +
                "Test case with all personal pronoun\n" +
                "    Given I am on login page\n" +
                "    When I put my usernam and password\n" +
                "    Then I should be on the welcome page\n" +
                "\n" +
                "*** Keywords ***\n" +
                "I am on login page\n" +
                "    Log    I am on login page\n" +
                "\n" +
                "I put my usernam and password\n" +
                "    Log    I put my usernam and password\n" +
                "\n" +
                "I should be on the welcome page\n" +
                "    Log    I should be on the welcome page";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Test case with all personal pronoun").iterator().next();
        final UsingPersonalPronounCheck check = new UsingPersonalPronounCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(1., metric.getValue(), 0.0001);
    }

    @Test
    void testWithSomePronoun(){
        final String code =
                "*** Test Cases ***\n" +
                "\n" +
                "Test case with some personal pronoun\n" +
                "    Given I am on login page\n" +
                "    When I put my usernam and password\n" +
                "    Then welcome page should be open\n" +
                "\n" +
                "*** Keywords ***\n" +
                "Browser is opened to login page\n" +
                "    Log    Browser is opened to login page\n" +
                "\n" +
                "User \"${username}\" logs in with password \"${password}\"\n" +
                "    Log    ${username}\n" +
                "    Log    ${password}\n" +
                "\n" +
                "Welcome page should be open\n" +
                "    Log    Welcome page should be open\n" +
                "\n" +
                "I am on login page\n" +
                "    Log    I am on login page\n" +
                "\n" +
                "I put my usernam and password\n" +
                "    Log    I put my usernam and password\n" +
                "\n" +
                "I should be on the welcome page\n" +
                "    Log    I should be on the welcome page";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Test case with some personal pronoun").iterator().next();
        final UsingPersonalPronounCheck check = new UsingPersonalPronounCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(0.6666, metric.getValue(), 0.0001);
    }
}
