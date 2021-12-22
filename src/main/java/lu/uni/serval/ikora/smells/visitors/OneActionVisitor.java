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
import lu.uni.serval.ikora.core.model.UserKeyword;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class OneActionVisitor extends SmellVisitor {
    private int keywordsCount;
    private Set<Keyword.Type> types;

    public OneActionVisitor(Keyword.Type... types){
        this.keywordsCount = 0;
        this.types = new HashSet<>(Arrays.asList(types));
    }

    public int getSingleActionCount(){
        return getNodes().size();
    }

    public int getKeywordsCount() {
        return keywordsCount;
    }

    @Override
    public void visit(UserKeyword keyword, VisitorMemory memory) {
        if(NodeUtils.isSingleAction(keyword, this.types)){
            addNode(keyword);
        }

        ++keywordsCount;

        super.visit(keyword, memory);
    }
}
