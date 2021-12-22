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

import lu.uni.serval.ikora.core.utils.ValueFetcher;
import lu.uni.serval.ikora.smells.utils.LocatorUtils;

import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.types.LocatorType;

public class ComplexLocatorVisitor extends SmellVisitor {
    private int locators = 0;
    private final int maximumLocatorSize;

    public ComplexLocatorVisitor(int maximumLocatorSize){
        this.maximumLocatorSize = maximumLocatorSize;
    }

    public int getComplexLocators() {
        return getNodes().size();
    }

    public int getLocators() {
        return locators;
    }

    @Override
    public void visit(Argument argument, VisitorMemory memory) {
        if(argument.isType(LocatorType.class)){
            if(ValueFetcher.getValues(argument).stream().anyMatch(v -> LocatorUtils.isComplex(v, this.maximumLocatorSize))){
                addNode(argument);
            }

            ++locators;
        }
    }
}
