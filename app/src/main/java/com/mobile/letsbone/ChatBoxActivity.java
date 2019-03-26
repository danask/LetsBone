package com.mobile.letsbone;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class ChatBoxActivity extends AppCompatActivity {
    public RecyclerView myRecylerView ;
    public List<ChatMessage> MessageList ;
    public ChatBoxAdapter chatBoxAdapter;
    public EditText messagetxt ;
    public Button send ;
    //declare socket object
    private Socket socket;

    public String partner = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_box);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle("Chat Room");


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        setHasOptionsMenu(true);

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        final String currentUser = sharedPref.getString("currentUser", "-");



        messagetxt = (EditText) findViewById(R.id.message);  // from input box
        send = (Button)findViewById(R.id.send);


        // get the nickame of the user

        if(getIntent().getExtras() != null)
            partner= (String)getIntent().getExtras().getString("NICKNAME");

        //connect you socket client to the server
        try {
//            socket = IO.socket("http://10.1.111.23:3000");
//            socket = IO.socket("http://10.1.111.92:3000");
//            socket = IO.socket("http://192.168.0.3:3000");
            socket = IO.socket("http://99.79.62.21:3000");
            socket.connect();
            socket.emit("join", partner);
        } catch (URISyntaxException e) {
            e.printStackTrace();

        }
       //setting up recyler
        MessageList = new ArrayList<>();
        myRecylerView = (RecyclerView) findViewById(R.id.messagelist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        myRecylerView.setLayoutManager(mLayoutManager);
        myRecylerView.setItemAnimator(new DefaultItemAnimator());


        messagetxt.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode==KeyEvent.KEYCODE_ENTER)
                {
//                    if(!messagetxt.getText().toString().isEmpty())
//                    {
//                        socket.emit("messagedetection", Nickname, messagetxt.getText().toString());
//
//                        // make instance of message
//
//                        ChatMessage m = new ChatMessage(Nickname, "S" + messagetxt.getText().toString());
//
//
//                        //add the message to the messageList
//
//                        MessageList.add(m);
//
//                        // add the new updated list to the dapter
//                        chatBoxAdapter = new ChatBoxAdapter(MessageList);
//
//                        // notify the adapter to update the recycler view
//
//                        chatBoxAdapter.notifyDataSetChanged();
//
//                        //set the adapter for the recycler view
//
//                        myRecylerView.setAdapter(chatBoxAdapter);
//
//
//
//                        messagetxt.setText(" ");
//
//                        hideKeyboard(ChatBoxActivity.this);
//                    }
                }
                return true;
            }
        });

        // message send action
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //retrieve the nickname and the message content and fire the event messagedetection
                if(!messagetxt.getText().toString().isEmpty())
                {
                    socket.emit("messagedetection", currentUser, messagetxt.getText().toString());

                    // make instance of message

                    ChatMessage m = new ChatMessage(currentUser, "S" + messagetxt.getText().toString());


                    //add the message to the messageList

                    MessageList.add(m);

                    // add the new updated list to the dapter
                    chatBoxAdapter = new ChatBoxAdapter(MessageList);

                    // notify the adapter to update the recycler view

                    chatBoxAdapter.notifyDataSetChanged();

                    //set the adapter for the recycler view

                    myRecylerView.setAdapter(chatBoxAdapter);



                    messagetxt.setText(" ");

                    hideKeyboard(ChatBoxActivity.this);
                }


            }
        });

        //implementing socket listeners
        socket.on("userjoinedthechat", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data = (String) args[0];

                        Toast.makeText(ChatBoxActivity.this,data,Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
        socket.on("userdisconnect", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data = (String) args[0];

                        Toast.makeText(ChatBoxActivity.this,data,Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        socket.on("message", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        try {
                            //extract data from fired event

                            String nickname = data.getString("senderNickname");
                            String message = data.getString("message");


                            Log.d("[=====CHAT=====]", "REC: "+ nickname + ", "+ message);

                            if(!nickname.equalsIgnoreCase(currentUser))
                            {
                                // make instance of message

                                ChatMessage m = new ChatMessage(nickname, message);


                                //add the message to the messageList

                                MessageList.add(m);

                                // add the new updated list to the dapter
                                chatBoxAdapter = new ChatBoxAdapter(MessageList);

                                // notify the adapter to update the recycler view

                                chatBoxAdapter.notifyDataSetChanged();

                                //set the adapter for the recycler view

                                myRecylerView.setAdapter(chatBoxAdapter);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
            }
        });
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View currentFocusedView = activity.getCurrentFocus();
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();

//        int id = item.getItemId();
//
//        if(id == R.id.action_settings);
//        {
//
//        }

        return true;
//        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_socket, menu);
        return super.onCreateOptionsMenu(menu);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        int id = item.getItemId();

//        if(id == R.id.actionSignOut)
//        {
//            auth.signOut();
//            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
//        }


        // TODO from FB

//        if(id == R.id.actionQuit)
//        {
//            Intent intent = new Intent(this, SignInActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.putExtra("Exit", true);
//            startActivity(intent);
//            finish();
//        }

//        return super.onOptionsItemSelected(item);
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.disconnect();
  }
}
