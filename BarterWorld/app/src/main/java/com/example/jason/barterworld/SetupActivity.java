package com.example.jason.barterworld;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.UUID;

public class SetupActivity extends AppCompatActivity {


    private ImageButton btn_profile_setup;

    private EditText txt_email_setup;

    private EditText txt_name_setup;
    private EditText txt_pwd_setup;
    private EditText txt_pwd_setup2;

    private Button btn_setup;



    private static final int GALLERY_REQUEST_CODE=1;

    private DatabaseReference databaseReference_user;

    private FirebaseAuth setup_auth;

    private Uri profile_img_uri= null;
    private StorageReference user_profile_storage;

    private ProgressDialog progress_dialog;

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(SetupActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        btn_profile_setup=(ImageButton)findViewById(R.id.btn_img_profile);
        btn_setup=(Button)findViewById(R.id.btn_register_setup);

        databaseReference_user = FirebaseDatabase.getInstance().getReference().child("User");
        setup_auth = FirebaseAuth.getInstance();

        user_profile_storage = FirebaseStorage.getInstance().getReference().child("Profile_image");
        progress_dialog = new ProgressDialog(this);




        txt_email_setup=(EditText)findViewById(R.id.txt_email);
        txt_name_setup=(EditText)findViewById(R.id.txt_post_name);
        txt_pwd_setup=(EditText)findViewById(R.id.txt_pwd);
        txt_pwd_setup2=(EditText)findViewById(R.id.txt_pwd2);

        txt_email_setup.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        txt_name_setup.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        txt_pwd_setup.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        txt_pwd_setup2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        btn_profile_setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideKeyboard(v);
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(galleryIntent.createChooser(galleryIntent, "Select Picture"), GALLERY_REQUEST_CODE);



                //  startActivityForResult(galleryIntent,GALLERY_REQUEST);


            }
        });

        btn_setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                startSetUpAccount();
            }
        });

    }


    protected void startSetUpAccount(){


        final String user_uid = setup_auth.getCurrentUser().getUid();

        final String  user_name = txt_name_setup.getText().toString().trim();
        final String user_email = txt_email_setup.getText().toString().trim();
        final String user_pwd1 = txt_pwd_setup.getText().toString().trim();
        final String user_pwd2 = txt_pwd_setup2.getText().toString().trim();

        progress_dialog.setMessage("Please wait while We are setting up your account...");


        if(profile_img_uri!=null && !TextUtils.isEmpty(user_name)  && !TextUtils.isEmpty(user_email)  && !TextUtils.isEmpty(user_pwd1)  && !TextUtils.isEmpty(user_pwd2)){


if(user_pwd1.equals(user_pwd2)) {


    progress_dialog.show();

    String uuid = UUID.randomUUID().toString();
    StorageReference filePath = user_profile_storage.child(profile_img_uri.getLastPathSegment() + "_" + uuid+"_.png");

    filePath.putFile(profile_img_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            @SuppressWarnings("VisibleForTests")  String download_uri = taskSnapshot.getDownloadUrl().toString();

            databaseReference_user.child(user_uid).child("user_name").setValue(user_name);
            databaseReference_user.child(user_uid).child("user_email").setValue(user_email);

            databaseReference_user.child(user_uid).child("user_pwd").setValue(user_pwd1);

            databaseReference_user.child(user_uid).child("user_img").setValue(download_uri);



            progress_dialog.dismiss();

            Intent main_activity = new Intent(SetupActivity.this,MainActivity.class);
            main_activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(main_activity);

        }
    });

}else{

    Toast.makeText(SetupActivity.this,"Both Password must be matched!",Toast.LENGTH_SHORT).show();

}



        }else{
            Toast.makeText(getApplicationContext(),"Pls fill in all the information!",Toast.LENGTH_SHORT).show();


        }


    }







    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


try {

    if (requestCode == GALLERY_REQUEST_CODE && resultCode == SetupActivity.RESULT_OK) {

        Uri uri = data.getData();

        CropImage.activity(uri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(this);


    }
    if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
        CropImage.ActivityResult result = CropImage.getActivityResult(data);
        Log.d("APP_DEBUG",result.toString());


        if (resultCode == SetupActivity.RESULT_OK) {

            profile_img_uri = result.getUri();

                /*
                InputStream is;
                Drawable icon;
                try {
                    is = this.getContentResolver().openInputStream( profile_img_uri );
                    BitmapFactory.Options options=new BitmapFactory.Options();
                    options.inSampleSize = 10;
                    Bitmap preview_bitmap=BitmapFactory.decodeStream(is,null,options);

                     icon = new BitmapDrawable(getResources(),preview_bitmap);

                } catch (FileNotFoundException e) {
                    //set default image from the button
                    icon = getResources().getDrawable(R.mipmap.profile);
                }

                btn_profile_setup.setBackground(icon);
*/
            btn_profile_setup.setImageURI(null);

            btn_profile_setup.setImageURI(profile_img_uri);


            Bitmap bitmap = MediaStore.Images.Media.getBitmap(SetupActivity.this.getContentResolver(), profile_img_uri);
            btn_profile_setup.setImageBitmap(bitmap);
            //  btn_profile_setup.setImageURI(profile_img_uri);

        }   if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

            Exception error = result.getError();


        }

    } else {
        Toast.makeText(SetupActivity.this, "You haven't picked Image",
                Toast.LENGTH_LONG).show();
    }

    // btn_profile_setup.setImageURI(profile_img_uri);

}catch (Exception e) {
    Toast.makeText(SetupActivity.this, "Something went wrong"+e.getMessage(), Toast.LENGTH_LONG)
            .show();
}

    }
}
