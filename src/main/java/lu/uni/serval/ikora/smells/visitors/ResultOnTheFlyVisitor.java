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

public class ResultOnTheFlyVisitor extends SmellVisitor {
    private int expectedCount = 0;
    private int onTheFlyCount = 0;

    public int getOnTheFly() {
        return onTheFlyCount;
    }

    public int getExpected() {
        return expectedCount;
    }

    @Override
    public void visit(Argument argument, VisitorMemory memory) {
        if(argument.getType().getName().equalsIgnoreCase("expected")){
            processOnTheFly(argument);
            ++expectedCount;
        }
    }

    private void processOnTheFly(Argument argument){
        if(!argument.isVariable()){
            return;
        }

        final Variable variable = (Variable) argument.getDefinition();

        for(Dependable definition: variable.getDefinition(Link.Import.BOTH)){
           if((definition instanceof UserKeyword) || (definition instanceof Assignment)){
               ++onTheFlyCount;
               addNode((SourceNode) definition);
               addNode(variable);
           }
        }
    }
}
