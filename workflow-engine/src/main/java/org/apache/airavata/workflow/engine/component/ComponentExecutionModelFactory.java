/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

package org.apache.airavata.workflow.engine.component;

import java.util.List;

import org.apache.airavata.workflow.engine.component.exception.ComponentExecutionModelProviderException;
import org.apache.airavata.workflow.model.graph.Node;

public class ComponentExecutionModelFactory {
	private static List<ComponentExecutionModelProvider> providers;
	public static ComponentExecutionModel getComponentExecutionModel(Node node) throws ComponentExecutionModelProviderException{
		String componentType = node.getComponent().getName();
		for (ComponentExecutionModelProvider provider : providers) {
			if (provider.getComponentName().equals(componentType)){
				return provider.createComponentExecutionModel(node);
			}
		}
		throw new ComponentExecutionModelProviderException("Provider for '"+componentType+"' was not found!!!");
	}
}
