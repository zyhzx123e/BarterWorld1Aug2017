package json.retrofit.jason.json_webservice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText txtEmail;
    private EditText txtPwd;
    private Button btn_login;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtEmail=(EditText)findViewById(R.id.txt_email);
        txtPwd=(EditText)findViewById(R.id.txt_pwd);
        btn_login=(Button)findViewById(R.id.btn_login);



        final Retrofit retrofit = new Retrofit.Builder().baseUrl("http://webservicebarter-001-site1.1tempurl.com/Webservice.asmx/")
                .addConverterFactory(GsonConverterFactory.create()).build();



        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String uid = txtEmail.getText().toString();


                UserInterface api_interface_user =
                       retrofit.create(UserInterface.class);


                Call<List <User>> call = api_interface_user.getUser(uid);

                call.enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                        Toast.makeText(MainActivity.this, "response: "+response.toString()+  " raw: "+response, Toast.LENGTH_LONG).show();
                        if(response!=null && response.isSuccessful()) {
                            User user = response.body().get(0);


                            String get_pwd=user.getUser_pwd().toString();

                            if(get_pwd.equals(txtPwd.getText().toString().trim())){

                                Toast.makeText(MainActivity.this, "The retrived Email: "+user.getUser_email(), Toast.LENGTH_SHORT).show();

                            }else{

                                Toast.makeText(MainActivity.this, "Invalid Credential! ", Toast.LENGTH_SHORT).show();

                            }



                        }else{
                            Toast.makeText(MainActivity.this, "Failed...", Toast.LENGTH_SHORT).show();
                        }
                        }

                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {

                    }
                });






            }
        });




    }
}
