package org.reginpaul.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.reginpaul.EventAdapter;
import org.reginpaul.R;
import org.reginpaul.RegEvent;
import org.reginpaul.RequestHandler;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class UpEventFragment extends Fragment {

    List<RegEvent> eventList;
    RecyclerView recyclerView;
    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_upevents, container, false);
        recyclerView = rootView.findViewById(R.id.recylcerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(),1));
        
        eventList = new ArrayList<>();
        loadProducts();
        return rootView;

    }

    private void loadProducts() {
        PerformNetworkRequest request = new PerformNetworkRequest("http://mindvoice.info/rpweb/v1/Api.php?apicall=getuser", null, CODE_GET_REQUEST);
        request.execute();
        EventAdapter eventAdapter = new EventAdapter(getActivity(), eventList);
        recyclerView.setAdapter(eventAdapter);
    }

    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;
        int requestCode;

        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                   // Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    Log.d("Events", object.toString());
                    refreshList(object.getJSONArray("users"));
                } else
                    Log.d("Events", object.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);


            if (requestCode == CODE_GET_REQUEST) {
                Log.d("Events", url);
                String getstring = requestHandler.sendGetRequest(url);

                return requestHandler.sendGetRequest(url);
            }
            return null;
        }
    }

    private void refreshList(JSONArray pfiles) throws JSONException {
        eventList.clear();
        for (int i = 0; i < pfiles.length(); i++) {
            JSONObject obj = pfiles.getJSONObject(i);
            eventList.add(new RegEvent(obj.getInt("id"), obj.getString("name"),obj.getString("type"), obj.getString("date"),obj.getString("loc"), obj.getString("image")));
            if (eventList.size()>0){
                sortList(eventList);
            }
        }

        EventAdapter eventAdapter = new EventAdapter(getActivity(), eventList);
        recyclerView.setAdapter(eventAdapter);
    }

    private void sortList(List<RegEvent> eventList) {
        Collections.sort(eventList, new EventDetailSortByDate());
        Collections.reverse(eventList);
    }

    private class EventDetailSortByDate implements java.util.Comparator<RegEvent> {
        @Override
        public int compare(RegEvent event1, RegEvent event2) {
            Date DateObject1 = StringToDate(event1.getDate());
            Date DateObject2 = StringToDate(event2.getDate());

            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(DateObject1);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(DateObject2);

            int month1 = cal1.get(Calendar.MONTH);
            int month2 = cal2.get(Calendar.MONTH);

            if (month1 < month2)
                return -1;
            else if (month1 == month2)
                return cal1.get(Calendar.DAY_OF_MONTH) - cal2.get(Calendar.DAY_OF_MONTH);

            else return 1;
        }
    }


    public static Date StringToDate(String theDateString) {
        Date returnDate = new Date();
        if (theDateString.contains("-")) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            try {
                returnDate = dateFormat.parse(theDateString);
            } catch (ParseException e) {
                SimpleDateFormat altdateFormat = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    returnDate = altdateFormat.parse(theDateString);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            try {
                returnDate = dateFormat.parse(theDateString);
            } catch (ParseException e) {
                SimpleDateFormat altdateFormat = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    returnDate = altdateFormat.parse(theDateString);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return returnDate;
    }

}