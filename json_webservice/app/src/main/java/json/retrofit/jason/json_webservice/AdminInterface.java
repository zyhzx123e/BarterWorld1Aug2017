package json.retrofit.jason.json_webservice;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by User on 11/8/2017.
 */

public interface AdminInterface {
    @GET("/Admin.json")
    Call<List<Admin>> getAdmin();
}
