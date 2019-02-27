package Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shjunaid.bnurideshare.FirstActivity;
import com.example.shjunaid.bnurideshare.HomeActivity;
import com.example.shjunaid.bnurideshare.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import java.util.concurrent.ExecutionException;

import API.IApiCaller;
import API.apiCaller;
import Models.AppConstants;
import Models.UserModel;


public class LoginFragment extends Fragment {
    private Button btn;
    private EditText STId,Password;
    private TextView chk,chk1,chk2;
    SharedPreferences sharedPreferences;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.loginfragment,container,false);
        btn = (Button)v.findViewById(R.id.login_button);
        STId = (EditText)v.findViewById(R.id.Student_Id);
        Password = (EditText)v.findViewById(R.id.Password);
        chk = (TextView)v.findViewById(R.id.signup_here);
        chk1 = (TextView)v.findViewById(R.id.chkapi);
        chk2 = (TextView)v.findViewById(R.id.forgotpswd);

        return v;
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        this.con = this.getActivity();
//    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                SignupFragment sf = new SignupFragment();
                ft.setCustomAnimations(R.anim.slidein_left, 0, R.anim.slidein_right,0);
                ft.replace(R.id.login_fr, sf);
                ft.addToBackStack("sf");
                ft.commit();
            }
        });
        chk2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ForgotPasswordFragment fpf = new ForgotPasswordFragment();
                ft.setCustomAnimations(R.anim.slidein_left, 0, R.anim.slidein_right,0);
                ft.replace(R.id.login_fr, fpf);
                ft.addToBackStack("fpf");
                ft.commit();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                //Log.d("FireBase Instance ID", "Refreshed token: " + refreshedToken);

                apiCaller caller = new apiCaller(getActivity(), new IApiCaller() {
                    @Override
                    public void onResult(String data) {
                        if(data.equals("404")){
                            STId.setError("invalid student id/password");
                            Password.setError("invalid student id/password");
                            Toast.makeText(getActivity(), "Invalid username or password", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Gson g = new Gson();
                            UserModel user = g.fromJson(data, UserModel.class);
                            sharedPreferences = getActivity().getSharedPreferences(AppConstants.loginpref_name, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString(AppConstants.key_stdempid,user.getStudent_EmployeeId());
                            editor.putString(AppConstants.key_password,user.getPassword());
                            editor.putInt(AppConstants.key_userid, user.getId());
                            editor.putString(AppConstants.key_username, user.getUserName());
                            editor.commit();

                            Toast.makeText(getActivity(), "Welcome "+user.getUserName().toString(), Toast.LENGTH_LONG).show();
                            Intent i = new Intent(getActivity(), HomeActivity.class );
                            startActivity(i);
                            getActivity().finish();
                        }
                    }
                });
                String id = STId.getText().toString();
                String password = Password.getText().toString();

                caller.execute(id, password, refreshedToken);
//                try {
//                    String data = (String)caller.get();
//
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                }


            }
        });

    }
}
