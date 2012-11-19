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
	$("#hostType").change(function() {
        if("Local" == $(this).val()) {
            $(".endpoint").hide();
        } else {
            $(".endpoint").show();
        }
    });

    $("#saveHostButton").click(function(){
        //alert($(this).val());
        var hostName = $("#hostName1").val();
        var hostAddress = $("#hostAddress1").val();
        var hostEndpoint = $("#hostEndpoint1").val();
        var gatekeeperEndpoint = $("#gatekeeperEndpoint1").val();
        var xml = $('<type:hostDescription xmlns:type="http://schemas.airavata.apache.org/gfac/type"><type:hostName>' + hostName + '</type:hostName><type:hostAddress>' + hostAddress + '</type:hostAddress></type:hostDescription>');

        var xmlData= $(xml);
        var xmlString;
        if (window.ActiveXObject){
            xmlString = xmlData.xml;
        } else {
            var oSerializer = new XMLSerializer();
            xmlString = oSerializer.serializeToString(xmlData[0]);
        }
        console.log(xmlString);

        var url = "http://localhost:7080/airavata-rest-services/registry/api/descriptors/hostdescriptor/save";
//        var url = "http://localhost:7080/airavata-registry-rest-services/registry/api/hostdescriptor/save";

        if (("" == hostEndpoint) || ("" == gatekeeperEndpoint)) {
            alert("if Case");
            $.ajax({

                beforeSend: function(x) {
                    if (x && x.overrideMimeType) {
                        x.overrideMimeType("application/j-son;charset=UTF-8");
                    }
                },

                type: "POST",
                dataType: "json",
                contentType: "application/json;charset=utf-8",
                url: url,
                data: JSON.stringify({
                    "hostname": hostName,
                    "hostAddress": hostAddress
                })
            }).done(function( msg ) {
                    alert( "Data Saved: " + msg );
                });
        } else {
            alert("end Case");
            $.ajax({
                beforeSend: function(x) {
                    if (x && x.overrideMimeType) {
                        x.overrideMimeType("application/j-son;charset=UTF-8");
                    }
                },
                type: "POST",
                dataType: "json",
                contentType: "application/json;charset=utf-8",
                url: url,
                data: JSON.stringify({
                    "hostname": hostName,
                    "hostAddress": hostAddress,
                    "hostEndpoint" : hostEndpoint,
                    "gatekeeperEndpoint" : gatekeeperEndpoint
                })
            }).done(function( msg ) {
                    alert( "Data Saved: " + msg );
                });
        }

    });
}

$(document).ready(function(){
    $(".endpoint").hide();
    initButtons();
});