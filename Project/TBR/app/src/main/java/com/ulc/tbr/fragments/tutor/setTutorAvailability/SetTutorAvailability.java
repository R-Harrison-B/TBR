package com.ulc.tbr.fragments.tutor.setTutorAvailability;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ulc.tbr.activities.MainActivity;
import com.ulc.tbr.R;
import com.ulc.tbr.databases.DatabaseHelper;
import com.ulc.tbr.fragments.common.login.Mysingleton;
import com.ulc.tbr.models.util.*;
import com.ulc.tbr.models.users.*;
import com.ulc.tbr.fragments.common.Adapters.CalendarAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.ulc.tbr.fragments.common.Adapters.CalendarAdapter.gridSlots;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link com.ulc.tbr.fragments.tutor.setTutorAvailability.SetTutorAvailability#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SetTutorAvailability extends Fragment implements AdapterView.OnItemSelectedListener {

    // UPPER MENU BEGIN
    Spinner spinner_homeMenu;
    ArrayList<String> homeMenu;
    ArrayAdapter<String> adapter_homeMenu;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    // Required empty public constructor
    private User user;


    private Spinner spinner_week;
    private ArrayList<String> available_week;
    private ArrayAdapter<String> adapter_week;
    private GridView calendar;
    String[] headerText = {"Time","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
    String[] slotText = {
            "07:00am","","","","","","","",
            "07:30am","","","","","","","",
            "08:00am","","","","","","","",
            "08:30am","","","","","","","",
            "09:00am","","","","","","","",
            "09:30am","","","","","","","",
            "10:00am","","","","","","","",
            "10:30am","","","","","","","",
            "11:00am","","","","","","","",
            "11:30am","","","","","","","",
            "12:00pm","","","","","","","",
            "12:30pm","","","","","","","",
            "01:00pm","","","","","","","",
            "01:30pm","","","","","","","",
            "02:00pm","","","","","","","",
            "02:30pm","","","","","","","",
            "03:00pm","","","","","","","",
            "03:30pm","","","","","","","",
            "04:00pm","","","","","","","",
            "04:30pm","","","","","","","",
            "05:00pm","","","","","","","",
            "05:30pm","","","","","","","",
            "06:00pm","","","","","","","",
            "06:30pm","","","","","","","",
            "07:00pm","","","","","","","",
            "07:30pm","","","","","","",""
    } ;


    public SetTutorAvailability() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment tutorSetDateAndTime.
     */
    // TODO: Rename and change types and number of parameters
    public static SetTutorAvailability newInstance(String param1, String param2) {
        SetTutorAvailability fragment = new SetTutorAvailability();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MainActivity ma = (MainActivity) getActivity();
        Bundle bundle = this.getArguments();
        user = (User) bundle.getSerializable("user");
//        user = (User) getActivity().getIntent().getSerializableExtra("user");
        return inflater.inflate(R.layout.fragment_tutor_set_date_and_time, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        spinner_homeMenu = (Spinner) view.findViewById(R.id.spinner_homeMenu);

        homeMenu = new ArrayList<String>();

        int currFragmentIndex;

        if (user.isTutor() && user.isTutee()) { // StudentTutor
            homeMenu.add("Home");
            homeMenu.add("Get A Tutor");
            homeMenu.add("My Sessions");
            homeMenu.add("Change Availability");
            homeMenu.add("Change Courses");
            homeMenu.add("Logout");

            currFragmentIndex = 3;
        }
        else if (user.isTutor()) { // Tutor
            homeMenu.add("Home");
            homeMenu.add("My Sessions");
            homeMenu.add("Change Availability");
            homeMenu.add("Change Courses");
            homeMenu.add("Logout");

            currFragmentIndex = 2;
        }
        else if (user.isTutee()) { // Student
            homeMenu.add("Home");
            homeMenu.add("Get A Tutor");
            homeMenu.add("My Sessions");
            homeMenu.add("Logout");

            currFragmentIndex = 0; // student should not be able to navigate here
        }
        else {
            // ERROR UNREACHABLE CASE
            homeMenu = null;
            currFragmentIndex = 0;
        }

        adapter_homeMenu = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, homeMenu);
        spinner_homeMenu.setAdapter(adapter_homeMenu);

        spinner_homeMenu.setOnItemSelectedListener(this);
        spinner_homeMenu.setSelection(currFragmentIndex, true);
        spinner_homeMenu.setEnabled(true);
        spinner_homeMenu.setClickable(true);


        CalendarAdapter adapter = new CalendarAdapter(getContext(),slotText);
        CalendarAdapter headerAdapter = new CalendarAdapter(getContext(),headerText);

        Button confirm = (Button) view.findViewById(R.id.confirm_availability);

        GridView header = (GridView) view.findViewById(R.id.calendar_header);
        header.setAdapter(headerAdapter);


        calendar=(GridView) view.findViewById(R.id.calendar);
        calendar.setAdapter(adapter);
        calendar.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getContext(), "You Clicked at " +slotText[+ position], Toast.LENGTH_SHORT).show();
                if(!spinner_week.getSelectedItem().toString().equals("Select a week")) {
                    int row = (position / 8);
                    int col = position % 8;
                    if (gridSlots[row][col] == 1) {
                        gridSlots[row][col] = 0;
                    } else if(gridSlots[row][col] == 0) {
                        gridSlots[row][col] = 1;
                    }else if(gridSlots[row][col] == 2){
                        gridSlots[row][col] = 3;
                    }else if(gridSlots[row][col] == 3) {
                        gridSlots[row][col] = 2;
                    }
                    adapter.notifyDataSetChanged();

                }
            }
        });


        spinner_week          = (Spinner) view.findViewById(R.id.selectWeek);
        //////////////////////////////////////////////////////////////////////////////////

        // BEGIN WEEK
        available_week = new ArrayList<String>();
        // TODO: NEED A GOOD WAY TO LOAD THESE FOR ONLY VALID WEEKS EACH SEMESTER
        // THIS WILL DO FOR NOW THO
        // ADMIN ACTIVITY TO SET WEEKS?

    ////////////////////////////////////////////////////////////////////////////////////////////////

        Calendar cal = Calendar.getInstance();
        int currWeek = cal.get(Calendar.WEEK_OF_YEAR);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String currentDate = sdf.format(cal.getTime());


//        available_week.add("Select a week");

        for(int i= 0; i< 15; i++){
            cal.set(Calendar.WEEK_OF_YEAR, currWeek + i);
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            String monday = sdf.format(cal.getTime());
            cal.set(Calendar.WEEK_OF_YEAR, currWeek + i + 1);
            cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            String sunday = sdf.format(cal.getTime());
            available_week.add(monday + " - " + sunday);
        }


    ////////////////////////////////////////////////////////////////////////////////////////////////
        
        adapter_week = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, available_week);
//        adapter_week.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_week.setAdapter(adapter_week);
        spinner_week.setSelection(0);
        spinner_week.setEnabled(true);
        spinner_week.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (int[] row : gridSlots) {
                    Arrays.fill(row, 0);
                }
                adapter.notifyDataSetChanged();
                if(!spinner_week.getSelectedItem().toString().equals("Select a week")) {
//                    03/28 - 04/03
                    String[] week = weekConverter(spinner_week.getSelectedItem().toString());
                    for (String date : week) {
                        getTutorAvailability(user.getStudentID(), date, user.getNetID(), adapter);

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        //////////////////////////////////////////////////////////////////////////////////

        if(!spinner_week.getSelectedItem().toString().equals("Select a week")){
            Toast.makeText(getContext(), "You selected a week", Toast.LENGTH_SHORT).show();
        }

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < 26; i++){
                    for(int j= 0; j < 8; j++){
                        if(gridSlots[i][j]==1){
                            String time = timeConverter(slotText[i*8]);
                            Log.d("DAT","Date");
                            String date = dateConverter(spinner_week.getSelectedItem().toString(), j);
                            String tutorID = user.getStudentID();
                            // TODO: Add availability
                            addAvailability(tutorID,date,time);
                            gridSlots[i][j] = 2;
                        }else if(gridSlots[i][j]==3){
                            String time = timeConverter(slotText[i*8]);
                            String date = dateConverter(spinner_week.getSelectedItem().toString(), j);
                            String tutorID = user.getStudentID();
                            // TODO: Delete availability
                            deleteAvailability(tutorID,date,time);
//                            dbHelper.deleteAvailability(tutorID,date,time);
                            gridSlots[i][j] = 0;
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }

    private void setAvailability(String date){

    }

    private void getTutorAvailability(String tutor_id, String date, String NetID, CalendarAdapter adapter ){
        Log.i("RIGHT HERE", "HELL02");
        String url = "https://pistachio.khello.co/get_availability.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.i("TUTOR","HIT THE DATABASE HURRAy");
                    Log.i("TUTOR", response);
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = (JSONArray) jsonObject.get("Availability: ");
                    ArrayList<TutorAvailablity> tutorSessions = new ArrayList<TutorAvailablity>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonArray = (JSONObject) array.get(i);
                        tutorSessions.add(new TutorAvailablity(
                                NetID,
                                (String) jsonArray.get("tutor_id"),
                                (String) jsonArray.get("date"),
                                (String) jsonArray.get("time"),
                                (Boolean) jsonArray.get("booked").equals("TRUE")));
                    }
                    for (TutorAvailablity avail : tutorSessions) {
                        String tempDate = avail.getDate();
                        String tempTime = avail.getTime();
                        Boolean tempBooked = avail.isBooked();
                        if (!tempBooked) {
                            Log.d("NOT", "booked");
                            int col = dayToColumn(tempDate, spinner_week.getSelectedItem().toString());
                            int row = timeToRow(tempTime);
                            gridSlots[row][col] = 2;
                        } else {
                            Log.d("is", "booked");
                            int col = dayToColumn(tempDate, spinner_week.getSelectedItem().toString());
                            int row = timeToRow(tempTime);
                            gridSlots[row][col] = 4;
                        }
                        adapter.notifyDataSetChanged();
                    }
                    Log.i("on response", String.valueOf(tutorSessions.size()));
                    Log.i("tutor", String.valueOf(tutorSessions.toString()));


                } catch (JSONException e) {
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Some sort of unique string identifier here",error.toString());
                user = null;
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.i("tutor_id",tutor_id);
                Log.i("date",date);
                Map<String, String> Params = new HashMap<String, String>();
                Params.put("tutor_id", tutor_id);
                Params.put("date", date);
                return Params;
            }
        };
        Mysingleton.getInstance(getContext()).addTorequestque(stringRequest);
        //requestQueue.add(stringRequest);
    }

    private void deleteAvailability(String tutor_id, String date, String time) {
        Log.i("Input",tutor_id + " " + date + " " + time );


        String url = "https://pistachio.khello.co/remove_tutor_availability.php";
        RequestQueue requestQueue = Mysingleton.getInstance(getContext()).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Response", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Some sort of unique string identifier here",error.toString());
                user = null;
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> Params = new HashMap<String, String>();
                Params.put("tutor_id", tutor_id);
                Params.put("date", date);
                Params.put("time", time);
                return Params;
            }
        };
        Mysingleton.getInstance(getContext()).addTorequestque(stringRequest);
    }

    public void addAvailability(String tutor_id, String date, String time){
        Log.i("Input",tutor_id + " " + date + " " + time );


        String url = "https://pistachio.khello.co/post_availability.php";
        RequestQueue requestQueue = Mysingleton.getInstance(getContext()).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Response", response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Some sort of unique string identifier here",error.toString());
                user = null;
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> Params = new HashMap<String, String>();
                Params.put("tutor_id", tutor_id);
                Params.put("date", date);
                Params.put("time", time);
                return Params;
            }
        };
        Mysingleton.getInstance(getContext()).addTorequestque(stringRequest);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spinner_homeMenu) {
            String menuSelection = (String) spinner_homeMenu.getSelectedItem();

            Bundle userData = new Bundle();
            userData.putSerializable("user", user);

            switch(menuSelection) {
                case "Home":
                    NavHostFragment.findNavController(SetTutorAvailability.this)
                            .navigate(R.id.action_set_tutor_availability_to_home, userData);

                    break;
                case "Get A Tutor":
                    NavHostFragment.findNavController(SetTutorAvailability.this)
                            .navigate(R.id.action_set_tutor_availability_to_get_a_tutor, userData);

                    break;
                case "My Sessions":
                    NavHostFragment.findNavController(SetTutorAvailability.this)
                            .navigate(R.id.action_set_tutor_availability_to_my_sessions, userData);

                    break;
                case "Change Courses":
                    NavHostFragment.findNavController(SetTutorAvailability.this)
                            .navigate(R.id.action_set_tutor_availability_to_set_tutor_courses, userData);

                    break;
                case "Logout" :
                    NavHostFragment.findNavController(SetTutorAvailability.this)
                            .navigate(R.id.action_set_tutor_availability_logout, userData);

                    break;
                default :
                    break;
            }


        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        if (parent.getId() == R.id.spinner_homeMenu) {

        }

    }



    public String dateConverter(String week, int col){
        //   03/28 - 04/03
        //   03/28/2020 - 04/03/2020

        String toReturn = "";
        int daysLeft = 7 - Integer.parseInt(week.substring(16, 18));
        if(!week.substring(0,2).equals(week.substring(14,16)) && daysLeft < col) {
            int date = col - daysLeft ;
            if(date >= 10) {
                toReturn = week.substring(13, 16) + String.valueOf(date) + week.substring(18);
            }else{
                toReturn = week.substring(13, 16) + "0" + String.valueOf(date) + week.substring(18);
            }
        }else{
            int date = Integer.parseInt(week.substring(3,5)) + col - 1;
            if(date>=10){
                toReturn = week.substring(0,3) + String.valueOf(date) + week.substring(5, 10);
            }else{
                toReturn = week.substring(0,3) + "0" + String.valueOf(date) + week.substring(5, 10);
            }
        }
        return toReturn;
    }

    public String timeConverter(String time){
        int hour = Integer.parseInt(time.substring(0,2));
        if(time.substring(5).equals("pm") && hour != 12){
            hour += 12;
        }
        String toReturn;
        if(hour >= 10) {
            toReturn = String.valueOf(hour) + ":" + time.substring(3, 5);
        }else{
            toReturn = "0" + String.valueOf(hour) + ":" + time.substring(3, 5);
        }
        return toReturn;
    }
    public int timeToRow(String time){
        int row = (Integer.parseInt(time.substring(0,2)) - 7)*2;
        if(time.substring(3,5).equals("30")){
            row += 1;
        }

        return row;
    }
    public int dayToColumn(String day, String week){
        int col = 0;
        if(week.substring(0,2).equals(day.substring(0,2))){
            int start = Integer.parseInt(week.substring(3,5));
            col = Integer.parseInt(day.substring(3,5)) - start + 1;
        }else{
            String s1 = week.substring(16, 18);
            String s2 = day.substring(3, 5);
            int diff = Integer.parseInt(week.substring(16, 18)) - Integer.parseInt(day.substring(3,5));
            col = 7 - diff;
        }
        return col;
    }
    public String[] weekConverter(String week){
        //   03/28 - 04/03
        //   03/28/2020 - 04/03/2020

        String[] toReturn = new String[7];
        int day = Integer.parseInt(week.substring(16, 18)); //11
        int i = 0;
        while(day!=0 && i!=7){
            String sDay;
            if(day >= 10){
                sDay = String.valueOf(day);
            }else{
                sDay = "0" + String.valueOf(day);
            }
            toReturn[6-i] = week.substring(13,16) + sDay + week.substring(18);//8,11
            i++;
            day--;
        }

        if(day==0 && i!=7){
            int day2 = Integer.parseInt(week.substring(3,5));

            for(int j=0; j< 7-i; j++) {
                String sDay = sDay = String.valueOf(day2);
                toReturn[j] = week.substring(0, 3) + sDay + week.substring(5,10);
                day2++;
            }
        }

        return toReturn;
    }
}