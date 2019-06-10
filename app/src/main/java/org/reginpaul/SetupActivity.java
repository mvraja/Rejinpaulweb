package org.reginpaul;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {
    private EditText userName, userPhoneNo, userDob;
    private Button saveInformationbutton;
    private ProgressDialog loadingBar;
    private Spinner sDept;
    private RadioButton rbMale, rbFemale;

    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;

    String dept[] = {"ANNA UNIVERSITY", "JNTU", "SCHOOL BOARD", "COMPETITIVE EXAMS"};
    ArrayAdapter deptArray;
    String currentUserID, firebaseToken, gender;
    int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);


        userName = findViewById(R.id.setup_username);
        userPhoneNo = findViewById(R.id.setup_ph_number);
        userDob = findViewById(R.id.setup_dob);
        saveInformationbutton = findViewById(R.id.setup_information_button);
        loadingBar = new ProgressDialog(this);
        sDept = findViewById(R.id.setup_dept);
        rbMale = findViewById(R.id.male);
        rbFemale = findViewById(R.id.female);


        deptArray = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, dept);
        deptArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sDept.setAdapter(deptArray);

        saveInformationbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveAccountSetupInformation();
            }
        });

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(SetupActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                firebaseToken = instanceIdResult.getToken();
            }
        });


        userDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate();
            }
        });
    }


    private void SaveAccountSetupInformation() {
        String username = userName.getText().toString();
        String phone = userPhoneNo.getText().toString();
        String dob = userDob.getText().toString();
        String dept = sDept.getSelectedItem().toString();
        if (rbMale.isChecked()){
            gender = "male";
        }
        else {
            gender = "female";
        }

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Please enter your username", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(dob)) {
            Toast.makeText(this, "Please select your birth date", Toast.LENGTH_SHORT).show();
        }
        if (phone.length()!=10){
            Toast.makeText(this, "Invalid Phone number", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Saving Information");
            loadingBar.setMessage("Please wait for a while...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            HashMap userMap = new HashMap();
            userMap.put("username", username);
            userMap.put("phonenumber", phone);
            userMap.put("dob", dob);
            userMap.put("dept", dept);
            userMap.put("token", firebaseToken);
            userMap.put("gender", gender);
            UsersRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        SendUserToMainActivity();
                        Toast.makeText(SetupActivity.this, "your Account is created Successfully.", Toast.LENGTH_LONG).show();
                        loadingBar.dismiss();
                    } else {
                        String message = task.getException().getMessage();
                        Toast.makeText(SetupActivity.this, "Error Occured: " + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }


    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(SetupActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void selectDate() {

        final Calendar myCalendar = Calendar.getInstance();
        year = myCalendar.get(Calendar.YEAR);
        month = myCalendar.get(Calendar.MONTH);
        day = myCalendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(SetupActivity.this, pickerListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }


    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            userDob.setText(new StringBuilder().append(day)
                    .append("-").append(month + 1).append("-").append(year));
        }
    };

}
