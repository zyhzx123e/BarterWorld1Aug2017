package asp.asmx.asp.jason.webservicetest;

/**
 * Created by User on 10/21/2017.
 */

import android.os.AsyncTask;
import android.os.Debug;
import android.renderscript.RenderScript;
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

public class CallSoap
{
    public  final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";
    public  final String SOAP_ADDRESS = "http://webservicebarter-001-site1.1tempurl.com/Webservice.asmx";

    //getSpecificUser
    public final String SOAP_ACTION_getSpecificUser = "http://tempuri.org/getSpecificUser_xml";
    public  final String OPERATION_NAME_getSpecificUser = "getSpecificUser_xml";



    public CallSoap()
    {
    }



    public SoapObject getSpecificUser(String User_name)
            throws XmlPullParserException, IOException{
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME_getSpecificUser);
        PropertyInfo pi=new PropertyInfo();
        pi.setName("User_name");
        pi.setValue(User_name);
        pi.setType(String.class);
        request.addProperty(pi);

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

            httpTransport.call(SOAP_ACTION_getSpecificUser, envelope);

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






    public void parsingGetSpecificUser(SoapObject response) {
        MainActivity.users_list.clear();
        for (int i = 0; i < response.getPropertyCount(); i++) {

            PropertyInfo pi = new PropertyInfo();
            response.getPropertyInfo(i, pi);
            Object property = response.getProperty(i);
            if (pi.name.equals("User") && property instanceof SoapObject) {
                SoapObject transDetail = (SoapObject) property;

                //getting object properties
                String user_email = transDetail.getPrimitivePropertyAsString("User_email");
                String user_pwd = transDetail.getPrimitivePropertyAsString("User_pwd");

                Log.d("parse_xml", "get useremail: " + user_email);
                Log.d("parse_xml", "get userpwd: " + user_pwd);
                MainActivity.user_pwd_get_from_web=user_pwd;

                user entity = new user();
                entity.setUser_email(user_email);
                entity.setUser_pwd(user_pwd);

                // add to list after all
                MainActivity.users_list.add(entity);
            }
        }


    }











}
