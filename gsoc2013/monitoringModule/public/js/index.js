
function init() {
		var socket = io.connect('/'),
		container = document.getElementById('container');

		socket.on('status', function (data) {
		var row = $("<tr><td>"+data+"</td></tr>");
		$("#status").append(row).css({width : '300px', height: '100px'});		            
		});


		socket.on('timestamp', function (data) {
		var row = $("<tr><td>"+data+"</td></tr>");
		$("#time").append(row).css({width : '300px', height: '100px'});
		});

		socket.on('description', function (data) {
		if(data != null && data.trim().length != 0)
		{
		var row = $("<tr><td>"+data+"</td></tr>");      
		$("#message_content").append(row).css({width : '300px', height: '100px'});
		}

		if(data.trim().length == 0)
		{ 
		console.log("I am in nbsp");
		var row = $("<tr><td>&nbsp;</td></tr>"); 
		$("#message_content").append(row).css({width : '300px', height: '100px'});
		}});


		socket.on('component', function (data) {
		if(data != null && data.trim().length != 0)
		{
		 var row = $("<tr><td>"+data+"</td></tr>");          
		 $("#component").append(row).css({width : '300px', height: '100px'});			
		}

		if(data.trim().length == 0)
		{ 
		console.log("I am in nbsp");
		var row = $("<tr><td>&nbsp;</td></tr>"); 
		$("#component").append(row).css({width : '300px', height: '100px'});
		}});

}

$(document).on('ready', init);

