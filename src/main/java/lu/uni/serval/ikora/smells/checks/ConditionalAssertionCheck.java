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
import lu.uni.serval.ikora.core.analytics.visitor.PathMemory;
import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.core.types.KeywordType;
import lu.uni.serval.ikora.smells.*;
import lu.uni.serval.ikora.smells.visitors.CollectCallsByTypeVisitor;

import lu.uni.serval.ikora.core.model.*;

import java.util.Set;
import java.util.stream.Collectors;

public class ConditionalAssertionCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        final CollectCallsByTypeVisitor visitor = visit(testCase, new PathMemory());

        int totalAssertions = visitor.getNodes().size();

        double rawValue = visitor.getNodes().stream()
                .map(KeywordCall.class::cast)
                .filter(this::isCallingAssertion)
                .count();

        double normalizedValue = totalAssertions > 0 ? rawValue / totalAssertions : 0.0;

        return new SmellResult(SmellMetric.Type.CONDITIONAL_ASSERTION, rawValue, normalizedValue, visitor.getNodes());
    }

    @Override
    public Set<SourceNode> collectInstances(SourceFile file, SmellConfiguration configuration) {
        return visit(file, new FileMemory(file)).getNodes().stream()
                .map(KeywordCall.class::cast)
                .filter(this::isCallingAssertion)
                .collect(Collectors.toSet());
    }

    private CollectCallsByTypeVisitor visit(SourceNode node, VisitorMemory memory){
        final CollectCallsByTypeVisitor visitor = new CollectCallsByTypeVisitor(Keyword.Type.BRANCHING);
        visitor.visit(node, memory);

        return visitor;
    }

    private boolean isCallingAssertion(KeywordCall assertion) {
        for(Argument argument: assertion.getArgumentList()){
            if(argument.isType(KeywordType.class)){
                final KeywordCall call = (KeywordCall)argument.getDefinition();
                return NodeUtils.isCallType(call, Keyword.Type.ASSERTION, true);
            }
        }

        return false;
    }
}
