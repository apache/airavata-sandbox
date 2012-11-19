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

$(document).ready(function(){

    $('[name="btn2"]').click(function(){

        $.ajax({
            beforeSend: function(x) {
                if (x && x.overrideMimeType) {
                    x.overrideMimeType("application/j-son;charset=UTF-8");
                }
            },
            type: "GET",
            dataType: "json",
            data : {user : "admin"},
            contentType: "application/json;charset=utf-8",
            url: "http://localhost:7080/airavata-rest-services/registry/api/provenanceregistry/get/experimentId/user",
            error: function(ajaxrequest, ajaxOptions, thrownError){
                alert(thrownError);
            }

        }).done(function(msg) {
                $("#experiment-list").html(JSON.stringify(msg));
            });

    });

    $('[name="btn3"]').click(function(){
        var experimentId = $("#experimentId1").val();

        $.ajax({
            beforeSend: function(x) {
                if (x && x.overrideMimeType) {
                    x.overrideMimeType("application/j-son;charset=UTF-8");
                }
            },
            type: "GET",
            dataType: "json",
            data : {experimentId : experimentId},
            contentType: "application/json;charset=utf-8",
            url: "http://localhost:7080/airavata-rest-services/registry/api/provenanceregistry/get/experiment",
            error: function(ajaxrequest, ajaxOptions, thrownError){
                alert(thrownError);
            }

        }).done(function(msg) {
                $("#experiment-data").html(JSON.stringify(msg));
            });

    });
});