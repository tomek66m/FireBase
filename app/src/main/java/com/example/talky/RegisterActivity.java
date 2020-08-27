package com.example.talky;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.talky.DatabaseClasses.User;
import com.example.talky.FireBaseAPI.FireBaseAPIManager;
import com.example.talky.utilities.Utilites;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity{

    private Handler _RegisterSucceedHandler;

    private FirebaseAuth _Auth;
    private FirebaseDatabase _Database;
    private DatabaseReference _DBReference;
    private Boolean _IsRegisterSuceeded = false;
    private User _CurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        _Auth = FirebaseAuth.getInstance();
        _Database = FirebaseDatabase.getInstance();
        _DBReference = _Database.getReference();


    }
    public void registerUser(View view)
    {
        String email = ((EditText)findViewById(R.id.emailTextForRegister)).getText().toString().trim();
        String password = ((EditText) findViewById(R.id.passwordTextForRegister)).getText().toString();
        String name = ((EditText)findViewById(R.id.nameTextForRegister)).getText().toString().trim();
        String surname = ((EditText) findViewById(R.id.surnameTextForRegister)).getText().toString();

        FireBaseRegister(email, password, name, surname);


    }

    private void FireBaseRegister(final String pEmail, String pPassword, final String pName, final String pSurname)
    {
        _Auth.createUserWithEmailAndPassword(pEmail.trim(), pPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Get added user
                            FirebaseUser user = _Auth.getCurrentUser();

                            // Create database row with additional parameters except password
                            User newUser = new User();
                            newUser._Name = pName;
                            newUser._Surname = pSurname;
                            newUser._Email = pEmail;

                            String uniqueKeyIdentifier = _DBReference.child("users").push().getKey();
                            Log.d("KEY OBTAINED", "obtained key");
                            newUser._ID = uniqueKeyIdentifier;

                            Map<String, Object> newUserMap = newUser.toMap();
                            Map<String, Object> mapObjectToSendToServer = new HashMap<>();
                            Log.d("KEY PUTTED", "putted key");
                            mapObjectToSendToServer.put("/users/" + uniqueKeyIdentifier, newUserMap);

                            _DBReference.updateChildren(mapObjectToSendToServer);
                            _CurrentUser = newUser;
                            _IsRegisterSuceeded= true;

                            // test
                            SharedPreferences sharedPref = RegisterActivity.this.getSharedPreferences("UserDataPref", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("User_ID", _CurrentUser._ID);
                            editor.putString("User_Email", newUser._Email);
                            editor.putString("User_Name", newUser._Name);
                            editor.putString("User_Surname", newUser._Surname);
                            editor.commit();

                            // test



                            Intent MessagesListActivity = new Intent(RegisterActivity.this, MessagesListActivity.class);
                            startActivity(MessagesListActivity);


                        } else {
                            // If sign in fails, display a message to the user.
                            FirebaseAuthException e = (FirebaseAuthException) task.getException();
                            Toast.makeText(RegisterActivity.this, "Could not create an account. ", Toast.LENGTH_LONG);
                        }
                    }
                });
    }


}