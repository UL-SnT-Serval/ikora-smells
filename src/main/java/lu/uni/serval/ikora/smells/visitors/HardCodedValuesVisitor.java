package lu.uni.serval.ikora.smells.visitors;

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

import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.types.ConditionType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class HardCodedValuesVisitor extends SmellVisitor {
    private int totalArguments = 0;
    private final Set<Keyword.Type> types;

    public HardCodedValuesVisitor(Keyword.Type... types) {
        this.types = new HashSet<>(Arrays.asList(types));
    }

    public int getNumberHardcodedValues() {
        return getNodes().size();
    }

    public int getTotalArguments(){
        return totalArguments;
    }

    @Override
    public void visit(KeywordCall call, VisitorMemory memory) {
        if(ofInterest(call)){
            for(Argument argument: call.getArgumentList()){
                if(argument.isLiteral() && !argument.isType(ConditionType.class)){
                    addNode(argument.getDefinition());
                }

                ++totalArguments;
            }
        }

        super.visit(call, memory);
    }

    private boolean ofInterest(KeywordCall call){
        if(types.isEmpty()){
            return true;
        }

        return call.getKeyword().map(k -> types.contains(k.getType())).orElse(false);
    }
}
