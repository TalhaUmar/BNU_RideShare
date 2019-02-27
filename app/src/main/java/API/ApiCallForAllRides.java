package API;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import Models.AppConstants;

/**
 * Created by Talha on 1/6/2017.
 */
public class ApiCallForAllRides extends AsyncTask {
    private ProgressDialog prg;
    private Activity activity;
    public ApiCallForAllRides(Activity a)
    {
        this.activity = a;
        prg = new ProgressDialog(a);

    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        prg=ProgressDialog.show(activity, "Loading....", "please wait", true);
    }
    @Override
    protected String doInBackground(Object[] objects) {
        String link = AppConstants.RideCon+"SelectAllRides/";
        String result ="";
        try{
            URI uri = new URI(link);
            HttpClient client= new DefaultHttpClient();
            HttpGet get= new HttpGet();
            get.setURI(uri);
            HttpResponse httpResponse = client.execute(get);
            int scode = httpResponse.getStatusLine().getStatusCode();
            if (scode == 200) {
                result = new BasicResponseHandler().handleResponse(httpResponse);
            }
            else {
                result = "404";
                return result;
            }
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
        //super.onPostExecute(o);


        prg.dismiss();


    }
}
