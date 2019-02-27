package API;

import android.app.Activity;
import android.app.ProgressDialog;
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
 * Created by Talha on 12/15/2016.
 */
public class ApiCallForStdId extends AsyncTask {
    private ProgressDialog progressDialog;
    private Activity act;
    public IApiCaller delegate = null;
    public ApiCallForStdId(Activity activity, IApiCaller delegate){
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
    protected String doInBackground(Object[] params) {
        String id = (String) params[0];
        String link = AppConstants.UsersCon+"StdEmpId/?id="+id;
        String result ="";
        try{
            URI uri = new URI(link);
            HttpClient client= new DefaultHttpClient();
            HttpGet get= new HttpGet();
            get.setURI(uri);
            HttpResponse httpResponse = client.execute(get);
            int scode = httpResponse.getStatusLine().getStatusCode();
            if (scode == 200) {
                //result = "200";
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
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        this.delegate.onResult((String)result);
        progressDialog.dismiss();
    }
}
