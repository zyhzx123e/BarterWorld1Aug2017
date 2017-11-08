package com.example.jason.barterworld;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jason.barterworld.model.Comment;
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
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BarterSingleActivity extends AppCompatActivity {


    public String post_key = null;
    public String current_comment_key;
    private DatabaseReference databaseReference_barter_item;

    private FirebaseAuth auth_user;

    private DatabaseReference databaseReference_user;

    //layout components:
    private ImageView barter_img_view;
    private TextView barter_title;
    private TextView barter_description;
    private TextView barter_owner_name;
    private TextView barter_post_time;
    private TextView barter_post_type;
    private TextView barter_post_value;


    private Button btn_delete;
    private Button btn_quote;
    private Button btn_comment;

    private static final String TAG = "BarterSingleActivity";





    public String post_barter_img ="";//1
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

    public String user_img="";//12 => use to set the img of chat view of user


    private DatabaseReference msg_db_ref;//new msg db
    private DatabaseReference outgoing_quotes_db_ref;//new msg db
    private DatabaseReference incoming_quotes_db_ref;//new msg db
    private DatabaseReference chat_db_post_db;

    private DatabaseReference  comment_db_ref;
    private DatabaseReference barter_comment_db_ref;

    public String current_msg_key;
    public String new_msg_key;


    public String user_uid="";

    private FirebaseAuth user_auth;

    private FirebaseUser currentUser;

    private DatabaseReference databaseReference_user_chat;

    public String current_user_name="",current_user_img_url="";
    private RecyclerView comment_recyclerView;

    public Boolean flag1,flag2,flag3= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barter_single);
      //  getActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_backbtn);


        comment_recyclerView = (RecyclerView)findViewById(R.id.comment_recycler_list);
        comment_recyclerView.setHasFixedSize(true);
        comment_recyclerView.setLayoutManager(new LinearLayoutManager(this));





        user_auth = FirebaseAuth.getInstance();
        currentUser = user_auth.getCurrentUser();
        //user_uid = user_auth.getInstance().getCurrentUser().getUid();
        user_uid = currentUser.getUid();


        databaseReference_user_chat = FirebaseDatabase.getInstance().getReference().child("User").child(currentUser.getUid());

        msg_db_ref=FirebaseDatabase.getInstance().getReference().child("Message");
        chat_db_post_db=FirebaseDatabase.getInstance().getReference().child("Message");
        outgoing_quotes_db_ref=FirebaseDatabase.getInstance().getReference().child("Outgoing_Quotes");
        incoming_quotes_db_ref=FirebaseDatabase.getInstance().getReference().child("Incoming_Quotes");

        post_key = getIntent().getExtras().getString("barter_item_id");


        msg_db_ref.keepSynced(true);
        outgoing_quotes_db_ref.keepSynced(true);
        incoming_quotes_db_ref.keepSynced(true);
        chat_db_post_db.keepSynced(true);

        comment_db_ref= FirebaseDatabase.getInstance().getReference().child("Comments").child(post_key);
        btn_delete = (Button)findViewById(R.id.btn_delete);
        btn_quote = (Button)findViewById(R.id.btn_quote);
        btn_comment= (Button)findViewById(R.id.btn_comment);


        barter_img_view = (ImageView)findViewById(R.id.barter_img);
        barter_title = (TextView)findViewById(R.id.barter_title);
        barter_description = (TextView)findViewById(R.id.barter_description);
        barter_owner_name = (TextView)findViewById(R.id.barter_owner);
        barter_post_time = (TextView)findViewById(R.id.barter_post_time);
        barter_post_type = (TextView)findViewById(R.id.barter_type);
        barter_post_value = (TextView)findViewById(R.id.barter_value);




        databaseReference_user= FirebaseDatabase.getInstance().getReference().child("User");

        auth_user = FirebaseAuth.getInstance();
        databaseReference_barter_item = FirebaseDatabase.getInstance().getReference().child("Barter_Posts");

        databaseReference_barter_item.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                 post_title =  dataSnapshot.child("title").getValue(String.class);
                 post_description =   dataSnapshot.child("description").getValue(String.class);
                 post_owner=   dataSnapshot.child("username").getValue(String.class);
                 post_time =   dataSnapshot.child("time").getValue(String.class);
                 post_type =  dataSnapshot.child("type").getValue(String.class);
                 post_value =  dataSnapshot.child("value").getValue(String.class);

                post_latitude =  dataSnapshot.child("latitude").getValue(String.class);
                post_longitude=  dataSnapshot.child("longitude").getValue(String.class);
               post_like_count =  dataSnapshot.child("like_count").getValue(String.class);



                 post_user_uid =  dataSnapshot.child("uid").getValue(String.class);
                 post_barter_img =  dataSnapshot.child("barter_img").getValue(String.class);



                barter_title.setText(post_title);
                barter_description.setText(post_description);
                barter_owner_name.setText(post_owner);
                barter_post_time.setText(post_time);
                barter_post_type.setText(post_type);
                barter_post_value.setText(post_value);

                Picasso.with(BarterSingleActivity.this).load(post_barter_img).fit().placeholder(R.drawable.ic_media_play)
                        .into(barter_img_view);



                if(auth_user.getCurrentUser().getUid().equals(post_user_uid)){

                    btn_delete.setVisibility(View.VISIBLE);
                    btn_quote.setVisibility(View.INVISIBLE);
                }else{
                    btn_quote.setVisibility(View.VISIBLE);
                    btn_delete.setVisibility(View.INVISIBLE);
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference_user.child(post_user_uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                user_img = dataSnapshot.child("user_img").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Toast.makeText(BarterSingleActivity.this,post_key,Toast.LENGTH_SHORT).show();







        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference_barter_item.child(post_key).removeValue();

                Intent mainIntent = new Intent(BarterSingleActivity.this,MainActivity.class);

                startActivity(mainIntent);
            }
        });




        btn_quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*
*
* public String post_barter_img ="";//1
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
     public String user_img="";//12 => use to set the img of chat view of user
*
* */

                current_msg_key=msg_db_ref.push().getKey();
                new_msg_key = msg_db_ref.child(current_msg_key).push().getKey();
                final DatabaseReference root_msg=msg_db_ref.child(current_msg_key);//push to the node so to create the random id

                final DatabaseReference new_msg=msg_db_ref.child(current_msg_key).child(new_msg_key);//push to the node so to create the random id


                databaseReference_user_chat.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String strDate = sdf.format(c.getTime());

                        user_uid = currentUser.getUid();

//root msg store user info and barter info
              //       root_msg.child("barter_id").setValue(post_key);
              //         root_msg.child("barter_name").setValue(post_title);
              //      root_msg.child("barter_img").setValue(post_barter_img);

              //        root_msg.child("uid1").setValue(post_user_uid);

              //       root_msg.child("uid2").setValue(user_uid);


//new msg store msg
                        new_msg.child("barter_id").setValue(post_key);
                        new_msg.child("barter_name").setValue(post_title);
                        new_msg.child("barter_img").setValue(post_barter_img);

                        new_msg.child("toid").setValue(post_user_uid);
                        new_msg.child("toname").setValue(post_owner);
                        new_msg.child("to_img").setValue(user_img);


                        new_msg.child("msg_content").setValue("Hi");


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

//save to outgoing quotes to sender:

                final DatabaseReference new_outgoing_quotes=outgoing_quotes_db_ref.child(user_uid);//push to the node so to create the random id

                final String new_outgoing_key = new_outgoing_quotes.push().getKey();
                final DatabaseReference new_outgoing_quotes_child = new_outgoing_quotes.child(new_outgoing_key);


                databaseReference_user_chat.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String strDate = sdf.format(c.getTime());

                        user_uid = currentUser.getUid();

                        new_outgoing_quotes_child.child("uid").setValue(post_user_uid);
                        new_outgoing_quotes_child.child("barter_id").setValue(post_key);
                        new_outgoing_quotes_child.child("title").setValue(post_title);
                        new_outgoing_quotes_child.child("description").setValue(post_description);

                        new_outgoing_quotes_child.child("barter_img").setValue(post_barter_img);
                        new_outgoing_quotes_child.child("type").setValue(post_type);
                        new_outgoing_quotes_child.child("value").setValue(post_value);

                        new_outgoing_quotes_child.child("username").setValue(post_owner);
                        new_outgoing_quotes_child.child("time").setValue(post_time);
                        new_outgoing_quotes_child.child("like_count").setValue(post_like_count);
                        new_outgoing_quotes_child.child("outgoing_msg").setValue(current_msg_key);







                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




                //save to incoming quotes to receiver

                final DatabaseReference new_incoming_quotes=incoming_quotes_db_ref.child(post_user_uid);//push to the node so to create the random id

                final String new_incoming_key = new_incoming_quotes.push().getKey();

                final DatabaseReference new_incoming_quotes_child = new_incoming_quotes.child(new_incoming_key);


                databaseReference_user_chat.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
                        String strDate = sdf.format(c.getTime());

                        user_uid = currentUser.getUid();


                        new_incoming_quotes_child.child("sender_uid").setValue(user_uid);
                        new_incoming_quotes_child.child("sender_name").setValue(dataSnapshot.child("user_name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {


                            }
                        });
                        new_incoming_quotes_child.child("sender_img").setValue(dataSnapshot.child("user_img").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {


                            }
                        });

                        new_incoming_quotes_child.child("uid").setValue(post_user_uid);
                        new_incoming_quotes_child.child("barter_id").setValue(post_key);
                        new_incoming_quotes_child.child("title").setValue(post_title);

                        new_incoming_quotes_child.child("description").setValue(post_description);
                        new_incoming_quotes_child.child("barter_img").setValue(post_barter_img);
                        new_incoming_quotes_child.child("type").setValue(post_type);

                        new_incoming_quotes_child.child("value").setValue(post_value);
                        new_incoming_quotes_child.child("username").setValue(post_owner);
                        new_incoming_quotes_child.child("time").setValue(post_time);

                        new_incoming_quotes_child.child("like_count").setValue(post_like_count);
                        new_incoming_quotes_child.child("latitude").setValue(post_latitude);
                        new_incoming_quotes_child.child("longitude").setValue(post_longitude);

                        new_incoming_quotes_child.child("incoming_msg").setValue(current_msg_key);





                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




                Intent quoteIntent = new Intent(BarterSingleActivity.this,ChatActivity.class);
                quoteIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                quoteIntent.putExtra("msg_key",current_msg_key);//key
              /*  quoteIntent.putExtra("barter_img",post_barter_img);//1
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
*/

                startActivity(quoteIntent);
            }
        });












        btn_comment.setOnClickListener(new View.OnClickListener() {//open comment dialog and SAVE Comments for current barter item
            @Override
            public void onClick(View v) {


                AlertDialog.Builder mBuilder = new AlertDialog.Builder(BarterSingleActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_comment, null);
                final EditText comment_content = (EditText) mView.findViewById(R.id.txt_comment);


                Button btn_comment = (Button) mView.findViewById(R.id.btn_comment);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();


                btn_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        barter_comment_db_ref=FirebaseDatabase.getInstance().getReference().child("Comments").child(post_key);
                        barter_comment_db_ref.keepSynced(true);
                        current_comment_key=barter_comment_db_ref.push().getKey();

                        final DatabaseReference db_new_comment_ref = barter_comment_db_ref.child(current_comment_key);



                        databaseReference_user_chat.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                current_user_name=dataSnapshot.child("user_name").getValue(String.class);
                                current_user_img_url=dataSnapshot.child("user_img").getValue(String.class);

                                db_new_comment_ref.child("comment_uid").setValue(user_uid);
                                db_new_comment_ref.child("comment_user_name").setValue(current_user_name);
                                db_new_comment_ref.child("comment_user_img").setValue(current_user_img_url);
                                db_new_comment_ref.child("comment_content").setValue(comment_content.getText().toString().trim());
                                dialog.dismiss();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });




                    }
                });



            }
        });






    }


    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerAdapter<Comment,BarterSingleActivity.CommentViewHolder> firebase_RecyclerAdapter = new FirebaseRecyclerAdapter<Comment, BarterSingleActivity.CommentViewHolder>(
                Comment.class,
                R.layout.comment_row,
                BarterSingleActivity.CommentViewHolder.class,
                comment_db_ref ) {
            @Override
            protected void populateViewHolder(BarterSingleActivity.CommentViewHolder viewHolder, Comment model, int position) {

                final String comment_key = getRef(position).getKey();


                String sender_id = model.getComment_uid();



                viewHolder.setUserImg(getApplicationContext(),model.getComment_user_img() );
                viewHolder.set_comment_user_name(model.getComment_user_name());
                viewHolder.set_comment_txt(model.getComment_content());

            }
        };

        comment_recyclerView.setAdapter(firebase_RecyclerAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getParentActivityIntent() == null) {
                    Log.i(TAG, "You have forgotten to specify the parentActivityName in the AndroidManifest!");
                    onBackPressed();
                } else {
                    NavUtils.navigateUpFromSameTask(this);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }





    //create recycler view holder class
    //------------------------------view holder class-----------------------------


    public static class CommentViewHolder extends RecyclerView.ViewHolder{


        FirebaseAuth user_auth;

        View comment_view;

        ImageButton likeBtn_;



        public CommentViewHolder(View itemView) {
            super(itemView);
            comment_view=itemView;

            user_auth = FirebaseAuth.getInstance();

        }


        public void set_comment_user_name(String uname){


            TextView txt_user_name= (TextView)comment_view.findViewById(R.id.txt_post_name);

            txt_user_name.setText(uname);


        }


        public void set_comment_txt(String comments){


            TextView txt_msg= (TextView)comment_view.findViewById(R.id.txt_comment);

            txt_msg.setText(comments);


        }
//user_img

        public void setUserImg(final Context ctx, final String img){

            final ImageView user_img = (ImageView)comment_view.findViewById(R.id.user_img);
            //Picasso.with(ctx).load(img).into(post_img);

            Picasso.with(ctx).load(img).fit()
                    .placeholder(R.drawable.ic_media_play)
                    .centerCrop()
                    .transform(new RoundedTransformation(80, 4))
                    .into(user_img, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                            Picasso.with(ctx).load(img).fit()
                                    .placeholder(R.drawable.ic_media_play).centerCrop()
                                    .transform(new RoundedTransformation(80, 4))
                                    .into(user_img);
                        }
                    });

        }







    }
    //-----------------------------view holder class------------------------------



}
