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

class ConditionalAssertionCheckTest {
    @Test
    void testWithOneDirectConditionalAssertion(){
        final String code =
                "*** Test Cases ***\n" +
                "Some very interesting test\n" +
                "    Make some clever assertion\n" +
                "\n" +
                "*** Keywords ***\n" +
                "Make some clever assertion\n" +
                "    Run Keyword If  ${True}  Should be empty  ${Container}\n" +
                "\n" +
                "*** Variables ***\n" +
                "${Container}  Not Empty";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Some very interesting test").iterator().next();
        final ConditionalAssertionCheck check = new ConditionalAssertionCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(1., metric.getNormalizedValue(), 0.0001);
    }
}
