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

class NoisyLoggingCheckTest {
    @Test
    void testWithOneLogInSetup(){
        final String code =
                "*** Test Cases ***\n" +
                "Valid Login\n" +
                "    [Setup]    Open Browser To Login Page\n" +
                "    User \"demo\" logs in with password \"mode\"\n" +
                "\n" +
                "*** Keywords ***\n" +
                "Open Browser To Login Page\n" +
                "    Open Browser    http://localhost/    chrome\n" +
                "    Maximize Browser Window\n" +
                "    Log    Setup completed successfully!\n" +
                "\n" +
                "User \"${username}\" logs in with password \"${password}\"\n" +
                "    Input Text    username_field    ${username}\n" +
                "    Input Text    login_button    ${password}\n" +
                "    Click Button    login_button\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final NoisyLoggingCheck check = new NoisyLoggingCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(0.3333, metric.getNormalizedValue(), 0.0001);
        assertEquals(1., metric.getRawValue(), 0.0001);
    }

    @Test
    void testWithNoLogInSetup(){
        final String code =
                "*** Test Cases ***\n" +
                "Valid Login\n" +
                "    [Setup]    Open Browser To Login Page\n" +
                "    User \"demo\" logs in with password \"mode\"\n" +
                "\n" +
                "*** Keywords ***\n" +
                "Open Browser To Login Page\n" +
                "    Open Browser    http://localhost/    chrome\n" +
                "    Maximize Browser Window\n" +
                "\n" +
                "User \"${username}\" logs in with password \"${password}\"\n" +
                "    Input Text    username_field    ${username}\n" +
                "    Input Text    login_button    ${password}\n" +
                "    Click Button    login_button\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final NoisyLoggingCheck check = new NoisyLoggingCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(0., metric.getNormalizedValue(), 0.0001);
        assertEquals(0., metric.getRawValue(), 0.0001);
    }

    @Test
    void testWithNoSetup(){
        final String code =
                "*** Test Cases ***\n" +
                "Valid Login\n" +
                "    User \"demo\" logs in with password \"mode\"\n" +
                "\n" +
                "*** Keywords ***\n" +
                "User \"${username}\" logs in with password \"${password}\"\n" +
                "    Input Text    username_field    ${username}\n" +
                "    Input Text    login_button    ${password}\n" +
                "    Click Button    login_button\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final NoisyLoggingCheck check = new NoisyLoggingCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(Double.NaN, metric.getNormalizedValue(), 0.0001);
        assertEquals(Double.NaN, metric.getRawValue(), 0.0001);
    }
}
