package asp.asmx.asp.jason.webservicetest;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    public static ArrayList<user> user;
    private UserInterface userInterface;


    Retrofit retrofit = new Retrofit.Builder().client(new OkHttpClient()).baseUrl("http://webservicebarter-001-site1.1tempurl.com/Webservice.asmx/").addConverterFactory(SimpleXmlConverterFactory.create()).build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);



        recyclerView=(RecyclerView)findViewById(R.id.user_recyclerview);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);







        new AsyncTask<Void, Void, Void>() {
            @Override public Void doInBackground(Void... arg) {


                try{

                    BindRecycler.parsingGetSpecificUser(BindRecycler.getAllUser());

                }catch (Exception e){

                    Log.d("Asyntask...","error: "+e);
                }



                return null;

            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                user=BindRecycler.user_list;
                adapter=new RecyclerAdapter(user);
                recyclerView.setAdapter(adapter);

            }
        }.execute();














     /*   UserInterface api_interface_user = retrofit.create(UserInterface.class);


        Call<ArrayList<user>> call = api_interface_user.getAllUsers();


        call.enqueue(new Callback<ArrayList<user>>() {
            @Override
            public void onResponse(Call<ArrayList<user>> call, Response<ArrayList<user>> response) {
                Toast.makeText(ListActivity.this, "response: "+response.toString()+  " raw: "+response, Toast.LENGTH_LONG).show();

                user = response.body();
                adapter=new RecyclerAdapter(user);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ArrayList<user>> call, Throwable t) {

            }
        });

        */
    }
}
