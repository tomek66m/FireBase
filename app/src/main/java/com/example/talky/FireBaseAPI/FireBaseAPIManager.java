package com.example.talky.FireBaseAPI;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.talky.DatabaseClasses.User;
import com.example.talky.MessagesListActivity;
import com.example.talky.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

// VARIABLES NAMING
// _ class members
// p function args

// NEED TO CHECK IF ITS INITIALIZED BEFORE RUNNING ANYTHING

public class FireBaseAPIManager {

    private FireBaseAPIManager _FireBaseAPIManager = null;

    private FirebaseDatabase _Database;
    private FirebaseUser _CurrentFirebaseUser;
    public FirebaseAuth _Auth;
    private DatabaseReference _DBReference;

    private User _CurrentUser;

    // state variables
    private Boolean _IsRegisterSucced;
    public Boolean GetRegisterResult() {
        return _IsRegisterSucced;
    }

    public FireBaseAPIManager() {
        _Database = FirebaseDatabase.getInstance();
        _Auth = FirebaseAuth.getInstance();
        _CurrentFirebaseUser = _Auth.getCurrentUser();
        _DBReference = _Database.getReference();

        _IsRegisterSucced = false;

    };

    public void CreateAccount(final String pName, final String pSurname, final String pEmail, String pPassword, Activity currentActivity)
    {
        Log.d("create account", "function");
        _Auth.createUserWithEmailAndPassword(pEmail.trim(), pPassword)
                .addOnCompleteListener(currentActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Get added user
/*                            FirebaseUser user = _Auth.getCurrentUser();

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
                            newUser = _CurrentUser;
                            _IsRegisterSucced = true;*/


                        } else {
                            // If sign in fails, display a message to the user.
                            FirebaseAuthException e = (FirebaseAuthException) task.getException();
                            //Toast.makeText(currentActivity, "Could not create an account. " + e.getMessage(), Toast.LENGTH_LONG);
                            Log.d("ERROR AUTH: " , e.getMessage());
                        }
                    }
                });
    }

}
