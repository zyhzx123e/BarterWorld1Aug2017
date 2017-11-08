package com.example.jason.barterworld;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.Text;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class BarterSingleActivity extends AppCompatActivity {


    private String post_key = null;
    private DatabaseReference databaseReference_barter_item;

    private FirebaseAuth auth_user;



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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barter_single);


        post_key = getIntent().getExtras().getString("barter_item_id");


        btn_delete = (Button)findViewById(R.id.btn_delete);
        btn_quote = (Button)findViewById(R.id.btn_quote);



        barter_img_view = (ImageView)findViewById(R.id.barter_img);
        barter_title = (TextView)findViewById(R.id.barter_title);
        barter_description = (TextView)findViewById(R.id.barter_description);
        barter_owner_name = (TextView)findViewById(R.id.barter_owner);
        barter_post_time = (TextView)findViewById(R.id.barter_post_time);
        barter_post_type = (TextView)findViewById(R.id.barter_type);
        barter_post_value = (TextView)findViewById(R.id.barter_value);






        auth_user = FirebaseAuth.getInstance();
        databaseReference_barter_item = FirebaseDatabase.getInstance().getReference().child("Barter_Posts");

        databaseReference_barter_item.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String post_title = (String) dataSnapshot.child("title").getValue();
                String post_description = (String) dataSnapshot.child("description").getValue();
                String post_owner= (String) dataSnapshot.child("username").getValue();
                String post_time = (String) dataSnapshot.child("time").getValue();
                String post_type = (String)dataSnapshot.child("type").getValue();
                String post_value = (String)dataSnapshot.child("value").getValue();


                String post_user_uid = (String)dataSnapshot.child("uid").getValue();
                String post_barter_img = (String)dataSnapshot.child("barter_img").getValue();



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
                }else{

                    btn_delete.setVisibility(View.INVISIBLE);
                }



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



    }
}
