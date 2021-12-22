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
import lu.uni.serval.ikora.core.libraries.builtin.keywords.Sleep;
import lu.uni.serval.ikora.core.model.Keyword;
import lu.uni.serval.ikora.core.model.KeywordCall;

import java.util.Optional;

public class SleepCounterVisitor extends SmellVisitor {
    private int syncCounter = 0;

    public int getSleepCalls() {
        return getNodes().size();
    }

    public int getSyncCalls(){
        return syncCounter;
    }

    @Override
    public void visit(KeywordCall call, VisitorMemory memory) {
        final Optional<Keyword> keyword = call.getKeyword();
        if(keyword.isPresent() && keyword.get().getType() == Keyword.Type.SYNCHRONIZATION){
            if(keyword.get() instanceof Sleep){
                addNode(call);
            }
            ++syncCounter;
        }

        super.visit(call, memory);
    }
}
