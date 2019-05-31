package org.reginpaul.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.reginpaul.LoginActivity;
import org.reginpaul.R;
import org.reginpaul.fragment.HomeFragment;


public class ProfileFragment extends Fragment implements View.OnClickListener {

    private TextView tName, tPhone, tDob;
    private Spinner tDept;
    private Button tBtnSave, tBtnCan;
    private DatabaseReference profile;
    private FirebaseAuth mAuth;
    private String currentUser;
    private ArrayAdapter deptArray;

    String dept[] = {"ANNA UNIVERSITY", "JNTU", "SCHOOL BOARD", "COMPETITIVE EXAMS"};
    String sname, sphone, sdob, stoken, sgender, sdept;
    AppCompatActivity activity;
    ActionBar actionBar;
    ViewGroup viewGroup;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        viewGroup = container;

        tName = rootView.findViewById(R.id.tname);
        tPhone = rootView.findViewById(R.id.tnumber);
        tDob = rootView.findViewById(R.id.tdob);
        tDept = rootView.findViewById(R.id.tdept);
        tBtnSave = rootView.findViewById(R.id.tbutton1);
        tBtnCan = rootView.findViewById(R.id.tbutton2);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();
        profile = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser);


        deptArray = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, dept);
        deptArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tDept.setAdapter(deptArray);
        
        profile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    sname = dataSnapshot.child("username").getValue().toString();
                    sphone = dataSnapshot.child("phonenumber").getValue().toString();
                    sdob = dataSnapshot.child("dob").getValue().toString();
                    stoken = dataSnapshot.child("token").getValue().toString();
                    sgender = dataSnapshot.child("gender").getValue().toString();

                    tName.setText("Username: "+sname);
                    tPhone.setText("Phone Number: "+sphone);
                    tDob.setText("Date of Birth: "+sdob);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        
        tBtnSave.setOnClickListener(this);
        tBtnCan.setOnClickListener(this);

        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
        activity = (AppCompatActivity) getActivity();
        actionBar = activity.getSupportActionBar();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tbutton1:
                updateUserData();
                break;
            case R.id.tbutton2:
                getFragmentManager().beginTransaction().replace(R.id.main_container, new HomeFragment()).commit();
                actionBar.setTitle("Home");
                break;
        }
    }

    private void updateUserData() {
        sdept = tDept.getSelectedItem().toString();
        profile.child("dept").setValue(sdept);
        showCustomDialog();
    }

    private void showCustomDialog() {
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.custom_dialog, viewGroup, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),android.R.style.Theme_Light);
        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button btn = alertDialog.findViewById(R.id.buttonOk);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                SendUserToLoginActivity();
            }
        });
    }

    private void SendUserToLoginActivity() {
        Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        getActivity().finish();
    }

}