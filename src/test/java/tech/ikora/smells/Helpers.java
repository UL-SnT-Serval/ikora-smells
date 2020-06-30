package tech.ikora.smells;

import tech.ikora.BuildConfiguration;
import tech.ikora.builder.BuildResult;
import tech.ikora.builder.Builder;
import tech.ikora.model.Project;
import tech.ikora.model.TestCase;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.fail;

public class Helpers {
    public static Project compileProject(String projectName){
        final BuildResult result = Builder.build(getResourceFile(projectName), new BuildConfiguration(), true);

        if(result.getProjects().size() != 1){
            fail("Failed to load projects");
        }

        return result.getProjects().iterator().next();
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
}
