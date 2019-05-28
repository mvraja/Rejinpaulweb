package org.reginpaul;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class NotificationActivity extends AppCompatActivity{

    TextView titleView;
    TextView messageView;

    String title;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        titleView = (TextView) findViewById(R.id.title);
        messageView = (TextView) findViewById(R.id.message);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        message = intent.getStringExtra("message");
        Log.d("Notifactivity_title",title);
        titleView.setText(title);
        messageView.setText(message);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        title = intent.getStringExtra("title");
        message = intent.getStringExtra("message");

        titleView.setText("Refreshed Notification: \n"+title);
        messageView.setText(message);

    }
}