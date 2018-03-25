const functions = require('firebase-functions');
const admin = require('firebase-admin');
var request = require('request-promise');



admin.initializeApp(functions.config().firebase);
const nodemailer = require('nodemailer');

const mailTransport = nodemailer.createTransport({
    host: 'smtp.gmail.com',
    port: '465',
    secure: true,
    auth:{
        user: 'ai.hr.bot@gmail.com',
        pass: 'zyhzx123e'
    }

});



const newsAPIkey = "41b6b91fab704556b33d8082f47f0506";
const mailOptions={
    from: '"avocado bot Email Reminder=>" <ai.hr.bot@gmail.com>',
    bcc: 'summer.yuennn@gmail.com',
    subject: ''+new Date().getTime()+'-firebase function email',
    text: 'test firebase function nodemailer send email'
};


const db_root = admin.database().ref();

var  email_time= null;


function email_push(){

 return mailTransport.sendMail(mailOptions);
}


exports.sendEmail = functions.https.onRequest((req,res)=>{
    return mailTransport.sendMail(mailOptions).then(()=>{
     res.send('now is '+curTime+' has already reach the time '+email_time+' /n ');
        res.send('425352086@qq.com has received email');
    });
});


exports.search_guy_in_db = functions.https.onRequest((req,res)=>{

  const parameters = req.body.result.parameters;
  var get_name = parameters.any;
  console.log('get the name: '+get_name);
    db_root.child('C-level/0').once('value').then(function(snapshot) {
      var guy = snapshot.val();
      console.log("user == "+guy);
      var name_of_guy = guy.name;
      // ...
        res.send('Our CEO is '+name_of_guy);
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

function sendSlackResponse(message, res, context) {
    res.setHeader('Content-Type', 'application/json');
    res.setHeader('Cache-Control', 'no-cache');
    let botResponse = {
        "speech": message, //reply speech message, TTS
        "displayText": message,// reply text message
        "data": {},
        "contextOut": context,
        "source": "apiai-webhook"
    };

    let slackMessage = {
        "text":""+message
    }
    botResponse.data = {};
    botResponse.data.slack = slackMessage;
    return res.send(JSON.stringify(botResponse))
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


exports.test_slack_chat = functions.https.onRequest((req, res) => {

    var message ='hi ther -- zyh';
    return sendSlackResponse(message, res, null);

});


function send_msg_to_slack(msg){

  var myJSONObject = {
"type": "message",
"channel": "#general",
"text": "<!everyone>" +msg

};
  request({
      url: "https://slack.com/api/chat.postMessage",
      method: "POST",
      headers: {
          'Authorization': 'Bearer xoxb-284155260963-IWxR1eyVNUclkyFukJ8ND2A7'
      },
      json: true,   // <--Very important!!!
      body: myJSONObject
  }, function (error, response, body) {
      console.log("Response: " + response);
      console.log("Body: " + body);
      console.log("error: " + error);

  });

}

function send_msg_to_slack_private(msg){

  var myJSONObject = {
"type": "message",
"channel": "#general",
"text": msg

};
  request({
      url: "https://slack.com/api/chat.postMessage",
      method: "POST",
      headers: {
          'Authorization': 'Bearer xoxb-284155260963-IWxR1eyVNUclkyFukJ8ND2A7'
      },
      json: true,   // <--Very important!!!
      body: myJSONObject
  }, function (error, response, body) {
      console.log("Response: " + response);
      console.log("Body: " + body);
      console.log("error: " + error);

  });

}

exports.test_send_msg_to_slack = functions.https.onRequest((req, res) => {
    var promises =[];

     var email_p= email_push();
     var msg_p= send_msg_to_slack("hi");
    promises.push(email_p);
    promises.push(msg_p);

     return Promise.all(promises);

});

function toTimestamp(strDate){
//alert(toTimestamp('02/13/2009 23:31:30'));
   var datum = Date.parse(strDate);
   return datum;
}

function sendRichSlackResponse(message, attachment, res, context) {
    res.setHeader('Content-Type', 'application/json');
    let botResponse = {
        "speech": message, //reply speech message, TTS
        "displayText": message,// reply text message
        "data": {},
        "contextOut": context,
        "source": "apiai-webhook"
    };

    let slackMessage = {
        "text":message,
        "attachment": attachment
    }
    botResponse.data = {};
    botResponse.data.slack = slackMessage;
    return res.send(JSON.stringify(botResponse))
}

function convertFirstUpp(text) {
    return text.substr(0, 1).toUpperCase() + text.substr(1);
}

function attachmentJson(nameArray) {
    var name = [];
    for (var i = 0; i < nameArray.length; i++) {
        name.push({ text:  nameArray[i] });

    }
    return name;
}
function sendSlackResponseWithAttachment(message, res, context, attachment) {
    res.setHeader('Content-Type', 'application/json');
    let botResponse = {
        "speech": message, //reply speech message, TTS
        "displayText": message,// reply text message
        "data": {},
        "contextOut": context,
        "source": "apiai-webhook"
    };


    var generatedAttachment =  attachmentJson(attachment);

    let slackMessage = {
        "text": message,
        "attachments" : generatedAttachment
    }

    console.log(generatedAttachment);
    botResponse.data = {};
    botResponse.data.slack = slackMessage;
    return res.send(JSON.stringify(botResponse))
}




exports.apiaiwebhook = functions.https.onRequest((req, res) => {
    // Grab the text parameter.
    console.log(req.body);
     console.log(JSON.stringify(req.body));
    const parameters = req.body.result.parameters;
    const intent = req.body.result.metadata.intentName;

    console.log(parameters); //Light
    console.log(intent); //turn on
    var message='';

    switch (intent) {

  case "people.search":

  var get_name = parameters.any.toLowerCase();
  console.log('get the name: '+get_name);

    var get_position='';
    var position_array=[];


    db_root.child('department').once('value').then(snapshot => {

        snapshot.forEach(department_child => {

            department_child.forEach(snap => {

                            var getname=snap.val().name.toLowerCase();
                                if(getname.includes(get_name)){
                                    get_position=snap.val().position;
                                    position_array=get_position.split(" ");
                                console.log('get the position array[0]: '+position_array[0]);
                                console.log('get the position array[1]: '+position_array[1]);
                                console.log('get the position from api.ai: '+get_position);

                                }
            })

     })

        message = get_name+' is the '+get_position+' of Our Company \n You can approach '+get_name+' at '+position_array[0]+' office';

      return sendSlackResponse(message, res, null);
       // res.send(get_name+' is the '+get_position+' of Our Company');
    });

        break;
        case "announcement":
                var msg = parameters.message;
                var myJSONObject = {
                    "type": "message",
                    "channel": "#general",
                    "text": "@everyone " + msg

                };
                request({
                    url: "https://slack.com/api/chat.postMessage",
                    method: "POST",
                    headers: {
                        'Authorization': 'Bearer xoxb-284155260963-IWxR1eyVNUclkyFukJ8ND2A7'
                    },
                    json: true,   // <--Very important!!!
                    body: myJSONObject
                }, function (error, response, body) {
                    console.log("Response: " + response);
                    console.log("Body: " + body);
                    console.log("error: " + error);

                });

                message = 'We are currently sending your response.';

                return sendSlackResponse(message, res, null);
                break;

    case "talent.search.specification.again":
                const contextParameters = req.body.result.contexts[0].parameters;
                console.log(contextParameters);
                var IT_title = contextParameters.IT_title;
                var IT_title1 = contextParameters.IT_title1;
                var past_company = contextParameters.past_company;

                var IT_one = false;
                var IT_two = false;
                var past_comp = false;
                var found = 0;
                var list = [];

                var ref = admin.database().ref("users").once("value", function (snapshot) {
                    snapshot.forEach(function (data) {
                        for (var i = 0; i < data.val().past_job_experience.length; i++) {
                            if (data.val().past_job_experience[i].company == past_company) {
                             console.log(' past_comp = true');
                                past_comp = true;
                            }
                            //  console.log(' for : '+i+'');
                        }
                        for (var i = 0; i < data.val().skills.length; i++) {
                            if (data.val().skills[i] == IT_title) {
                             console.log('  IT_one = true');
                                IT_one = true;
                            }
                            if (data.val().skills[i] == IT_title1) {
                              console.log('  IT_two = true');
                                IT_two = true;
                            }
                          //    console.log(' i : '+i);
                        }

                        if (IT_one && IT_two && past_comp) {
                           console.log('found++');
                            found++;
                           list.push(data.val().name);
                            IT_one=false;
                            IT_two=false;
                            past_comp=false;

                        }

                    });
                }).then((output) => {
                        console.log('=> then');
                    if (found > 0)
                        message = "You're in luck! There's about " + found + " of them that did! I have attached their contacts! Good luck in your project!";
                    else
                        meesage = "Too bad! I couldn't find any records.";
                    return sendSlackResponseWithAttachment(message, res, null, list);
                });

                break;

      case "talent.search":
                  var skill = parameters.IT_title;
                  var found = 0;
                  var ref = admin.database().ref("users").once("value", function (snapshot) {
                      snapshot.forEach(function (data) {
                          for (var i = 0; i < data.val().skills.length; i++) {
                              if (data.val().skills[i] == skill) {
                                  //console.log("found " + data.val().name);
                                  found++;
                              }
                          }
                      });
                  }).then((output) => {

                      message = "There's around " + found + " employees that does that, any specification?";
                      return sendSlackResponse(message, res, null);
                  });

                  break;

                 case "guide.user":
                          var par = parameters.accountant_desc;
                           var par_team_quote = parameters.team_quote.toLowerCase();
                           var par_answer = parameters.answer.toLowerCase();
                            var par_username_ori = parameters.user_name;
                           var par_username = parameters.user_name.toLowerCase();
                            var par_looklike=parameters.looklike;
                             var par_get_help=parameters.get_help;


                             var user_department='';
                          var claim_str='claim';
                          var full_text= req.body.result.resolvedQuery.toLowerCase();
                          var random_accountant='';
                           var random_accountant_contact='';

                          var pplarray=[];
                           var depart_pplarray=[];
                             var depart_pplarray_pos=[];
                               var personality_array=[];



                          if(claim_str==par){
                              db_root.child("zremember_name").set({
                                    temp_name: par_username_ori
                                         });



                             var ref = admin.database().ref("department/accounting").once("value", function (snapshot) {
                                                                                      snapshot.forEach(function (data) {
                                                                                       var name_get=data.val().name;
                                                                                        pplarray.push(name_get);

                                                                                      });
                                                                                  }).then((output) => {
                                                                                      var ppl_array_length = pplarray.length;
                                                                                      var ran=Math.floor(Math.random() * ppl_array_length);
                                                                                        random_accountant=pplarray[1];
                                                                                         console.log('random guy'+random_accountant);

                               message = "How are you "+par_username+"! You may refer to Our Accounting department Staff "+pplarray[1]+" , the accountant for this case, would you like to have the contact?";
                              return sendSlackResponse(message, res, null);

                                                                });


                                var ref1 = admin.database().ref("users").once("value", function (snapshot) {
                                  snapshot.forEach(function (data) {
                                       //   console.log('get guy name'+data.val().name+' -----'+random_accountant);

                                    if(data.val().name.includes(random_accountant) || data.val().name==random_accountant){
                                             random_accountant_contact=data.val().contact;
                                             console.log('random guy contact'+random_accountant_contact);
                                       }


                                 })

                                     });




                          }else if(par_answer.includes('sure') || par_answer.includes('yeah')){

var get_name_accountant='';
var get_name_accountant_contact='';

 var ref = admin.database().ref("department/accounting").once("value", function (snapshot) {
                                                                                      snapshot.forEach(function (data) {
                                                                                       var name_get=data.val().name;
                                                                                        pplarray.push(name_get);

                                                                                      });
                                                                                  }).then((output) => {
                                                                                      var ppl_array_length = pplarray.length;
                                                                                      var ran=Math.floor(Math.random() * ppl_array_length);
                                                                                        random_accountant=pplarray[1];
                                                                                        get_name_accountant=random_accountant;
                                                                                         console.log('random guy'+random_accountant);

                                                                });


                                var ref1 = admin.database().ref("users").once("value", function (snapshot) {
                                  snapshot.forEach(function (data) {
                                       //   console.log('get guy name'+data.val().name+' -----'+random_accountant);

                                    if(data.val().name.includes(random_accountant) || data.val().name==random_accountant){
                                             random_accountant_contact=data.val().contact;
                                             get_name_accountant_contact=random_accountant_contact;
                                             console.log('random guy contact'+random_accountant_contact);
                                       }


                                 })
                            message = "I have linked you to "+get_name_accountant+" , anything else I can help you? \n\nAccountant Staff:"+get_name_accountant+"\nAccountant Staff Contact:"+get_name_accountant_contact+"";
                          return sendSlackResponse(message, res, null);

                                     });

                        //  sendRichSlackResponse
                          }else if(par_team_quote.includes('team')){
                              //par_username
                                var get_u_from_db='';
                                 db_root.child('zremember_name').once('value').then(snapshot => {
                                    par_username = snapshot.val().temp_name;
                                       console.log('grab para user='+par_username);


/*
  var ref = admin.database().ref("users").once("value", function (snapshot) {
                    snapshot.forEach(function (data) {
                        for (var i = 0; i < data.val().past_job_experience.length; i++) {}*/

                               db_root.child('users').once('value').then(snapshot => {
                                     snapshot.forEach(u_child => {
                                        if( u_child.val().name==par_username){
                                            user_department=u_child.val().department;
                                              console.log('get u department ='+user_department);
                                        }

                                    })






                                    var combine_str_ref="department/"+user_department;
                                 console.log('--combine_str_ref ='+combine_str_ref);

                                 db_root.child(combine_str_ref).once('value').then(snapshot => {



                                      snapshot.forEach(department_child => {

                                           console.log('loop department u name='+department_child.val().name);
                                                depart_pplarray.push(department_child.val().name);
                                                depart_pplarray_pos.push(department_child.val().position);


                                   })
                                 var count_a = depart_pplarray.length;
                                console.log('get  count_a ='+count_a);



                               message = "Hi "+par_username+", you are in "+convertFirstUpp(user_department)+" Department, and your team mates are:\n\n";


                               for(var i=0; i<count_a; i++){

                                      message=message+convertFirstUpp(depart_pplarray_pos[i])+" : "+convertFirstUpp(depart_pplarray[i])+"\n\n";
                               }


                                  return sendSlackResponse(message, res, null);


//convertFirstUpp();
                                     // res.send(get_name+' is the '+get_position+' of Our Company');
                                  });


                               })

    })


                          }else if(par_looklike=='like'){

                                var key='';
                                  db_root.child('users').once('value').then(snapshot => {
                                          snapshot.forEach(u_child => {

                                          var uname_small=u_child.val().name;
                                          uname_small=uname_small.toLowerCase();

                                          if(uname_small.includes(par_username) || uname_small==par_username){

                                           for (var i = 0; i < u_child.val().personality.length; i++) {
                                                  personality_array.push( u_child.val().personality[i]);
                                                                  console.log('personality '+ u_child.val().personality);
                                           }

                                          }


                                      })

                                                var len_personality =personality_array.length;
                                                var ran_p=Math.floor(Math.random() * len_personality);
                                                message = "Oh, "+par_username+" is a "+personality_array[ran_p]+" person! \nYou'll have fun working with "+par_username+"";
                                               return sendSlackResponse(message, res, null);


  })


//personality_array
//par_username


                          }else if(par_get_help!=''){

                             message = "That's my pleasure! See you again";
                             return sendSlackResponse(message, res, null);

                          }
                          else{
                             message = "What specific task I can help you?";
                              return sendSlackResponse(message, res, null);
                          }




         break;

        case "people.search.year":
         console.log('get para ');
                    var temp = [];
                    var context = null;
                    var para = parameters.duration.amount;
                    var ref = admin.database().ref("users").once("value", function(snapshot) {
                      snapshot.forEach(function(data) {
                          var date = data.val().date_joined;
                          var parts = date.split('/');
                          var convertdate = new Date(parts[2],parts[1],parts[0]-1).getTime();

                         if(new Date().getTime() - convertdate > (365 * para * 24 * 60 * 60 * 1000)){
                              temp.push(data.val().name);
                        }
                      });
                      message = "Oh great! A celebration! I got the list ready, with " + temp.length + " people(s) invited. Would you like to blast it or keep it to yourself?";

                      return sendSlackResponseWithAttachment(message, res, null, temp);
                    });


                  break;
          case "people.search.year-invitation":
                    var msg = "Hey, you have been invited to event!";
                    var myJSONObject = {
                        "type": "message",
                        "channel": "#general",
                        "text": "@everyone " + msg

                    };
                    request({
                        url: "https://slack.com/api/chat.postMessage",
                        method: "POST",
                        headers: {
                            'Authorization': 'Bearer xoxb-284155260963-IWxR1eyVNUclkyFukJ8ND2A7'
                        },
                        json: true,   // <--Very important!!!
                        body: myJSONObject
                    }, function (error, response, body) {
                        console.log("Response: " + response);
                        console.log("Body: " + body);
                        console.log("error: " + error);

                    });

                    message = 'We are currently sending your response.';

                    return sendSlackResponse(message, res, null);
                    break;

        case "people.set.meeting":
           console.log('--');

        var get_date_from=parameters.date;
         console.log('get time -- '+get_date_from);
           var promises=[];
        var meet_ppl = parameters.any;
        var meeting_timestamp='';
        if(get_date_from!=''){
              var date_get = parameters.date;
              var dateParts=[];
            dateParts = date_get.split('-');
             date_get = dateParts[1]+'/'+dateParts[2]+'/'+dateParts[0];
             console.log('get date'+date_get);
             var time_get = parameters.time;
              var timeParts=[];

               console.log('get time'+time_get);
             var date_time_get= date_get+' '+time_get;
              console.log('get datetime'+date_time_get);


//function toTimestamp(strDate)
//alert(toTimestamp('02/13/2009 23:31:30'));

              meeting_timestamp=toTimestamp(date_time_get);
              console.log('meeting_timestamp = '+date_time_get);
                 var curTime= new Date().getTime();
                            console.log('current timestamp ='+curTime);
                            console.log('meeting timestamp ='+meeting_timestamp);
                          var wait_time=meeting_timestamp-curTime;


             var d_message = 'Alright, The meeting has been set at '+date_time_get+',  you will get an email for the reminder for meeting with '+meet_ppl+' when the time reach';
             var d_promise =  sendSlackResponse(d_message,res,null);//send_msg_to_slack(d_message);//
             promises.push(d_promise);




               var curTime= new Date().getTime();
                           console.log('current timestamp ='+curTime);
                           console.log('meeting timestamp ='+meeting_timestamp);
                         var wait_time=meeting_timestamp-curTime;
                         console.log('wait time for meeting ='+wait_time);

                        // sleep_delay(wait_time);//wait
                         message='Now is the meeting time '+today_generate+' with '+meet_ppl+' ! ';

                         var next_p =sendSlackResponse(message,res,null);//send_msg_to_slack(message);
                           //var email_p= email_push();
                            promises.push(next_p);
        }else{

                     var time_get = parameters.time;

                       console.log('get time '+time_get);

                      var get_part_time=[];
                      get_part_time=time_get.split(':');
                      var get_h=get_part_time[0];
                      var get_m=get_part_time[1];
                      var get_s=get_part_time[2];

                      var get_time_timestamp = (get_s+(get_m*60)+(get_h*60*60))*1000;//milli s
//function toTimestamp(strDate)
//alert(toTimestamp('02/13/2009 23:31:30'));

// var dateString = '17-09-2013 10:08',

                var d_sys = new Date(); // for now
                 var get_h_sys=d_sys.getHours()+8; // => 9\

                 if(get_h_sys>24){
                    get_h_sys=get_h_sys-24;
                 }
                  console.log('get_h_sys == '+get_h_sys);
                 var get_m_sys=d_sys.getMinutes(); // =>  30
                  console.log('get_m_sys == '+get_m_sys);
                 var get_s_sys=d_sys.getSeconds(); // => 51
                   console.log('get_s_sys == '+get_s_sys);

                  var get_time_timestamp_sys = (get_s_sys+(get_m_sys*60)+(get_h_sys*60*60))*1000;//milli s

                var t_message = 'Alright! The meeting has been set at '+time_get+', you will get an email for the reminder for meeting with '+meet_ppl+' when the time reach';
                                //sendSlackResponse(message, res, null);
                                 sendSlackResponse(t_message, res, null);//send_msg_to_slack(t_message);



                 console.log('meeting_timestamp == '+get_time_timestamp);
                  console.log('wait min == '+get_m-get_m_sys+' min');
                 var wait_t_send = (get_m-get_m_sys)*60*1000;
                  console.log('wait for ...... '+wait_t_send);

                 sleep_delay(wait_t_send);//wait
                 message='Now is the meeting time '+time_get+' with '+meet_ppl+' ! ';

                  return email_push();//sendSlackResponse(message, res, null);//send_msg_to_slack(message);
                   // promises.push(next_p_wait);

        }


              //meeting_timestamp  => meeting time

             //  promises.push(email_p);
             // return Promise.all(promises);

            break;

        case "date.year.check":
            message = 'darling, it\'s 2017 now.';
             return sendSlackResponse(message,res,null);
            break;

        case "news.query":
            //TODO:
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

            return sendSlackResponse(message,res,null);
            break;
        case "news.search":
            //TODO:
            var data = '';
            https.get('https://newsapi.org/v1/articles?source=' + parameters.source + '&apiKey=' + newsAPIkey,
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

            return sendSlackResponse(message,res,null);
            break;

        case "time.check":
            var time = new Date().getTime() + (8 * 60 * 60 * 1000); //GMT +8
            var curDate = new Date(time);
            var curTime = curDate.getHours().toString() + ":00:00";

            var format;
            if (curDate.getHours() > 12) {
                format = PM;
            } else {
                format = AM;
            }

            if (parameters.time == curTime) {
                message = 'yes, you are correct! the time now is ' + curDate.getHours() + format + '.';
            }
            else {
                message = 'no, the time is ' + curDate.getHours() + 'hour.';
            }
            return sendSlackResponse(message,res,null);
            break;
        case "time.get":
            var time = new Date().getTime() + (8 * 60 * 60 * 1000); //GMT +8
            var curDate = new Date(time);
            var curTime = curDate.getHours();

            if (curTime > 12) {
                curTime -= 12;
                message = 'hey! the time now is ' + curTime + 'PM';
            }
            else {
                message = 'hey good morning! the time now is ' + curTime + 'AM';
            }
            return sendSlackResponse(message,res,null);
            break;


    }





});



exports.period_mail = functions.https.onRequest((req,res)=>{

 setEmailTime(300000);
 /* var myinterval =  setInterval(function(){
   mail_out();

    },  60000);
*/
     var curTime= new Date().getTime();
       if(curTime>email_time){
            clearInterval(myinterval);
          res.send('now is '+curTime+' has already exceeds the time '+email_time+' clear interval');

       }else{
       res.send('not yet..'+curTime+' < '+email_time+' ');

       }

});


exports.wait_action = functions.https.onRequest((req,res)=>{

    sleep_delay(600000);//wait 10 min
                    var curTime= new Date().getTime();
                  return mailTransport.sendMail(mailOptions).then(()=>{
                                             email_time=null;//after fired this action, set the action_time back to null again
                                             res.send('now is '+curTime+' has already  425352086@qq.com has received email');

                                            });

});

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
exports.sendNotification = functions.database.ref("department/{title}")
.onWrite(event => {

});


// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });
