package lu.uni.serval.ikora.smells.checks;

import edu.stanford.nlp.neural.NeuralUtils;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellResult;

import lu.uni.serval.ikora.core.builder.BuildResult;
import lu.uni.serval.ikora.core.builder.Builder;
import lu.uni.serval.ikora.core.model.Project;
import lu.uni.serval.ikora.core.model.TestCase;

import org.ejml.simple.SimpleMatrix;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class EagerTestCheckTest {
    private static String codeLogin;
    private static double[][] values;

    @BeforeAll
    static void setup(){
        values = new double[1][3];

        codeLogin =
                "*** Settings ***\n" +
                "Documentation     A test suite with a single Gherkin style test.\n" +
                "...\n" +
                "...               This test is functionally identical to the example in\n" +
                "...               valid_login.robot file.\n" +
                "Library           SeleniumLibrary\n" +
                "Test Teardown     Close Browser\n" +
                "\n" +
                "*** Test Cases ***\n" +
                "Valid Login\n" +
                "    Given browser is opened to login page\n" +
                "    When user \"demo\" logs in with password \"mode\"\n" +
                "    Then welcome page should be open\n" +
                "\n" +
                "*** Keywords ***\n" +
                "Browser is opened to login page\n" +
                "    Open browser to login page\n" +
                "\n" +
                "User \"${username}\" logs in with password \"${password}\"\n" +
                "    Input username    ${username}\n" +
                "    Input password    ${password}\n" +
                "    Submit credentials\n" +
                "\n" +
                "Open Browser To Login Page\n" +
                "    Open Browser    ${LOGIN URL}    ${BROWSER}\n" +
                "    Maximize Browser Window\n" +
                "    Sleep 5\n" +
                "    Login Page Should Be Open\n" +
                "\n" +
                "Login Page Should Be Open\n" +
                "    Title Should Be    Login Page\n" +
                "\n" +
                "Go To Login Page\n" +
                "    Go To    ${LOGIN URL}\n" +
                "    Login Page Should Be Open\n" +
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
                "Welcome Page Should Be Open\n" +
                "    Location Should Be    ${WELCOME URL}\n" +
                "    Title Should Be    Welcome Page\n" +
                "\n" +
                "*** Variables ***\n" +
                "${SERVER}         localhost:7272\n" +
                "${BROWSER}        Firefox\n" +
                "${DELAY}          0\n" +
                "${VALID USER}     demo\n" +
                "${VALID PASSWORD}    mode\n" +
                "${LOGIN URL}      http://${SERVER}/\n" +
                "${WELCOME URL}    http://${SERVER}/welcome.html\n" +
                "${ERROR URL}      http://${SERVER}/error.html\n" +
                "${PASSWORD_FIELD}      password_field\n";
    }

    @Test
    void testDetectionWithExampleProject(){
        final BuildResult build = Builder.build(codeLogin, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final EagerTestCheck check = new EagerTestCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertTrue(metric.getNormalizedValue() < 1.);
        assertTrue(metric.getNormalizedValue() > 0.);
    }

    @Test
    void testCosineWithSameVectors(){
        values[0] = new double[]{0.,0.,15.};
        final SimpleMatrix vector1 = new SimpleMatrix(values);

        values[0] = new double[]{0.,0.,10.};
        final SimpleMatrix vector2 = new SimpleMatrix(values);

        assertEquals(1., NeuralUtils.cosine(vector1, vector2));
    }

    @Test
    void testWithOrthogonalVectors(){
        values[0] = new double[]{1.,0.,0.};
        final SimpleMatrix vector1 = new SimpleMatrix(values);

        values[0] = new double[]{0.,0.,3.};
        final SimpleMatrix vector2 = new SimpleMatrix(values);

        assertEquals(0., NeuralUtils.cosine(vector1, vector2));
    }

    @Test
    void testWithOppositeVectors(){
        values[0] = new double[]{5.,0.,0.};
        SimpleMatrix vector1 = new SimpleMatrix(values);

        values[0] = new double[]{-3.,0.,0.};
        SimpleMatrix vector2 = new SimpleMatrix(values);

        assertEquals(-1., NeuralUtils.cosine(vector1, vector2));
    }
}
