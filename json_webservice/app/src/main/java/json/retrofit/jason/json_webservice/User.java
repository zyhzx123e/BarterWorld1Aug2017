package json.retrofit.jason.json_webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 11/1/2017.
 */

public class User {

    @SerializedName("user_name")
    @Expose
    private String user_name;

    @SerializedName("user_email")
    @Expose
    private String user_email;

    @SerializedName("user_pwd")
    @Expose
    private String user_pwd;

    public User(){}

    public User(String user_name,String user_email, String user_pwd){
        this.user_name=user_name;
        this.user_email=user_email;
        this.user_pwd=user_pwd;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_pwd() {
        return user_pwd;
    }

    public void setUser_pwd(String user_pwd) {
        this.user_pwd = user_pwd;
    }
}
