package com.example.mobileplatformsandprogramming;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    EditText editTextUsername;
    EditText editTextPassword;
    DataHelper dh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dh = new DataHelper(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("userSaved");
            Toast.makeText(LoginActivity.this, "User: " + value + " is saved", Toast.LENGTH_SHORT).show();
        }

        ImageView imageSearch = (ImageView) findViewById(R.id.loginLogo);
        imageSearch.setImageResource(R.drawable.logologin);

        ImageView iconLogin = (ImageView) findViewById(R.id.iconLogin);
        iconLogin.setImageResource(R.drawable.login);

        ImageView iconPassword = (ImageView) findViewById(R.id.iconPassword);
        iconPassword.setImageResource(R.drawable.password);

        CardView cardView = (CardView) findViewById(R.id.cv);
        cardView.setCardBackgroundColor(Color.argb(1, 111,111,111));

        loginUser();
        registerUser();
    }

    private void loginUser() {
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> users = new HashMap();
                Boolean canInsert = false;
                Cursor res = dh.getAllData();

                if (res.getCount() == 0) {
                    Toast.makeText(LoginActivity.this, "Wrong username or password", Toast.LENGTH_SHORT).show();
                } else {
                    while (res.moveToNext()) {
                        users.put(res.getString(1), res.getString(2));
                    }

                    for (Map.Entry<String, String> entry : users.entrySet()) {
                        if (entry.getKey().equals(editTextUsername.getText().toString()) && entry.getValue().equals(editTextPassword.getText().toString())) {
                            canInsert = true;
                            Intent i = new Intent(LoginActivity.this, LogedUserActivity.class);
                            i.putExtra("logedUserUsername", entry.getValue());
                            i.putExtra("logedUserId", entry.getKey());
                            startActivity(i);
                            break;
                        }
                    }
                }

                if (!canInsert) {
                    Toast.makeText(LoginActivity.this, "Wrong username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void registerUser() {
        Button btnRegisterPage = (Button) findViewById(R.id.btnRegister);
        btnRegisterPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
    }

    private void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
