package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellResult;

import lu.uni.serval.ikora.core.builder.BuildResult;
import lu.uni.serval.ikora.core.builder.Builder;
import lu.uni.serval.ikora.core.model.Project;
import lu.uni.serval.ikora.core.model.TestCase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SensitiveLocatorCheckTest {
    @ParameterizedTest
    @CsvSource ({
            "css:username_field,0.",
            "css:.covid-form > div > div.react-grid-Container > div > div,1.",
            "xpath:/html/body/div[2]/div[1]/div/h4[1]/b/html[1]/body[1]/div[2]/div[1]/div[1]/h4[1]/b[1],1."
    })
    void testHardcodedLocator(String locator, double normalizedValue){
        final String code =
                "*** Test Cases ***\n" +
                "Write in text field\n" +
                "    Input Text    " + locator + "    Bob";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Write in text field").iterator().next();
        final SensitiveLocatorCheck check = new SensitiveLocatorCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(normalizedValue, metric.getNormalizedValue(), 0.0001);
    }

    @Test
    void testIndirectFromVariableTableSimpleXmlLocator(){
        final String code =
                "*** Test Cases ***\n" +
                "Write in text field\n" +
                "    Input Text    ${locator}    Bob\n" +
                "\n" +
                "*** Variables ***\n" +
                "${locator}    css:username_field";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Write in text field").iterator().next();
        final SensitiveLocatorCheck check = new SensitiveLocatorCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(0., metric.getNormalizedValue(), 0.0001);
    }

    @Test
    void testIndirectFromVariableTableComplexXmlLocator(){
        final String code =
                "*** Test Cases ***\n" +
                "Write in text field\n" +
                "    Input Text    ${locator}    Bob\n" +
                "\n" +
                "*** Variables ***\n" +
                "${locator}    css:.covid-form > div > div.react-grid-Container > div > div";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Write in text field").iterator().next();
        final SensitiveLocatorCheck check = new SensitiveLocatorCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(1., metric.getNormalizedValue(), 0.0001);
    }

    @Test
    void testIndirectFromKeywordParametersComplexXmlLocator(){
        final String code =
                "*** Test Cases ***\n" +
                "Write in text field\n" +
                "    Input Name Bob    css:.covid-form > div > div.react-grid-Container > div > div\n" +
                "\n" +
                "*** Keywords ***\n" +
                "Input Name Bob\n" +
                "    [Arguments]    ${locator}\n" +
                "    Input Text    ${locator}    Bob\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Write in text field").iterator().next();
        final SensitiveLocatorCheck check = new SensitiveLocatorCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(1., metric.getNormalizedValue(), 0.0001);
    }

    @Test
    void testIndirectFromKeywordParametersAndVariableTableComplexXmlLocator(){
        final String code =
                "*** Test Cases ***\n" +
                "Write in text field\n" +
                "    Input Name Bob    ${locator_global}\n" +
                "\n" +
                "*** Keywords ***\n" +
                "Input Name Bob\n" +
                "    [Arguments]    ${locator}\n" +
                "    Input Text    ${locator}    Bob\n" +
                "\n" +
                "*** Variables ***\n" +
                "${locator_global}    css:.covid-form > div > div.react-grid-Container > div > div";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Write in text field").iterator().next();
        final SensitiveLocatorCheck check = new SensitiveLocatorCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(1., metric.getNormalizedValue(), 0.0001);
    }
}
