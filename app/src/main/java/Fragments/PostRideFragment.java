package Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.shjunaid.bnurideshare.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import Models.AppConstants;
import Models.RideModel;


public class PostRideFragment extends Fragment {
    private String[] seatsArray = {"Select","1","2","3","4","5"};
    private String[] CostArray = {"Select","3","4","5","6","7","8","9","10","11","12","13","14","15"};
    private String[] smokingArray = {"Select","Smoking is not allowed","Smoking is allowed"};
    private String[] foodArray = {"Select","food/drinks not allowed","food/drinks are allowed"};
    private String[] vtypeArray = {"Select","Car","Bike"};
    private EditText time_edit_text,date_edit_text,source,destination;
    private TextView txt_time,txt_date,txt_seats,txt_smoking,txt_cost,txt_vtype,txt_food;
    private Calendar mycalendar = Calendar.getInstance();
    private Spinner seats,cost,smoking,food,vtype;
    private Button selectRouteButton;

    private String DTime="",DDate="",PDate="",Seats="",Cost="",Smoking="1",Food="1",Vtype="1",Source="",Destination="";
    private RideModel rideModel;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.post_ride_fragment,container,false);
        time_edit_text = (EditText)v.findViewById(R.id.post_ride_time);
        txt_time = (TextView)v.findViewById(R.id.post_ride_txt_time);
        txt_date=(TextView)v.findViewById(R.id.post_ride_text_date);
        date_edit_text = (EditText)v.findViewById(R.id.post_ride_date);
        seats = (Spinner)v.findViewById(R.id.post_ride_seats);
        txt_seats = (TextView)v.findViewById(R.id.post_ride_text_seats);
        cost = (Spinner)v.findViewById(R.id.post_ride_cost);
        txt_cost = (TextView)v.findViewById(R.id.post_ride_text_cost);
        smoking = (Spinner)v.findViewById(R.id.post_ride_smoking);
        txt_smoking = (TextView)v.findViewById(R.id.post_ride_text_smoking);
        vtype = (Spinner)v.findViewById(R.id.post_ride_vtype);
        txt_vtype = (TextView)v.findViewById(R.id.post_ride_text_vtype);
        food = (Spinner)v.findViewById(R.id.post_ride_food);
        txt_food = (TextView)v.findViewById(R.id.post_ride_text_food);
        //source = (EditText)v.findViewById(R.id.source_place);
        //destination = (EditText)v.findViewById(R.id.destination_place);
        selectRouteButton = (Button) v.findViewById(R.id.selectroute_btn);



//        ArrayAdapter<String> dropspin = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item,array);
//        //dropspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        dropdown.setAdapter(dropspin);

        ArrayAdapter<String> seatspin = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item,seatsArray);
        seatspin.setDropDownViewResource(R.layout.spinner_textview);
        seats.setAdapter(seatspin);

        ArrayAdapter<String> costspin = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item,CostArray);
        costspin.setDropDownViewResource(R.layout.spinner_textview);
        cost.setAdapter(costspin);
        ArrayAdapter<String> smokingspin = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item,smokingArray);
        smokingspin.setDropDownViewResource(R.layout.spinner_textview);
        smoking.setAdapter(smokingspin);
        ArrayAdapter<String> foodspin = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item,foodArray);
        foodspin.setDropDownViewResource(R.layout.spinner_textview);
        food.setAdapter(foodspin);
        ArrayAdapter<String> vtypespin = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item,vtypeArray);
        vtypespin.setDropDownViewResource(R.layout.spinner_textview);
        vtype.setAdapter(vtypespin);

        return v;

    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //selectRouteButton = (Button) getActivity().findViewById(R.id.selectroute_btn);
        time_edit_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(getActivity(), listenerTime, mycalendar.get(Calendar.HOUR_OF_DAY), mycalendar.get(Calendar.MINUTE), true).show();

            }
        });

        date_edit_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            DatePickerDialog dp  = new DatePickerDialog(getActivity(), listener, mycalendar.get(Calendar.YEAR), mycalendar.get(Calendar.MONTH), mycalendar.get(Calendar.DAY_OF_MONTH));
                dp.getDatePicker().setMinDate(mycalendar.getTimeInMillis());
                dp.show();
            }
        });




//        source.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(getActivity());
//                    startActivityForResult(intent,1);
//                } catch (GooglePlayServicesRepairableException e) {
//                    e.printStackTrace();
//                } catch (GooglePlayServicesNotAvailableException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        destination.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(getActivity());
//                    startActivityForResult(intent,2);
//                } catch (GooglePlayServicesRepairableException e) {
//                    e.printStackTrace();
//                } catch (GooglePlayServicesNotAvailableException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });

        selectRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = true;
                rideModel = new RideModel();

                if (date_edit_text.getText().toString().equals("")){
                    date_edit_text.setError("select departure date");
                    flag=false;
                }
                if (time_edit_text.getText().toString().equals("")){
                    time_edit_text.setError("select departure time");

                    flag = false;
                }

                if(seats.getSelectedItem() !=null ) {
                    Seats = (String)seats.getSelectedItem();
                    if(Seats.equals("Select")){
                        TextView errorText = (TextView)seats.getSelectedView();
                        errorText.setError("select seats");
                        flag = false;
                    }
                    else rideModel.setAvailableSeats(Seats);
                }
                if(cost.getSelectedItem() !=null ) {
                    Cost = (String)cost.getSelectedItem();
                    if(Cost.equals("Select")){
                        TextView errorText = (TextView)cost.getSelectedView();
                        errorText.setError("select cost");
                        flag = false;
                    }
                    else rideModel.setCostPerKm(Cost);
                }
                if(smoking.getSelectedItem() !=null ) {
                    Smoking = (String)smoking.getSelectedItem();
                    if(Smoking.equals("Select")){
                        TextView errorText = (TextView)smoking.getSelectedView();
                        errorText.setError("select smoking status");
                        flag = false;
                    }
                    else {
                        if (smoking.getSelectedItem().toString() == smokingArray[1])
                        {
                            Smoking = "1";
                        }
                        else Smoking = "2";
                    }
                    rideModel.setSmoking(Smoking);
                }
                if(food.getSelectedItem() !=null ) {
                    Food = (String) food.getSelectedItem();
                    if (Food.equals("Select")) {
                        TextView errorText = (TextView) food.getSelectedView();
                        errorText.setError("select food/drinks status");
                        flag = false;
                    } else {
                        if (food.getSelectedItem().toString() == foodArray[1]) {
                            Food = "1";
                        } else Food = "2";
                    }
                    rideModel.setFoodDrinks(Food);
                }
                if(vtype.getSelectedItem() !=null ) {
                    Vtype = (String) vtype.getSelectedItem();
                    if (Vtype.equals("Select")) {
                        TextView errorText = (TextView) vtype.getSelectedView();
                        errorText.setError("select Vehicle Type");
                        flag = false;
                    } else {
                        if (vtype.getSelectedItem().toString() == vtypeArray[1]) {
                            Vtype = "1";
                            TextView errorText = (TextView)seats.getSelectedView();
                            if(!errorText.getText().equals("")){
                                errorText.setError(null);
                            }

                        } else Vtype = "2";
                    }
                    rideModel.setVehicleType(Vtype);
                }
                if (Vtype.equals("2")){
                    if (!Seats.equals("1")){
                        TextView errorText = (TextView)seats.getSelectedView();
                        errorText.setError("1 seat should be available for bike");
                        flag = false;
                        Toast.makeText(getActivity(),"1 seat should be available for bike",Toast.LENGTH_LONG).show();
                    }
                }
                if (!date_edit_text.getText().toString().equals("") && !time_edit_text.getText().toString().equals("")) {
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    String currentdate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
                    String depdate = date_edit_text.getText().toString();
                    String deptime = time_edit_text.getText().toString();
                    deptime = deptime.concat(":00");
                    depdate = depdate.concat(" " + deptime);
                    Date d1 = null;
                    Date d2 = null;
                    try {
                        d1 = format.parse(depdate);
                        d2 = format.parse(currentdate.toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    int a = d1.compareTo(d2);
                    if (a < 0) {
                        time_edit_text.setError("departure time is lesser than current time");
                        flag = false;
                    }
                }

                rideModel.setDepartureDate(date_edit_text.getText().toString());
                rideModel.setDepartureTime(time_edit_text.getText().toString());
                PDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
                rideModel.setPostDate(PDate.toString());
                rideModel.setRideFrequency("Daily");
//                rideModel.setSource(stemp);
//                rideModel.setDestination(dtemp);
                sharedPreferences = getActivity().getSharedPreferences(AppConstants.loginpref_name, Context.MODE_PRIVATE);
                int userid = sharedPreferences.getInt(AppConstants.key_userid,0);
                rideModel.setUserId(userid);
                if (flag == true){
                    Gson gson = new Gson();
                    String ridemodelstring = gson.toJson(rideModel);
                    Bundle bundle=new Bundle();
                    bundle.putString("RideModelObj", ridemodelstring);
                    RidePostMapViewFragment prmvf = new RidePostMapViewFragment();
                    prmvf.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_content_area, prmvf);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
            }
        });


    }

    DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            date_edit_text.setText(i2+"/"+(i1+1)+"/"+i);
            date_edit_text.setError(null);
        }
    };
    TimePickerDialog.OnTimeSetListener listenerTime = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            time_edit_text.setText(i+":"+i1);
            time_edit_text.setError(null);
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 1) {
            if (resultCode == getActivity().RESULT_OK) {
                // retrive the data by using getPlace() method.
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                //Log.e("Tag", "Place: " + place.getAddress() + place.getPhoneNumber());
                source.setText(place.getName());
                source.setError(null);

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
                destination.setText(place.getName());
                destination.setError(null);

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
