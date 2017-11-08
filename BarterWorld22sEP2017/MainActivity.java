package com.example.jason.barterworld;

import android.app.FragmentManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jason.barterworld.model.Post;
import com.example.jason.barterworld.tools.RoundedTransformation;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

//import com.microsoft.windowsazure.notifications.NotificationsManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


//import com.microsoft.windowsazure.notifications.NotificationsManager;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.List;

public class MainActivity extends AppCompatActivity   implements   View.OnClickListener , NavigationView.OnNavigationItemSelectedListener {



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

    protected List<Post> mDataOrigin;
    private boolean barter_like_status = false;


    private DatabaseReference current_user_post_display;
    private Query query_current_user_post;

    private DatabaseReference filter_post_display;
    private Query filter_post_query;

    private Query filter_post_queryServices;
    private Query filter_post_queryGoods;

    public String chk_uid = "";
    public String chk_username = "";
    public String chk_user_email = "";
    public String chk_user_profile = "";


    public EditText txt_search;
    public ImageView btn_search;
    public Button btn_filter_service;
    public Button btn_filter_goods;

    private LinearLayoutManager mLayoutManager;
    public TextView user_name ;
    public TextView user_email ;
    public TextView textView_likeNum_;
    public ImageView user_profile_img ;
    public NavigationView navigationView;
    public View header;

    public  int count_login=0;

    public   int likeCount = 0 ;
    private static final String TAG = "MainActivity";
    public  NotificationManager notificationManager=null;

    private NotificationManager getNotificationManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


         Log.d(TAG, "firebase token: jason: -- : "+FirebaseInstanceId.getInstance().getToken());


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        txt_search = (EditText)findViewById(R.id.txt_barter);
        btn_search = (ImageView)findViewById(R.id.btn_barter_search);
        btn_filter_service = (Button)findViewById(R.id.btn_services);
        btn_filter_goods = (Button)findViewById(R.id.btn_goods);

         navigationView= (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

         header= navigationView.getHeaderView(0);

        user_name = (TextView)header.findViewById(R.id.nav_header_username);

        textView_likeNum_ =  (TextView)header.findViewById(R.id.textView_likeNum);
        user_email = (TextView)header.findViewById(R.id.nav_header_user_email);
      // user_profile_img = (ImageView)header.findViewById(R.id.nav_barter_profile);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

         mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext()).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                Toast.makeText(getApplicationContext(),"Connection Failed , please try again!",Toast.LENGTH_SHORT).show();
            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();



       // fire_db = FirebaseDatabase.getInstance().getReference().child("Barter_Posts");


        /* filter post only current user's display


        String current_uid = user_auth.getCurrentUser().getUid();

        current_user_post_display = FirebaseDatabase.getInstance().getReference().child("Barter_Posts");
        query_current_user_post = current_user_post_display.orderByChild("uid").equalTo(current_uid);
        */


      //  fire_db.keepSynced(true);

        user_auth= FirebaseAuth.getInstance();

        if(user_auth.getCurrentUser()==null){
            // Toast.makeText(getApplicationContext(),"current user is null detected!",Toast.LENGTH_SHORT).show();

            Intent login_intent = new Intent(MainActivity.this,LoginActivity.class);
            login_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(login_intent);

        }
        //addAuthListener();
        fire_db_post_db= FirebaseDatabase.getInstance().getReference().child("Barter_Posts");
        fire_db_user = FirebaseDatabase.getInstance().getReference().child("User");
        fire_db_like = FirebaseDatabase.getInstance().getReference().child("Like");
        filter_post_display= FirebaseDatabase.getInstance().getReference().child("Barter_Posts");


        fire_db_user.keepSynced(true);
        fire_db_like.keepSynced(true);
        fire_db_post_db.keepSynced(true);

        filter_post_display.keepSynced(true);

/*
        if(user_auth.getCurrentUser().getUid()!=null){


            chk_uid = user_auth.getCurrentUser().getUid();
            chk_username = fire_db_user.child(chk_uid).child("user_name").toString();
            chk_user_email = fire_db_user.child(chk_uid).child("user_email").toString();
            chk_user_profile = fire_db_user.child(chk_uid).child("user_img").toString();
           set_nav_user_email(chk_user_email);
            set_nav_username(chk_username);
            //set_nav_user_profile_pic(MainActivity.this,chk_user_profile);
        }


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);*/

      /*  LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        */

        mLayoutManager = new LinearLayoutManager(MainActivity.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        // Now set the layout manager and the adapter to the RecyclerView



        recyclerView = (RecyclerView)findViewById(R.id.post_recycler_list);
         recyclerView.setHasFixedSize(true);
       // recyclerView.setLayoutManager(new LinearLayoutManager(this).setReverseLayout(true));
        recyclerView.setLayoutManager(mLayoutManager);



        btn_search.setOnClickListener(this);
        btn_filter_goods.setOnClickListener(this);
        btn_filter_service.setOnClickListener(this);

        txt_search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

    }

    protected void hideSoftKeyboard(EditText input) {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(MainActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }




    @Override
    public void onClick(View v)
    {
        if(v.getId()==R.id.btn_barter_search)
        { Toast.makeText(getApplicationContext(), "search88 Loading...", Toast.LENGTH_LONG).show();

            filter_post_query= filter_post_display.orderByChild("description")
                    .startAt(txt_search.getText().toString())
                    .endAt( txt_search.getText().toString()+ "\uf8ff");

            /*
            *
            * charText = charText.toLowerCase();
    clearDataSet();
    if (charText.length() == 0) {
        mDataset.addAll(mDataOrigin);
    } else {
        for (User user : mDataOrigin) {
            if (user.getName().toLowerCase().contains(charText)) {
                mDataset.add(user);
            }
        }
    }
    mAdapter.notifyDataSetChanged();
            *
            * */

            filter_post_query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists()) {
                        /* for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            Toast.makeText(getApplicationContext(), "id = " + snapshot.getKey(), Toast.LENGTH_LONG).show();
                        } */
                        Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_LONG).show();

                        load_page(filter_post_query);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Could Not found the barter with related keywords, Please try again.", Toast.LENGTH_LONG).show();


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    throw databaseError.toException();
                }
            });
            //load_page(filter_post_display);
        }else if(v.getId()==R.id.btn_services){
            //current_user_post_display.orderByChild("uid").equalTo(current_uid);
            Toast.makeText(getApplicationContext(), "services Loading...", Toast.LENGTH_LONG).show();

            filter_post_queryServices= filter_post_display.orderByChild("type")
                    .equalTo("Services");
            filter_post_queryServices.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists()) {
                        /* for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            Toast.makeText(getApplicationContext(), "id = " + snapshot.getKey(), Toast.LENGTH_LONG).show();
                        } */
                        Toast.makeText(getApplicationContext(), "Services Loading...", Toast.LENGTH_LONG).show();

                        load_page(filter_post_queryServices);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Could Not found the barter with related keywords, Please try again.", Toast.LENGTH_LONG).show();


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    throw databaseError.toException();
                }
            });

            //load_page(filter_post_display);
        }else if(v.getId()==R.id.btn_goods){
            Toast.makeText(getApplicationContext(), "goods Loading...", Toast.LENGTH_LONG).show();

            filter_post_queryGoods= filter_post_display.orderByChild("type")
                    .equalTo("Goods");
            filter_post_queryGoods.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists()) {
                        /* for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            Toast.makeText(getApplicationContext(), "id = " + snapshot.getKey(), Toast.LENGTH_LONG).show();
                        } */
                        Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_LONG).show();

                        load_page(filter_post_queryGoods);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Could Not found the barter with related keywords, Please try again.", Toast.LENGTH_LONG).show();


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    throw databaseError.toException();
                }
            });
        }





    }





    public void setup_info_display() {
        DatabaseReference userdbRef = FirebaseDatabase.getInstance().getReference("User");
     if(user_auth.getCurrentUser()!=null){

         chk_uid = user_auth.getCurrentUser().getUid();
         DatabaseReference userSpecificRef=userdbRef.child(chk_uid);
         userSpecificRef.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {

                 chk_username = dataSnapshot.child("user_name").getValue(String.class);
                 chk_user_email = dataSnapshot.child("user_email").getValue(String.class);
                 chk_user_profile =dataSnapshot.child("user_img").getValue(String.class);

                 if(count_login<1) {
                     Toast.makeText(getApplicationContext(), "Welcom " + chk_username + " !", Toast.LENGTH_SHORT).show();
                     count_login++;
                 }

                 set_nav_user_email(chk_user_email);
                 set_nav_username(chk_username);
                 set_nav_user_profile_pic(getApplicationContext(), chk_user_profile);
                 }

             @Override
             public void onCancelled(DatabaseError databaseError) {
            //  Log.w(TAG, "onCancelled", databaseError.toException());
             }
         });



    }

    }

    public void addAuthListener(){
        user_auth.addAuthStateListener(user_auth_listnener);
        user_auth_listnener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(user_auth.getCurrentUser()==null){
                   // Toast.makeText(getApplicationContext(),"current user is null detected!",Toast.LENGTH_SHORT).show();

                    Intent login_intent = new Intent(MainActivity.this,LoginActivity.class);
                    login_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(login_intent);

                }  else{

                     chk_uid = user_auth.getCurrentUser().getUid();
                     chk_username = fire_db_user.child(chk_uid).child("user_name").toString();
                     chk_user_email = fire_db_user.child(chk_uid).child("user_email").toString();
                     chk_user_profile = fire_db_user.child(chk_uid).child("user_img").toString();
                    set_nav_user_email(chk_user_email);
                    set_nav_username(chk_username);
                    set_nav_user_profile_pic(MainActivity.this,chk_user_profile);

               }


            }
        };

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        //user_auth.signOut();
       // google_signOut();
    }

    @Override
    protected void onResume(){
        super.onResume();

        if(user_auth.getCurrentUser()==null){
            // Toast.makeText(getApplicationContext(),"current user is null detected!",Toast.LENGTH_SHORT).show();

           Intent login_intent = new Intent(MainActivity.this,LoginActivity.class);
            login_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(login_intent);

        }else {
            setup_info_display();
        }
    }

    public void load_page( Query db){
        //addAuthListener();
        FirebaseRecyclerAdapter<Post,PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(
                Post.class,
                R.layout.post_row,
                PostViewHolder.class,
                db //change to   query_current_user_post

        ) {
            @Override
            protected void populateViewHolder(PostViewHolder viewHolder,  Post model, int position) {

                final String post_key = getRef(position).getKey();

                viewHolder.setBarterTitle(model.getTitle());
                viewHolder.setDescription(model.getDescription());
                //  Toast.makeText(MainActivity.this,model.getDescription()+"--"+model.getTitle()+ "test descrip",Toast.LENGTH_SHORT);
                viewHolder.setType(model.getType());
                viewHolder.setVal(model.getValue());
                viewHolder.setUser(model.getUsername());
                viewHolder.setPostTime(model.getTime());
                viewHolder.setLikeCount(model.getLike_count());
                viewHolder.setImg(MainActivity.this,model.getBarter_img());

                viewHolder.setLikeBtn_(post_key);


                viewHolder.post_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Toast.makeText(MainActivity.this,"This is the "+model.getTitle()+" - "+post_key+"",Toast.LENGTH_LONG).show();
                        //click on each barter item
                        Intent BarterSingleActivity = new Intent(MainActivity.this, com.example.jason.barterworld.BarterSingleActivity.class);

                        BarterSingleActivity.putExtra("barter_item_id",post_key);

                        startActivity(BarterSingleActivity);


                    }
                });

                fire_db_like_child = FirebaseDatabase.getInstance().getReference().child("Like").child(post_key);


                viewHolder.likeBtn_.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        barter_like_status = true;


                        //first get the current total like count from db for current post key
                        fire_db_post_db.child(post_key).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String count = dataSnapshot.child("like_count").getValue(String.class);
                                likeCount = Integer.parseInt(count);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                        //then chk the user click on the like button or not, if the button was clicked , the user would add one to the like count
                        fire_db_like.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {


                                if(barter_like_status) {


                                    if (dataSnapshot.child(post_key).hasChild(user_auth.getCurrentUser().getUid())) {
                                        //does exist the user
                                        fire_db_like.child(post_key).child(user_auth.getCurrentUser().getUid()).removeValue();

                                        likeCount--;
                                        barter_like_status = false;
                                        String likeCountStr=String.valueOf(likeCount);
                                        fire_db_post_db.child(post_key).child("like_count").setValue(likeCountStr);


                                    } else {

                                        fire_db_like.child(post_key).child(user_auth.getCurrentUser().getUid()).setValue("liked");
                                        likeCount++;

                                        barter_like_status = false;
                                        String likeCountStr=String.valueOf(likeCount);
                                        fire_db_post_db.child(post_key).child("like_count").setValue(likeCountStr);
                                    }


                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });




                    }
                });



            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onStart() {
        super.onStart();

        //load_page(fire_db_post_db);
        //
        //addAuthListener();
        FirebaseRecyclerAdapter<Post,PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(
                Post.class,
                R.layout.post_row,
                PostViewHolder.class,
                fire_db_post_db //change to   query_current_user_post

        ) {
            @Override
            protected void populateViewHolder(PostViewHolder viewHolder,  Post model, int position) {

                final String post_key = getRef(position).getKey();

                viewHolder.setBarterTitle(model.getTitle());
                viewHolder.setDescription(model.getDescription());
                //  Toast.makeText(MainActivity.this,model.getDescription()+"--"+model.getTitle()+ "test descrip",Toast.LENGTH_SHORT);
                viewHolder.setType(model.getType());
                viewHolder.setVal(model.getValue());
                viewHolder.setUser(model.getUsername());
                viewHolder.setPostTime(model.getTime());
                viewHolder.setLikeCount(model.getLike_count());
                viewHolder.setImg(MainActivity.this,model.getBarter_img());

                viewHolder.setLikeBtn_(post_key);


                viewHolder.post_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Toast.makeText(MainActivity.this,"This is the "+model.getTitle()+" - "+post_key+"",Toast.LENGTH_LONG).show();
                        //click on each barter item
                        Intent BarterSingleActivity = new Intent(MainActivity.this, com.example.jason.barterworld.BarterSingleActivity.class);

                        BarterSingleActivity.putExtra("barter_item_id",post_key);

                        startActivity(BarterSingleActivity);


                    }
                });

                fire_db_like_child = FirebaseDatabase.getInstance().getReference().child("Like").child(post_key);


                viewHolder.likeBtn_.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        barter_like_status = true;


                        //first get the current total like count from db for current post key
                        fire_db_post_db.child(post_key).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String count = dataSnapshot.child("like_count").getValue(String.class);
                                likeCount = Integer.parseInt(count);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                        //then chk the user click on the like button or not, if the button was clicked , the user would add one to the like count
                        fire_db_like.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {


                                if(barter_like_status) {


                                    if (dataSnapshot.child(post_key).hasChild(user_auth.getCurrentUser().getUid())) {
                                        //does exist the user
                                        fire_db_like.child(post_key).child(user_auth.getCurrentUser().getUid()).removeValue();

                                        likeCount--;
                                        barter_like_status = false;
                                        String likeCountStr=String.valueOf(likeCount);
                                        fire_db_post_db.child(post_key).child("like_count").setValue(likeCountStr);


                                    } else {

                                        fire_db_like.child(post_key).child(user_auth.getCurrentUser().getUid()).setValue("liked");
                                        likeCount++;

                                        barter_like_status = false;
                                        String likeCountStr=String.valueOf(likeCount);
                                        fire_db_post_db.child(post_key).child("like_count").setValue(likeCountStr);
                                    }


                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });




                    }
                });



            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
        check_user_exist();

    }

    public void check_user_exist(){


        if(user_auth.getCurrentUser()!=null) {

            final String uid = user_auth.getCurrentUser().getUid();

            fire_db_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (!dataSnapshot.hasChild(uid)) {

                        Intent setup_intent = new Intent(MainActivity.this, SetupActivity.class);
                        setup_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(setup_intent);
//bring non-legit user to setup activity

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_main);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.nav_barter_world) {


            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        } else if (id == R.id.nav_barter_map) {

            Intent intent = new Intent(getApplicationContext(), MapActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }//nav_barter_self_admin
        else if (id == R.id.nav_barter_self_admin) {

            Intent intent = new Intent(getApplicationContext(), SelfAdminActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }
        /*else if (id == R.id.nav_barter_add_new){
            Intent intent = new Intent(getApplicationContext(), PostActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }*/ else if (id == R.id.nav_interested_barters){
            Intent intent = new Intent(getApplicationContext(), OutgoingQuotesActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }  else if (id == R.id.nav_quotes_to_me){
            Intent intent = new Intent(getApplicationContext(), IncomingQuotesActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        } else if (id == R.id.nav_barter_profile) {
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        } else if (id == R.id.nav_barter_logout) {
            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_main);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public static class PostViewHolder extends RecyclerView.ViewHolder{


        DatabaseReference db_ref_like;
        FirebaseAuth user_auth;

        View post_view;

        TextView txt_title;

        ImageButton likeBtn_;

        TextView like_count_txt;

        public PostViewHolder(View itemView) {
            super(itemView);
            post_view=itemView;

            db_ref_like = FirebaseDatabase.getInstance().getReference().child("Like");
            user_auth = FirebaseAuth.getInstance();

            db_ref_like.keepSynced(true);


            likeBtn_ = (ImageButton)post_view.findViewById(R.id.likeBtn);
            //like_count_txt= (TextView)post_view.findViewById(R.id.textView_likeNum);

            // txt_title= (TextView)post_view.findViewById(R.id.txt_post_title);

    /*        txt_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v("MainActivity","This is the barter item"+txt_title.getText().toString()+"");
                }
            });


*/





        }

        public void setLikeBtn_(final String post_key){

            db_ref_like.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(user_auth.getCurrentUser()!=null) {
                        if (dataSnapshot.child(post_key).hasChild(user_auth.getCurrentUser().getUid())) {

                            likeBtn_.setImageResource(R.mipmap.ic_thumb_up_black_24dp_red);

                        } else {
                            likeBtn_.setImageResource(R.mipmap.ic_thumb_up_black_24dp);


                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



        }



        public void setBarterTitle(String title){

          TextView txt_title= (TextView)post_view.findViewById(R.id.txt_post_name);

            txt_title.setText(title);

        }




        public void setDescription(String desc){

            TextView txt_desc= (TextView)post_view.findViewById(R.id.txt_comment);
            txt_desc.setText(desc);


        }
        public void setType(String type){

            TextView txt_type= (TextView)post_view.findViewById(R.id.txt_post_type);
            txt_type.setText(type);

        }
        public void setVal(String value){

            TextView txt_value= (TextView)post_view.findViewById(R.id.txt_post_value);
            txt_value.setText("$ "+value);

        }
        public void setUser(String user){

            TextView txt_user= (TextView)post_view.findViewById(R.id.txt_post_user);
            txt_user.setText("Owner : "+user);

        }
        public void setPostTime(String time){

            TextView txt_time= (TextView)post_view.findViewById(R.id.txt_post_upload_time);
            txt_time.setText("Post on : "+time);

        }
        public void setLikeCount(String count){

            TextView txt_likeCunt= (TextView)post_view.findViewById(R.id.textView_likeNum);
            txt_likeCunt.setText(count);

        }

        public void setImg(final Context ctx, final String img){

            final ImageView post_img = (ImageView)post_view.findViewById(R.id.img_post);
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


         /*  Picasso.with(ctx).load(img).networkPolicy(NetworkPolicy.OFFLINE).into(post_img, new Callback() {
                @Override     CX
                public void onSuccess() {

                }

                @Override
                public void onError() {

                    Picasso.with(ctx).load(img).into(post_img);
                }
            });*/

        }








    }

    public void set_nav_username( String username_){

        user_name.setText(username_);

    }
    public void set_nav_user_email( String user_email_){

        user_email.setText(user_email_);

    }


    public void set_nav_user_profile_pic(final Context ctx, final String img){

        final ImageView user_profile_img = (ImageView)header.findViewById(R.id.nav_header_profile_pic);
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


         /*  Picasso.with(ctx).load(img).networkPolicy(NetworkPolicy.OFFLINE).into(post_img, new Callback() {
                @Override     CX
                public void onSuccess() {

                }

                @Override
                public void onError() {

                    Picasso.with(ctx).load(img).into(post_img);
                }
            });*/

    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);



        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        if(item.getItemId()==R.id.action_logout){
                    logout();

        }else if (item.getItemId()==R.id.main_barter_map_admin){//main_barter_map_admin
            Intent intent = new Intent(getApplicationContext(), MapActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }else if (item.getItemId()==R.id.main_barter_self_admin){//main_barter_self_admin
            Intent intent = new Intent(getApplicationContext(), SelfAdminActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }else if (item.getItemId()==R.id.main_profile_admin){//main_profile_admin
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }



    public void logout(){

        user_auth.signOut();
        google_signOut();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));

    }

    private void google_signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // ...
                    }
                });
    }





}
