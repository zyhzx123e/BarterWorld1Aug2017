package com.example.jason.barterworld;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout.LayoutParams;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jason.barterworld.model.Message;
import com.example.jason.barterworld.tools.RoundedTransformation;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity {



    private RecyclerView chat_recyclerView;

    public String post_key = null; //key
    public String post_barter_img ;//1
    public String post_description ="";//2
    public String post_latitude ="";//3

    public String post_like_count ="";//4

    public String post_longitude="";//5
    public String post_time="";//6
    public String post_title="";//7
    public String post_type ="";//8
    public String post_user_uid ="";//9
    public String post_owner="";//10


    public String post_value="";//11
    public String user_img;//12 => use to set the img of chat view of user



    public TextView textView_user_name;
    public TextView textView_barter_title;
    public EditText editText_msg_send;


    public ImageView user_profile_img ;
    public ImageView barter_img ;
    private ImageButton btn_send;

    private String current_msg_key;
    private String new_msg_key;


    //firebase storeage reference

    private DatabaseReference msg_db_ref;//new msg db
    private DatabaseReference outgoing_quotes_db_ref;//new msg db
    private DatabaseReference incoming_quotes_db_ref;//new msg db
    private DatabaseReference chat_db_post_db;

    public String user_uid="";
    public String display_uid="";

    private FirebaseAuth user_auth;

    private FirebaseUser currentUser;

    private DatabaseReference databaseReference_user;
    private DatabaseReference databaseReference_display_user;
    private ProgressDialog progress_dialog;



    public String uid1="";
    public String uid2="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_backbtn);

        user_auth = FirebaseAuth.getInstance();
        currentUser = user_auth.getCurrentUser();
        //user_uid = user_auth.getInstance().getCurrentUser().getUid();
        user_uid = currentUser.getUid();


        databaseReference_user = FirebaseDatabase.getInstance().getReference().child("User").child(currentUser.getUid());


        outgoing_quotes_db_ref=FirebaseDatabase.getInstance().getReference().child("Outgoing_Quotes");
        incoming_quotes_db_ref=FirebaseDatabase.getInstance().getReference().child("Incoming_Quotes");

        textView_user_name = (TextView)findViewById(R.id.user_name);
        user_profile_img = (ImageView)findViewById(R.id.user_img);

        textView_barter_title = (TextView)findViewById(R.id.txt_post_name);
        barter_img = (ImageView)findViewById(R.id.img_post);
        btn_send=(ImageButton)findViewById(R.id.btn_send);
        editText_msg_send=(EditText)findViewById(R.id.txt_msg_send);

/*
*               quoteIntent.putExtra("barter_item_id",post_key);//key
                quoteIntent.putExtra("barter_img",post_barter_img);//1
                quoteIntent.putExtra("barter_description",post_description);//2
                quoteIntent.putExtra("barter_latitude",post_latitude);//3
                quoteIntent.putExtra("barter_like_count",post_like_count);//4
                quoteIntent.putExtra("barter_longitude",post_longitude);//5
                quoteIntent.putExtra("barter_time",post_time);//6
                quoteIntent.putExtra("barter_title",post_title);//7
                quoteIntent.putExtra("barter_type",post_type);//8
                quoteIntent.putExtra("barter_owner_uid",post_user_uid);//9
                quoteIntent.putExtra("barter_owner_name",post_owner);//10
                quoteIntent.putExtra("barter_value",post_value);//11
                   quoteIntent.putExtra("user_img",user_img);//12
* */
        Bundle extras = getIntent().getExtras();

        if(extras!=null){

              current_msg_key = getIntent().getExtras().getString("msg_key");
          //  Toast.makeText(this, current_msg_key, Toast.LENGTH_SHORT).show();

           final  DatabaseReference root_msg= FirebaseDatabase.getInstance().getReference().child("Message").child(current_msg_key);

            root_msg.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


//-----------------------88check again



                for (DataSnapshot dataSnapshot_child: dataSnapshot.getChildren()) {
                        uid1= dataSnapshot_child.child("toid").getValue(String.class);
                        uid2= dataSnapshot_child.child("fromid").getValue(String.class);
                        post_barter_img= dataSnapshot_child.child("barter_img").getValue(String.class);
                        post_title= dataSnapshot_child.child("barter_name").getValue(String.class);


                    break;

                    }
                    if(user_uid.equals(uid1)){
                        display_uid = uid2;
                        post_user_uid = uid2;
                    }
                    if(user_uid.equals(uid2)){
                        display_uid = uid1;
                        post_user_uid = uid1;

                    }




                   // Toast.makeText(ChatActivity.this, "-------------display_uid___________"+display_uid, Toast.LENGTH_SHORT).show();


                    databaseReference_display_user = FirebaseDatabase.getInstance().getReference().child("User").child(display_uid);

                    databaseReference_display_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            post_owner =  dataSnapshot.child("user_name").getValue(String.class);
                            user_img =dataSnapshot.child("user_img").getValue(String.class);


                            set_barter_pic(ChatActivity.this, post_barter_img);
                            set_user_profile_pic(ChatActivity.this, user_img);
                            set_username(post_owner);
                            set_barter_title(post_title);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });











                    //post_barter_img = dataSnapshot.child("barter_img").getValue(String.class);
                   // post_title = dataSnapshot.child("barter_name").getValue(String.class);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



            editText_msg_send.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        hideKeyboard(v);
                    }
                }
            });



        }

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsg();
            }
        });


        msg_db_ref=FirebaseDatabase.getInstance().getReference().child("Message");
        chat_db_post_db=FirebaseDatabase.getInstance().getReference().child("Message").child(current_msg_key);
        chat_db_post_db.keepSynced(true);

        chat_recyclerView = (RecyclerView)findViewById(R.id.chat_recycler_list);
        chat_recyclerView.setHasFixedSize(true);
        chat_recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerAdapter<Message,ChatViewHolder> firebase_RecyclerAdapter = new FirebaseRecyclerAdapter<Message, ChatViewHolder>(
                Message.class,
                R.layout.chat_bubble,
                ChatViewHolder.class,
                chat_db_post_db ) {
            @Override
            protected void populateViewHolder(ChatViewHolder viewHolder, Message model, int position) {

                final String chat_msg_key = getRef(position).getKey();


                //String msg_from_id = getRef(position).child("fromid").toString();
                String sender_id = model.getFromid();
              //  Toast.makeText(ChatActivity.this,model.getFromid()+"--"+model.getToid()+ "test descrip",Toast.LENGTH_SHORT);



                if(user_uid.equals(sender_id)) {
                    viewHolder.set_chat_txt(model.getMsg_content(), "right");
                }else{
                    viewHolder.set_chat_txt(model.getMsg_content(), "left");
                }
                //  Toast.makeText(MainActivity.this,model.getDescription()+"--"+model.getTitle()+ "test descrip",Toast.LENGTH_SHORT);



            }
        };

        chat_recyclerView.setAdapter(firebase_RecyclerAdapter);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(ChatActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    protected void hideSoftKeyboard(EditText input) {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
    }


    //create recycler view holder class
    //------------------------------view holder class-----------------------------


    public static class ChatViewHolder extends RecyclerView.ViewHolder{


        FirebaseAuth user_auth;

        View chat_view;



        ImageButton likeBtn_;



        public ChatViewHolder(View itemView) {
            super(itemView);
            chat_view=itemView;

            user_auth = FirebaseAuth.getInstance();



        }








        public void set_chat_txt(String txt,String leftORright){

            LinearLayout chat_linear_layout_parent= (LinearLayout) chat_view.findViewById(R.id.bubble_layout_parent);
            LinearLayout chat_linear_layout= (LinearLayout) chat_view.findViewById(R.id.bubble_layout);

            TextView txt_msg= (TextView)chat_view.findViewById(R.id.message_text);

            if(leftORright.equals("right")) {
                txt_msg.setGravity(Gravity.RIGHT);
                chat_linear_layout.setBackgroundResource(R.drawable.right_msg);

               LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                params.setMargins(250, 0, 0, 0);
                chat_linear_layout_parent.setLayoutParams(params);

//bubble_layout_parent
                txt_msg.setText(txt);
            }else if(leftORright.equals("left")){
                txt_msg.setGravity(Gravity.LEFT);
                chat_linear_layout.setBackgroundResource(R.drawable.left_msg);

                txt_msg.setText(txt);
            }

        }









    }
    //-----------------------------view holder class------------------------------

    public void sendMsg(){

/*
*     private String fromid;//1
    private String fromname;//2
    private String from_img;//3

    private String toid;//4
    private String toname;//5
    private String to_img;//6

    private String barter_id;//7
    private String barter_name;//8
    private String barter_img;//9

    private String msg_content;//10
* */


        final String to_id = post_user_uid;
        final String to_name = post_owner;
         final String to_img = user_img;


        final String barter_id = post_key;
        final String barter_name=post_title;
        final String barter_img=post_barter_img;

        final String msg_content=editText_msg_send.getText().toString().trim();



        if(!TextUtils.isEmpty(msg_content)  ){

            String uuid = UUID.randomUUID().toString();

            //current_msg_key=msg_db_ref.push().getKey();
            new_msg_key = msg_db_ref.child(current_msg_key).push().getKey();
            final DatabaseReference new_msg=msg_db_ref.child(current_msg_key).child(new_msg_key);//push to the node so to create the random id


                    databaseReference_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Calendar c = Calendar.getInstance();
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
                            String strDate = sdf.format(c.getTime());

                            user_uid = currentUser.getUid();

                            new_msg.child("barter_id").setValue(barter_id);
                            new_msg.child("barter_name").setValue(barter_name);
                            new_msg.child("barter_img").setValue(barter_img);

                            new_msg.child("toid").setValue(to_id);
                            new_msg.child("toname").setValue(to_name);
                            new_msg.child("to_img").setValue(to_img);


                            new_msg.child("msg_content").setValue(msg_content);


                            new_msg.child("fromid").setValue(user_uid);
                            new_msg.child("fromname").setValue(dataSnapshot.child("user_name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {


                                }
                            });
                            new_msg.child("from_img").setValue(dataSnapshot.child("user_img").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {


                                }
                            });




                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


        }

        hideSoftKeyboard(this.editText_msg_send);
        editText_msg_send.setText("");
    }






    public void set_username( String username_){

        textView_user_name.setText(username_);

    }
    public void set_barter_title( String barter_title_){

        textView_barter_title.setText(barter_title_);

    }
    public void set_barter_pic(final Context ctx, final String img){

        final ImageView barter_img = (ImageView)findViewById(R.id.img_post);
        //Picasso.with(ctx).load(img).into(post_img);

        Picasso.with(ctx).load(img).fit()
                .placeholder(R.drawable.ic_media_play)
                .transform(new RoundedTransformation(10, 4))
                .into(barter_img, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                        Picasso.with(ctx).load(img).fit().placeholder(R.drawable.ic_media_play)
                                .into(barter_img);
                    }
                });


    }

    public void set_user_profile_pic(final Context ctx, final String img){

        final ImageView user_profile_img = (ImageView)findViewById(R.id.user_img);
        //Picasso.with(ctx).load(img).into(post_img);

        Picasso.with(ctx).load(img).fit()
                .placeholder(R.drawable.ic_media_play)
                .transform(new RoundedTransformation(80, 4))
                .into(user_profile_img, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                        Picasso.with(ctx).load(img).fit().placeholder(R.drawable.ic_media_play)
                                .into(user_profile_img);
                    }
                });


    }


}
