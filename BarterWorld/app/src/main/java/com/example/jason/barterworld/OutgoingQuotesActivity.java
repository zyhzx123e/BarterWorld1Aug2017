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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jason.barterworld.model.OutgoingQuote;
import com.example.jason.barterworld.quotes.OutgoingQuoteFragment;
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

public class OutgoingQuotesActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private DatabaseReference fire_db_outgoing_quote_db;
    private DatabaseReference fire_db_outgoing_quote_db_chk;

    private DatabaseReference Barter_Post_Ref;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public String likesNum;

    public String user_uid="";
    public String display_uid="";

    private FirebaseAuth user_auth;

    private FirebaseUser currentUser;
    public String msg_key;

    // private OnFragmentInteractionListener mListener;

    private Context c;

    private LinearLayoutManager manager;
    private LinearLayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outgoing_quotes);

        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_backbtn);



        //  private LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(OutgoingQuotesActivity.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        recyclerView = (RecyclerView)findViewById(R.id.outgoing_quote_recycler_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);



        user_auth = FirebaseAuth.getInstance();
        currentUser = user_auth.getCurrentUser();
        //user_uid = user_auth.getInstance().getCurrentUser().getUid();
        user_uid = currentUser.getUid();

        fire_db_outgoing_quote_db= FirebaseDatabase.getInstance().getReference().child("Outgoing_Quotes").child(user_uid);
        fire_db_outgoing_quote_db_chk= FirebaseDatabase.getInstance().getReference().child("Outgoing_Quotes");

        fire_db_outgoing_quote_db_chk.keepSynced(true);
        fire_db_outgoing_quote_db.keepSynced(true);
        Barter_Post_Ref= FirebaseDatabase.getInstance().getReference().child("Barter_Posts");




        Barter_Post_Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                likesNum=dataSnapshot.child("like_count").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public String post_key;

    public void adapter_setView(){

        FirebaseRecyclerAdapter<OutgoingQuote,OutgoingQuoteViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<OutgoingQuote,OutgoingQuoteViewHolder>(
                OutgoingQuote.class,
                R.layout.outgoing_quote_row,
                OutgoingQuoteViewHolder.class,
                fire_db_outgoing_quote_db //change to   query_current_user_post

        ) {
            @Override
            protected void populateViewHolder(OutgoingQuoteViewHolder viewHolder, OutgoingQuote model, int position) {

                post_key = getRef(position).getKey();
              final DatabaseReference dbRef=  Barter_Post_Ref.child(post_key);
              //  Toast.makeText(c, "+testGetKey+"+post_key, Toast.LENGTH_SHORT).show();
                dbRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        likesNum = dataSnapshot.child("like_count").getValue(String.class);
                       // Toast.makeText(c, "++testLikesNum"+likesNum, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                final String msg_key_ = model.getOutgoing_msg();

                viewHolder.setBarterTitle(model.getTitle());
                viewHolder.setDescription(model.getDescription());
                //  Toast.makeText(MainActivity.this,model.getDescription()+"--"+model.getTitle()+ "test descrip",Toast.LENGTH_SHORT);
                viewHolder.setType(model.getType());
                viewHolder.setVal(model.getValue());
                viewHolder.setUser(model.getUsername());

                //viewHolder.setLikeCount(likesNum);
                viewHolder.setImg(OutgoingQuotesActivity.this,model.getBarter_img());



                viewHolder.outgoing_quote_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Toast.makeText(MainActivity.this,"This is the "+model.getTitle()+" - "+post_key+"",Toast.LENGTH_LONG).show();
                        //click on each barter item

                        Intent Chat_Intent = new Intent(OutgoingQuotesActivity.this, com.example.jason.barterworld.ChatActivity.class);

                        Chat_Intent.putExtra("msg_key",msg_key_);

                        startActivity(Chat_Intent);



                    }
                });





            }
        };


  /*      firebaseRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);

                int friendlyMessageCount = firebaseRecyclerAdapter.getItemCount();
                int lastVisiblePosition =
                        firebaseRecyclerAdapter.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    firebaseRecyclerAdapter.scrollToPosition(positionStart);
                }
            }
        });
*/
        recyclerView.setAdapter(firebaseRecyclerAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();

        fire_db_outgoing_quote_db_chk.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.hasChild(user_uid)) {
                    adapter_setView();
                }else{

                    //pop up msg to notify user no content
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(OutgoingQuotesActivity.this);
                    View mView = getLayoutInflater().inflate(R.layout.dialog_alert, null);
                    final TextView alert_txt = (TextView) mView.findViewById(R.id.txt_alert_content);

                    alert_txt.setText("Sorry, You Did not send any one quotes yet, try to quote anyone on Barter World Platform!");
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

    public static class OutgoingQuoteViewHolder extends RecyclerView.ViewHolder{

        FirebaseAuth user_auth;

        View outgoing_quote_view;


        public OutgoingQuoteViewHolder(View itemView) {
            super(itemView);
            outgoing_quote_view=itemView;

            user_auth = FirebaseAuth.getInstance();

        }


        public void setBarterTitle(String title){

            TextView txt_title= (TextView)outgoing_quote_view.findViewById(R.id.txt_post_name);
            txt_title.setText(title);
        }




        public void setDescription(String desc){

            TextView txt_desc= (TextView)outgoing_quote_view.findViewById(R.id.txt_comment);
            txt_desc.setText(desc);

        }
        public void setType(String type){

            TextView txt_type= (TextView)outgoing_quote_view.findViewById(R.id.txt_post_type);
            txt_type.setText(type);

        }
        public void setVal(String value){

            TextView txt_value= (TextView)outgoing_quote_view.findViewById(R.id.txt_post_value);
            txt_value.setText("$value "+value);

        }
        public void setUser(String user){

            TextView txt_user= (TextView)outgoing_quote_view.findViewById(R.id.txt_post_user);
            txt_user.setText("Owner : "+user);

        }

        public void setLikeCount(String count){

            TextView txt_likeCunt= (TextView)outgoing_quote_view.findViewById(R.id.txt_likes_num);
            txt_likeCunt.setText("Liked by "+count+" people");

        }

        public void setImg(final Context ctx, final String img){

            final ImageView post_img = (ImageView)outgoing_quote_view.findViewById(R.id.img_post);
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








    }


    //-----------------------------------------------------view holder class--------------------------------


}
