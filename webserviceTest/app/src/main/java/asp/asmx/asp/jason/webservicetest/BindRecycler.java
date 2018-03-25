package asp.asmx.asp.jason.webservicetest;

/**
 * Created by User on 10/21/2017.
 */

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class BindRecycler
{
    public static final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";
    public static  final String SOAP_ADDRESS = "http://webservicebarter-001-site1.1tempurl.com/Webservice.asmx";

    //getSpecificUser
    public static final String SOAP_ACTION_getAllUser = "http://tempuri.org/getAllUsers_xml";
    public static final String OPERATION_NAME_getAllUser = "getAllUsers_xml";

    public static ArrayList<user> user_list = new ArrayList<user>();


    public BindRecycler()
    {
    }



    public static SoapObject getAllUser()
            throws XmlPullParserException, IOException{
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME_getAllUser);
       /* PropertyInfo pi=new PropertyInfo();
        pi.setName("User_name");
        pi.setValue(User_name);
        pi.setType(String.class);
        request.addProperty(pi);
*/
        /*
        pi=new PropertyInfo();
        pi.setName("b");
        pi.setValue(b);
        pi.setType(Integer.class);
        request.addProperty(pi);
        */

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        SoapObject response=null;
        try
        {

            httpTransport.call(SOAP_ACTION_getAllUser, envelope);

            response = (SoapObject) envelope.getResponse();
            StringBuffer result;
            result = new StringBuffer(response.toString());
            Log.d("Soap",result.toString());




        }
     catch (SoapFault e) {
    Log.e("Soap_Exception", "SoapFaultException");
    throw e;
} catch (HttpResponseException e) {
    Log.e("Soap_Exception", "HttpResponseException");
    throw e;
} catch (XmlPullParserException e) {
    Log.e("Soap_Exception", "XmlPullParserException");
    throw e;
} catch (IOException e) {
    Log.e("Soap_Exception", "IOException");
    throw e;
}
        catch (Exception exception)
        {
            Log.d("Soap_Exception",exception.toString());

        }
        return response;
    }






    public static void parsingGetSpecificUser(SoapObject response) {

        Log.d("sss_check", "count: " + response.getPropertyCount());

        for (int i = 0; i < response.getPropertyCount(); i++) {

            Log.d("sss_check", "start loop "+i);


            // add to list after all

            PropertyInfo pi = new PropertyInfo();
            response.getPropertyInfo(i, pi);
            Object property = response.getProperty(i);
            if (pi.name.equals("User") && property instanceof SoapObject) {
                SoapObject transDetail = (SoapObject) property;

                //getting object properties
                String user_name = transDetail.getPrimitivePropertyAsString("User_name");
                String user_email = transDetail.getPrimitivePropertyAsString("User_email");
                String user_pwd = transDetail.getPrimitivePropertyAsString("User_pwd");
                Log.d("sss_check", "user name " +user_name +" :"+i);


                Log.d("parse_xml_2", "get useremail: " + user_email);
                Log.d("parse_xml_2", "get userpwd: " + user_pwd);

                user entity = new user();

                entity.setUser_name(user_name);
                entity.setUser_email(user_email);
                entity.setUser_pwd(user_pwd);

                Log.d("sss_check", "count: " + entity.getUser_email());
                Log.d("sss_check", "zzzz");

                // add to list after all
                user_list.add(entity);
                Log.d("sss_check", "count: " + entity.getUser_email());

            }
        }


    }











}
