package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.core.model.TestCase;
import lu.uni.serval.ikora.smells.Helpers;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HidingTestDataCheckTest {
    @Test
    void testWithHiddenTestData(){
        final String code =
                "*** Test Cases ***\n" +
                "Valid Login\n" +
                "    [Setup]    Open Browser To Login Page\n" +
                "    User logs in with password\n" +
                "\n" +
                "*** Keywords ***\n" +
                "Open Browser To Login Page\n" +
                "    ${jsonfile}    Get File     /file/path/data.json\n" +
                "    Set Suite Variable    ${jsonfile.username}\n" +
                "    Set Suite Variable    ${jsonfile.password}\n" +
                "    Open Browser    http://localhost/    chrome\n" +
                "    Maximize Browser Window\n" +
                "\n" +
                "User logs in with password\n" +
                "    Input Text    username_field    ${username}\n" +
                "    Input Text    login_button    ${password}\n" +
                "    Click Button    login_button\n";

        final TestCase testCase = Helpers.getTestCase(code, "Valid Login");
        final HidingTestDataCheck check = new HidingTestDataCheck();
        final SmellResult metric = check.computeMetric(testCase, new SmellConfiguration());

        assertEquals(1., metric.getRawValue());
    }
}
