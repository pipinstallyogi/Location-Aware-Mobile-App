var app = require('express')();

var http = require('http').Server(app);

var io = require('socket.io')(http);
var admin = require("firebase-admin");


/*
Initialization
*/

var userAccountRequests = (io) =>{
  io.on('connection',(socket)=>{
    console.log(`Client ${socket.id} has connected!`);


    updateProfilePicture(socket,io);
    detectDisconnection(socket,io);
    registerUser(socket,io);
    logUserIn(socket,io);
    deleteUser(socket,io);
    resetPassword(socket,io);
  });
};


/*
This function updates the profile picture url in database in one's own account and also in friends accounts
*/


function updateProfilePicture(socket,io){
  socket.on('userUpdatedPicture',(data) =>{
    console.log(data.email);
    console.log(data.picUrl);
    var db = admin.database();
    var ref = db.ref(`users`);
    var userRef = ref.child(encodeEmail(data.email)).child('userPicture');
    userRef.set(data.picUrl);

    var userFriendRef = db.ref('userFriends').child(encodeEmail(data.email));
    userFriendRef.orderByChild("email").on("child_added",(snapshot)=>{
      var friendRef = db.ref('userFriends').child(encodeEmail(snapshot.val().email))
      .child(encodeEmail(data.email)).child('userPicture');
      friendRef.set(data.picUrl);
    });
});

}


/*
This function notifies about password reset mail
*/

function resetPassword(socket,io){
  socket.on('resetUserPassword',(data) =>{
    var email = data.email
    .then(()=> {
        console.log('Email with reset instructions sent to '+email);
        })
    .catch((error)=> {
      console.log(error);
    });
});
}


/*
This function lets the user login and get the authtoken so that they can login with it later
*/


function logUserIn(socket,io){
  socket.on('userInfo',(data)=>{
    admin.auth().getUserByEmail(data.email)
    .then((userRecord)=>{

      var db = admin.database();
      var ref = db.ref('users');
      var userRef = ref.child(encodeEmail(data.email));

      userRef.once('value',(snapshot) =>{
        var additionalClaims = {
          email:data.email
        };

        admin.auth().createCustomToken(userRecord.uid,additionalClaims)
        .then((customToken) =>{

          Object.keys(io.sockets.sockets).forEach((id)=>{
            if (id == socket.id) {
              var token = {
                authToken:customToken,
                email:data.email,
                photo:snapshot.val().userPicture,
                displayName:snapshot.val().userName
              }

              userRef.child('hasLoggedIn').set(true);

              io.to(id).emit('token',{token});
            }
          });

        }).catch((error)=>{
          console.log(error.message);

          Object.keys(io.sockets.sockets).forEach((id)=>{
            if (id == socket.id) {
              var token = {
                authToken:error.message,
                email:'error',
                photo:'error',
                displayName:'error'
              }
              io.to(id).emit('token',{token});
            }
          });
        });
      });
    });
  });
}


/*
This function creates a new user and also includes user details in database
*/


function registerUser(socket,io){
  socket.on('userData',(data)=>{
    admin.auth().createUser({
      email:data.email,
      displayName:data.userName,
      password:data.password
        })
    .then((userRecord)=>{
      console.log('User was registered successfully');
      var db = admin.database();
      var ref = db.ref('users');
      var userRef = ref.child(encodeEmail(data.email));
      var date = {
        data:admin.database.ServerValue.TIMESTAMP
      };

      userRef.set({
        email:data.email,
        userName:data.userName,
        userPicture:'https://dl.dropboxusercontent.com/s/sdmw0p5avpvh41g/635319915.jpg?dl=0',
        dateJoined:date,
        hasLoggedIn:false
      });

      Object.keys(io.sockets.sockets).forEach((id)=>{
        if (id == socket.id) {
          var message = {
            text:'Success'
          }
          console.log(message);
          io.to(id).emit('message',{message});
        }
      });


    }).catch((error)=>{
      Object.keys(io.sockets.sockets).forEach((id)=>{
        console.log(error.message);
        if (id == socket.id) {
          var message = {
            text:error.message
          }
          io.to(id).emit('message',{message});
        }
      });
    });
  });
}


/*
This function allows the user to delete his profile completely
*/



function deleteUser(socket,io){
  socket.on('userDelete',(data)=>{
    admin.auth().getUserByEmail(data.email)
    .then((userRecord)=>{

      var db = admin.database();
      var userRef = db.ref('users');
      var userTokenRef = db.ref('userToken');
      var userChatRoomsRef = db.ref('userChatRooms');
      var userFriendsRef = db.ref('userFriends');
      var userMessagesRef = db.ref('userMessages');



      userRef.once('value',(snapshot) =>{
        if (snapshot.hasChild(encodeEmail(data.email))){
          userRef.child(encodeEmail(data.email)).remove();
        }
      });

      userTokenRef.once('value',(snapshot) =>{
        if (snapshot.hasChild(encodeEmail(data.email))){
          userTokenRef.child(encodeEmail(data.email)).remove();
        }
      });

      userChatRoomsRef.once('value',(snapshot) =>{
        if (snapshot.hasChild(encodeEmail(data.email))){
          userChatRoomsRef.child(encodeEmail(data.email)).remove();
        }
      });

      userFriendsRef.once('value',(snapshot) =>{
        if (snapshot.hasChild(encodeEmail(data.email))){
          userFriendsRef.child(encodeEmail(data.email)).remove();
        }
      });

      userMessagesRef.once('value',(snapshot) =>{
        if (snapshot.hasChild(encodeEmail(data.email))){
          userMessagesRef.child(encodeEmail(data.email)).remove();
        }
      });

      admin.auth().deleteUser(userRecord.uid)
      .then(() =>{
             console.log("Successfully deleted user");
           }).catch((error)=>{
             console.log(error.message);
             Object.keys(io.sockets.sockets).forEach((id)=>{
               if (id == socket.id) {
                 var token = {
                   authToken:error.message,
                 }
                 io.to(id).emit('token',{token});
               }
             });
           });
         });
       });
     }


/*
This function detects the socket disconnections
*/

function detectDisconnection(socket,io){
      socket.on('disconnect',()=>{
        console.log('A client has disconnected');
      });
}

/*
firebase db doesn't allow "." in keys, this function replaces all "." with ","
*/

function encodeEmail(email){
  return email.replace(/\./g,',');
}





module.exports = {
  userAccountRequests
}
