package com.example.vizora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class RegistrationActivity extends AppCompatActivity {
    private static final String LOG_TAG = RegistrationActivity.class.getName();
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private static final int SECRET_KEY = 99;
    
    EditText userNameET;
    EditText emailET;
    EditText passwordET;
    EditText passwordAgainET;
    EditText bornDateET;
    EditText szerzodesszamET;
    RadioGroup sexTypeRG;

    private SharedPreferences preferences;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Bundle bundle = getIntent().getExtras();
        // int secret_key = bundle.getInt("SECRET_KEY");
        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);

        if (secret_key != 99) {
            finish();
        }

        userNameET = findViewById(R.id.editTextUsername);
        emailET = findViewById(R.id.editTextEmail);
        passwordET = findViewById(R.id.editTextPassword);
        passwordAgainET = findViewById(R.id.editTextPasswordAgain);
        bornDateET = findViewById(R.id.editTextBornDate);
        szerzodesszamET = findViewById(R.id.editTextSzerzodesszam);
        sexTypeRG = findViewById(R.id.radioGroup);
        sexTypeRG.check(R.id.manRadioButton);

        Calendar naptar = Calendar.getInstance();
        final int ev = naptar.get(Calendar.YEAR);
        final int honap = naptar.get(Calendar.MONTH);
        final int nap = naptar.get(Calendar.DAY_OF_MONTH);

        bornDateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegistrationActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int ev, int honap, int nap) {
                        honap = honap + 1;
                        String date = ev + ".";
                        if (honap > 0 && honap < 10) {
                            date = date + "0";
                        }
                        date = date + honap + ".";
                        if (nap > 0 && nap < 10) {
                            date = date + "0";
                        }
                        date = date + nap + ".";
                        bornDateET.setText(date);
                    }
                }, ev, honap, nap);
                datePickerDialog.show();
            }
        });

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        String userEmail = preferences.getString("userEmail", "");
        String password = preferences.getString("password", "");

        emailET.setText(userEmail);
        passwordET.setText(password);

        mAuth = FirebaseAuth.getInstance();

        Log.i(LOG_TAG, "RegistrationActivity: onCreate()");
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG, "RegistrationActivity: onStart()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_TAG, "RegistrationActivity: onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "RegistrationActivity: onDestroy()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOG_TAG, "RegistrationActivity: onPause()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "RegistrationActivity: onResume()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(LOG_TAG, "RegistrationActivity: onRestart()");
    }

    public void registration(View view) {
        String userName = userNameET.getText().toString();
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        String passwordAgain = passwordAgainET.getText().toString();
        String bornDate = bornDateET.getText().toString();
        String szerzodesszam = szerzodesszamET.getText().toString();

        int checkedId = sexTypeRG.getCheckedRadioButtonId();
        RadioButton radioButton = sexTypeRG.findViewById(checkedId);
        //sexTypeRG.indexOfChild(radioButton);
        String sexType = radioButton.getText().toString();

        if (!password.equals(passwordAgain)){
            Log.e(LOG_TAG, "Nem egyenlőek a jelszavak.");
            return;
        }

        // Log.i(LOG_TAG, "Regisztrált: " + userName + ", email: " + email + ", születési dátum: " + bornDate + ", szerződésszám: " + szerzodesszam);
        // startFomenu();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(LOG_TAG, "User created succesfully");
                    startFomenu();
                } else {
                    Log.d(LOG_TAG, "User wasn't created succesfully");
                    Toast.makeText(RegistrationActivity.this, "User wasn't created succesfully:" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void cancel(View view) {
        finish();
    }

    private void startFomenu(){
        Intent intent = new Intent(this, FomenuActivity.class);
        // intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    };
}