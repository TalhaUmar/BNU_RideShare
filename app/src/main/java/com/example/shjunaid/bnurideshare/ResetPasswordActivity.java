package com.example.shjunaid.bnurideshare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.concurrent.ExecutionException;

import API.UserCredentialUpdateCall;
import Models.AppConstants;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText stdIdforresetpswd,resetpswd,resetpswdconfirm;
    private Button submitresetpswd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        init();
    }

    private void init() {
        resetpswd = (EditText)  findViewById(R.id.reset_password);
        resetpswdconfirm = (EditText)  findViewById(R.id.reset_password_confirm);
        stdIdforresetpswd = (EditText)  findViewById(R.id.stdId_for_resetpswd);
        submitresetpswd = (Button) findViewById(R.id.submit_resetpassword_button);
        submitresetpswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int callId = 2;
                String stdid = stdIdforresetpswd.getText().toString();
                String password = resetpswd.getText().toString();
                String ConfirmPswd = resetpswdconfirm.getText().toString();
                if (!ConfirmPswd.equals(password)) {
                    resetpswdconfirm.setError("Password does not match");
                }
                UserCredentialUpdateCall userCredentialUpdateCall = new UserCredentialUpdateCall();
                userCredentialUpdateCall.execute(stdid,callId,password);
                try {
                    String data = (String) userCredentialUpdateCall.get();
                    if(data.equals("200")){
                        Intent i = new Intent(ResetPasswordActivity.this,FirstActivity.class);
                        startActivity(i);
                        finish();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }


            }
        });
    }
}
