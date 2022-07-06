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

import lu.uni.serval.ikora.core.model.SourceNode;

import java.util.*;

public class SmellResults implements Iterable<SmellResult> {
    private final EnumMap<SmellMetric.Type, SmellResult> results;

    public SmellResults(){
        this.results = new EnumMap<>(SmellMetric.Type.class);
    }

    public synchronized void add(SmellResult smellResult){
        this.results.put(smellResult.getType(), smellResult);
    }

    public int getNumberMetrics() {
        return results.size();
    }

    public double getRawValue(SmellMetric.Type type){
        final SmellResult smellResult = results.get(type);

        if(smellResult == null){
            return Double.NaN;
        }

        return smellResult.getRawValue();
    }

    public double getNormalizedValue(SmellMetric.Type type){
        final SmellResult smellResult = results.get(type);

        if(smellResult == null){
            return Double.NaN;
        }

        return smellResult.getNormalizedValue();
    }

    public Set<SourceNode> getNodes(SmellMetric.Type type){
        final SmellResult smellResult = results.get(type);

        if(smellResult == null){
            return Collections.emptySet();
        }

        return smellResult.getNodes();
    }

    @Override
    public Iterator<SmellResult> iterator() {
        return results.values().iterator();
    }
}
