package com.example.talky;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.talky.DatabaseClasses.Room;
import com.example.talky.DatabaseClasses.User;
import com.example.talky.utilities.UsersAdapter;
import com.example.talky.utilities.Utilites;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChosePersonToChatWithActivity extends AppCompatActivity {
    private FirebaseDatabase _Database;
    private FirebaseAuth _Auth;
    private DatabaseReference _DBReference;
    private ListView usersListview;
    private List<User> usersList;
    //private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        // firebase
        _Database = FirebaseDatabase.getInstance();
        _Auth = FirebaseAuth.getInstance();
        _DBReference = _Database.getReference();
        usersListview = (ListView) findViewById(R.id.usersListView);
        usersList = new ArrayList<User>();

        LoadAllUsersToListView();


    }

    private void SetListViewOnClickListener(ListView pListView)
    {
         pListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                        //String nameOfPersonToChat
                        String selectedID =((TextView)arg1.findViewById(R.id.hiddenid)).getText().toString();
                        String selectedName =((TextView)arg1.findViewById(R.id.name)).getText().toString();
                        String selectedSurname =((TextView)arg1.findViewById(R.id.surname)).getText().toString();

                        // set shared pref

                        SharedPreferences sharedPref = ChosePersonToChatWithActivity.this.getSharedPreferences("ChattingActivity", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("ChattingActivity_userToChat_ID", selectedID);
                        editor.putString("ChattingActivity_userToChat_Name", selectedName);
                        editor.putString("ChattingActivity_userToChat_Surname", selectedSurname);
                        editor.commit();
                        Intent ChattingActivity = new Intent(ChosePersonToChatWithActivity.this, com.example.talky.ChattingActivity.class);
                        startActivity(ChattingActivity);

                    }
                });
    }

    private void LoadAllUsersToListView()
    {
        _Database.getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
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
        });

    }
}