const functions = require('firebase-functions');
const admin = require('firebase-admin');

admin.initializeApp(functions.config().firebase);
const nodemailer = require('nodemailer');
const mailTransport = nodemailer.createTransport({
    host: 'smtp.gmail.com',
    port: '465',
    secure: true,
    auth:{
        user: 'barterworld2017@gmail.com',
        pass: 'zyhzx123e'
    }

});
const newsAPIkey = "41b6b91fab704556b33d8082f47f0506";
const mailOptions={
    from: '"ZYH" <barterworld2017@gmail.com>',
    bcc: '425352086@qq.com',
    subject: ''+new Date().getTime()+'-firebase function email',
    text: 'test firebase function nodemailer send email'
}

var  email_time= null;

exports.sendEmail = functions.https.onRequest((req,res)=>{
    return mailTransport.sendMail(mailOptions).then(()=>{
     res.send('now is '+curTime+' has already reach the time '+email_time+' /n ');
        res.send('425352086@qq.com has received email');
    });
});

function setEmailTimeNull(){
 return email_time=null;
}

function setEmailTime(time){
 return  email_time=new Date().getTime()+time;
}

function sleep_delay(delay) {
        var start = new Date().getTime();
        while (new Date().getTime() < start + delay);
      }

function mail_out() {
    return mailTransport.sendMail(mailOptions);
}

function sendResponse(message, res, context) {
    res.setHeader('Content-Type', 'application/json');
    return res.send(JSON.stringify({
        "speech": message, //reply speech message, TTS
        "displayText": message,// reply text message
        "data": {},
        "contextOut": context,
        "source": "apiai-webhook"
    }))
}

exports.apiaiwebhook = functions.https.onRequest((req, res) => {

 console.log(req.body);
    const parameters = req.body.result.parameters;
    const intent = req.body.result.metadata.intentName;

    console.log(parameters); //Light
    console.log(intent); //turn on
    var message;

       var data = '';
                https.get('https://newsapi.org/v2/everything?q=' + parameters.query + '&sortBy=popularity&apiKey=' + newsAPIkey,
                    (resp) => {
                        // A chunk of data has been recieved.    
                        resp.on('data', (chunk) => { data += chunk; });
                        // The whole response has been received. Print out the result.    
                        resp.on('end', () => {
                            console.log(JSON.parse(data).explanation);
                            data = JSON.parse(data);
                            var randomArticles = Math.floor(Math.random() * data.articles.length);
                            var msg = data.articles[randomArticles]["title"] + ". " + data.articles[randomArticles]["description"];
                            sendResponse(msg, res, null);
                        });
                    })
                    .on("error", (err) => {
                        console.log("Error: " + err.message);
                    });

})




exports.period_mail = functions.https.onRequest((req,res)=>{

 setEmailTime(300000);
 /* var myinterval =  setInterval(function(){
   mail_out();

    },  60000);*/

     var curTime= new Date().getTime();
       if(curTime>email_time){
             //clearInterval(myinterval);
          res.send('now is '+curTime+' has already exceeds the time '+email_time+' clear interval');

       }else{
       res.send('not yet..'+curTime+' < '+email_time+' ');

       }

})

exports.wait_action = functions.https.onRequest((req,res)=>{

    sleep_delay(600000);//wait 10 min
                    var curTime= new Date().getTime();
                  return mailTransport.sendMail(mailOptions).then(()=>{
                                             email_time=null;//after fired this action, set the action_time back to null again
                                             res.send('now is '+curTime+' has already  425352086@qq.com has received email');

                                            });

})

exports.set_emailSendTime = functions.https.onRequest((req,res)=>{
    if(email_time==null){
            setEmailTime(120000);//in milli seconds:  2 min
             res.send('time set at :'+email_time);

    }else{

        var curTime= new Date().getTime();
        if(email_time!=null){

            if(curTime>email_time){

                //action to perform when the time reach at that time
                return mailTransport.sendMail(mailOptions).then(()=>{
                 email_time=null;//after fired this action, set the action_time back to null again
                 res.send('now is '+curTime+' has already reach the time '+email_time+' \n425352086@qq.com has received email');

                });
                //this action could be change to any other action such as bot grab more bunch of people start a group chat


            }else{
                 res.send('now is '+curTime+' havent reach the time '+email_time+' yet');
            }
        }
     //  res.send('time already set at :'+email_time);
    }
});

exports.print_emailTime = functions.https.onRequest((req,res)=>{
    if(email_time!=null){
     res.send('get Emailtime :'+email_time);
    }else{
      res.send('get null Emailtime :'+email_time);
    }

});

exports.set_emailTimeBackToNull = functions.https.onRequest((req,res)=>{
        if(email_time!=null){
         setEmailTimeNull();
        }else{
          res.send('Emailtime has already set at to null');
        }
});


exports.say_hi = functions.https.onRequest((req,res)=>{
      res.send('Hi There');
});




 //firebase db onwrite
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
