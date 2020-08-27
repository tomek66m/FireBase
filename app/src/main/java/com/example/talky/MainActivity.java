package com.example.talky;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.talky.DatabaseClasses.Message;
import com.example.talky.FireBaseAPI.FireBaseAPIManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase _Database;
    private DatabaseReference _DBReference;
    private FirebaseAuth _Auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // check if user logged in
        mAuth = FirebaseAuth.getInstance();


    }

    @Override
    public void onStart()
    {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) // if logged
        {
            Intent messagesListActivity = new Intent(this, MessagesListActivity.class);
            startActivity(messagesListActivity);
        }
        else // if not logged
        {


            //Intent registerAcitivity = new Intent(this, RegisterActivity.class);
            //startActivity(registerAcitivity);
        }

        _Database = FirebaseDatabase.getInstance();
        _DBReference = _Database.getReference();
        _Auth = FirebaseAuth.getInstance();
    }

    public void loginUser(View view)
    {
        EditText emailFieldInput = (EditText)findViewById(R.id.emailFieldForLogin);
        EditText passwordFieldInput = (EditText)findViewById(R.id.passwordFieldForLogin);
        mAuth.signInWithEmailAndPassword(emailFieldInput.getText().toString(), passwordFieldInput.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {

                            Toast.makeText(MainActivity.this, "Logged in", Toast.LENGTH_LONG).show();
                            //FirebaseUser user = mAuth.getCurrentUser();
                            _DBReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for(DataSnapshot child : dataSnapshot.getChildren())
                                        {
                                            Map<String, String> tu = (HashMap<String, String>) child.getValue();
                                            if(tu.get("_Email").trim().equals(_Auth.getCurrentUser().getEmail()))
                                            {
                                                SharedPreferences sharedPref = MainActivity.this.getSharedPreferences("UserDataPref", MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPref.edit();
                                                editor.putString("User_ID", tu.get("_ID"));
                                                Intent messagesListActivity = new Intent(MainActivity.this, MessagesListActivity.class);
                                                startActivity(messagesListActivity);
                                            }
                                        }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Incorrect login or password", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    public void goToRegisterActivity(View view)
    {
        Intent registerActivity = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(registerActivity);
    }
}