var app = require('express')();

var http = require('http').Server(app);

var io = require('socket.io')(http);
var admin = require("firebase-admin");
var assert = require('assert');


/*
Creating a client by Specify an API key and Promise constructor
*/

const googleMapsClient = require('@google/maps').createClient({
  key: //'Insert Your Key Here',
  Promise: Promise // 'Promise' is the native constructor.
});


/*
Initialization
*/
var userPlaceRequests = (io) =>{
  io.on('connection',(socket)=>{
    console.log(`Client ${socket.id} has connected to places services!`);

    webGetPlacesInformation(socket,io);
    getPlacesInformation(socket,io);
    detectDisconnection(socket,io);
    getThisPlaceInformation(socket,io);
    userCheckIn(socket,io);
    PlaceReview(socket,io);

  });
};

/*
This function takes the places search requests from web page and send the results back
*/

function webGetPlacesInformation(socket,io){
  socket.on('webGetPlacesInfo',(data)=>{
    console.log(data);
    googleMapsClient.places({
      query : data.query,
      language : data.language,
      location : [parseFloat(data.latitude),parseFloat(data.longitude)],
      radius : data.radius,
      minprice : data.minprice,
      maxprice : data.maxprice,
      opennow : data.opennow,
      type : data.type,
      pagetoken : data.pagetoken
    })
    .asPromise()
    .then((response) => {
      Object.keys(io.sockets.sockets).forEach((id)=>{
        if (id == socket.id) {
          console.log(response);
          io.to(id).emit('response',{response});
        }
      });
    })
    .catch((error) => {
      console.log(error);
      Object.key(io.sockets.sockets).forEach((id)=>{
        if (id == socket.id){
          io.to(id).emit('error',{error});
        }
      });
    });
  });
}


/*
This function takes the places seacrh requests from Android App and send the results of all places
*/


function getPlacesInformation(socket,io){
  socket.on('getPlacesInfo',(data)=>{
    console.log(data);
    var lat = parseFloat(data.latitude);
    var long = parseFloat(data.longitude);
    var email = data.email;
    var uname = data.uname;
    var db = admin.database();
    var ref = db.ref(`userLocation`);
    if (email!=''){
      var userRef = ref.child(encodeEmail(email));
      userRef.set({
        email:email,
        uname:uname,
        lati:lat,
        longi:long
      });
    }
    googleMapsClient.places({
      query : data.query,
      language : data.language,
      location : [parseFloat(data.latitude),parseFloat(data.longitude)],
      radius : data.radius,
      minprice : data.minprice,
      maxprice : data.maxprice,
      opennow : data.opennow,
      type : data.type,
      pagetoken : data.pagetoken
    })
    .asPromise()
    .then((response) => {
      Object.keys(io.sockets.sockets).forEach((id)=>{
        if (id == socket.id) {
          console.log(response);
          io.to(id).emit('response',{response});
        }
      });
    })
    .catch((error) => {
      console.log(error);
      Object.key(io.sockets.sockets).forEach((id)=>{
        if (id == socket.id){
          io.to(id).emit('error',{error});
        }
      });
    });
  });
}

/*
This function takes the place details requests from Android App and send the result of that particular place
*/


function getThisPlaceInformation(socket,io){
  socket.on('getThisPlace',(data)=>{
    console.log(data);
    googleMapsClient.place({
      placeid : data.placeid
      })
    .asPromise()
    .then((response) => {
      Object.keys(io.sockets.sockets).forEach((id)=>{
        if (id == socket.id) {
          console.log(response.json);
          io.to(id).emit('response',{response});
        }
      });
    })
    .catch((err) => {
      console.log(err);
      Object.key(io.sockets.sockets).forEach((id)=>{
        if (id == socket.id){
          io.to(id).emit('error',{err});
        }
      });
    });
  });
}


/*
This function allows user to check in to one particular place
*/


function userCheckIn(socket,io){
  socket.on('checkUserIn',(data) =>{
    var email = data.email;
    var placeId = data.placeid;
    var name = data.name;
    var picture = data.picture;

    var db = admin.database();
    var ref = db.ref(`usersCheckIns`);
    var placeRef = ref.child(placeId);
    var userRef = placeRef.child(encodeEmail(email));
    var date = {
        data:admin.database.ServerValue.TIMESTAMP
      };
    placeRef.once('value',(snapshot) =>{
      if (snapshot.hasChild(encodeEmail(email))){
        userRef.once('value',(snapshot)=>{
          var newcount = snapshot.val().count+1;
          userRef.update({
            count:newcount,
            latestCheckin:date
          });
        });
      }
      else {
        userRef.set({
            email:email,
            userName:name,
            userPicture:picture,
            latestCheckin:date,
            count:1
          });
      }
    });
    console.log("Checkin Successful");
});
}


/*
This function allows user to write a review to one particular place
*/


function PlaceReview(socket,io){
  socket.on('insertPlaceReview',(data) =>{
    var email = data.email;
    var placeId = data.placeid;
    var name = data.name;
    var picture = data.picture;
    var review = data.review;

    var db = admin.database();
    var ref = db.ref(`placeReviews`);
    var placeRef = ref.child(placeId);
    var userRef = placeRef.child(encodeEmail(email));
    var date = {
        data:admin.database.ServerValue.TIMESTAMP
      };
    placeRef.once('value',(snapshot) =>{
      if (snapshot.hasChild(encodeEmail(email))){
        userRef.once('value',(snapshot)=>{
          var newreview = review;
          userRef.update({
            review:newreview,
            latestReviewDate:date
          });
        });
      }
      else {
        userRef.set({
            email:email,
            userName:name,
            userPicture:picture,
            latestReviewDate:date,
            review:review
          });
      }
    });
    console.log("Review Added to place:"+placeId);
});
}


/*
This function detects the socket disconnections
*/

function detectDisconnection(socket,io){
  socket.on('disconnect',()=>{
    console.log('A client has disconnected from places service');
  });
}


/*
firebase db doesn't allow "." in keys, this function replaces all "." with ","
*/

function encodeEmail(email){
  return email.replace(/\./g,',');
}


module.exports = {
  userPlaceRequests
}
