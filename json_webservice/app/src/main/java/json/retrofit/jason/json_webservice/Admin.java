package json.retrofit.jason.json_webservice;

import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 11/8/2017.
 */

public class Admin {
    @SerializedName("adminid")
    private String adminid;

    @SerializedName("adminpwd")
    private String adminpwd;

    public Admin(){}

    public Admin(String adminid,String adminpwd){
        this.adminid=adminid;
        this.adminpwd=adminpwd;
    }

    public String getAdminid() {
        return adminid;
    }

    public void setAdminid(String adminid) {
        this.adminid = adminid;
    }

    public String getAdminpwd() {
        return adminpwd;
    }

    public void setAdminpwd(String adminpwd) {
        this.adminpwd = adminpwd;
    }
}
