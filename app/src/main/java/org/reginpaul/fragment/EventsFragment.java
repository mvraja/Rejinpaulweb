package org.reginpaul.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.reginpaul.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EventsFragment extends Fragment {

    private static EditText eventName, eventDate, eventLocation;
    private Button saveEvent;
    private Spinner sType;
    String event[] = {"WORKSHOP", "CONFERENCE", "SYMPOSIUM"};
    ArrayAdapter eventArray;
    int mYear, mMonth, mDay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_events, container, false);

        eventName = rootView.findViewById(R.id.event_name);
        eventDate = rootView.findViewById(R.id.event_date);
        eventLocation = rootView.findViewById(R.id.event_location);
        sType = rootView.findViewById(R.id.event_type);
        saveEvent = rootView.findViewById(R.id.event_send);

        eventArray = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, event);
        eventArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sType.setAdapter(eventArray);

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dFragment = new SelectDate();
                dFragment.show(getActivity().getFragmentManager(), "Date Picker");
            }
        });


        saveEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveEventInformation();
            }
        });


        return rootView;

    }

    public static class SelectDate extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar cal1 = Calendar.getInstance();
            int year = cal1.get(Calendar.YEAR);
            int month = cal1.get(Calendar.MONTH);
            int day = cal1.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dpd = new DatePickerDialog(getActivity(), R.style.timePickerTheme, this, year, month, day);
            dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            return dpd;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            // TODO Auto-generated method stub
            Calendar str1 = Calendar.getInstance();
            str1.setTimeInMillis(0);
            str1.set(year, month, day, 0, 0, 0);
            Date chosenDate = str1.getTime();
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            String res = df.format(chosenDate);
            eventDate.setText(res);
        }
    }


    private void SaveEventInformation() {
        String strName, strDate, strLoc;
        strName = eventName.getText().toString();
        strDate = eventDate.getText().toString();
        strLoc = eventLocation.getText().toString();
        if (TextUtils.isEmpty(strName)) {
            Toast.makeText(getActivity(), "Please enter event name", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(strDate)) {
            Toast.makeText(getActivity(), "Please enter event date", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(strLoc)) {
            Toast.makeText(getActivity(), "Please enter event location", Toast.LENGTH_SHORT).show();
        } else {
            eventName.setText("");
            eventDate.setText("");
            eventLocation.setText("");
            Toast.makeText(getActivity().getApplicationContext(), "Event Information Saved", Toast.LENGTH_SHORT).show();
        }
    }

}
