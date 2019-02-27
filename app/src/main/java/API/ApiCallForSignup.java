package API;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import Models.AppConstants;


public class ApiCallForSignup extends AsyncTask {
    private ProgressDialog progressDialog;
    private Activity act;
    public IApiCaller delegate = null;
    public ApiCallForSignup(Activity activity, IApiCaller delegate){
        act = activity;
        progressDialog = new ProgressDialog(activity);
        this.delegate = delegate;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog = ProgressDialog.show(act, "loading...", "please wait...", true);
    }

    @Override
    protected String doInBackground(Object[] objects) {
        String Student_EmployeeId = (String) objects[0];
        String UserName = (String) objects[1];
        String Phone = (String) objects[2];
        String PhoneStatus = (String) objects[3];
        String Password = (String) objects[4];
        String Token = (String) objects[5];
        String result ="";
        String link = AppConstants.UsersCon+"UserInsert/";
        try{
            URI uri = new URI(link);
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost();
            httpPost.setURI(uri);
            String json = "";
            JSONObject param = new JSONObject();
            try {
                param.put("Student_EmployeeId",Student_EmployeeId);
                param.put("UserName",UserName);
                param.put("Phone",Phone);
                param.put("PhoneStatus",PhoneStatus);
                param.put("Password",Password);
                param.put("Token",Token);
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
                //result = "200";
                result = new BasicResponseHandler().handleResponse(httpResponse);
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
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        this.delegate.onResult((String)result);
        progressDialog.dismiss();
    }
}
