package lu.uni.serval.ikora.smells.visitors;

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

import lu.uni.serval.ikora.core.analytics.visitor.FixedMemory;
import lu.uni.serval.ikora.core.analytics.visitor.PathMemory;
import lu.uni.serval.ikora.core.builder.BuildResult;
import lu.uni.serval.ikora.core.builder.Builder;
import lu.uni.serval.ikora.core.model.KeywordCall;
import lu.uni.serval.ikora.core.model.Project;
import lu.uni.serval.ikora.core.model.UserKeyword;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComplexLocatorVisitorTest {
    @Test
    void testVisitCallWithoutLocators(){
        final String code =
                "*** Settings ***\n" +
                "Library    Selenium2Library\n" +
                "*** Keywords ***\n" +
                "User \"${username}\" logs in with password \"${password}\"\n" +
                "    Input username    ${username}\n" +
                "    Input password    ${password}\n" +
                "    Submit credentials\n" +
                "\n" +
                "Input Username\n" +
                "    [Arguments]    ${username}\n" +
                "    Input Text    username_field    ${username}\n" +
                "\n" +
                "Input Password\n" +
                "    [Arguments]    ${password}\n" +
                "    Input Text    ${PASSWORD_FIELD}    ${password}\n" +
                "\n" +
                "Submit Credentials\n" +
                "    Click Button    login_button\n" +
                "\n" +
                "*** Variables ***\n" +
                "${PASSWORD_FIELD}      password_field";

        final BuildResult result = Builder.build(code, true);
        final Project project = result.getProjects().iterator().next();

        final UserKeyword login = project.findUserKeyword("<IN_MEMORY>", "User \"${username}\" logs in with password \"${password}\"").iterator().next();
        assertNotNull(login);

        ComplexLocatorVisitor visitor = new ComplexLocatorVisitor(1);
        visitor.visit(login, new FixedMemory(1, KeywordCall.class));

        assertEquals(0, visitor.getComplexLocators());
        assertEquals(0, visitor.getLocators());
        assertEquals(0, visitor.getNodes().size());
    }

    @Test
    void testVisitCallWithLiteralLocator(){
        final String code =
                "*** Settings ***\n" +
                "Library    Selenium2Library\n" +
                "*** Keywords ***\n" +
                "User \"${username}\" logs in with password \"${password}\"\n" +
                "    Input username    ${username}\n" +
                "    Input password    ${password}\n" +
                "    Submit credentials\n" +
                "\n" +
                "Input Username\n" +
                "    [Arguments]    ${username}\n" +
                "    Input Text    username_field    ${username}\n" +
                "\n" +
                "Input Password\n" +
                "    [Arguments]    ${password}\n" +
                "    Input Text    ${PASSWORD_FIELD}    ${password}\n" +
                "\n" +
                "Submit Credentials\n" +
                "    Click Button    login_button\n" +
                "\n" +
                "*** Variables ***\n" +
                "${PASSWORD_FIELD}      password_field";

        final BuildResult result = Builder.build(code, true);
        final Project project = result.getProjects().iterator().next();

        final UserKeyword inputUsername = project.findUserKeyword("<IN_MEMORY>", "Input Username").iterator().next();
        assertNotNull(inputUsername);

        ComplexLocatorVisitor visitor = new ComplexLocatorVisitor(1);
        visitor.visit(inputUsername, new FixedMemory(1, KeywordCall.class));

        assertEquals(0, visitor.getComplexLocators());
        assertEquals(1, visitor.getLocators());
        assertEquals(0, visitor.getNodes().size());
    }

    @Test
    void testVisitCallWithVariableLocator(){
        final String code =
                "*** Settings ***\n" +
                "Library    Selenium2Library\n" +
                "*** Keywords ***\n" +
                "User \"${username}\" logs in with password \"${password}\"\n" +
                "    Input username    ${username}\n" +
                "    Input password    ${password}\n" +
                "    Submit credentials\n" +
                "\n" +
                "Input Username\n" +
                "    [Arguments]    ${username}\n" +
                "    Input Text    username_field    ${username}\n" +
                "\n" +
                "Input Password\n" +
                "    [Arguments]    ${password}\n" +
                "    Input Text    ${PASSWORD_FIELD}    ${password}\n" +
                "\n" +
                "Submit Credentials\n" +
                "    Click Button    login_button\n" +
                "\n" +
                "*** Variables ***\n" +
                "${PASSWORD_FIELD}      password_field";

        final BuildResult result = Builder.build(code, true);
        final Project project = result.getProjects().iterator().next();

        final UserKeyword inputPassword = project.findUserKeyword("<IN_MEMORY>", "Input Password").iterator().next();
        assertNotNull(inputPassword);

        ComplexLocatorVisitor visitor = new ComplexLocatorVisitor(1);
        visitor.visit(inputPassword, new FixedMemory(1, KeywordCall.class));

        assertEquals(0, visitor.getComplexLocators());
        assertEquals(1, visitor.getLocators());
        assertEquals(0, visitor.getNodes().size());
    }

    @Test
    void testVisitCallWithArgumentLocator(){
        final String code =
                "*** Settings ***\n" +
                "Library    Selenium2Library\n" +
                "\n" +
                "*** Test Cases ***\n" +
                "Run the test\n" +
                "    Keyword calling other keyword with parameters\n" +
                "\n" +
                "*** Keywords ***\n" +
                "Keyword calling other keyword with parameters\n" +
                "    Follow the link  ${simple}\n" +
                "    Follow the link  ${complex}\n" +
                "\n" +
                "Follow the link\n" +
                "    [Arguments]    ${locator}\n" +
                "    Click Link    ${locator}\n" +
                "\n" +
                "*** Variables ***\n" +
                "${simple}  link_to_click\n" +
                "${complex}  css:.covid-form > div > div.react-grid-Container > div > div > div.react-grid-Header > div > div > div:nth-child(3) > div";

        final BuildResult result = Builder.build(code, true);
        final Project project = result.getProjects().iterator().next();

        final UserKeyword followLink = project.findUserKeyword("<IN_MEMORY>", "Follow the link").iterator().next();
        assertNotNull(followLink);

        ComplexLocatorVisitor visitor = new ComplexLocatorVisitor(1);
        visitor.visit(followLink, new PathMemory());

        assertEquals(1, visitor.getComplexLocators());
        assertEquals(1, visitor.getLocators());
        assertEquals(1, visitor.getNodes().size());

        assertEquals(followLink.getStep(0).getArgumentList().get(0), visitor.getNodes().iterator().next());
    }
}
