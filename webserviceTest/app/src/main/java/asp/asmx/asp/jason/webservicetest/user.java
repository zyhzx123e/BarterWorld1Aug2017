package asp.asmx.asp.jason.webservicetest;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by User on 10/21/2017.
 */

@Root(name = "User")
public class user {

    @Element(name = "User_name")
    private String User_name;
    @Element(name = "User_email")
    private String User_email;
    @Element(name = "User_pwd")
    private String User_pwd;

    public user(){}
    public user(String User_name,String User_email, String User_pwd){
        this.User_name=User_name;
        this.User_email=User_email;
        this.User_pwd=User_pwd;
    }


    public String getUser_name() {
        return User_name;
    }

    public void setUser_name(String user_name) {
        User_name = user_name;
    }

    public String getUser_email() {
        return User_email;
    }

    public void setUser_email(String user_email) {
        User_email = user_email;
    }

    public String getUser_pwd() {
        return User_pwd;
    }

    public void setUser_pwd(String user_pwd) {
        User_pwd = user_pwd;
    }
}
