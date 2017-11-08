package com.example.jason.barterworld;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {


    private TextView txt_name;
    private TextView txt_email;
    private TextView txt_pwd;
    private TextView txt_pwd1;

    private Button btn_register;
    private ImageButton btn_img_profile;


    private static final int GALLERY_REQUEST=1;
    private Uri user_imgUri;

    private StorageReference user_storage_ref;
    private DatabaseReference user_db_ref;
    private FirebaseAuth user_auth;

    private ProgressDialog progress_dialog;

    private DatabaseReference db_ref_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        user_db_ref= FirebaseDatabase.getInstance().getReference().child("User");
        user_storage_ref= FirebaseStorage.getInstance().getReference();

        progress_dialog = new ProgressDialog(this);
        user_auth = FirebaseAuth.getInstance();

        txt_name =(TextView)findViewById(R.id.txt_user_name_setup);
        txt_email=(TextView)findViewById(R.id.txt_email_setup);
        txt_pwd=(TextView)findViewById(R.id.txt_pwd_setup);
        txt_pwd1=(TextView)findViewById(R.id.txt_pwd2);

        btn_register=(Button)findViewById(R.id.btn_register_setup);
        btn_img_profile=(ImageButton)findViewById(R.id.btn_img_profile_setup);

        btn_img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(galleryIntent.createChooser(galleryIntent, "Select Picture"), GALLERY_REQUEST);
              //  startActivityForResult(galleryIntent,GALLERY_REQUEST);

            }
        });


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

    }

    public void register(){

//****

        progress_dialog.setMessage("We are signing you up...");

        final String user_name = txt_name.getText().toString().trim();
        final String user_email = txt_email.getText().toString().trim();

        final String user_pwd = txt_pwd.getText().toString().trim();

        final String user_pwd2=txt_pwd1.getText().toString().trim();



        if(!TextUtils.isEmpty(user_name) && !TextUtils.isEmpty(user_email) && !TextUtils.isEmpty(user_pwd) &&! TextUtils.isEmpty(user_pwd2) && user_imgUri!=null ){
           if(user_pwd.equals(user_pwd2)) {



               progress_dialog.show();



               user_auth.createUserWithEmailAndPassword(user_email,user_pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {


                       if (task.isSuccessful()) {

                           String user_uid=user_auth.getCurrentUser().getUid();

                            db_ref_user = user_db_ref.child(user_uid);

                       String uuid = UUID.randomUUID().toString();

                       StorageReference filePath = user_storage_ref.child("User_Images").child(user_imgUri.getLastPathSegment() + "_" + uuid);

                       filePath.putFile(user_imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                           @Override
                           public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                               @SuppressWarnings("VisibleForTests")     Uri downloadUri = taskSnapshot.getDownloadUrl();


                               db_ref_user.child("user_name").setValue(user_name);
                               db_ref_user.child("user_email").setValue(user_email);
                               db_ref_user.child("user_pwd").setValue(user_pwd);


                               db_ref_user.child("user_img").setValue(downloadUri.toString());

                               progress_dialog.dismiss();

                               Intent main_activity = new Intent(RegisterActivity.this,MainActivity.class);
                               main_activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                               startActivity(main_activity);
                           }
                       });


                   }
                   }
               });

           }else{
               alert_box("Sorry! Both Password need to be matched!");


           }
        }else{

alert_box("Sorry! You need to fill in all the information!");

        }



//****
    }

    public void alert_box(String txt){


        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(txt);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

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


try {


    if (requestCode == GALLERY_REQUEST && resultCode == RegisterActivity.RESULT_OK) {

        Uri uri = data.getData();

        CropImage.activity(uri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(this);


    }
    if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
        CropImage.ActivityResult result = CropImage.getActivityResult(data);

        if (resultCode == RegisterActivity.RESULT_OK) {

            user_imgUri = result.getUri();





                /*
                InputStream is;
                Drawable icon;
                try {
                    is = this.getContentResolver().openInputStream( user_imgUri );
                    BitmapFactory.Options options=new BitmapFactory.Options();
                    options.inSampleSize = 10;
                    Bitmap preview_bitmap=BitmapFactory.decodeStream(is,null,options);

                    icon = new BitmapDrawable(getResources(),preview_bitmap);

                } catch (FileNotFoundException e) {
                    //set default image from the button
                    icon = getResources().getDrawable(R.mipmap.profile);
                }
                btn_img_profile.setBackground(icon);


                */
            //  btn_img_profile.setImageBitmap(getImageBitmap(user_imgUri.toString()));


            Uri imgUri = Uri.parse("android.resource://my.package.name/" + R.mipmap.profile);
            btn_img_profile.setImageURI(null);
            btn_img_profile.setImageURI(user_imgUri);
            //  btn_img_profile.setImageURI(user_imgUri);

            Bitmap bitmap =  MediaStore.Images.Media.getBitmap(RegisterActivity.this.getContentResolver(), user_imgUri);
            btn_img_profile.setImageBitmap(bitmap);

        }
        if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

            Exception error = result.getError();


        }


    } else {
        Toast.makeText(RegisterActivity.this, "You haven't picked Image",
                Toast.LENGTH_LONG).show();
    }


}catch (Exception e) {
    Toast.makeText(RegisterActivity.this, "Something went wrong"+e.getMessage(), Toast.LENGTH_LONG)
            .show();
}

    }


    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(),"Error getting image",Toast.LENGTH_LONG).show();

        }
        return bm;
    }
}
