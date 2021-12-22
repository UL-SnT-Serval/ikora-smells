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
import org.apache.commons.lang3.NotImplementedException;

import java.util.Optional;

public class TransitiveImportVisitor extends SmellVisitor {
    private int callCounter = 0;

    public int getNumberCalls() {
        return callCounter;
    }

    @Override
    public void visit(KeywordCall call, VisitorMemory memory) {
        final Optional<Keyword> keyword = call.getKeyword();

        if(keyword.isPresent()){
            if(!isDirectDependency(keyword.get(), call)){
                addNode(call);
            }

            ++callCounter;
        }

        super.visit(call, memory);
    }

    private static boolean isDirectDependency(Keyword keyword, Step step){
        if(SourceNode.class.isAssignableFrom(keyword.getClass())){
            final SourceFile definitionFile = ((SourceNode)keyword).getSourceFile();
            return step.getSourceFile().isDirectDependency(definitionFile);
        }
        else if(LibraryKeyword.class.isAssignableFrom(keyword.getClass())){
            final String libraryName = keyword.getLibraryName();
            return step.getSourceFile().isImportLibrary(libraryName);
        }

        throw new NotImplementedException("only support keywords in the source code or keyword from libraries for indirect dependency detection");
    }
}
