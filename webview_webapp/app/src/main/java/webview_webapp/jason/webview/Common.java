package webview_webapp.jason.webview;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by User on 11/22/2017.
 */

public class Common  {

    public static boolean connectionAvailable(Context context){

        ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo()!=null;
    }


}
