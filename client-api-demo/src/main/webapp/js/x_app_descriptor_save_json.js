/*
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
 */

function xmlToString(xml) {
    var xmlData = $(xml);
    var xmlString;
    if (window.ActiveXObject) {
        xmlString = xmlData.xml;
    } else {
        var oSerializer = new XMLSerializer();
        xmlString = oSerializer.serializeToString(xmlData[0]);
    }
    console.log(xmlString);
    return xmlString;
}

function initButtons() {
	var jsonRequest = {};
	var inputCount = 1;
	var outputCount = 1;

    $("#addInputButton").live("click", function(){
        inputCount++;
        $(this).before("<br>");
        $(this).before("<label class=\"span2\">Input Name         *:" +
                "</label><input type=\"text\" id=\"inputName" + inputCount + "\" name=\"inputName" + inputCount + "\" value=\"echo_input\">");

        $(this).before("<label class=\"span2\">Input Type         *:" +
        		"</label><input type=\"text\" id=\"inputType" + inputCount + "\" name=\"inputType" + inputCount + "\" value=\"String\">");
    });

    $("#addOutputButton").live("click", function(){
        outputCount++;
        $(this).before("<label class=\"span2\">Output Name         *:" +
        		"</label><input type=\"text\" id=\"outputName" + outputCount + "\" name=\"outputName" + outputCount + "\" value=\"echo_output\">");

        $(this).before("<label class=\"span2\">Output Type         *:" +
        		"</label><input type=\"text\" id=\"outputType" + outputCount + "\" name=\"outputType" + outputCount + "\" value=\"String\">");
    });

    $("#saveAppButton").click(function(){
        var appName = $("#appName1").val();
        var hostName = $("#hostName1").val();
        var serviceName = $("#serviceName1").val();
        var exeuctableLocation = $("#exeuctableLocation1").val();
        var projAccNumber = $("#projAccNumber1").val();

        var scratchWorkingDirectory = $("#scratchWorkingDirectory1").val();
        var maxMemory = $("#maxMemory1").val();
        var queueName = $("#queueName1").val();
        var cpuCount = $("#cpuCount1").val();
        var nodeCount = $("#nodeCount1").val();

        var inputName1 = $("#inputName1").val();
        var inputType1 = $("#inputType1").val();
        var outputName = $("#outputName1").val();
        var outputType = $("#outputType1").val();

        var applicationDescType = "Gram"; // TODO : input

        jsonRequest["name"] = appName;
        jsonRequest["projectNumber"] = projAccNumber;
        jsonRequest["jobType"] = "single"; // TODO : input
        jsonRequest["queueName"] = queueName;
        jsonRequest["applicationDescType"] = applicationDescType;
        jsonRequest["executablePath"] = exeuctableLocation;
        jsonRequest["workingDir"] = scratchWorkingDirectory;
        jsonRequest["cpuCount"] = cpuCount;
        jsonRequest["hostdescName"] = hostName;
        jsonRequest["maxMemory"] = maxMemory;
        jsonRequest["maxWallTime"] = "10"; //TODO
        jsonRequest["minMemory"] = "4"; //TODO
        jsonRequest["nodeCount"] = nodeCount;
        jsonRequest["processorsPerNode"] = "3"; //TODO

        var inArray = [];
        for(var j=1; j<inputCount+1; j++) {
            var input = {};
            input["dataType"] = "input";
            input["description"] = "empty";
            input["name"] = "name"; //$("#inputName" + j+1).val();
            input["type"] = "type"; //$("#inputType" + j+1).val();
            inArray[j-1] = input;

            console.log("input : " + j);
            console.log(JSON.stringify(input));
        }

        var outArray = new Array();
        for(j=1; j<outputCount+1; j++) {
            var output = {};
            output["dataType"] = "output";
            output["description"] = "empty";
            output["name"] = "name"; //$("#outputName" + j+1).val();
            output["type"] = "type"; //$("#outputType" + j+1).val();
//                output["name"] = $('#outputName'.concat(j+1)).val();
//                output["type"] = $('#outputType'.concat(j+1)).val();
            outArray[j-1] = output;
        }

        var serviceDesc = {};
        serviceDesc["serviceName"] = serviceName;
        serviceDesc["inputParams"] = inArray;
        serviceDesc["outputParams"] = outArray;
        jsonRequest["serviceDescriptor"] = serviceDesc;
        console.log(JSON.stringify(jsonRequest));

        $.ajax({
            beforeSend: function(x) {
                if (x && x.overrideMimeType) {
                    x.overrideMimeType("application/j-son;charset=UTF-8");
                }
            },
            type: "POST",
            dataType: "json",
            contentType: "application/json;charset=utf-8",
            url: "http://localhost:7080/airavata-rest-services/registry/api/descriptors/applicationdescriptor/build/save",
            data: JSON.stringify(jsonRequest)
        }).done(function( msg ) {
                alert( "Data Saved: " + msg );
            });

    });
}

$(document).ready(function(){
	initButtons();
});