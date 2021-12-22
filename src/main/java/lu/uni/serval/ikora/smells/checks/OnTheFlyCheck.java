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

import lu.uni.serval.ikora.core.analytics.visitor.FileMemory;
import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.smells.SmellCheck;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellMetric;
import lu.uni.serval.ikora.smells.SmellResult;
import lu.uni.serval.ikora.smells.visitors.ResultOnTheFlyVisitor;

import lu.uni.serval.ikora.core.analytics.visitor.PathMemory;
import lu.uni.serval.ikora.core.model.*;

import java.util.Set;

public class OnTheFlyCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        final ResultOnTheFlyVisitor visitor = visit(testCase, new PathMemory());

        double rawValue = visitor.getOnTheFly();
        double normalizedValue = rawValue / visitor.getExpected();

        return new SmellResult(SmellMetric.Type.ON_THE_FLY, rawValue, normalizedValue, visitor.getNodes());
    }

    @Override
    public Set<SourceNode> collectInstances(SourceFile file, SmellConfiguration configuration) {
        return visit(file, new FileMemory(file)).getNodes();
    }

    private ResultOnTheFlyVisitor visit(SourceNode node, VisitorMemory memory){
        final ResultOnTheFlyVisitor visitor = new ResultOnTheFlyVisitor();
        visitor.visit(node, memory);

        return visitor;
    }
}
