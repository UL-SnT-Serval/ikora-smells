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

class LackOfEncapsulationCheckTest {
    @Test
    void testWithOneDirectLibraryCall(){
        final String code =
            "*** Settings ***\n" +
            "Library    Selenium2Library\n" +
            "*** Test Cases ***\n" +
            "Valid Login\n" +
            "    Input username    user\n" +
            "    Input password    password\n" +
            "    Click Button    login_button\n" +
            "    Click Button    confirm_button\n" +
            "\n" +
            "*** Keywords ***\n" +
            "Input Username\n" +
            "    [Arguments]    ${username}\n" +
            "    Input Text    username_field    ${username}\n" +
            "\n" +
            "Input Password\n" +
            "    [Arguments]    ${password}\n" +
            "    Input Text    password_field    ${password}";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();
        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();

        final LackOfEncapsulationCheck check = new LackOfEncapsulationCheck();
        final SmellResult metric = check.computeMetric(testCase, new SmellConfiguration());

        assertEquals(0.5, metric.getNormalizedValue(), 0.0001);
        assertEquals(2, metric.getRawValue(), 0.0001);
    }

    @Test
    void testWithNoDirectLibraryCall(){
        final String code =
                "*** Settings ***\n" +
                "Library    Selenium2Library\n" +
                "*** Test Cases ***\n" +
                "Valid Login\n" +
                "    Input username    user\n" +
                "    Input password    password\n" +
                "\n" +
                "*** Keywords ***\n" +
                "Input Username\n" +
                "    [Arguments]    ${username}\n" +
                "    Input Text    username_field    ${username}\n" +
                "\n" +
                "Input Password\n" +
                "    [Arguments]    ${password}\n" +
                "    Input Text    password_field    ${password}";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final LackOfEncapsulationCheck check = new LackOfEncapsulationCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(0., metric.getNormalizedValue(), 0.0001);
    }
}
