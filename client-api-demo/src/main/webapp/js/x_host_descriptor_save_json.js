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
    $(window).load(function () {
        $("div").hide();
    });

    $("select[name='drop1']").change(function() {
        //alert($(this).val());
        if("Local" == $(this).val()) {
            $("div").hide();
        } else {
            $("div").show();
        }
    });

    $('[name="saveHostButton"]').click(function(){
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
                url: "http://localhost:7080/airavata-registry-rest-services/registry/api/hostdescriptor/save",
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
                url: "http://localhost:7080/airavata-registry-rest-services/registry/api/hostdescriptor/save",
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
});