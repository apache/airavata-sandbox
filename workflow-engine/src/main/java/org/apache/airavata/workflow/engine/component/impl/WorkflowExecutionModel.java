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

package org.apache.airavata.workflow.engine.component.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.airavata.workflow.engine.component.ComponentExecutionInput;
import org.apache.airavata.workflow.engine.component.ComponentExecutionListener;
import org.apache.airavata.workflow.engine.component.ComponentExecutionModel;
import org.apache.airavata.workflow.engine.component.ComponentExecutionOutput;
import org.apache.airavata.workflow.engine.component.ComponentExecutionState;

public class WorkflowExecutionModel extends WorkflowModel implements ComponentExecutionModel{
	private Map<String,ComponentExecutionInput> workflowInputs;
	private boolean active=true;
	private ComponentExecutionState state=ComponentExecutionState.PENDING_INIT;
	private ComponentExecutionModel parent;
	@Override
	public void addInput(ComponentExecutionInput... componentInputs) {
		for (ComponentExecutionInput executionInput : componentInputs) {
			getWorkflowInputs().put(executionInput.getName(), executionInput);
		}
	}
	
	private Map<String,ComponentExecutionInput> getWorkflowInputs(){
		if (workflowInputs==null){
			workflowInputs=new HashMap<String,ComponentExecutionInput>();
		}
		return workflowInputs;
	}
	
	@Override
	public void setWorkflowExecutionGraph() {
		
	}

	@Override
	public void activate(ComponentExecutionModel parent) {
		active=true;
		execute();
	}

	@Override
	public boolean isActivated() {
		return active;
	}

	@Override
	public boolean isReady() {
		return false;
	}

	@Override
	public void execute() {
		
	}

	@Override
	public void cancelExecution() {
		
	}

	@Override
	public void pauseExecution() {
		
	}

	@Override
	public void deactivate() {
		active=false;
	}

	@Override
	public boolean isInputPortSet(String portName) {
		return false;
	}

	@Override
	public ComponentExecutionInput getInputPort(String portName) {
		return null;
	}

	@Override
	public List<ComponentExecutionInput> getInputPorts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isOutputPortSet(String portName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ComponentExecutionOutput getOutputPort(String portName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ComponentExecutionOutput> getOutputPorts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ComponentExecutionModel> getDependents() {
		return null;
	}

	@Override
	public ComponentExecutionState getComponentExecutionState() {
		return state;
	}

	@Override
	public void registerExecutionListener(ComponentExecutionListener listener) {
		
	}

	@Override
	public void removeExecutionListener(ComponentExecutionListener listener) {
		
	}

	@Override
	public ComponentExecutionListener[] getExecutionListeners() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initialize(ComponentExecutionModel parent) {
		this.parent=parent;
	}

}
