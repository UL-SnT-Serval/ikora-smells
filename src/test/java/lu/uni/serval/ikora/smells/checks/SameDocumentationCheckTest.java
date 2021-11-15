package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.core.model.TestCase;
import lu.uni.serval.ikora.smells.Helpers;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellResult;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class SameDocumentationCheckTest {
    @ParameterizedTest
    @CsvSource({
            ",0.,0",
            "[Documentation]    Valid Login,1.,1.",
            "[Documentation]    Verifies that the login action works as expected,0.,0."
    })
    void testSameDocumentation(String documentation, double normalized, double raw){
        final String code =
                "*** Test Cases ***\n" +
                "Valid Login\n" +
                "    " + documentation + "\n" +
                "    Open Browser    http://localhost/    chrome";

        final TestCase testCase = Helpers.getTestCase(code, "Valid Login");
        final SmellResult metric = new SameDocumentationCheck().computeMetric(testCase, new SmellConfiguration());

        assertEquals(normalized, metric.getNormalizedValue(), 0.0001);
        assertEquals(raw, metric.getRawValue(), 0.0001);
    }
}