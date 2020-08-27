package com.example.talky;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.talky.DatabaseClasses.User;
import com.example.talky.utilities.UsersAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MessagesListActivity extends AppCompatActivity {

    private ChildEventListener listener;
    private ArrayList messages;
    private ListView messagesListView;
    private DatabaseReference _DBReference;
    private FirebaseDatabase _Database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_list);
        // firebase
        _DBReference = FirebaseDatabase.getInstance().getReference();


        messagesListView = findViewById(R.id.messagesListView);

        readMessages();


    }

    private void readMessages() {
        messages = new ArrayList();
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, messages);
        messagesListView.setAdapter(adapter);
        _DBReference.child("messages").getRef();
        listener = null;
        _DBReference.child("messages").getRef();

    }

    private void readMessagesFromDB()
    {
/*        _Database.getReference().child("room").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren())
                {
                    User tempUser = new User();
                    Map<String, String> tu = (HashMap<String, String>) child.getValue();
                    tempUser._ID = tu.get("_ID");
                    tempUser._Name= tu.get("_Name");
                    tempUser._Surname = tu.get("_Surname");
                    tempUser._Email = tu.get("_Email");

                    usersList.add(tempUser);
                }
                ArrayList<User> users = new ArrayList<User>();
                for(User u : usersList)
                {
                    users.add(u);
                }
                UsersAdapter adapter = new UsersAdapter(ChosePersonToChatWithActivity.this, users);
                usersListview.setAdapter(adapter);

                SetListViewOnClickListener(usersListview);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

    }


    public void logoutUser(View view)
    {
        FirebaseAuth.getInstance().signOut();
        Intent mainActivity = new Intent(this, MainActivity.class);
        startActivity(mainActivity);
    }

    public void goToSendMessageActivity(View view)
    {
        Intent WriteMessageActivity = new Intent(this, ChosePersonToChatWithActivity.class);
        startActivity(WriteMessageActivity);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}