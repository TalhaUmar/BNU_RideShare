package API;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.google.gson.Gson;

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
import Models.RideModel;


public class ApiCallForRidePost extends AsyncTask {
    private ProgressDialog progressDialog;
    private Activity act;
    public IApiCaller delegate = null;
    public ApiCallForRidePost(Activity activity, IApiCaller delegate){
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
        RideModel rideModel = (RideModel) objects[0];

        String result ="";
        String link = AppConstants.RidePost;
        try{
            URI uri = new URI(link);
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost();
            httpPost.setURI(uri);
            Gson gson = new Gson();
            String json = gson.toJson(rideModel);
            StringEntity se = new StringEntity(json);
            httpPost.setEntity(se);
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("Accept", "application/json");
            HttpResponse httpResponse = httpclient.execute(httpPost);
            int scode = httpResponse.getStatusLine().getStatusCode();
            if (scode == 200) {
                result = "200";
            }
            else {
                result = "404";
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
