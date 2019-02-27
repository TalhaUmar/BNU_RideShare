package API;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shjunaid.bnurideshare.FirstActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

import Fragments.LoginFragment;
import Models.AppConstants;
import Models.UserModel;


/**
 * Created by Sh Junaid on 12/2/2016.
 */
public class apiCaller extends AsyncTask {
    private ProgressDialog progressDialog;
    private Activity act;
    public IApiCaller delegate = null;
    public apiCaller(Activity activity, IApiCaller delegate){
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

        //String result = "Hello world";

        String id = (String) objects[0];
        String pass = (String) objects[1];
        String token = (String) objects[2];
        String link = AppConstants.UsersCon+"Login/?id="+id+"&pass="+pass+"&token="+token;
//        for(int i=0;i<1000000;i++){
//
//        }

        String result ="";
        try {
            URI uri = new URI(link);
            HttpClient client= new DefaultHttpClient();
            HttpGet get= new HttpGet();
            get.setURI(uri);
            HttpResponse httpResponse = client.execute(get);
            int scode = httpResponse.getStatusLine().getStatusCode();
            if (scode == 200) {
                result = new BasicResponseHandler().handleResponse(httpResponse);
                return result;
            }
            else {
                result = "404";

            }



        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }





        return result;

        /*String data = result;

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson g = gsonBuilder.create();
        UserModel userModel = g.fromJson(data, UserModel.class);


        return data;
        /*Gson gson;
        gson = new Gson();

        Type t = new TypeToken<UserModel>(){}.getType();
        UserModel  user = (UserModel)gson.fromJson(data,t);
        if(data.equals("user not found")){
            return data;
        }
        else{
            //UserModel userModel = gson.fromJson(data,UserModel.class);
            return data;
        }*/



    }

    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        this.delegate.onResult((String)result);
        progressDialog.dismiss();
    }


}
