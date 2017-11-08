package com.example.jason.barterworld;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.net.URI;
import java.util.UUID;

public class ProfileActivity extends AppCompatActivity {


    private ImageButton btn_user_profile;

    private EditText txt_username;
    private EditText txt_email;
    private EditText txt_pwd;
    private EditText txt_confirm_pwd;

    private String str_username,str_email,str_pwd,str_user_img;

    private Button btn_editInfo;
    private Button btn_saveInfo;

    private static final int GALLERY_REQUEST_CODE=1;
    public String user_uid="";
    public String display_uid="";

    private FirebaseAuth user_auth;
    private DatabaseReference databaseReference_user;
    private FirebaseUser currentUser;



    private Uri profile_img_uri= null;
    private StorageReference user_profile_storage;

    private ProgressDialog progress_dialog;


    public String random_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_backbtn);

        user_auth = FirebaseAuth.getInstance();
        currentUser = user_auth.getCurrentUser();
        //user_uid = user_auth.getInstance().getCurrentUser().getUid();
        user_uid = currentUser.getUid();

        progress_dialog = new ProgressDialog(this);
        databaseReference_user = FirebaseDatabase.getInstance().getReference().child("User").child(currentUser.getUid());
        user_profile_storage=FirebaseStorage.getInstance().getReference();

        txt_username = (EditText)findViewById(R.id.txt_post_name);
        txt_email = (EditText)findViewById(R.id.txt_email);
        txt_pwd = (EditText)findViewById(R.id.txt_pwd);
        txt_confirm_pwd = (EditText)findViewById(R.id.txt_pwd2);


        //event btns
        btn_editInfo=(Button)findViewById(R.id.btn_edit_info);
        btn_saveInfo=(Button)findViewById(R.id.btn_save_info);


        btn_user_profile = (ImageButton)findViewById(R.id.btn_img_profile);






        databaseReference_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                str_username=dataSnapshot.child("user_name").getValue(String.class);
                txt_username.setText(str_username);
                str_email=dataSnapshot.child("user_email").getValue(String.class);
                txt_email.setText(str_email);
                str_pwd=dataSnapshot.child("user_pwd").getValue(String.class);
                txt_pwd.setText(str_pwd);

                str_user_img=dataSnapshot.child("user_img").getValue(String.class);
                set_user_profile_pic(getApplicationContext(),str_user_img);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        // btn_user_profile.setEnabled(false);
        txt_username.setEnabled(false);
        txt_email.setEnabled(false);
        txt_pwd.setEnabled(false);
        txt_confirm_pwd.setEnabled(false);
        txt_confirm_pwd.setVisibility(View.INVISIBLE);
        btn_saveInfo.setVisibility(View.INVISIBLE);


        btn_user_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(galleryIntent.createChooser(galleryIntent, "Select Picture"), GALLERY_REQUEST_CODE);


            }
        });

        btn_editInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_user_profile.setEnabled(true);
                txt_username.setEnabled(true);

                txt_pwd.setEnabled(true);
                txt_confirm_pwd.setEnabled(true);
                txt_confirm_pwd.setVisibility(View.VISIBLE);
                btn_saveInfo.setVisibility(View.VISIBLE);
            }
        });



        btn_saveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {






                String uuid = UUID.randomUUID().toString();


               progress_dialog.setMessage("Please wait , we are saving your account...");

                if((txt_pwd.getText().toString().trim()).equals(txt_confirm_pwd.getText().toString().trim())   ) {




                    progress_dialog.show();

                    if(profile_img_uri!=null) {

                        StorageReference filePath = user_profile_storage.child("User_Images").child(profile_img_uri.getLastPathSegment() + "_" + uuid + "_.png");

                        filePath.putFile(profile_img_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                @SuppressWarnings("VisibleForTests") String download_uri = taskSnapshot.getDownloadUrl().toString();


                                    databaseReference_user.child("user_name").setValue(txt_username.getText().toString().trim());

                                    databaseReference_user.child("user_pwd").setValue(txt_pwd.getText().toString().trim());
                                    databaseReference_user.child("user_img").setValue(download_uri);



                                final String txt_email_send = txt_email.getText().toString().trim();
                                final String txt_email_receiver = txt_username.getText().toString().trim();
                                final String txt_user_new_pwd = txt_pwd.getText().toString().trim();
//-------------------------send email
                                new AsyncTask<Void, Void, Void>() {
                                    @Override
                                    public Void doInBackground(Void... arg) {

                                        Mail m = new Mail("barterworld2017@gmail.com", "zyhzx123e");

                                        String[] toArr = {txt_email_send};
                                        m.setTo(toArr);
                                        m.setFrom("barterworld2017@gmail.com");

                                        m.setSubject("Barter World");
                                        // m.setBody("<h3>Dear "+txt_email_receiver+":</h3><br/><br/><h3> You have just changed your password. Your new password : "+txt_pwd.getText().toString().trim()+"</h3><<br/><br/><br/><br/><h4>Student : Zhang Yu Hao (TP037390) | UC3F1706IT(MBT)</h4>");
                                        m.setBody("Dear " + txt_email_receiver + " :   You have just changed your password. Your new password : <" + txt_user_new_pwd + ">   ---------------------------------------------    Student : Zhang Yu Hao (TP037390) | UC3F1706IT(MBT)</h4>");

                                        try {
                                            // m.addAttachment("/sdcard/filelocation");

                                            if (m.send()) {
                                                Log.v("Mail has sent!", "Sent");
                                            } else {
                                                Log.v("Mail failed to send", "Failed");
                                            }
                                        } catch (Exception e) {
                                            //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
                                            Log.e("Mail ", "Could not send email, error!", e);
                                        }


                                        return null;
                                    }
                                }.execute();
//-------------------------sned email


                                progress_dialog.dismiss();
                                txt_username.setEnabled(false);
                                txt_email.setEnabled(false);
                                txt_pwd.setEnabled(false);
                                txt_confirm_pwd.setEnabled(false);
                                txt_confirm_pwd.setVisibility(View.INVISIBLE);
                                btn_saveInfo.setVisibility(View.INVISIBLE);

                                Intent main_activity = new Intent(ProfileActivity.this, MainActivity.class);
                                main_activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(main_activity);

                            }
                        });


                    }else{


                                databaseReference_user.child("user_name").setValue(txt_username.getText().toString().trim());

                                databaseReference_user.child("user_pwd").setValue(txt_pwd.getText().toString().trim());

                                final String txt_email_send = txt_email.getText().toString().trim();
                                final String txt_email_receiver = txt_username.getText().toString().trim();
                                final String txt_user_new_pwd = txt_pwd.getText().toString().trim();



                        PassResetViaEmail(txt_email.getText().toString().trim());

//-------------------------send email
                                new AsyncTask<Void, Void, Void>() {
                                    @Override
                                    public Void doInBackground(Void... arg) {

                                        Mail m = new Mail("barterworld2017@gmail.com", "zyhzx123e");

                                        String[] toArr = {txt_email_send};
                                        m.setTo(toArr);
                                        m.setFrom("barterworld2017@gmail.com");

                                        m.setSubject("Barter World");
                                        // m.setBody("<h3>Dear "+txt_email_receiver+":</h3><br/><br/><h3> You have just changed your password. Your new password : "+txt_pwd.getText().toString().trim()+"</h3><<br/><br/><br/><br/><h4>Student : Zhang Yu Hao (TP037390) | UC3F1706IT(MBT)</h4>");
                                        m.setBody("Dear " + txt_email_receiver + " :   You have just changed your password. Your new password : <" + txt_user_new_pwd + ">  please set this password again to the <noreply> Email link we have sent you in order to secure your account! Orelse the password will not works! ---------------------------------------------    Student : Zhang Yu Hao (TP037390) | UC3F1706IT(MBT) Project <Barter World>");

                                        try {
                                            // m.addAttachment("/sdcard/filelocation");

                                            if (m.send()) {
                                                Log.v("Mail has sent!", "Sent");
                                            } else {
                                                Log.v("Mail failed to send", "Failed");
                                            }
                                        } catch (Exception e) {
                                            //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
                                            Log.e("Mail ", "Could not send email, error!", e);
                                        }


                                        return null;
                                    }
                                }.execute();
//-------------------------sned email


                                progress_dialog.dismiss();
                                txt_username.setEnabled(false);
                                txt_email.setEnabled(false);
                                txt_pwd.setEnabled(false);
                                txt_confirm_pwd.setEnabled(false);
                                txt_confirm_pwd.setVisibility(View.INVISIBLE);
                                btn_saveInfo.setVisibility(View.INVISIBLE);

                        Toast.makeText(getApplicationContext(), "Please check your Email to further complete the account information change !", Toast.LENGTH_LONG).show();



                        Intent main_activity = new Intent(ProfileActivity.this, MainActivity.class);
                                main_activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(main_activity);



                    }



                }else{
                    Toast.makeText(ProfileActivity.this, "Make Sure both of your password are matched! ", Toast.LENGTH_SHORT).show();
                }







            }
        });


    }



    /*
    * edittext1.setKeyListener(null);
edittext1.setCursorVisible(false);
edittext1.setPressed(false);
edittext1.setFocusable(false);
    * */

    public void set_user_profile_pic(final Context ctx, final String img){

        final ImageButton user_profile_img = (ImageButton) findViewById(R.id.btn_img_profile);
        //Picasso.with(ctx).load(img).into(post_img);

        Picasso.with(ctx).load(img).fit()
                .placeholder(R.drawable.ic_media_play)
                .transform(new RoundedTransformation(85, 4))
                .into(user_profile_img, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                        Picasso.with(ctx).load(img).fit()
                                .placeholder(R.drawable.ic_media_play)
                                .transform(new RoundedTransformation(85, 4))
                                .into(user_profile_img);
                    }
                });


    }











    private void PassResetViaEmail(String email_send){
        if(user_auth != null) {
            Log.w(" if Email authenticated", "Recovery Email has been  sent to " + email_send);
            user_auth.sendPasswordResetEmail(email_send);
        } else {
            Log.w(" error ", " bad entry ");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri1 = Uri.parse(str_user_img);

        try {

            if (requestCode == GALLERY_REQUEST_CODE && resultCode == SetupActivity.RESULT_OK) {

                Uri uri = data.getData();

                CropImage.activity(uri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this);


            }else{

                profile_img_uri=uri1;

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
                    btn_user_profile.setImageURI(null);

                    btn_user_profile.setImageURI(profile_img_uri);


                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(ProfileActivity.this.getContentResolver(), profile_img_uri);
                    btn_user_profile.setImageBitmap(bitmap);
                    //  btn_profile_setup.setImageURI(profile_img_uri);

                }   if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                    Exception error = result.getError();


                }

            } else {
                Toast.makeText(ProfileActivity.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
                profile_img_uri=uri1;
            }

            // btn_profile_setup.setImageURI(profile_img_uri);

        }catch (Exception e) {
            Toast.makeText(ProfileActivity.this, "Something went wrong"+e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }

    }
}
