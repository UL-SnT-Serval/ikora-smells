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

import lu.uni.serval.ikora.smells.NodeUtils;

import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.core.model.Keyword;
import lu.uni.serval.ikora.core.model.KeywordCall;

public class CollectCallsByTypeVisitor extends SmellVisitor {
    private final Keyword.Type type;
    private int totalVisited;


    public CollectCallsByTypeVisitor(Keyword.Type type){
        this.type = type;
        this.totalVisited = 0;
    }

    public int getTotalVisited() {
        return totalVisited;
    }

    @Override
    public void visit(KeywordCall call, VisitorMemory memory) {
        if(NodeUtils.isType(call, this.type, false)){
            addNode(call);
        }
        ++totalVisited;

        super.visit(call, memory);
    }
}
