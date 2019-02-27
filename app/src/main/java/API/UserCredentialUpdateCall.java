package API;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import Models.AppConstants;

/**
 * Created by Talha on 1/2/2017.
 */
public class UserCredentialUpdateCall extends AsyncTask {
    @Override
    protected Object doInBackground(Object[] objects) {
        String Student_EmployeeId = (String) objects[0];
        int callID = (int) objects[1];
        String Phone = "",PhoneStatus = "";
        String Password = "";
        if (callID == 1){
            Phone = (String) objects[2];
            PhoneStatus = (String) objects[3];
        }
        else {
            Password = (String) objects[2];
        }
        String result ="";
        String link = AppConstants.UsersCon+"UpdateUserCredential/";
        try{
            URI uri = new URI(link);
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost();
            httpPost.setURI(uri);
            String json = "";
            JSONObject param = new JSONObject();
            try {
                param.put("Student_EmployeeId",Student_EmployeeId);
                if (callID == 1){
                    param.put("Phone",Phone);
                    param.put("PhoneStatus",PhoneStatus);
                }
                else {
                    param.put("Password",Password);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            json = param.toString();
            StringEntity se = new StringEntity(json);
            httpPost.setEntity(se);
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("Accept", "application/json");
            HttpResponse httpResponse = httpclient.execute(httpPost);
            int scode = httpResponse.getStatusLine().getStatusCode();
            if (scode == 200) {
                result = "200";
                return result;
            }
            else {
                result = "404";
                return result;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }
}
