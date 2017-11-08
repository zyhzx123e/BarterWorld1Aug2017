package com.example.jason.barterworld;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jason.barterworld.model.Post;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.api.GoogleApiClient;
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

public class SelfAdminActivity extends AppCompatActivity {




    private RecyclerView recyclerView;

    private CardView cardView;

    private DatabaseReference fire_db_post_db;

    private GoogleApiClient mGoogleApiClient;


    private FirebaseAuth user_auth;
    private FirebaseAuth.AuthStateListener user_auth_listnener;

    //  private DatabaseReference fire_db;

    private DatabaseReference fire_db_user;
    private DatabaseReference fire_db_like;
    private DatabaseReference fire_db_like_child;


    private boolean barter_like_status = false;


    private DatabaseReference current_user_post_display;
    private Query query_current_user_post;

    private DatabaseReference filter_post_display;
    private Query filter_post_query;

    private Query filter_post_queryServices;
    private Query filter_post_queryGoods;



    private LinearLayoutManager mLayoutManager;


    public int count_login=0;

    public   int likeCount = 0 ;
    private static final String TAG = "MainActivity";

    private FirebaseUser currentUser;

    public String user_uid="";
    public String display_uid="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_admin);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_backbtn);



        user_auth = FirebaseAuth.getInstance();
        currentUser = user_auth.getCurrentUser();
        //user_uid = user_auth.getInstance().getCurrentUser().getUid();
        user_uid = currentUser.getUid();
        //addAuthListener();
        fire_db_post_db= FirebaseDatabase.getInstance().getReference().child("Barter_Posts");
        fire_db_user = FirebaseDatabase.getInstance().getReference().child("User");

        filter_post_display= FirebaseDatabase.getInstance().getReference().child("Barter_Posts");
        fire_db_user.keepSynced(true);

        fire_db_post_db.keepSynced(true);

        filter_post_display.keepSynced(true);



        mLayoutManager = new LinearLayoutManager(SelfAdminActivity.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        // Now set the layout manager and the adapter to the RecyclerView



        recyclerView = (RecyclerView)findViewById(R.id.post_recycler_list_admin);
        recyclerView.setHasFixedSize(true);
        // recyclerView.setLayoutManager(new LinearLayoutManager(this).setReverseLayout(true));
        recyclerView.setLayoutManager(mLayoutManager);




    }

    @Override
    protected void onStart() {
        super.onStart();





        filter_post_query= filter_post_display.orderByChild("uid")
                .equalTo(user_uid);

        filter_post_query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {
                        /* for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            Toast.makeText(getApplicationContext(), "id = " + snapshot.getKey(), Toast.LENGTH_LONG).show();
                        } */

                    load_page(filter_post_query);
                }
                else {

                    //pop up msg to notify user no content
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(SelfAdminActivity.this);
                    View mView = getLayoutInflater().inflate(R.layout.dialog_alert, null);
                    final TextView alert_txt = (TextView) mView.findViewById(R.id.txt_alert_content);

                    alert_txt.setText("Sorry, You Still not yet post any Barter Item ,You may try to post some Barter Goods/Services then come here again! See you soon!");
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
                throw databaseError.toException();
            }
        });

    }

    public void load_page(Query db){
        //addAuthListener();
        FirebaseRecyclerAdapter<Post,SelfAdminActivity.PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post, SelfAdminActivity.PostViewHolder>(
                Post.class,
                R.layout.barter_self_admin_row,
                SelfAdminActivity.PostViewHolder.class,
                db //change to   query_current_user_post  post_recycler_list_admin

        ) {
            @Override
            protected void populateViewHolder(SelfAdminActivity.PostViewHolder viewHolder, Post model, int position) {

                final String post_key = getRef(position).getKey();

                viewHolder.setBarterTitle(model.getTitle());
                viewHolder.setDescription(model.getDescription());
                //  Toast.makeText(MainActivity.this,model.getDescription()+"--"+model.getTitle()+ "test descrip",Toast.LENGTH_SHORT);

                viewHolder.setImg(SelfAdminActivity.this,model.getBarter_img());


//txt_post_name_admin
                //txt_desc_admin
                viewHolder.txt_desc_admin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Toast.makeText(MainActivity.this,"This is the "+model.getTitle()+" - "+post_key+"",Toast.LENGTH_LONG).show();
                        //click on each barter item
                        Intent intent = new Intent(SelfAdminActivity.this, com.example.jason.barterworld.SelfAdminEditActivity.class);

                        intent.putExtra("barter_item_id",post_key);

                        startActivity(intent);


                    }
                });


                viewHolder.txt_post_name_admin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Toast.makeText(MainActivity.this,"This is the "+model.getTitle()+" - "+post_key+"",Toast.LENGTH_LONG).show();
                        //click on each barter item
                        Intent intent = new Intent(SelfAdminActivity.this, com.example.jason.barterworld.SelfAdminEditActivity.class);

                        intent.putExtra("barter_item_id",post_key);

                        startActivity(intent);


                    }
                });

                viewHolder.post_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Toast.makeText(MainActivity.this,"This is the "+model.getTitle()+" - "+post_key+"",Toast.LENGTH_LONG).show();
                        //click on each barter item
                        Intent intent = new Intent(SelfAdminActivity.this, com.example.jason.barterworld.SelfAdminEditActivity.class);

                        intent.putExtra("barter_item_id",post_key);

                        startActivity(intent);


                    }
                });





            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }






    public static class PostViewHolder extends RecyclerView.ViewHolder{


        DatabaseReference db_ref_like;
        FirebaseAuth user_auth;

        View post_view;

        TextView txt_post_name_admin;
        TextView txt_desc_admin;



        public PostViewHolder(View itemView) {
            super(itemView);
            post_view=itemView;


            user_auth = FirebaseAuth.getInstance();
            txt_post_name_admin =(TextView)post_view.findViewById(R.id.txt_post_name_admin);
            txt_desc_admin =(TextView)post_view.findViewById(R.id.txt_desc_admin);






        }


        public void setBarterTitle(String title){

            TextView txt_title= (TextView)post_view.findViewById(R.id.txt_post_name_admin);

            txt_title.setText(title);

        }




        public void setDescription(String desc){

            TextView txt_desc= (TextView)post_view.findViewById(R.id.txt_desc_admin);
            txt_desc.setText(desc);


        }

        public void setImg(final Context ctx, final String img){

            final ImageView post_img = (ImageView)post_view.findViewById(R.id.img_post_admin);
            //Picasso.with(ctx).load(img).into(post_img);

            Picasso.with(ctx).load(img).fit()
                    .placeholder(R.drawable.ic_media_play)
                    .centerCrop()
                    //.transform(new RoundedTransformation(50, 4))
                    .into(post_img, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                            Picasso.with(ctx).load(img).fit()
                                    .placeholder(R.drawable.ic_media_play).centerCrop()
                                    //.transform(new RoundedTransformation(50, 4))
                                    .into(post_img);
                        }
                    });


        }

    }





}
