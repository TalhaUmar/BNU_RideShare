package Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shjunaid.bnurideshare.HomeActivity;
import com.example.shjunaid.bnurideshare.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import java.util.concurrent.ExecutionException;

import API.ApiCallForSignup;
import API.ApiCallForStdId;
import API.IApiCaller;
import Models.AppConstants;
import Models.RegexCheck;
import Models.UserModel;


public class SignupFragment extends Fragment {

    private EditText stdEmpId,name,phone,password,confirmPassword;
    private Switch phoneStatus;
    private Button signup;
    private TextView phoneStatus_txt;
    private String Std_EmpId="";
    private UserModel userModelObj;
    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.signup, container, false);
        stdEmpId = (EditText) v.findViewById(R.id.Student_Id);
        name = (EditText) v.findViewById(R.id.Name_Id);
        phone = (EditText) v.findViewById(R.id.Phone_Id);
        phoneStatus = (Switch) v.findViewById(R.id.phoneStatus_id);
        password = (EditText) v.findViewById(R.id.Password);
        confirmPassword = (EditText) v.findViewById(R.id.Confirm_Password);
        phoneStatus_txt = (TextView) v.findViewById(R.id.Switch_txt);
        signup = (Button) v.findViewById(R.id.signup_button);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        StdEmpIdCheck();
        phoneStatus.setChecked(false);
        phoneStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    phoneStatus_txt.setText("Private");
                }

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Name = name.getText().toString();
                String Phone = phone.getText().toString();
                String PhoneStatus = "0";
                String Password = password.getText().toString();
                String ConfirmPswd = confirmPassword.getText().toString();
                if (!ConfirmPswd.equals(Password)) {
                    confirmPassword.setError("Password does not match");
                }
                if (!RegexCheck.UserNameCheck(Name)) {
                    name.setError("Enter valid name");
                }
                if (!RegexCheck.PhoneCheck(Phone)) {
                    phone.setError("Enter valid phone#");
                }
                if (phoneStatus.isChecked()) {
                    PhoneStatus = "1";
                }
                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                ApiCallForSignup apiCallForSignup = new ApiCallForSignup(getActivity(), new IApiCaller() {
                    @Override
                    public void onResult(String data) {
                        if (!data.equals("404")) {
                            Gson g = new Gson();
                            UserModel user = g.fromJson(data, UserModel.class);
                            sharedPreferences = getActivity().getSharedPreferences(AppConstants.loginpref_name, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(AppConstants.key_stdempid, user.getStudent_EmployeeId());
                            editor.putString(AppConstants.key_password, user.getPassword());
                            editor.putInt(AppConstants.key_userid, user.getId());
                            editor.putString(AppConstants.key_username, user.getUserName());
                            editor.commit();
                            Toast.makeText(getActivity(), Name + ", you are registered member now.", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(getActivity(), HomeActivity.class);
                            startActivity(i);
                            getActivity().finish();
                        }
                    }
                });
                apiCallForSignup.execute(Std_EmpId, Name, Phone, PhoneStatus, Password, refreshedToken);


            }
        });


    }

    private void StdEmpIdCheck() {
        stdEmpId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Std_EmpId = stdEmpId.getText().toString();
                if (RegexCheck.StdEmpIdCheck(Std_EmpId)) {
                    ApiCallForStdId apicall = new ApiCallForStdId(getActivity(), new IApiCaller() {
                        @Override
                        public void onResult(String data) {
                            if (data == "404") {
                                stdEmpId.setError("Not registered in BNU");
                            }
                        }
                    });
                    apicall.execute(Std_EmpId);

                } else {
                    stdEmpId.setError("Invalid Id");
                }
            }
        });
    }

}
