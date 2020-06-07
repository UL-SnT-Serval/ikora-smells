package tech.ikora.smells;

import tech.ikora.BuildConfiguration;
import tech.ikora.builder.BuildResult;
import tech.ikora.builder.Builder;
import tech.ikora.model.Project;
import tech.ikora.model.Projects;
import tech.ikora.model.TestCase;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.fail;

public class Helpers {
    public static Projects compileProjects(String projectName){
        final BuildResult result = Builder.build(getResourceFile(projectName), new BuildConfiguration(), true);

        if(result.getProjects().isEmpty()){
            fail("Failed to load projects");
        }

        return result.getProjects();
    }

    public static File getResourceFile(String name){
        File file;

        try {
            URL resource = Helpers.class.getClassLoader().getResource(name);
            if(resource == null) throw new Exception("Null URI returned for resources!");
            file = Paths.get(resource.toURI()).toFile();
        } catch (Exception e) {
            fail(String.format("Failed to load resource '%s': %s", name, e.getMessage()));
            file = null;
        }

        return file;
    }

    public static TestCase getTestCase(Projects projects, String name){
        if(projects.size() != 1){
            fail("Expected only one project to be loaded");
        }

        final Project project = projects.iterator().next();

        final List<TestCase> testCases = project.getTestCases().stream()
                .filter(testCase -> name.equalsIgnoreCase(testCase.getName()))
                .collect(Collectors.toList());

        if(testCases.size() != 1){
            fail(String.format("Expected to get 1 test case with the name '%s' in project '%s' but got %d instead",
                    name,
                    project.getName(),
                    testCases.size()));
        }

        return testCases.get(0);
    }
}
