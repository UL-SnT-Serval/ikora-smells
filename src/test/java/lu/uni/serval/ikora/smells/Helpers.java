package lu.uni.serval.ikora.smells;

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
