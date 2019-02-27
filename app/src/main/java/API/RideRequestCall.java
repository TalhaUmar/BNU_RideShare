package API;

import android.os.AsyncTask;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import Models.AppConstants;
import Models.RideRequestModel;

/**
 * Created by Talha on 1/6/2017.
 */
public class RideRequestCall extends AsyncTask {
    @Override
    protected String doInBackground(Object[] objects) {
        RideRequestModel requestModel = (RideRequestModel) objects[0];
        String link = AppConstants.RideCon+"RideRequestNotify/";
        String result = "";
        try{
            URI uri = new URI(link);
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost();
            httpPost.setURI(uri);
            Gson gson = new Gson();
            String json = gson.toJson(requestModel);
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
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }
}
