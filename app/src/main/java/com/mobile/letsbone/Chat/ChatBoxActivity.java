package com.mobile.letsbone.Chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.letsbone.Entities.ChatBoxData;
import com.mobile.letsbone.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatBoxActivity extends AppCompatActivity {
    //Toolbar
    Toolbar toolBar;

    //RecyclerView
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mChatAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager;

    //DatabaseReference
    private DatabaseReference mDatabaseReferenceUsers, mDatabaseReferenceMessages, databaseReference;
    private FirebaseAuth mAuth;

    //Array list object of ChatBoxData
    private ArrayList<ChatBoxData> chatBoxDataList = new ArrayList<>();

    //Private members
    private String mUserKey, mMatchKey, mMessageKey, mMatchFirstName, mMatchLastName;
    private EditText mMessageBox;
    private Button sendButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_box);

        //getting friends list selected user key
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        mMatchKey = getIntent().getExtras().getString("MatchKey");
        mUserKey = mAuth.getCurrentUser().getUid();


        //getting database reference for user
        mDatabaseReferenceUsers = databaseReference
                .child(mUserKey)
                .child("Connections")
                .child("Matches")
                .child(mMatchKey)
                .child("MessageId");

        //getting database reference for messages
        mDatabaseReferenceMessages = FirebaseDatabase.getInstance().getReference().child("Messages");

        getMessageId();

        //initiating connection with recycler view
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(false);
        mChatLayoutManager = new LinearLayoutManager(ChatBoxActivity.this);
        mRecyclerView.setLayoutManager(mChatLayoutManager);
        mChatAdapter = new ChatBoxCustomAdapter(chatBoxDataList, ChatBoxActivity.this);
        mRecyclerView.setAdapter(mChatAdapter);

        //creating connection with edittext and button
        mMessageBox = findViewById(R.id.message);
        sendButton = findViewById(R.id.send);

        //sendButton on click listener will send messages to friends
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });



    }

    //method to send messages
    private void sendMessage() {
        String sendText = mMessageBox.getText().toString();

        //condition so user cant send empty text
        if(!sendText.isEmpty()){
            DatabaseReference messageData = mDatabaseReferenceMessages.push();

            Map newMessage = new HashMap();
            newMessage.put("SentBy", mUserKey);
            newMessage.put("Text", sendText);

            messageData.setValue(newMessage);
        }
        mMessageBox.setText(null);
    }

    //method to get message id for each user
    private void getMessageId(){
        mDatabaseReferenceUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Condition to see if child exists
                if(dataSnapshot.exists()){
                    mMessageKey = dataSnapshot.getValue().toString();
                    mDatabaseReferenceMessages = mDatabaseReferenceMessages.child(mMessageKey);
                    getMessagesFromFireBase();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //method to get messages between useres
    private void getMessagesFromFireBase() {
        mDatabaseReferenceMessages.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //if child exist
                if(dataSnapshot.exists()){
                    String text = null;
                    String sentBy = null;

                    //if Text child is not null get the value and store it into string
                    if(dataSnapshot.child("Text").getValue()!= null){
                        text = dataSnapshot.child("Text").getValue().toString();
                    }
                    //if SentBy child is not null get teh value and store it into string
                    if(dataSnapshot.child("SentBy").getValue()!=null){
                        sentBy = dataSnapshot.child("SentBy").getValue().toString();
                    }

                    if(text!=null && sentBy!=null){
                        Boolean isCurrentUser = false;
                        if(sentBy.equals(mUserKey)){
                            isCurrentUser = true;
                        }
                        ChatBoxData chatBoxData = new ChatBoxData(text, isCurrentUser);
                        chatBoxDataList.add(chatBoxData);
                        mChatAdapter.notifyDataSetChanged();
                    }
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


}
