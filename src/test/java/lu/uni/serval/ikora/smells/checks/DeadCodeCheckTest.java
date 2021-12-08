package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.core.builder.BuildResult;
import lu.uni.serval.ikora.core.builder.Builder;
import lu.uni.serval.ikora.core.model.Project;
import lu.uni.serval.ikora.core.model.SourceNode;
import lu.uni.serval.ikora.core.utils.FileUtils;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DeadCodeCheckTest {
    @Test
    void testWithNoDeadCode(){
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
                "    Set Selenium Speed    0\n" +
                "    Maximize Browser Window\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();
        final DeadCodeCheck check = new DeadCodeCheck();
        final Set<SourceNode> nodes = check.collectInstances(project.getSourceFile(FileUtils.IN_MEMORY).get(), new SmellConfiguration());

        assertTrue(nodes.isEmpty());
    }

    @Test
    void testWithDeadCodeKeyword(){
        final String code =
                "*** Settings ***\n" +
                "Library    Selenium2Library\n" +
                "*** Test Cases ***\n" +
                "Valid Login\n" +
                "    Open Browser To Login Page\n" +
                "\n" +
                "*** Keywords ***\n" +
                "Open Browser To Login Page\n" +
                "    Open Browser    ${SERVER}    chrome\n" +
                "    Set Selenium Speed    0\n" +
                "    Maximize Browser Window\n" +
                "Go To Login Page\n" +
                "    Go To    ${LOGIN URL}\n" +
                "    Login Page Should Be Open\n" +
                "\n" +
                "*** Variables ***\n" +
                "${SERVER}         localhost:7272\n" +
                "${LOGIN URL}      http://${SERVER}/\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();
        final DeadCodeCheck check = new DeadCodeCheck();
        final Set<SourceNode> nodes = check.collectInstances(project.getSourceFile(FileUtils.IN_MEMORY).get(), new SmellConfiguration());

        assertEquals(1, nodes.size());
    }

    @Test
    void testWithDeadCodeVariable(){
        final String code =
                "*** Settings ***\n" +
                "Library    Selenium2Library\n" +
                "*** Test Cases ***\n" +
                "Valid Login\n" +
                "    Open Browser To Login Page\n" +
                "\n" +
                "*** Keywords ***\n" +
                "Open Browser To Login Page\n" +
                "    Open Browser    ${SERVER}    chrome\n" +
                "    Set Selenium Speed    0\n" +
                "    Maximize Browser Window\n" +
                "\n" +
                "*** Variables ***\n" +
                "${SERVER}         localhost:7272\n" +
                "${LOGIN URL}      http://${SERVER}/\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();
        final DeadCodeCheck check = new DeadCodeCheck();
        final Set<SourceNode> nodes = check.collectInstances(project.getSourceFile(FileUtils.IN_MEMORY).get(), new SmellConfiguration());

        assertEquals(1, nodes.size());
    }
}
