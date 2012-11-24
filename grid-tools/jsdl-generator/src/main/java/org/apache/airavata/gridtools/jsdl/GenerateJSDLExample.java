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

package org.apache.airavata.gridtools.jsdl;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.ogf.jsdl.ApplicationType;
import org.ogf.jsdl.FileNameType;
import org.ogf.jsdl.JobDefinitionType;
import org.ogf.jsdl.JobDescriptionType;
import org.ogf.jsdl.JobIdentificationType;
import org.ogf.jsdl.ObjectFactory;
import org.ogf.jsdl.POSIXApplicationType;

public class GenerateJSDLExample {

    /**
     * @param args
     */
    public static void main(String[] args) {

        try {

            ObjectFactory jsdlObjFact = new ObjectFactory();
            JobDefinitionType jsdlJobDefType = jsdlObjFact.createJobDefinitionType();
            JobDescriptionType jsdlJobDescType = jsdlObjFact.createJobDescriptionType();
            
            
            JobIdentificationType jsdlJobIdenType = jsdlObjFact.createJobIdentificationType();
            jsdlJobIdenType.setJobName("Airavata Test");
            jsdlJobIdenType.setDescription("Airavata JSDL Test Job");
            jsdlJobDescType.setJobIdentification(jsdlJobIdenType);
            
            POSIXApplicationType jsdlPosixAppType = jsdlObjFact.createPOSIXApplicationType();
            FileNameType execFileType = jsdlObjFact.createFileNameType();
            execFileType.setValue("/bin/date");
            jsdlPosixAppType.setExecutable(execFileType);
            JAXBElement<POSIXApplicationType> jsdlPosixApp = jsdlObjFact.createPOSIXApplication(jsdlPosixAppType);
            
            ApplicationType jsdlAppType = jsdlObjFact.createApplicationType();
            jsdlAppType.setApplicationName("Test Date");
            jsdlAppType.setApplicationVersion("v1.0");
            jsdlAppType.setDescription("Testing Date");
            jsdlAppType.getAny().add(jsdlPosixApp);
            jsdlJobDescType.setApplication(jsdlAppType);          
            
            jsdlJobDefType.setJobDescription(jsdlJobDescType);
            JAXBElement<JobDefinitionType> jsdlJobDef = jsdlObjFact.createJobDefinition(jsdlJobDefType);

            //generate the required jsdl
            JAXBContext jaxbContext = JAXBContext.newInstance(new Class[] { JobDefinitionType.class });
            StringWriter jsdlXMLString = new StringWriter();
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(jsdlJobDef, jsdlXMLString);
            System.out.println(jsdlXMLString.toString());
         
        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

}
