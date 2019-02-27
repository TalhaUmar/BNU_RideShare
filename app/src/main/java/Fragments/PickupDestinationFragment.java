package Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.shjunaid.bnurideshare.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

/**
 * Created by Sh Junaid on 1/7/2017.
 */
public class PickupDestinationFragment extends Fragment {
    private EditText pick,Dest;
    private Button Go;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.pickup_destination,container,false);
        Go = (Button)v.findViewById(R.id.go_btn);
        pick = (EditText)v.findViewById(R.id.pickup_location);
        Dest = (EditText)v.findViewById(R.id.destination_location);

        return  v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LatLng a = new LatLng(31.392003, 74.287901);
        LatLng b = new LatLng(31.600712, 74.293237);
        final LatLngBounds lahorebounds = new LatLngBounds(a,b);
        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).setBoundsBias(lahorebounds).build(getActivity());
                    startActivityForResult(intent, 1);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        Dest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).setBoundsBias(lahorebounds).build(getActivity());
                    startActivityForResult(intent, 2);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

            }
        });
        Go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean flag = true;
                if (pick.getText().toString().equals("") ) {
                    pick.setError("Select input loction");
                    flag = false;
                }
                if(Dest.getText().toString().equals("")){
                    Dest.setError("Select Destination Location");
                    flag = false;
                }
                if(pick.getText().toString().equals(Dest.getText().toString())){
                    pick.setError("Source and destination cannot be same");
                    flag = false;
                }
                if(!(pick.getText().toString().equals("Beaconhouse National University Tarogil Campus") || Dest.getText().toString().equals("Beaconhouse National University Tarogil Campus"))){
                    pick.setError("You cannot search rides out of the university");
                    flag = false;
                }
                if (flag==true){
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    SearchRidesFragment prf = new SearchRidesFragment();
                    prf.setPickLocation(pick.getText().toString());
                    prf.setDestination(Dest.getText().toString());
                    ft.setCustomAnimations(R.anim.slidein_left, 0, R.anim.slidein_right,0);
                    ft.replace(R.id.main_content_area, prf);
                    ft.addToBackStack("prf");
                    ft.commit();
                }

            }
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 1) {
            if (resultCode == getActivity().RESULT_OK) {
                // retrive the data by using getPlace() method.
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                //Log.e("Tag", "Place: " + place.getAddress() + place.getPhoneNumber());

                pick.setText(place.getName());

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                // TODO: Handle the error.
                Log.e("Tag", status.getStatusMessage());

            } else if (resultCode == getActivity().RESULT_CANCELED) {
                // The user canceled the operation.
            }


        }
        else if (requestCode == 2) {
            if (resultCode == getActivity().RESULT_OK) {
                // retrive the data by using getPlace() method.
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                //Log.e("Tag", "Place: " + place.getAddress() + place.getPhoneNumber());

                Dest.setText(place.getName());

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                // TODO: Handle the error.
                Log.e("Tag", status.getStatusMessage());

            } else if (resultCode == getActivity().RESULT_CANCELED) {
                // The user canceled the operation.
            }


        }


    }
}
