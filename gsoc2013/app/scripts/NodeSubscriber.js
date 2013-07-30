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


//Javascript API that talks to rabbitMQ AMQP server.

//importing rabbit.js
var context = require('rabbit.js').createContext();

//importing express
var express = require('express');
var app 	= express();

//importing jquery
var $ = require('jquery');
var sleep = require('sleep');

var Server = require('http').createServer(app);
var io = require('socket.io').listen(Server);
Server.listen(1337);

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
        // sleep.usleep(100000);

    });

});


app.get('/',function(req,res){
    res.sendfile(__dirname+'/../..' + '/app/views/monitor.canvas.html');

});




