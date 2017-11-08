package com.example.jason.barterworld;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class PostActivity extends AppCompatActivity {




    private ImageButton post_img_btn;
    private TextView txt_barter_title;
    private TextView txt_barter_value;
    private TextView txt_barter_desc;

    private EditText editText_lat;
    private EditText editText_long;
    private Spinner spin_barter_type;

    private Button btn_post;

    private Uri post_imgUri=null;

    private static final int GALLERY_REQUEST=1;

    //firebase storeage reference
    private StorageReference post_storage_ref;
    private DatabaseReference post_db_ref;

    private FirebaseAuth user_auth;

    private FirebaseUser currentUser;

    private DatabaseReference databaseReference_user;
    private ProgressDialog progress_dialog;

    String user_uid="";

    String barter_latitude="";
    String barter_longitude="";

    private static final String TAG = "PostActivity";

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
       // getActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_backbtn);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            barter_latitude = extras.getString("barter_latitude");
            barter_longitude = extras.getString("barter_longitude");
            //The key argument here must match that used in the other activity

        }

        user_auth = FirebaseAuth.getInstance();
        currentUser = user_auth.getCurrentUser();
        //user_uid = user_auth.getInstance().getCurrentUser().getUid();



        databaseReference_user = FirebaseDatabase.getInstance().getReference().child("User").child(currentUser.getUid());

        progress_dialog = new ProgressDialog(this);

        post_db_ref= FirebaseDatabase.getInstance().getReference().child("Barter_Posts");
        post_storage_ref = FirebaseStorage.getInstance().getReference().child("Barter_Posts");

        post_img_btn=(ImageButton)findViewById(R.id.btn_img_profile);

        txt_barter_title=(TextView)findViewById(R.id.txt_post_name);
        txt_barter_desc=(TextView)findViewById(R.id.txt_barter_desc);
        txt_barter_value=(TextView)findViewById(R.id.txt_barter_value);
        editText_lat=(EditText)findViewById(R.id.editText_lat);
        editText_long=(EditText)findViewById(R.id.editText_long);
        spin_barter_type=(Spinner)findViewById(R.id.spinner_barter_type);


        btn_post=(Button)findViewById(R.id.btn_post);



        editText_lat.setText(barter_latitude);
        editText_long.setText(barter_longitude);

        post_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");

                startActivityForResult(galleryIntent,GALLERY_REQUEST);

            }
        });

        btn_post.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            startPosting();
            }



        });




    }

    public void startPosting(){

        progress_dialog.setMessage("Posting Your Barter Item...");

        final String barter_title = txt_barter_title.getText().toString().trim();
        final String barter_desc = txt_barter_desc.getText().toString().trim();

        final String barter_value = txt_barter_value.getText().toString().trim();

        final String barter_type=spin_barter_type.getSelectedItem().toString().trim();


        final String barter_latitude = editText_lat.getText().toString().trim();
        final String barter_longitude = editText_long.getText().toString().trim();


        if(!TextUtils.isEmpty(barter_longitude) && !TextUtils.isEmpty(barter_latitude) && !TextUtils.isEmpty(barter_title) && !TextUtils.isEmpty(barter_desc) && !TextUtils.isEmpty(barter_value) &&! TextUtils.isEmpty(barter_type) && post_imgUri!=null ){
            progress_dialog.show();
            String uuid = UUID.randomUUID().toString();

            StorageReference filePath=post_storage_ref.child("Post_Images").child(post_imgUri.getLastPathSegment()+"_"+uuid+"_.png");

            filePath.putFile(post_imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    @SuppressWarnings("VisibleForTests")       final Uri downloadUri = taskSnapshot.getDownloadUrl();
                    final DatabaseReference new_post=post_db_ref.push();//push to the node so to create the random id


                    databaseReference_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {



                            Calendar c = Calendar.getInstance();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String strDate = sdf.format(c.getTime());

                            user_uid = currentUser.getUid();

                            new_post.child("uid").setValue(user_uid);
                            new_post.child("title").setValue(barter_title);
                            new_post.child("description").setValue(barter_desc);
                            new_post.child("value").setValue(barter_value);
                            new_post.child("type").setValue(barter_type);
                            new_post.child("time").setValue(strDate);
                            new_post.child("like_count").setValue("0");
                            new_post.child("latitude").setValue(barter_latitude);
                            new_post.child("longitude").setValue(barter_longitude);

                            new_post.child("barter_img").setValue(downloadUri.toString());
                            new_post.child("username").setValue(dataSnapshot.child("user_name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){


                                        startActivity(new Intent(PostActivity.this, MainActivity.class));

                                    }else{


                                        Toast.makeText(getApplicationContext(),"Setup Account Failed.",Toast.LENGTH_SHORT).show();
                                    }


                                }
                            });





                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                    progress_dialog.dismiss();
                }
            });

        }else{
            dialogBox("Please fill in all the information!");

        }


    }

    public void dialogBox(String txt) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(txt);
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });

        alertDialogBuilder.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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
}
