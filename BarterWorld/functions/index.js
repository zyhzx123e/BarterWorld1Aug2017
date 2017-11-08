const functions = require('firebase-functions');
const admin = require('firebase-admin');

admin.initializeApp(functions.config().firebase);

exports.sendNotification = functions.database.ref("Barter_Posts/{postId}")
.onWrite(event => {

    var request = event.data.val();
//d8UraHDpkQU:APA91bGE788ToS7dJm46TPNTFVwknmDI5z3csj4rk4qpNPTxjuwdeCRC4YEQl9jFu2hDk288QGpalWYvCQqcsXJ7zgHAvmRgIF486U2SB_aP3Gzi9huisUrGReaQUeLr8JZ1EKnGIgE_
//clBdOhRikvk:APA91bGSdT6Z4J-FE5thJ7mdVxGHMgybASVaVFEdVVQOCSExJVwCqrTYL_3oqn-v8yIov7tQsAogfYTBVVpxkOSCXz2AIhTeW_GHGwa9jewiMN3_KPY29MjbqaxqNxeotZ5UMQBer2gN
    const token = "clBdOhRikvk:APA91bGSdT6Z4J-FE5thJ7mdVxGHMgybASVaVFEdVVQOCSExJVwCqrTYL_3oqn-v8yIov7tQsAogfYTBVVpxkOSCXz2AIhTeW_GHGwa9jewiMN3_KPY29MjbqaxqNxeotZ5UMQBer2gN";
    var payload = {

       // to:token,
        data:{
                    id:"1"
        },
        notification:{
                    sound : "default",
                    priority : "high",
                  //  icon: request.barter_img,
                    body: request.description,
                    title: request.title
        }
    };

    //The topic variable can be anything from a username, to a uid
    // this approach much better than using the refresh token
    //as you can subscribe to someone's phone number, username, or some other unique identifier
    //to communicate between
//fcm token:
//clBdOhRikvk:APA91bGSdT6Z4J-FE5thJ7mdVxGHMgybASVaVFEdVVQOCSExJVwCqrTYL_3oqn-v8yIov7tQsAogfYTBVVpxkOSCXz2AIhTeW_GHGwa9jewiMN3_KPY29MjbqaxqNxeotZ5UMQBer2gN

    admin.messaging().sendToDevice("clBdOhRikvk:APA91bGSdT6Z4J-FE5thJ7mdVxGHMgybASVaVFEdVVQOCSExJVwCqrTYL_3oqn-v8yIov7tQsAogfYTBVVpxkOSCXz2AIhTeW_GHGwa9jewiMN3_KPY29MjbqaxqNxeotZ5UMQBer2gN", payload)//sendToDevice:tokenid   //sendToTopic
    .then(function(response){
        console.log("Successfully sent message: ", response);
        console.log(payload.notification.title, response);
        console.log(payload.notification.body, response);
    })
    .catch(function(error){
        console.log("Error sending message: ", error);
    })
})


// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });
