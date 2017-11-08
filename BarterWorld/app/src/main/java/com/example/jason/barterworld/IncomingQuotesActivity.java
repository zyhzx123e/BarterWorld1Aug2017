package com.example.jason.barterworld;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jason.barterworld.model.IncomingQuote;
import com.example.jason.barterworld.quotes.IncomingQuoteFragment;
import com.example.jason.barterworld.tools.RoundedTransformation;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class IncomingQuotesActivity extends AppCompatActivity {


    public String user_uid="";
    public String display_uid="";

    private FirebaseAuth user_auth;

    private FirebaseUser currentUser;
    public String msg_key;

    private RecyclerView recyclerView;
    private DatabaseReference fire_db_incoming_quote_db;
    private DatabaseReference fire_db_incoming_quote_db_chk;

    private LinearLayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_quotes);

        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_backbtn);


        user_auth = FirebaseAuth.getInstance();
        currentUser = user_auth.getCurrentUser();
        //user_uid = user_auth.getInstance().getCurrentUser().getUid();
        user_uid = currentUser.getUid();

        fire_db_incoming_quote_db= FirebaseDatabase.getInstance().getReference().child("Incoming_Quotes").child(user_uid);
        fire_db_incoming_quote_db_chk= FirebaseDatabase.getInstance().getReference().child("Incoming_Quotes");



        fire_db_incoming_quote_db.keepSynced(true);
        fire_db_incoming_quote_db_chk.keepSynced(true);


        //  private LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(IncomingQuotesActivity.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        recyclerView = (RecyclerView)findViewById(R.id.incoming_quote_recycler_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();
        fire_db_incoming_quote_db_chk.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.hasChild(user_uid)) {
                    adapter_set_view();
                }else{

                    //pop up msg to notify user no content
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(IncomingQuotesActivity.this);
                    View mView = getLayoutInflater().inflate(R.layout.dialog_alert, null);
                    final TextView alert_txt = (TextView) mView.findViewById(R.id.txt_alert_content);

                    alert_txt.setText("Sorry, You Still not receive any Barter Quotes From Any Others, Be Patient!, Try post some more Barter Items!");
                    Button btn_ok = (Button) mView.findViewById(R.id.btn_ok);

                    mBuilder.setView(mView);
                    final AlertDialog dialog = mBuilder.create();
                    dialog.show();


                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });



                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    //------------------------------------------------------view_holder class-------------------------------

    public static class IncomingQuoteViewHolder extends RecyclerView.ViewHolder{

        FirebaseAuth user_auth;

        View incoming_quote_view;


        public IncomingQuoteViewHolder(View itemView) {
            super(itemView);
            incoming_quote_view=itemView;

            user_auth = FirebaseAuth.getInstance();

        }


        public void setBarterTitle(String title){

            TextView txt_title= (TextView)incoming_quote_view.findViewById(R.id.txt_post_name);
            txt_title.setText(title);
        }
        public void setSenderName(String name){

            TextView txt_user_name= (TextView)incoming_quote_view.findViewById(R.id.txt_quote_user_name);
            txt_user_name.setText(name);
        }



        public void setDescription(String desc){

            TextView txt_desc= (TextView)incoming_quote_view.findViewById(R.id.txt_comment);
            txt_desc.setText(desc);

        }
        public void setType(String type){

            TextView txt_type= (TextView)incoming_quote_view.findViewById(R.id.txt_post_type);
            txt_type.setText(type);

        }
        public void setVal(String value){

            TextView txt_value= (TextView)incoming_quote_view.findViewById(R.id.txt_post_value);
            txt_value.setText("$value "+value);

        }




        public void setImg(final Context ctx, final String img){

            final ImageView post_img = (ImageView)incoming_quote_view.findViewById(R.id.img_post);
            //Picasso.with(ctx).load(img).into(post_img);

            Picasso.with(ctx).load(img).fit()
                    .placeholder(R.drawable.ic_media_play)
                    .centerCrop()
                    .transform(new RoundedTransformation(10, 4))
                    .into(post_img, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                            Picasso.with(ctx).load(img).fit()
                                    .placeholder(R.drawable.ic_media_play).centerCrop()
                                    .transform(new RoundedTransformation(10, 4))
                                    .into(post_img);
                        }
                    });



        }

        public void setUserImg(final Context ctx, final String img){

            final ImageView post_img = (ImageView)incoming_quote_view.findViewById(R.id.user_img);
            //Picasso.with(ctx).load(img).into(post_img);

            Picasso.with(ctx).load(img).fit()
                    .placeholder(R.drawable.ic_media_play)
                    .centerCrop()
                    .transform(new RoundedTransformation(80, 4))
                    .into(post_img, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                            Picasso.with(ctx).load(img).fit()
                                    .placeholder(R.drawable.ic_media_play).centerCrop()
                                    .transform(new RoundedTransformation(80, 4))
                                    .into(post_img);
                        }
                    });



        }







    }


    //-----------------------------------------------------view holder class--------------------------------


    void adapter_set_view(){

        FirebaseRecyclerAdapter<IncomingQuote,IncomingQuoteViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<IncomingQuote,IncomingQuoteViewHolder>(
                IncomingQuote.class,
                R.layout.incoming_quote_row,
                IncomingQuoteViewHolder.class,
                fire_db_incoming_quote_db //change to   query_current_user_post

        ) {
            @Override
            protected void populateViewHolder(IncomingQuoteViewHolder viewHolder, IncomingQuote model, int position) {

                final String incoming_quote_key = getRef(position).getKey();//sender uid

             final String   msg_key_ = model.getIncoming_msg();

                viewHolder.setBarterTitle(model.getTitle());
                viewHolder.setDescription(model.getDescription());
                //  Toast.makeText(MainActivity.this,model.getDescription()+"--"+model.getTitle()+ "test descrip",Toast.LENGTH_SHORT);
                viewHolder.setType(model.getType());
                viewHolder.setVal(model.getValue());
                viewHolder.setSenderName(model.getSender_name());


                viewHolder.setImg(IncomingQuotesActivity.this,model.getBarter_img());
                viewHolder.setUserImg(IncomingQuotesActivity.this,model.getSender_img());


                viewHolder.incoming_quote_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Toast.makeText(MainActivity.this,"This is the "+model.getTitle()+" - "+post_key+"",Toast.LENGTH_LONG).show();
                        //click on each barter item

                        Intent Chat_Intent = new Intent(IncomingQuotesActivity.this, com.example.jason.barterworld.ChatActivity.class);

                        Chat_Intent.putExtra("msg_key",msg_key_);

                        startActivity(Chat_Intent);



                    }
                });





            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);


    }
}
