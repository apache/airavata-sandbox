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

import org.apache.airavata.workflow.model.wf.InvalidDataFormatException;

public class ComponentExecutionInput extends ComponentExecutionData {
	private boolean optional=false;
	
	public boolean isOptional() {
		return optional;
	}
	private void setOptional(boolean optional) {
		this.optional = optional;
	}
	public ComponentExecutionInput(String name, String type, Object defaultValue,
			Object value, boolean optional) throws InvalidDataFormatException {
		setName(name);
		setType(type);
		setValue(value);
		setOptional(optional);
	}
	public ComponentExecutionInput(String name, Object value)
			throws InvalidDataFormatException {
		this(name,"String",null,value,false);
	}


}
