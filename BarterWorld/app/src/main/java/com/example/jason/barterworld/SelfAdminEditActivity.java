package com.example.jason.barterworld;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jason.barterworld.tools.RoundedTransformation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

public class SelfAdminEditActivity extends AppCompatActivity {

    private Uri post_imgUri=null;

    private static final int GALLERY_REQUEST=1;


    public String post_key = null;
    public String current_comment_key;
    private DatabaseReference databaseReference_barter_item;

    private FirebaseAuth auth_user;

    private DatabaseReference databaseReference_user;
    private DatabaseReference databaseReference_Post;

    //layout components:



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

    private Uri profile_img_uri= null;
    private StorageReference Barter_Posts_Storage;

    private ProgressDialog progress_dialog;

    private DatabaseReference msg_db_ref;//new msg db
    private DatabaseReference outgoing_quotes_db_ref;//new msg db
    private DatabaseReference incoming_quotes_db_ref;//new msg db
    private DatabaseReference chat_db_post_db;

    private DatabaseReference  comment_db_ref;
    private DatabaseReference barter_admin_edit_db_ref;

    public String current_msg_key;
    public String new_msg_key;


    public String user_uid="";

    private FirebaseAuth user_auth;

    private FirebaseUser currentUser;

    private DatabaseReference databaseReference_user_chat;

    public String current_user_name="",current_user_img_url="";
    private RecyclerView comment_recyclerView;

    public Boolean flag1,flag2,flag3= false;

    private  Button btn_save_admin_edit_info,btn_delete;
    private ImageButton post_img_btn;
    private EditText barter_title;
    private EditText barter_description;
    private EditText editText_lat;
    private EditText editText_long;
    private Spinner spin_barter_type;
    private EditText barter_post_value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_admin_edit);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_backbtn);
        user_auth = FirebaseAuth.getInstance();
        currentUser = user_auth.getCurrentUser();
        //user_uid = user_auth.getInstance().getCurrentUser().getUid();
        user_uid = currentUser.getUid();

        progress_dialog = new ProgressDialog(this);
        databaseReference_user = FirebaseDatabase.getInstance().getReference().child("User").child(user_uid);
        Barter_Posts_Storage= FirebaseStorage.getInstance().getReference().child("Barter_Posts");

        post_img_btn=(ImageButton)findViewById(R.id.btn_img_profile_edit);
        barter_title=(EditText)findViewById(R.id.txt_post_name_edit);
        barter_description=(EditText)findViewById(R.id.txt_barter_desc_edit);
        barter_post_value=(EditText)findViewById(R.id.txt_barter_value_edit);
        editText_lat=(EditText)findViewById(R.id.editText_lat);
        editText_long=(EditText)findViewById(R.id.editText_long);
        spin_barter_type=(Spinner)findViewById(R.id.spinner_barter_type_edit);
        btn_save_admin_edit_info=(Button)findViewById(R.id.btn_post_edit);
        btn_delete=(Button)findViewById(R.id.btn_post_delete);


        post_key = getIntent().getExtras().getString("barter_item_id");

        databaseReference_barter_item = FirebaseDatabase.getInstance().getReference().child("Barter_Posts");
        barter_admin_edit_db_ref= FirebaseDatabase.getInstance().getReference().child("Barter_Posts").child(post_key);


        barter_admin_edit_db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                post_barter_img =dataSnapshot.child("barter_img").getValue(String.class);
                 post_description =dataSnapshot.child("description").getValue(String.class);
               post_latitude =dataSnapshot.child("latitude").getValue(String.class);



             post_longitude=dataSnapshot.child("longitude").getValue(String.class);

               post_title=dataSnapshot.child("title").getValue(String.class);
              post_type =dataSnapshot.child("type").getValue(String.class);
               post_user_uid =user_uid;







                String chk_type=dataSnapshot.child("type").getValue(String.class);

                set_barterTitle(dataSnapshot.child("title").getValue(String.class));
                set_barterDesc(dataSnapshot.child("description").getValue(String.class));
                set_barterValue(dataSnapshot.child("value").getValue(String.class));
                set_barterLat(dataSnapshot.child("latitude").getValue(String.class));
                set_barterLong(dataSnapshot.child("longitude").getValue(String.class));

                //0 => Services
                //1 => Goods
                //2 => Others
                if(chk_type.equals("Services")){
                    setSpinnerType(0);
                }else if(chk_type.equals("Goods")){
                    setSpinnerType(1);
                }else{
                    setSpinnerType(2);

                }

                set_nav_user_profile_pic(getApplicationContext(),dataSnapshot.child("barter_img").getValue(String.class));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        post_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");

                startActivityForResult(galleryIntent,GALLERY_REQUEST);

            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder mBuilder = new AlertDialog.Builder(SelfAdminEditActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_confirm, null);

                TextView txt = (TextView) mView.findViewById(R.id.txt_alert_content);
                txt.setText("Are you sure You want to delete this? Once deleted, the data will be permanently gone!");
                Button btn_ok = (Button) mView.findViewById(R.id.btn_ok);
                Button btn_cancel = (Button) mView.findViewById(R.id.btn_cancel);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();


                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                            dialog.dismiss();
                        databaseReference_barter_item.child(post_key).removeValue();

                        Intent mainIntent = new Intent(SelfAdminEditActivity.this,MainActivity.class);

                        startActivity(mainIntent);

                        Toast.makeText(getApplication(), "You have deleted Your Barter Item : " + post_title + " successfully!", Toast.LENGTH_SHORT).show();

                    }
                });

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        dialog.dismiss();

                    }
                });






                /////////validate delete confirmation
                ///////////////////////
                ///////////////////////

            }
        });


        btn_save_admin_edit_info.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                save_content();
            }



        });


    }




public void save_content(){


    String uuid = UUID.randomUUID().toString();


    progress_dialog.setMessage("Please wait , we are saving your account...");


    /*
    *
    *  barter_title=(EditText)findViewById(R.id.txt_post_name_edit);
        barter_description=(EditText)findViewById(R.id.txt_barter_desc_edit);
        barter_post_value=(EditText)findViewById(R.id.txt_barter_value_edit);
        editText_lat=(EditText)findViewById(R.id.editText_lat);
        editText_long=(EditText)findViewById(R.id.editText_long);
        spin_barter_type
    * */
    final String barter_name = barter_title.getText().toString().trim();
    final String barter_desc = barter_description.getText().toString().trim();

    final String barter_value = barter_post_value.getText().toString().trim();

    final String barter_type=spin_barter_type.getSelectedItem().toString().trim();


    final String barter_latitude = editText_lat.getText().toString().trim();
    final String barter_longitude = editText_long.getText().toString().trim();


    if(!TextUtils.isEmpty(barter_longitude) && !TextUtils.isEmpty(barter_latitude) && !TextUtils.isEmpty(barter_name) && !TextUtils.isEmpty(barter_desc) && !TextUtils.isEmpty(barter_value) &&! TextUtils.isEmpty(barter_type)){


        progress_dialog.show();

        if(profile_img_uri!=null) {

            StorageReference filePath =Barter_Posts_Storage.child("Post_Images").child(profile_img_uri.getLastPathSegment() + "_" + uuid + "_.png");

            filePath.putFile(profile_img_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    @SuppressWarnings("VisibleForTests") String download_uri = taskSnapshot.getDownloadUrl().toString();



                    barter_admin_edit_db_ref.child("title").setValue(barter_name);
                    barter_admin_edit_db_ref.child("description").setValue(barter_desc);
                    barter_admin_edit_db_ref.child("type").setValue(barter_type);
                    barter_admin_edit_db_ref.child("value").setValue(barter_value);
                    barter_admin_edit_db_ref.child("latitude").setValue(barter_latitude);
                    barter_admin_edit_db_ref.child("longitude").setValue(barter_longitude);
                    barter_admin_edit_db_ref.child("barter_img").setValue(download_uri);



                    progress_dialog.dismiss();



                    Intent main_activity = new Intent(SelfAdminEditActivity.this, MainActivity.class);
                    main_activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(main_activity);

                }
            });


        }else{


            barter_admin_edit_db_ref.child("title").setValue(barter_name);
            barter_admin_edit_db_ref.child("description").setValue(barter_desc);
            barter_admin_edit_db_ref.child("type").setValue(barter_type);
            barter_admin_edit_db_ref.child("value").setValue(barter_value);
            barter_admin_edit_db_ref.child("latitude").setValue(barter_latitude);
            barter_admin_edit_db_ref.child("longitude").setValue(barter_longitude);



            progress_dialog.dismiss();



            Intent main_activity = new Intent(SelfAdminEditActivity.this, SelfAdminActivity.class);
            main_activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(main_activity);



        }



    }else{
        Toast.makeText(SelfAdminEditActivity.this, "Make Sure You have filled in all the info! ", Toast.LENGTH_SHORT).show();
    }








}


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if(requestCode == GALLERY_REQUEST && resultCode == PostActivity.RESULT_OK ){

            Uri uri = data.getData();


            CropImage.activity(uri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);




        }


        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if(resultCode==PostActivity.RESULT_OK){


                // post_imgUri = data.getData();
                // post_img_btn.setImageURI(post_imgUri);
                post_imgUri = result.getUri();

                InputStream is;
                Drawable icon;
                try {
                    is = this.getContentResolver().openInputStream( post_imgUri );
                    BitmapFactory.Options options=new BitmapFactory.Options();
                    options.inSampleSize = 10;
                    Bitmap preview_bitmap=BitmapFactory.decodeStream(is,null,options);

                    icon = new BitmapDrawable(getResources(),preview_bitmap);

                } catch (FileNotFoundException e) {
                    //set default image from the button
                    icon = getResources().getDrawable(R.mipmap.ic_barter);
                }

                post_img_btn.setBackground(icon);


                //  btn_profile_setup.setImageURI(profile_img_uri);

            }
            else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){

                Exception error = result.getError();

            }

        }


        //*********

     /*   if(requestCode == GALLERY_REQUEST && resultCode==RESULT_OK){


            post_imgUri = data.getData();
            post_img_btn.setImageURI(post_imgUri);

        }*/
    }






    public void set_barterTitle( String title){

        barter_title.setText(title);

    }
    public void set_barterDesc( String desc){

        barter_description.setText(desc);

    }
    public void set_barterValue( String value){

        barter_post_value.setText(value);

    }

    public void set_barterLat( String lat){

        editText_lat.setText(lat);

    }
    public void set_barterLong( String longi){

        editText_long.setText(longi);

    }
    public  void setSpinnerType(int posId){
        spin_barter_type.setSelection(posId);
        //0 => Services
        //1 => Goods
        //2 => Others
    }


    public void set_nav_user_profile_pic(final Context ctx, final String img){

        final ImageView post_img = (ImageView)findViewById(R.id.btn_img_profile_edit);
        //Picasso.with(ctx).load(img).into(post_img);

        Picasso.with(ctx).load(img).fit()
                .placeholder(R.drawable.ic_media_play)
                .transform(new RoundedTransformation(80, 4))
                .into(post_img, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                        Picasso.with(ctx).load(img).fit().placeholder(R.drawable.ic_media_play)
                                .into(post_img);
                    }
                });



    }





}
