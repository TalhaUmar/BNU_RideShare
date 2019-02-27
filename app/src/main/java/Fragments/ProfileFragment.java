package Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.shjunaid.bnurideshare.R;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.concurrent.ExecutionException;

import API.ApiCallForStdId;
import API.IApiCaller;
import Models.AppConstants;
import Models.UserModel;

/**
 * Created by Sh Junaid on 12/28/2016.
 */
public class ProfileFragment extends Fragment {
    private TextView stdID,name,phone;
    private Button chngpswdbtn,chngphonbtn;
    SharedPreferences sharedPreferences;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_fragment,container,false);
        stdID = (TextView) v.findViewById(R.id.Student_Id1);
        name = (TextView) v.findViewById(R.id.Name_Id1);
        phone = (TextView) v.findViewById(R.id.Phone_Id1);
        chngpswdbtn = (Button)v.findViewById(R.id.change_pswd_button);
        chngphonbtn = (Button) v.findViewById(R.id.change_phonenum_btn);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(AppConstants.loginpref_name, Context.MODE_PRIVATE);
        String id = sharedPreferences.getString(AppConstants.key_stdempid, "");
        ApiCallForStdId apiCallForStdId = new ApiCallForStdId(getActivity(), new IApiCaller() {
            @Override
            public void onResult(String data) {
                Gson g = new Gson();
                UserModel user = g.fromJson(data, UserModel.class);
                stdID.setText(user.getStudent_EmployeeId());
                name.setText(user.getUserName());
                phone.setText(user.getPhone());
            }
        });
        apiCallForStdId.execute(id);


        chngphonbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePhoneFragment myDialog = new ChangePhoneFragment();
                myDialog.show(getFragmentManager(), "my_dialog");
            }
        });
        chngpswdbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePasswordFragment myDialog = new ChangePasswordFragment();
                myDialog.show(getFragmentManager(), "dialog_1");
            }
        });
    }
}
