package Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shjunaid.bnurideshare.FirstActivity;
import com.example.shjunaid.bnurideshare.R;

import java.util.concurrent.ExecutionException;

import API.ForgotPasswordCall;
import Models.RegexCheck;

public class ForgotPasswordFragment extends Fragment {
    private TextView phone;
    private Button submit_btn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_forgotpassword, container, false);
        phone = (TextView) v.findViewById(R.id.forgotpswd_phone);
        submit_btn = (Button) v.findViewById(R.id.submit_button);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phn = phone.getText().toString();
                ForgotPasswordCall forgotPasswordCall = new ForgotPasswordCall();
                forgotPasswordCall.execute(phn);
                try {
                    String data = (String) forgotPasswordCall.get();
                    if (data.equals("404")){
                        Toast.makeText(getActivity(),"You are not registered member Signup to continue!",Toast.LENGTH_LONG).show();
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        SignupFragment sf = new SignupFragment();
                        ft.setCustomAnimations(R.anim.slidein_left, 0, R.anim.slidein_right,0);

                        ft.replace(R.id.login_fr, sf);
                        ft.addToBackStack("sf");
                        ft.commit();
                    }
                    else{
                        Intent i = new Intent(getActivity(), FirstActivity.class);
                        startActivity(i);
                        getActivity().finish();
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
