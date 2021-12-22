package lu.uni.serval.ikora.smells.checks;

/*-
 * #%L
 * Ikora Smells
 * %%
 * Copyright (C) 2020 - 2021 University of Luxembourg
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
                "*** Settings ***\n" +
                "Library    Selenium2Library\n" +
                "Library    OperatingSystem\n" +
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
