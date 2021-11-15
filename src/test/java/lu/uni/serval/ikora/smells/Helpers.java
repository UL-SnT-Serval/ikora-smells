package lu.uni.serval.ikora.smells;

import lu.uni.serval.ikora.core.builder.BuildResult;
import lu.uni.serval.ikora.core.builder.Builder;
import lu.uni.serval.ikora.core.model.Project;
import lu.uni.serval.ikora.core.model.TestCase;
import lu.uni.serval.ikora.core.model.UserKeyword;

public class Helpers {
    private Helpers() {}

    public static TestCase getTestCase(String code, String name){
        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        return project.findTestCase("<IN_MEMORY>", name).iterator().next();
    }

    public static UserKeyword getUserKeyword(String code, String name) {
        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        return project.findUserKeyword("<IN_MEMORY>", name).iterator().next();
    }
}
