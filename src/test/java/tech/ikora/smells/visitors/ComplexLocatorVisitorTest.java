package tech.ikora.smells.visitors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tech.ikora.analytics.visitor.FixedMemory;
import tech.ikora.model.KeywordCall;
import tech.ikora.model.Project;
import tech.ikora.model.UserKeyword;
import tech.ikora.smells.Helpers;

import static org.junit.jupiter.api.Assertions.*;

public class ComplexLocatorVisitorTest {
    private static Project project;

    @BeforeAll
    static void loadProject(){
        project = Helpers.compileProject("login.robot");
    }

    @Test
    void testVisitCallWithoutLocators(){
        final UserKeyword login = project.findUserKeyword("login", "User \"${username}\" logs in with password \"${password}\"").iterator().next();
        assertNotNull(login);

        ComplexLocatorVisitor visitor = new ComplexLocatorVisitor();
        visitor.visit(login, new FixedMemory(1, KeywordCall.class));

        assertEquals(0, visitor.getComplexLocators());
        assertEquals(0, visitor.getLocators());
    }

    @Test
    void testVisitCallWithLiteralLocator(){
        final UserKeyword inputUsername = project.findUserKeyword("login", "Input Username").iterator().next();
        assertNotNull(inputUsername);

        ComplexLocatorVisitor visitor = new ComplexLocatorVisitor();
        visitor.visit(inputUsername, new FixedMemory(1, KeywordCall.class));

        assertEquals(0, visitor.getComplexLocators());
        assertEquals(1, visitor.getLocators());
    }

    @Test
    void testVisitCallWithVariableLocator(){
        final UserKeyword inputPassword = project.findUserKeyword("login", "Input Password").iterator().next();
        assertNotNull(inputPassword);

        ComplexLocatorVisitor visitor = new ComplexLocatorVisitor();
        visitor.visit(inputPassword, new FixedMemory(1, KeywordCall.class));

        assertEquals(0, visitor.getComplexLocators());
        assertEquals(1, visitor.getLocators());
    }
}
