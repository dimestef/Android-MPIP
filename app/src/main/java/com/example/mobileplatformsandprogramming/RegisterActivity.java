package com.example.mobileplatformsandprogramming;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    DataHelper dh;
    EditText editTextUsername;
    EditText editTextPassword;
    EditText editTextReenterPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        dh = new DataHelper(this);

        backToLoginPage();
        registerUser();
    }

    private void registerUser() {
        Button btnRegisterUserPage = (Button) findViewById(R.id.btnRegisterUser);
        btnRegisterUserPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextUsername = (EditText) findViewById(R.id.registerUsername);
                editTextPassword = (EditText) findViewById(R.id.registerPassword);
                editTextReenterPassword = (EditText) findViewById(R.id.reenterPassword);

                List<String> usernames = new ArrayList<String>();
                Boolean canInsert = true;
                Cursor res = dh.getAllData();
                if (res.getCount() == 0) {
                    showMessage("Error", "No data");
                } else {
                    if (!editTextPassword.getText().toString().equals(editTextReenterPassword.getText().toString())) {
                        showMessage("Title", "Passwords doesn't match");
                    } else {
                        StringBuffer sb = new StringBuffer();
                        while (res.moveToNext()) {
                            sb.append("Name " + res.getString(1) + "\n");
                            usernames.add(res.getString(1));
                        }

                        for (String s : usernames) {
                            if (s.equals(editTextUsername.getText().toString()) && s.equals("")) {
                                canInsert = false;
                                showMessage("Title", "The username " + editTextUsername.getText().toString() + " exists!");
                                break;
                            }
                        }

                        if (canInsert) {
                            dh.registerUser(editTextUsername.getText().toString(), editTextPassword.getText().toString());
                            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);

                            i.putExtra("userSaved", editTextUsername.getText().toString());
                            startActivity(i);
                        }
                    }
                }
            }
        });
    }

    private void backToLoginPage() {
        Button btnBackLoginPage = (Button) findViewById(R.id.btnBackLogin);
        btnBackLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }

    private void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
