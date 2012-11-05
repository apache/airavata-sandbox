
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
            contentType: "application/json;charset=utf-8",
            url: "http://localhost:7080/airavata-registry-rest-services/registry/api/get/hostdescriptors",
            success: function(data, status, settings) {
                var keys=[],result='';
                $.each(data.hostDescriptions,function(i,row){
                    $.each(row,function(key,value){
                        if ($.inArray(key,keys)==-1) keys.push(key);
                    })
                });
                result+="<thead><tr>";
                $.each(keys,function(i,key){
                    result+="<th>"+key+"<\/th>";
                });
                result+="<\/tr><\/thead><tbody>";
                $.each(data.hostDescriptions,function(i,row){
                    result+="<tr>";
                    $.each(keys,function(i,key){
                        result+="<td>"+ (row[key]||'') + "<\/td>";
                    });
                    result+="<\/tr>";
                });
                result+="<\/tbody>";
                $('#display').html(result);

            },
            error: function(ajaxrequest, ajaxOptions, thrownError){
                alert(thrownError);
            }

        });/*.done(function(msg) {
         alert( "Data Saved: " + JSON.stringify(msg));
         });*/

    });
});