package com.example.jason.barterworld;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {


    private TextView txt_username;
    private TextView txt_password;
    private Button btn_login;
    private Button btn_register;

    private FirebaseAuth user_login_auth;
    private DatabaseReference fire_db;

    private DatabaseReference fire_db_user;
    private ProgressDialog progress_bar;


    private SignInButton googleBtn;

    private static final int RC_SIGN_IN =1;

    private static final String TAG = "LOGIN_ACTIVITY";

    private GoogleApiClient mGoogleApiClient;


    private FirebaseAuth.AuthStateListener login_auth_listener;

    protected void hideSoftKeyboard(EditText input) {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(LoginActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

 /*       RelativeLayout rLayout = (RelativeLayout)this.findViewById (R.id.activity_login);
        Resources res = getResources(); //resource handle
        Drawable drawable_bg = res.getDrawable(R.mipmap.ic_launcher); //new Image that was added to the res folder

        rLayout.setBackground(drawable_bg);
*/

     //   user_login_auth.addAuthStateListener(login_auth_listener);
        login_auth_listener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() != null){

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }

            }
        };

        googleBtn =(SignInButton)findViewById(R.id.googleBtn);

        progress_bar = new ProgressDialog(this);

        fire_db=FirebaseDatabase.getInstance().getReference().child("Barter_post");

        fire_db.keepSynced(true);

        fire_db_user = FirebaseDatabase.getInstance().getReference().child("User");
        fire_db_user.keepSynced(true);

user_login_auth = FirebaseAuth.getInstance();

        txt_username=(EditText)findViewById(R.id.txt_email);
        txt_password=(EditText)findViewById(R.id.txt_pwd);
        btn_login = (Button)findViewById(R.id.btn_login);
        btn_register = (Button)findViewById(R.id.btn_register_setup);



        txt_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        txt_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkLogin();
            }
        });


        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signIn();
            }
        });



        btn_register.setOnClickListener(new View.OnClickListener(){

            @Override
            public  void onClick(View v){



                Intent register_activity = new Intent(LoginActivity.this,RegisterActivity.class);
                register_activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(register_activity);

            }

        });


        //google sign in
        // Configure Google Sign In
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

    }

    @Override
    protected void onStart() {
        super.onStart();
        user_login_auth.addAuthStateListener(login_auth_listener);



    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }




    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        user_login_auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        else{



                            progress_bar.dismiss();

                            check_user_exist();
                        }

                        // ...
                    }
                });
    }



    //google sign in result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

           progress_bar.setMessage("Google is signing in your account...");
           progress_bar.show();

            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                progress_bar.dismiss();
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
                progress_bar.dismiss();
                Toast.makeText(getApplicationContext(),"Error occurs while connecting to Google Account-"+result.getStatus()+"",Toast.LENGTH_SHORT).show();
            }
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


    public void checkLogin(){

        String username = this.txt_username.getText().toString().trim();
        String password = this.txt_password.getText().toString().trim();

        progress_bar.setMessage("We are checking your credential... please be patient...");

        if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)){
            progress_bar.show();
            user_login_auth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

check_user_exist();
                        progress_bar.dismiss();

                    }else{

                        dialogBox("Something went wrong, try again, if problem exists ");
                        progress_bar.dismiss();
                    }
                }
            });


        }else{

            dialogBox("Sorry! Please fill in both username and password!");
        }


    }



    public void check_user_exist(){



        if(user_login_auth.getCurrentUser()!=null) {

            final String uid = user_login_auth.getCurrentUser().getUid().toString();

            fire_db_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.hasChild(uid)) {
                        Intent mainintent = new Intent(LoginActivity.this, MainActivity.class);
                        mainintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainintent);
//bring legit user to main activity

                    }else{

                        Toast.makeText(getApplicationContext(),dataSnapshot.child("uid").toString(),Toast.LENGTH_LONG).show();
                        Log.d("myTag", "test login  message");

                        Intent setup_intent = new Intent(LoginActivity.this, SetupActivity.class);
                        setup_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(setup_intent);


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

}
