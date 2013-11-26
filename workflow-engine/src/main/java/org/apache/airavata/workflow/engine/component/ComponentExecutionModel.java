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


public interface ComponentExecutionModel extends ComponentModel {
	public void addInput(ComponentExecutionInput...componentInputs);
	public void setWorkflowExecutionGraph();
	
	/**
	 * Attempts self execution once requirements are fulfilled
	 */
	public void activate(ComponentExecutionModel parent);
	
	public boolean isActivated();
	public boolean isReady();
	
	public void initialize(ComponentExecutionModel parent);
	public void execute();
	public void cancelExecution();
	public void pauseExecution();
	
	/**
	 * Disables self execution
	 */
	public void deactivate();
	
	/***----------------------IO Data-----------------------***/
	public boolean isInputPortSet(String portName);
	public ComponentExecutionInput getInputPort(String portName);
	public List<ComponentExecutionInput> getInputPorts();
	public boolean isOutputPortSet(String portName);
	public ComponentExecutionOutput getOutputPort(String portName);
	public List<ComponentExecutionOutput> getOutputPorts();
	
	/**
	 * List of component executions this component execution is dependent on 
	 * @return
	 */
	public List<ComponentExecutionModel> getDependents();

	public ComponentExecutionState getComponentExecutionState();
	
	public void registerExecutionListener(ComponentExecutionListener listener);
	public void removeExecutionListener(ComponentExecutionListener listener);
	public ComponentExecutionListener[] getExecutionListeners();
	
}
