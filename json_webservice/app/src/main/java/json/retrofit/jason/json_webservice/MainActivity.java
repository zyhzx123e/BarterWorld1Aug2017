package json.retrofit.jason.json_webservice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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


    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<User> user;
    private UserInterface userInterface;


    Retrofit retrofit = new Retrofit.Builder().baseUrl("http://webservicebarter-001-site1.1tempurl.com/Webservice.asmx/").addConverterFactory(GsonConverterFactory.create()).build();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView=(RecyclerView)findViewById(R.id.user_recyclerview);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

      //  Retrofit retrofit = new Retrofit.Builder().baseUrl("https://barterworld-ad75e.firebaseio.com/").addConverterFactory(GsonConverterFactory.create()).build();

        UserInterface api_interface_user = retrofit.create(UserInterface.class);



      //  final Retrofit retrofit = new Retrofit.Builder().baseUrl("http://webservicebarter-001-site1.1tempurl.com/Webservice.asmx/").addConverterFactory(GsonConverterFactory.create()).build();



        //https://barterworld-ad75e.firebaseio.com/Admin/.json




             //   UserInterface api_interface_user = retrofit.create(UserInterface.class);


                Call<List <User>> call = api_interface_user.getAllUsers();

                call.enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                        Toast.makeText(MainActivity.this, "response: "+response.toString()+  " raw: "+response, Toast.LENGTH_LONG).show();

                            user = response.body();
                            adapter=new RecyclerAdapter(user);
                            recyclerView.setAdapter(adapter);


                        if(response==null || !response.isSuccessful()) {

                            Toast.makeText(MainActivity.this, "Failed...", Toast.LENGTH_SHORT).show();
                        }
                        }

                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "on Failed...", Toast.LENGTH_SHORT).show();
                    }
                });






    }
}
