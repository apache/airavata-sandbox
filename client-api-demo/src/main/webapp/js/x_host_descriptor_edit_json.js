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

function initButtons() {
    $("#hostType").change(function() {
        if("Local" == $(this).val()) {
            $(".endpoint").hide();
        } else {
            $(".endpoint").show();
        }
    });

    $("#updateHostButton").click(function(){
        var hostName = $("#hostName1").val();
        var hostAddress = $("#hostAddress1").val();
        var hostEndpoint = $("#hostEndpoint1").val();
        var gatekeeperEndpoint = $("#gatekeeperEndpoint1").val();

        var updateUrl = "http://localhost:7080/airavata-rest-services/registry/api/descriptors/hostdescriptor/update";

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
                url: updateUrl,
                data: JSON.stringify({
                    "hostname": hostName,
                    "hostAddress": hostAddress
                })
            }).done(function(msg) {
                    alert( "Host Updated : " + msg );
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
                url: updateUrl,
                data: JSON.stringify({
                    "hostname": hostName,
                    "hostAddress": hostAddress,
                    "hostEndpoint" : hostEndpoint,
                    "gatekeeperEndpoint" : gatekeeperEndpoint
                })
            }).done(function(msg) {
                    alert( "Host Updated : " + msg );
                });
        }

    });

    $("#deleteHostButton").click(function(){
        var hostName = $("#hostName1").val();
        var deleteUrl = "http://localhost:7080/airavata-rest-services/registry/api/descriptors/hostdescriptor/delete";
        alert("Delete button clicked!");
        alert("hostname : " + hostName);

        $.ajax({
            type: "DELETE",
            url: deleteUrl + "?hostname=" + hostName
        }).done(function( msg ) {
                alert( "Host Deleted: " + msg );
            });

    });
}

$(document).ready(function(){
    $(".endpoint").hide();
    initButtons();
});