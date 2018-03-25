package asp.asmx.asp.jason.webservicetest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by User on 11/1/2017.
 */

public interface UserInterface {



        /*
        *
        *

If you specify @GET("foobar?a=5"), then any @Query("b") must be appended using &, producing something like foobar?a=5&b=7.

If you specify @GET("foobar"), then the first @Query must be appended using ?, producing something like foobar?b=7.

That's how Retrofit works.

When you specify @GET("foobar?"), Retrofit thinks you already gave some query parameter, and appends more query parameters using &.

Remove the ? will get the desired result.
        * */

        @GET("/getAllUsers_xml")
        Call<ArrayList<user>> getAllUsers();

}
