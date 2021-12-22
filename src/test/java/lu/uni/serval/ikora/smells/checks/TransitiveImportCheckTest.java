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

import lu.uni.serval.ikora.core.BuildConfiguration;
import lu.uni.serval.ikora.core.builder.BuildResult;
import lu.uni.serval.ikora.core.builder.Builder;
import lu.uni.serval.ikora.core.model.Project;
import lu.uni.serval.ikora.core.model.TestCase;
import lu.uni.serval.ikora.core.utils.FileUtils;
import lu.uni.serval.ikora.smells.SmellCheck;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellResult;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

class TransitiveImportCheckTest {
    @Test
    void testBuiltIn(){
        final String code =
            "*** Test Cases ***\n" +
            "Test with BuiltIn call\n" +
            "\tLog\tHello there!\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Test with BuiltIn call").iterator().next();
        final SmellCheck check = new TransitiveImportCheck();
        final SmellResult metric = check.computeMetric(testCase, new SmellConfiguration());

        assertEquals(0, metric.getRawValue());
        assertEquals(0, metric.getNormalizedValue());
        assertEquals(0, metric.getNodes().size());
    }

    @Test
    void testInSameFile(){
        final String code =
                "*** Test Cases ***\n" +
                "Test with BuiltIn call\n" +
                "\tSay hello\n" +
                "*** Keywords ***\n" +
                "Say hello\n" +
                "\t Log\tHello there!";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Test with BuiltIn call").iterator().next();
        final SmellCheck check = new TransitiveImportCheck();
        final SmellResult metric = check.computeMetric(testCase, new SmellConfiguration());

        assertEquals(0, metric.getRawValue());
        assertEquals(0, metric.getNormalizedValue());
        assertEquals(0, metric.getNodes().size());
    }

    @Test
    void testTransitiveKeyword() throws IOException, URISyntaxException {
        final File file = FileUtils.getResourceFile("robot/transitive-dependency");
        final BuildResult build = Builder.build(file, new BuildConfiguration(), true);
        final Project project = build.getProjects().iterator().next();

        final TestCase testCase = project.findTestCase("test", "Some test").iterator().next();
        final SmellCheck check = new TransitiveImportCheck();
        final SmellResult metric = check.computeMetric(testCase, new SmellConfiguration());

        assertEquals(1, metric.getRawValue());
        assertEquals(0.5, metric.getNormalizedValue());
        assertEquals(1, metric.getNodes().size());
    }
}
