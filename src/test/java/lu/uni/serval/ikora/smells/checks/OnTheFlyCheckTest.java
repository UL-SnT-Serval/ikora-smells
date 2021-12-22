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

import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellResult;

import lu.uni.serval.ikora.core.builder.BuildResult;
import lu.uni.serval.ikora.core.builder.Builder;
import lu.uni.serval.ikora.core.model.Project;
import lu.uni.serval.ikora.core.model.TestCase;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OnTheFlyCheckTest {
    @Test
    void testWithNoResultsOnTheFly(){
        final String code =
                "*** Settings ***\n" +
                "Library    Selenium2Library\n" +
                "*** Test Cases ***\n" +
                "Valid Login\n" +
                "    Open Browser To Login Page\n" +
                "\n" +
                "*** Keywords ***\n" +
                "Open Browser To Login Page\n" +
                "    Open Browser    http://localhost/    chrome\n" +
                "    Set Selenium Speed    ${DELAY}\n" +
                "    Maximize Browser Window\n" +
                "    Title Should Be    Login Page\n" +
                "\n" +
                "*** Variables ***\n" +
                "${DELAY}      0\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();
        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final OnTheFlyCheck check = new OnTheFlyCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(0., metric.getNormalizedValue(), 0.0001);
        assertEquals(0., metric.getRawValue(), 0.0001);
    }

    @Test
    void testWithResultsOnTheFlyFromVariableAssignment(){
        final String code =
                "*** Settings ***\n" +
                "Library    Selenium2Library\n" +
                "*** Test Cases ***\n" +
                "Valid Login\n" +
                "    Open Browser To Login Page\n" +
                "\n" +
                "*** Keywords ***\n" +
                "Open Browser To Login Page\n" +
                "    Open Browser    http://localhost/    chrome\n" +
                "    Set Selenium Speed    ${DELAY}\n" +
                "    Maximize Browser Window\n" +
                "    Title Should Be    ${LOGIN PAGE}\n" +
                "\n" +
                "*** Variables ***\n" +
                "${DELAY}      0\n" +
                "${LOGIN PAGE}    Login Page";


        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();
        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final OnTheFlyCheck check = new OnTheFlyCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(0., metric.getNormalizedValue(), 0.0001);
        assertEquals(0., metric.getRawValue(), 0.0001);
    }

    @Test
    void testWithResultsOnTheFlyFromKeywordAssignment(){
        final String code =
                "*** Settings ***\n" +
                "Library    Selenium2Library\n" +
                "*** Test Cases ***\n" +
                "Valid Login\n" +
                "    Open Browser To Login Page\n" +
                "\n" +
                "*** Keywords ***\n" +
                "Open Browser To Login Page\n" +
                "    Open Browser    http://localhost/    chrome\n" +
                "    Set Selenium Speed    ${DELAY}\n" +
                "    Maximize Browser Window\n" +
                "    ${LOGIN PAGE}=    Get Element Attribute    title_page\n" +
                "    Title Should Be    ${LOGIN PAGE}\n" +
                "\n" +
                "*** Variables ***\n" +
                "${DELAY}      0\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();
        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final OnTheFlyCheck check = new OnTheFlyCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(1., metric.getNormalizedValue(), 0.0001);
        assertEquals(1., metric.getRawValue(), 0.0001);
    }

    @Test
    void testWithResultsOnTheFlyKeywordAssignment(){
        final String code =
                "*** Settings ***\n" +
                "Library    Selenium2Library\n" +
                "*** Test Cases ***\n" +
                "Valid Login\n" +
                "    Open Browser To   Login Page\n" +
                "\n" +
                "*** Keywords ***\n" +
                "Open Browser To\n" +
                "    [Arguments]    ${Login Page}\n" +
                "    Open Browser    http://localhost/    chrome\n" +
                "    Set Selenium Speed    ${DELAY}\n" +
                "    Maximize Browser Window\n" +
                "    Title Should Be    ${Login Page}\n" +
                "    Title Should Be    Some text\n" +
                "\n" +
                "*** Variables ***\n" +
                "${DELAY}      0\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final OnTheFlyCheck check = new OnTheFlyCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(0.5, metric.getNormalizedValue(), 0.0001);
        assertEquals(1., metric.getRawValue(), 0.0001);
    }
}
