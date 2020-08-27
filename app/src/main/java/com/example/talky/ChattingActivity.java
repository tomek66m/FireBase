package com.example.talky;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.talky.DatabaseClasses.Message;
import com.example.talky.DatabaseClasses.Room;
import com.example.talky.DatabaseClasses.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ChattingActivity extends AppCompatActivity {

    private FirebaseDatabase _Database;
    private FirebaseAuth _Auth;
    private DatabaseReference _DBReference;
    private DatabaseReference _MessagesReference;

    private User userToChat;
    private String currentUserID;
    private String chatterUserID;
    private String roomID;

    private boolean _CreateDataLoaded = false;


    private ArrayList<Message> messages;



    @Override
    protected void onStart()
    {
        super.onStart();
        Log.d("on start", "on start");
        setContentView(R.layout.activity_chatting);

        // firebase
        _Database = FirebaseDatabase.getInstance();
        _Auth = FirebaseAuth.getInstance();
        _DBReference = _Database.getReference();
        _MessagesReference = _DBReference.child("messages");

        _Auth.getCurrentUser();

        userToChat = new User();
        userToChat._ID = getIntent().getStringExtra("ChatterID");
        userToChat._Name = getIntent().getStringExtra("ChatterName");
        userToChat._Surname = getIntent().getStringExtra("ChatterSurname");


        SharedPreferences sharedPref = ChattingActivity.this.getSharedPreferences("ChattingActivity", MODE_PRIVATE);
        userToChat._ID = sharedPref.getString("ChattingActivity_userToChat_ID", null);
        userToChat._Name = sharedPref.getString("ChattingActivity_userToChat_Name", null);
        userToChat._Surname = sharedPref.getString("ChattingActivity_userToChat_Surname", null);

        ((TextView)findViewById(R.id.ChatPersonNameTextView)).setText(userToChat._Name + " " + userToChat._Surname);

            chatterUserID = userToChat._ID;
            messages = new ArrayList<Message>();

            // saving room ID


        // current user ID get

        _DBReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(currentUserID == null) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Map<String, String> tu = (HashMap<String, String>) child.getValue();
                        if (tu.get("_Email").trim().equals(_Auth.getCurrentUser().getEmail())) {
                            currentUserID = tu.get("_ID").trim();
                            messages = new ArrayList<Message>();
                            FindOrCreateRoom();
                            //ReadMessagesOnce();
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        _DBReference.child("messages").addChildEventListener(new ChildEventListener() { // TO IMPROVE PERFORMANCE CHANGE PATH TO DEEPER WHEN U ALREADY FOUND A ROOM
                                                                                        // OR CREATE MESSAGE1, MESSAGE2, MESSAGE3 ETC IN ROOM NODE
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Log.d("x", dataSnapshot.getValue().toString());
                Map<String, String> tu = (HashMap<String, String>) dataSnapshot.getValue();
                final Message message = new Message(tu.get("_Content"), tu.get("_SenderID"), tu.get("_RoomID"));
                if(message._RoomID.trim().equals(roomID))
                {
                    messages.add(message);

                    if(message._SenderID.trim().equals(currentUserID))
                    {
                        AddSentMessage(message._Content);
                    }
                    else
                        AddIncommingMessage(message._Content);

                    //((ScrollView)findViewById(R.id.MessagesScrollView)).fullScroll(View.FOCUS_DOWN);
                    Toast.makeText(ChattingActivity.this, "New message", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void ReadMessagesOnce()
    {
        // download data once
        _DBReference.child("messages").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messages.clear();
                for(DataSnapshot child : dataSnapshot.getChildren())
                {
                    Map<String, String> tu = (HashMap<String, String>) child.getValue();
                    if(tu.get("_RoomID").trim().equals(roomID))
                        messages.add(new Message(tu.get("_Content"), tu.get("_SenderID"), tu.get("_RoomID")));

                }

                // set messages to list view
                ScrollView messagesScrollView = (ScrollView)findViewById(R.id.MessagesScrollView);
                for(Message x : messages)
                {
                    if(x._SenderID.trim().equalsIgnoreCase(currentUserID))
                    {
                        AddSentMessage(x._Content);
                    }
                    else
                        AddIncommingMessage(x._Content);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void AddSentMessage(String pContent)
    {
/*        Button button = new Button(ChattingActivity.this);
        button.setText(pContent);
        button.setBackgroundColor(getResources().getColor(R.color.headerTextWhite));
        button.setBackground(getResources().getDrawable(R.drawable.btn_rounded));

        button.setAllCaps(false);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(5, 20, 5, 20);
        button.setLayoutParams(params);

        button.setGravity(Gravity.RIGHT);
        button.setMaxWidth(100);

        ((LinearLayout)findViewById(R.id.MessagesLinearLayout)).addView(button);*/

        // test with text view
        TextView temp = new TextView(ChattingActivity.this);
        temp.setText(pContent);
        temp.setGravity(Gravity.RIGHT);
        temp.setPadding(15, 15, 15, 15);
        temp.setBackground(getResources().getDrawable(R.drawable.btn_rounded));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(5, 20, 5, 20);
        params.gravity = Gravity.RIGHT;

        temp.setAutoSizeTextTypeUniformWithConfiguration(1, 17, 1, TypedValue.COMPLEX_UNIT_DIP);
        temp.setLayoutParams(params);
        ((LinearLayout)findViewById(R.id.MessagesLinearLayout)).addView(temp);
        //

    }

    private void AddIncommingMessage(String pContent)
    {
/*        Button button = new Button(ChattingActivity.this);
        button.setText(pContent);
        button.setBackground(getResources().getDrawable(R.drawable.btn_rounded_sent_messages));

        button.setAllCaps(false);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(5, 20, 5, 20);
        button.setLayoutParams(params);

        button.setGravity(Gravity.LEFT);
        button.setMaxWidth(100);
        ((LinearLayout)findViewById(R.id.MessagesLinearLayout)).addView(button);*/

        // test with text view
        TextView temp = new TextView(ChattingActivity.this);
        temp.setText(pContent);
        temp.setGravity(Gravity.LEFT);
        temp.setPadding(15, 15, 15, 15);
        temp.setBackground(getResources().getDrawable(R.drawable.btn_rounded_sent_messages));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(5, 20, 5, 20);
        params.gravity = Gravity.LEFT;

        temp.setAutoSizeTextTypeUniformWithConfiguration(1, 17, 1, TypedValue.COMPLEX_UNIT_DIP);
        temp.setLayoutParams(params);
        ((LinearLayout)findViewById(R.id.MessagesLinearLayout)).addView(temp);
        //
    }

    private void FindOrCreateRoom() // funkcja do poprawki. nie wpisuje jednego z ID1 albo ID2
    {
        _DBReference.child("rooms").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren())
                {
                    Room tempRoom = child.getValue(Room.class);

                    // tu poprawka!
                    if(currentUserID == null)
                        currentUserID = currentUserID;

                    //
                    if(tempRoom.ID1.trim().equals(currentUserID) && tempRoom.ID2.trim().equals(chatterUserID))
                    {
                        roomID = tempRoom.ID;
                        break;
                    }
                    if(tempRoom.ID1.trim().equals(chatterUserID) &&  tempRoom.ID2.trim().equals(currentUserID))
                    {
                        roomID = tempRoom.ID;
                        break;
                    }
                    //
                }

                if(roomID != null)
                {
                    TextView chatterName = (TextView) findViewById(R.id.ChatPersonNameTextView);
                    chatterName.setText(userToChat._Name + " " + userToChat._Surname);
                }
                else // need to create new room
                {
                    DatabaseReference roomRefference = _DBReference.child("rooms");

                    Room newRoom = new Room();
                    if(currentUserID == null)
                        currentUserID = _Auth.getUid();
                    newRoom.ID1 = currentUserID;
                    newRoom.ID2 = chatterUserID;

                    String uniqueKeyIdentifier = _DBReference.child("rooms").push().getKey();
                    newRoom.ID = uniqueKeyIdentifier;

                    Map<String, Object> newRoomMap = newRoom.toMap();
                    Map<String, Object> mapObjectToSendToServer = new HashMap<>();
                    mapObjectToSendToServer.put("/rooms/" + uniqueKeyIdentifier, newRoomMap);

                    _DBReference.updateChildren(mapObjectToSendToServer);

                    roomID = newRoom.ID;
                    TextView chatterName = (TextView) findViewById(R.id.ChatPersonNameTextView);
                    chatterName.setText(userToChat._Name + " " + userToChat._Surname);
                }
                Log.d("Current room", roomID);
                ReadMessagesOnce();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void SendMessage(View view)
    {
        EditText messageButton = (EditText)findViewById(R.id.messageContentTextView);
        Message currentMessage = new Message(messageButton.getText().toString(), currentUserID, roomID.toString());

        ((EditText)findViewById(R.id.messageContentTextView)).setText("");

        String uniqueKeyIdentifier = _DBReference.child("messages").push().getKey();
        currentMessage._ID = uniqueKeyIdentifier;

        Map<String, Object> newMessageMap = currentMessage.toMap();
        Map<String, Object> mapObjectToSendToServer = new HashMap<>();

        mapObjectToSendToServer.put("/messages/"+uniqueKeyIdentifier, newMessageMap);
        _DBReference.updateChildren(mapObjectToSendToServer);

    }


}