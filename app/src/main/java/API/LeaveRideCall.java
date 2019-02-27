package API;

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
 * Created by Talha on 1/13/2017.
 */
public class LeaveRideCall extends AsyncTask {
    @Override
    protected String doInBackground(Object[] objects) {
        int uid = (int) objects[0];
        int rid = (int) objects[1];
        String link = AppConstants.RideCon+"LeaveRide/?uid="+uid+"&rid="+rid;
        String result = "";
        try {
            URI uri = new URI(link);
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet();
            get.setURI(uri);
            HttpResponse httpResponse = client.execute(get);
            int scode = httpResponse.getStatusLine().getStatusCode();
            if (scode == 200) {
                result = "200";
                return result;
            } else {
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
        super.onPostExecute(o);
    }
}
