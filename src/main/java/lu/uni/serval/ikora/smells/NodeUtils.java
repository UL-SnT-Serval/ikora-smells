package lu.uni.serval.ikora.smells;

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

import lu.uni.serval.ikora.core.model.*;

import java.util.*;

public class NodeUtils {
    private NodeUtils() {}

    public static boolean isCallType(SourceNode sourceNode, Keyword.Type type, boolean allowIndirectCall){
        if(sourceNode instanceof KeywordCall){
            return isType((KeywordCall)sourceNode, type, allowIndirectCall);
        }

        return false;
    }

    public static boolean isSingleAction(UserKeyword keyword, Set<Keyword.Type> types){
        return keyword.getSteps().stream()
                .map(Step::getKeywordCall)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(KeywordCall::getKeyword)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Keyword::getType)
                .filter(t -> t != Keyword.Type.LOG)
                .filter(types::contains)
                .count() == 1;
    }

    public static boolean isType(KeywordCall call, Keyword.Type type, boolean allowIndirectCall){
        final Optional<Keyword> keyword = call.getKeyword();

        if(!keyword.isPresent()){
            return false;
        }

        if(keyword.get().getType() == type){
            return true;
        }

        if(allowIndirectCall && UserKeyword.class.isAssignableFrom(keyword.get().getClass())){
            return isSingleAction((UserKeyword)keyword.get(), Collections.singleton(type));
        }

        return false;
    }
}
