package asp.asmx.asp.jason.webservicetest;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity   {


    public EditText txt_uid;
    public EditText txt_pwd;
    public Button btn_login;

    public static ArrayList<user> users_list;
    public static String user_pwd_get_from_web;

    public static  String retrived_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt_uid=(EditText)findViewById(R.id.txt_login_uid);
        txt_pwd=(EditText)findViewById(R.id.txt_login_pwd);
        btn_login=(Button) findViewById(R.id.btn_login_);

        users_list = new ArrayList<user>();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(txt_uid.getText().toString().trim())  && !TextUtils.isEmpty(txt_pwd.getText().toString().trim())){
                   final CallSoap cs=new CallSoap();


                    final String ustr_username=txt_uid.getText().toString().trim();
                    final String str_uid = txt_uid.getText().toString();


                        new AsyncTask<Void, Void, Void>() {
                            @Override public Void doInBackground(Void... arg) {


                                try{




                                SoapObject obj=new SoapObject();
                        obj=(SoapObject) cs.getSpecificUser(ustr_username);

                        cs.parsingGetSpecificUser(obj);

                                }catch (Exception e){

                                    Log.d("Asyntask...","error: "+e);
                                }

                         retrived_pwd = users_list.get(0).getUser_pwd().toString();

                                Log.d("Asyntask...","retrived pwd: "+retrived_pwd);
                                return null;

                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);
                                if(user_pwd_get_from_web!=null) {
                                    Toast.makeText(MainActivity.this, "retrived pwd for " + str_uid + ": " + user_pwd_get_from_web, Toast.LENGTH_SHORT).show();
                                    user_pwd_get_from_web=null;
                                }

                            }
                        }.execute();







                }else{
                    Toast.makeText(MainActivity.this, "Pls fill in all the information!", Toast.LENGTH_SHORT).show();

                }

            }
        });


    }


}
