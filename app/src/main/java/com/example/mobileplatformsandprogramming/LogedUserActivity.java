package com.example.mobileplatformsandprogramming;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class LogedUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loged_user);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String userUsername = extras.getString("logedUserUsername");
            String userId = extras.getString("logedUserId");
            Toast.makeText(LogedUserActivity.this, "User: " + userUsername + " is logged with id: " + userId, Toast.LENGTH_SHORT).show();
        }
    }



    private void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LogedUserActivity.this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
