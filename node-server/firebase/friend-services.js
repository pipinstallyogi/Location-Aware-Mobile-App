var app = require('express')();

var http = require('http').Server(app);

var io = require('socket.io')(http);
var admin = require("firebase-admin");

var FCM = require('fcm-push');
var serverKey ='insert server key here';
var fcm = new FCM(serverKey);

/*
Initialization
*/

var userFriendsRequests = (io) =>{
  io.on('connection',(socket)=>{
    console.log(`Client ${socket.id} has connected to friend services!`);

    sendMessage(socket,io);
    approveOrDeclineFrienqRequest(socket,io);
    sendOrDeleteFriendRequest(socket,io);
    detectDisconnection(socket,io);

  });
};

/*
This function allows users to send messages and create chatrooms
*/

function sendMessage(socket,io){
  socket.on('details',(data)=>{
    var db = admin.database();
    var friendMessageRef = db.ref('userMessages').child(encodeEmail(data.friendEmail))
    .child(encodeEmail(data.senderEmail)).push();


    var newfriendMessagesRef = db.ref('newUserMessages').child(encodeEmail(data.friendEmail))
    .child(friendMessageRef.key);

    var chatRoomRef = db.ref('userChatRooms').child(encodeEmail(data.friendEmail))
    .child(encodeEmail(data.senderEmail));

      var message={
      messageId: friendMessageRef.key,
      messageText: data.messageText,
      messageSenderEmail: data.senderEmail,
      messageSenderPicture: data.senderPicture
    };

    var chatRoom = {
      friendPicture: data.senderPicture,
      friendName:data.senderName,
      friendEmail: data.senderEmail,
      lastMessage: data.messageText,
      lastMessageSenderEmail: data.senderEmail,
      lastMessageRead:false,
      sentLastMessage:true
    };

    friendMessageRef.set(message);
    newfriendMessagesRef.set(message);

    chatRoomRef.set(chatRoom);


  });
}


/*
This fuction allows users to accept a friend request of other users and cancel it.
*/

function approveOrDeclineFrienqRequest(socket,io){
  socket.on('friendRequestResponse',(data)=>{
        var db = admin.database();
        var friendRequestRef = db.ref('friendRequestsSent').child(encodeEmail(data.friendEmail))
        .child(encodeEmail(data.userEmail));
        friendRequestRef.remove();


        if (data.requestCode ==0) {
          var db = admin.database();
          var ref = db.ref('users');
          var userRef = ref.child(encodeEmail(data.userEmail));

          var userFriendsRef = db.ref('userFriends');
          var friendFriendRef = userFriendsRef.child(encodeEmail(data.friendEmail))
          .child(encodeEmail(data.userEmail));

          userRef.once('value',(snapshot)=>{
            friendFriendRef.set({
              email:snapshot.val().email,
              userName:snapshot.val().userName,
              userPicture:snapshot.val().userPicture,
              dateJoined:snapshot.val().dateJoined,
              hasLoggedIn:snapshot.val().hasLoggedIn
            });
          });
        }
  });
}


/*
This fuction allows users to send a friend request to other users and cancel.
*/


function sendOrDeleteFriendRequest(socket,io){
  socket.on('friendRequest',(data)=>{
    var friendEmail = data.email;
    var userEmail = data.userEmail;
    var requestCode = data.requestCode;


    var db = admin.database();
    var friendRef = db.ref('friendRequestRecieved').child(encodeEmail(friendEmail))
    .child(encodeEmail(userEmail));

    if (requestCode ==0) {
      var db = admin.database();
      var ref = db.ref('users');
      var userRef = ref.child(encodeEmail(data.userEmail));

      userRef.once('value',(snapshot)=>{
        friendRef.set({
          email:snapshot.val().email,
          userName:snapshot.val().userName,
          userPicture:snapshot.val().userPicture,
          dateJoined:snapshot.val().dateJoined,
          hasLoggedIn:snapshot.val().hasLoggedIn
        });
      });

      var tokenRef = db.ref('userToken');
      var friendToken = tokenRef.child(encodeEmail(friendEmail));

      friendToken.once("value",(snapshot)=>{
        var message = {
          to:snapshot.val().token,
          data:{
            title:'My Places',
            body:`Friend Request from ${userEmail}`
          },
        };

        fcm.send(message)
        .then((response)=>{
          console.log('Message sent!');
        }).catch((err)=>{
          console.log(err);
        });
      });

    } else{
      friendRef.remove();
    }

  });
}

/*
This function detects the socket disconnections
*/

function detectDisconnection(socket,io){
      socket.on('disconnect',()=>{
        console.log('A client has disconnected from friend services');
      });
}


/*
firebase db doesn't allow "." in keys, this function replaces all "." with ","
*/

function encodeEmail(email){
  return email.replace(/\./g,',');
}

module.exports = {
  userFriendsRequests
}
