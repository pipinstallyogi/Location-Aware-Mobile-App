var app = require('express')();
var http = require('http').Server(app);

var io = require('socket.io')(http);
var admin = require("firebase-admin");
var port = process.env.PORT || 3000;


var serviceAccount = require(__dirname+'/private/serviceAccountKey.json');

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: //" Insert Your Database link Here"
});

var accountRequests = require('./firebase/account-services');

var friendRequests = require('./firebase/friend-services');

var placeRequests = require('./places/places-services');

accountRequests.userAccountRequests(io);
friendRequests.userFriendsRequests(io);
placeRequests.userPlaceRequests(io);


http.listen(port,()=>{
  console.log('Server is listening on port 3000');
});



app.get('/',function(req,res){
	res.sendFile(__dirname+'/views/index.html')
});
