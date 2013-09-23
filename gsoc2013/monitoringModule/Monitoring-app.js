


//Javascript API that talks to rabbitMQ AMQP server.

//importing rabbit.js
var context = require('rabbit.js').createContext();
//var routes = require('./routes');


//importing express
var express = require('express');
var app = express();

//importing jquery
var $ = require('jquery');

var Server = require('http').createServer(app);

//hook socket.io to express
var io = require('socket.io').listen(Server);

// start server at the specified port
Server.listen(1337);


// Configuration

app.configure(function(){
  app.set('views', __dirname + '/views');
  app.set('view engine', 'jade');
  app.use(express.bodyParser());
  app.use(express.static(__dirname + '/public'));
  //app.use(app.router);
});

var st = io.of('status');
var tp = io.of('timestamp');
var des = io.of('description');

context.on('ready', function() {
         sub = context.socket('SUB');
         sub.connect('ws-messenger-fanout');
         sub.setEncoding('utf8');
        
         sub.on('data', function(note) { 
         $note = $(note);
    
         var status= $note.filter('*').eq(0)[0].nodeName;
         var status= status.substring(3);
         var timestamp = $note.find("ns\\:timestamp").text();
         var description = $note.find("ns\\:description").text();
         var component = " ";
         io.sockets.emit('status',status);
         io.sockets.emit('timestamp', timestamp);
         io.sockets.emit('description',description);
         io.sockets.emit('component',component);

      });

    });

app.get('/',function(req,res){
  res.render('index');

});



