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

class SneakyCheckingCheckTest {
    @Test
    void testWithBlockOfOneSneakyAssertion(){
        final String code =
                "*** Settings ***\n" +
                "Library    Selenium2Library\n" +
                "*** Test Cases ***\n" +
                "Valid Login\n" +
                "    Input username    user\n" +
                "    Input password    password\n" +
                "    Submit Credentials\n" +
                "    Welcome Page Should Be Open\n" +
                "\n" +
                "*** Keywords ***\n" +
                "Input Username\n" +
                "    [Arguments]    ${username}\n" +
                "    Input Text    username_field    ${username}\n" +
                "\n" +
                "Input Password\n" +
                "    [Arguments]    ${password}\n" +
                "    Input Text    password_field    ${password}\n" +
                "\n" +
                "Submit Credentials\n" +
                "    Click Button    login_button\n" +
                "\n" +
                "Welcome Page Should Be Open\n" +
                "    Location Should Be    http://example.com/welcome.html\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final SneakyCheckingCheck check = new SneakyCheckingCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(0.25, metric.getNormalizedValue(), 0.0001);
        assertEquals(1., metric.getRawValue(), 0.0001);
    }

    @Test
    void testWithBlockOfOneSneakyAssertionWithLogging(){
        final String code =
                "*** Settings ***\n" +
                        "Library    Selenium2Library\n" +
                        "*** Test Cases ***\n" +
                        "Valid Login\n" +
                        "    Input username    user\n" +
                        "    Input password    password\n" +
                        "    Submit Credentials\n" +
                        "    Welcome Page Should Be Open\n" +
                        "\n" +
                        "*** Keywords ***\n" +
                        "Input Username\n" +
                        "    [Arguments]    ${username}\n" +
                        "    Input Text    username_field    ${username}\n" +
                        "\n" +
                        "Input Password\n" +
                        "    [Arguments]    ${password}\n" +
                        "    Input Text    password_field    ${password}\n" +
                        "\n" +
                        "Submit Credentials\n" +
                        "    Click Button    login_button\n" +
                        "\n" +
                        "Welcome Page Should Be Open\n" +
                        "    Log    Asserting that page has the correct location\n" +
                        "    Location Should Be    http://example.com/welcome.html\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final SneakyCheckingCheck check = new SneakyCheckingCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(0.25, metric.getNormalizedValue(), 0.0001);
        assertEquals(1., metric.getRawValue(), 0.0001);
    }

    @Test
    void testWithBlockOfTwoAssertion(){
        final String code =
                "*** Settings ***\n" +
                        "Library    Selenium2Library\n" +
                        "*** Test Cases ***\n" +
                        "Valid Login\n" +
                        "    Input username    user\n" +
                        "    Input password    password\n" +
                        "    Submit Credentials\n" +
                        "    Welcome Page Should Be Open\n" +
                        "\n" +
                        "*** Keywords ***\n" +
                        "Input Username\n" +
                        "    [Arguments]    ${username}\n" +
                        "    Input Text    username_field    ${username}\n" +
                        "\n" +
                        "Input Password\n" +
                        "    [Arguments]    ${password}\n" +
                        "    Input Text    password_field    ${password}\n" +
                        "\n" +
                        "Submit Credentials\n" +
                        "    Click Button    login_button\n" +
                        "\n" +
                        "Welcome Page Should Be Open\n" +
                        "    Location Should Be    http://example.com/welcome.html\n" +
                        "    Title Should Be    Welcome Page\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final SneakyCheckingCheck check = new SneakyCheckingCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(0., metric.getNormalizedValue(), 0.0001);
        assertEquals(0., metric.getRawValue(), 0.0001);
    }

    @Test
    void testWithNoSneakyAssertion(){
        final String code =
                "*** Settings ***\n" +
                        "Library    Selenium2Library\n" +
                        "*** Test Cases ***\n" +
                        "Valid Login\n" +
                        "    Input username    user\n" +
                        "    Input password    password\n" +
                        "    Submit Credentials\n" +
                        "    Welcome Page Should Be Open\n" +
                        "    Location Should Be    http://example.com/welcome.html\n" +
                        "    Title Should Be    Welcome Page\n" +
                        "\n" +
                        "*** Keywords ***\n" +
                        "Input Username\n" +
                        "    [Arguments]    ${username}\n" +
                        "    Input Text    username_field    ${username}\n" +
                        "\n" +
                        "Input Password\n" +
                        "    [Arguments]    ${password}\n" +
                        "    Input Text    password_field    ${password}\n" +
                        "\n" +
                        "Submit Credentials\n" +
                        "    Click Button    login_button\n" +
                        "\n" +
                        "Welcome Page Should Be Open\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final SneakyCheckingCheck check = new SneakyCheckingCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(0., metric.getNormalizedValue(), 0.0001);
        assertEquals(0., metric.getRawValue(), 0.0001);
    }
}
