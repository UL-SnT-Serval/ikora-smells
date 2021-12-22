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
import lu.uni.serval.ikora.core.model.KeywordDefinition;
import lu.uni.serval.ikora.core.model.TestCase;
import lu.uni.serval.ikora.core.model.UserKeyword;

public class SameDocumentationVisitor extends SmellVisitor {
    private int totalKeywordsCounter = 0;

    public int getSameDocumentationKeywords(){
        return getNodes().size();
    }

    public int getTotalKeywords() {
        return totalKeywordsCounter;
    }

    @Override
    public void visit(TestCase testCase, VisitorMemory memory) {
        check(testCase);
        super.visit(testCase, memory);
    }

    @Override
    public void visit(UserKeyword keyword, VisitorMemory memory) {
        check(keyword);
        super.visit(keyword, memory);
    }

    public void check(KeywordDefinition keyword){
        if(keyword.getDocumentation().toString().equalsIgnoreCase(keyword.getName())){
            addNode(keyword.getDocumentation());
        }

        ++totalKeywordsCounter;
    }
}
