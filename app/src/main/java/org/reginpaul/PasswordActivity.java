package org.reginpaul;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class PasswordActivity extends AppCompatActivity {

    private Button btnSend;
    private EditText edEmail;
    private Toolbar toolbar;

    String strMail;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        edEmail = findViewById(R.id.reset_email);
        btnSend = findViewById(R.id.btn_reset);
        toolbar = findViewById(R.id.fp_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Reset Password");

        mAuth = FirebaseAuth.getInstance();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strMail = edEmail.getText().toString();

                if (TextUtils.isEmpty(strMail)){
                    Toast.makeText(PasswordActivity.this,"Field should not be blank",Toast.LENGTH_SHORT).show();
                }
                else {
                    mAuth.sendPasswordResetEmail(strMail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(PasswordActivity.this,"Please check your Email for Reset Password link",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(PasswordActivity.this,LoginActivity.class));
                            }
                            else {
                                Toast.makeText(PasswordActivity.this,"Error in sending mail",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

    }
}
