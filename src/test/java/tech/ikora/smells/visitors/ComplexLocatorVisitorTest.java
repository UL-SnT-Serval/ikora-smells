package tech.ikora.smells.visitors;

import org.junit.jupiter.api.Test;
import tech.ikora.analytics.visitor.FixedMemory;
import tech.ikora.builder.BuildResult;
import tech.ikora.builder.Builder;
import tech.ikora.model.KeywordCall;
import tech.ikora.model.Project;
import tech.ikora.model.UserKeyword;

import static org.junit.jupiter.api.Assertions.*;

public class ComplexLocatorVisitorTest {
    @Test
    void testVisitCallWithoutLocators(){
        final String code =
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

        ComplexLocatorVisitor visitor = new ComplexLocatorVisitor();
        visitor.visit(login, new FixedMemory(1, KeywordCall.class));

        assertEquals(0, visitor.getComplexLocators());
        assertEquals(0, visitor.getLocators());
        assertEquals(0, visitor.getNodes().size());
    }

    @Test
    void testVisitCallWithLiteralLocator(){
        final String code =
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

        ComplexLocatorVisitor visitor = new ComplexLocatorVisitor();
        visitor.visit(inputUsername, new FixedMemory(1, KeywordCall.class));

        assertEquals(0, visitor.getComplexLocators());
        assertEquals(1, visitor.getLocators());
        assertEquals(0, visitor.getNodes().size());
    }

    @Test
    void testVisitCallWithVariableLocator(){
        final String code =
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

        ComplexLocatorVisitor visitor = new ComplexLocatorVisitor();
        visitor.visit(inputPassword, new FixedMemory(1, KeywordCall.class));

        assertEquals(0, visitor.getComplexLocators());
        assertEquals(1, visitor.getLocators());
        assertEquals(0, visitor.getNodes().size());
    }

    @Test
    void testVisitCallWithArgumentLocator(){
        final String code =
                "*** Settings ***\n" +
                "Library           SeleniumLibrary\n" +
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

        ComplexLocatorVisitor visitor = new ComplexLocatorVisitor();
        visitor.visit(followLink, new FixedMemory(1, KeywordCall.class));

        assertEquals(1, visitor.getComplexLocators());
        assertEquals(2, visitor.getLocators());
        assertEquals(1, visitor.getNodes().size());

        final String value = visitor.getNodes().iterator().next().getName();
        assertEquals("css:.covid-form > div > div.react-grid-Container > div > div > div.react-grid-Header > div > div > div:nth-child(3) > div", value);
    }
}
