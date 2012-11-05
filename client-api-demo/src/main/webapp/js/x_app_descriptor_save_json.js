var jsonRequest = {};
var inputCount = 1;
var outputCount = 1;

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

$(document).ready(function(){

    $("p1").live("click", function(){
        inputCount++;
        $(this).after("<br/>Input Name         *:" +
            "<input type=&quot;text&quot; id=&quot;inputName" + inputCount + "&quot; name=&quot;inputName" + inputCount + "&quot; size=&quot;50&quot;><br/>" +
            "Input Type         *:" +
            "<input type=&quot;text&quot; id=&quot;inputType" + inputCount + "&quot; name=&quot;inputType" + inputCount + "&quot; size=&quot;50&quot;><br/>");
    });

    $("p2").live("click", function(){
        outputCount++;
        $(this).after("<br/>Output Name         *:" +
            "<input type=&quot;text&quot; id=&quot;outputName" + outputCount + "&quot; name=&quot;outputName" + outputCount + "&quot; size=&quot;50&quot;><br/>" +
            "Output Type         *:" +
            "<input type=&quot;text&quot; id=&quot;outputType" + outputCount + "&quot; name=&quot;outputType" + outputCount + "&quot; size=&quot;50&quot;><br/>");
    });

    $('[name="saveAppButton"]').click(function(){
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
            url: "http://localhost:7080/airavata-registry-rest-services/registry/api/applicationdescriptor/build/save",
            data: JSON.stringify(jsonRequest)
        }).done(function( msg ) {
                alert( "Data Saved: " + msg );
            });

    });
});