package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.core.model.TestCase;
import lu.uni.serval.ikora.smells.Helpers;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellResult;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class MissingDocumentationCheckTest {
    @ParameterizedTest
    @CsvSource({
            ",1.,1.",
            "[Documentation]    Valid Login,0.,0.",
            "[Documentation]    Verifies that the login action works as expected,0.,0."
    })
    void testMissingDocumentation(String documentation, double normalized, double raw){
        final String code =
                "*** Test Cases ***\n" +
                        "Valid Login\n" +
                        "    Open Browser To Login Page\n" +
                        "\n" +
                        "*** Keywords ***\n" +
                        "Open Browser To Login Page\n" +
                        "    " + documentation + "\n" +
                        "    Open Browser    http://localhost/    chrome\n" +
                        "    Maximize Browser Window\n" +
                        "    Title Should Be    Login Page\n";

        final TestCase testCase = Helpers.getTestCase(code, "Valid Login");
        final SmellResult metric = new MissingDocumentationCheck().computeMetric(testCase, new SmellConfiguration());

        assertEquals(normalized, metric.getNormalizedValue(), 0.0001);
        assertEquals(raw, metric.getRawValue(), 0.0001);
    }
}