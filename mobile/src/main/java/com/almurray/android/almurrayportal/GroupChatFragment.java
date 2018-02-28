package com.almurray.android.almurrayportal;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.almurray.android.almurrayportal.utils.FileUtils;
import com.almurray.android.almurrayportal.utils.PreferenceUtils;
import com.almurray.android.almurrayportal.utils.TextUtils;
import com.almurray.android.almurrayportal.utils.WebUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;
import com.sendbird.android.AdminMessage;
import com.sendbird.android.BaseChannel;
import com.sendbird.android.BaseMessage;
import com.sendbird.android.FileMessage;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.Member;
import com.sendbird.android.PreviousMessageListQuery;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;
import com.sendbird.android.UserMessage;
import com.squareup.picasso.Picasso;


import org.apache.http.conn.ssl.StrictHostnameVerifier;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


public class GroupChatFragment extends Fragment {

    private static final String CONNECTION_HANDLER_ID = "CONNECTION_HANDLER_GROUP_CHAT";

    private static final String LOG_TAG = GroupChatFragment.class.getSimpleName();

    private static final int STATE_NORMAL = 0;
    private static final int STATE_EDIT = 1;

    private static final String CHANNEL_HANDLER_ID = "CHANNEL_HANDLER_GROUP_CHANNEL_CHAT";
    private static final String STATE_CHANNEL_URL = "STATE_CHANNEL_URL";
    private static final int INTENT_REQUEST_CHOOSE_MEDIA = 301;
    private static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 13;
    static final String EXTRA_CHANNEL_URL = "EXTRA_CHANNEL_URL";

    private InputMethodManager mIMM;
    private HashMap<BaseChannel.SendFileMessageWithProgressHandler, FileMessage> mFileProgressHandlerMap;

    private RelativeLayout mRootLayout;
    private RecyclerView mRecyclerView;
    private GroupChatAdapter mChatAdapter;
    private LinearLayoutManager mLayoutManager;
    private EditText mMessageEditText;
    private Button mMessageSendButton;
    private ImageButton mUploadFileButton;
    private View mCurrentEventLayout;
    private TextView mCurrentEventText;
    private TextView noCanDo;

    private GroupChannel mChannel;
    private String mChannelUrl;
    private PreviousMessageListQuery mPrevMessageListQuery;

    private boolean mIsTyping;

    private int mCurrentState = STATE_NORMAL;
    private BaseMessage mEditingMessage = null;

    /**
     * To create an instance of this fragment, a Channel URL should be required.
     */
    public static GroupChatFragment newInstance(@NonNull String channelUrl) {
        GroupChatFragment fragment = new GroupChatFragment();

        Bundle args = new Bundle();
        args.putString(GroupChannelListFragment.EXTRA_GROUP_CHANNEL_URL, channelUrl);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIMM = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mFileProgressHandlerMap = new HashMap<>();

        if (savedInstanceState != null) {
            // Get channel URL from saved state.
            mChannelUrl = savedInstanceState.getString(STATE_CHANNEL_URL);
        } else {
            // Get channel URL from GroupChannelListFragment.
            mChannelUrl = getArguments().getString(GroupChannelListFragment.EXTRA_GROUP_CHANNEL_URL);
        }

        Log.d(LOG_TAG, mChannelUrl);

        mChatAdapter = new GroupChatAdapter(getActivity());
        setUpChatListAdapter();

        // Load messages from cache.
        mChatAdapter.load(mChannelUrl);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_group_chat, container, false);

        setRetainInstance(true);

        mRootLayout = (RelativeLayout) rootView.findViewById(R.id.layout_group_chat_root);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_group_chat);

        mCurrentEventLayout = rootView.findViewById(R.id.layout_group_chat_current_event);
        mCurrentEventText = (TextView) rootView.findViewById(R.id.text_group_chat_current_event);

        mMessageEditText = (EditText) rootView.findViewById(R.id.edittext_group_chat_message);
        mMessageSendButton = (Button) rootView.findViewById(R.id.button_group_chat_send);
        mUploadFileButton = (ImageButton) rootView.findViewById(R.id.button_group_chat_upload);

        noCanDo = (TextView) rootView.findViewById(R.id.noTypeText);

        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    mMessageSendButton.setEnabled(true);
                } else {
                    mMessageSendButton.setEnabled(false);
                }
            }
        });

        mMessageSendButton.setEnabled(false);
        mMessageSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentState == STATE_EDIT) {
                    String userInput = mMessageEditText.getText().toString();
                    if (userInput.length() > 0) {
                        if (mEditingMessage != null) {
                            editMessage(mEditingMessage, userInput);
                        }
                    }
                    setState(STATE_NORMAL, null, -1);
                } else {
                    String userInput = mMessageEditText.getText().toString();
                    if (userInput.length() > 0) {
                        sendUserMessage(userInput);
                        mMessageEditText.setText("");
                    }
                }
            }
        });

        mUploadFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestMedia();
            }
        });

        mIsTyping = false;
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!mIsTyping) {
                    setTypingStatus(true);
                }

                if (s.length() == 0) {
                    setTypingStatus(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        setUpRecyclerView();

        setHasOptionsMenu(true);



        return rootView;
    }

    public void refresh() {
        if (mChannel == null) {
            GroupChannel.getChannel(mChannelUrl, new GroupChannel.GroupChannelGetHandler() {
                @Override
                public void onResult(GroupChannel groupChannel, SendBirdException e) {
                    if (e != null) {
                        // Error!
                        e.printStackTrace();
                        return;
                    }

                    mChannel = groupChannel;
                    mChatAdapter.setChannel(mChannel);
                    mChatAdapter.loadLatestMessages(30, new BaseChannel.GetMessagesHandler() {
                        @Override
                        public void onResult(List<BaseMessage> list, SendBirdException e) {
                            mChatAdapter.markAllMessagesAsRead();
                        }
                    });
                    updateActionBarTitle();
                }
            });
        } else {
            mChannel.refresh(new GroupChannel.GroupChannelRefreshHandler() {
                @Override
                public void onResult(SendBirdException e) {
                    if (e != null) {
                        // Error!
                        e.printStackTrace();
                        return;
                    }

                    mChatAdapter.loadLatestMessages(30, new BaseChannel.GetMessagesHandler() {
                        @Override
                        public void onResult(List<BaseMessage> list, SendBirdException e) {
                            mChatAdapter.markAllMessagesAsRead();
                        }
                    });
                    updateActionBarTitle();
                }
            });
        }


    }

    @Override
    public void onResume() {
        super.onResume();

        //mChatAdapter.setContext(getActivity().getSupportFragmentManager().ge); // Glide bug fix (java.lang.IllegalArgumentException: You cannot start a load for a destroyed activity)

        // Gets channel from URL user requested
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        SharedPreferences prefs = getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        Log.d(LOG_TAG, "HELLO WE ARE RESUMING AT "+mChannelUrl);
        Intent intent = getActivity().getIntent();
        intent.putExtra("currentState", "inChat");
        intent.putExtra("inChatURL", mChannelUrl);
        refresh();
        SendBird.addChannelHandler(CHANNEL_HANDLER_ID, new SendBird.ChannelHandler() {
            @Override
            public void onMessageReceived(BaseChannel baseChannel, BaseMessage baseMessage) {
                if (baseChannel.getUrl().equals(mChannelUrl)) {
                    mChatAdapter.markAllMessagesAsRead();
                    // Add new message to view
                    mChatAdapter.addFirst(baseMessage);
                }
            }

            @Override
            public void onMessageDeleted(BaseChannel baseChannel, long msgId) {
                super.onMessageDeleted(baseChannel, msgId);
                if (baseChannel.getUrl().equals(mChannelUrl)) {
                    mChatAdapter.delete(msgId);
                }
            }

            @Override
            public void onMessageUpdated(BaseChannel channel, BaseMessage message) {
                super.onMessageUpdated(channel, message);
                if (channel.getUrl().equals(mChannelUrl)) {
                    mChatAdapter.update(message);
                }
            }

            @Override
            public void onReadReceiptUpdated(GroupChannel channel) {
                if (channel.getUrl().equals(mChannelUrl)) {
                    mChatAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTypingStatusUpdated(GroupChannel channel) {
                if (channel.getUrl().equals(mChannelUrl)) {
                    List<Member> typingUsers = channel.getTypingMembers();
                    displayTyping(typingUsers);
                }
            }

        });

        SendBird.addConnectionHandler(CONNECTION_HANDLER_ID, new SendBird.ConnectionHandler() {
            @Override
            public void onReconnectStarted() {
            }

            @Override
            public void onReconnectSucceeded() {
                refresh();
            }

            @Override
            public void onReconnectFailed() {
            }
        });

        if (SendBird.getConnectionState() == SendBird.ConnectionState.OPEN) {
            refresh();
        } else {
            if (SendBird.reconnect()) {
                // Will call onReconnectSucceeded()
            } else {
                String userId = PreferenceUtils.getUserId(getActivity());
                if (userId == null) {
                    Toast.makeText(getActivity(), "Uh oh please send error code: noSausagesAtChat", Toast.LENGTH_LONG).show();
                    return;
                }

                SendBird.connect(userId, new SendBird.ConnectHandler() {
                    @Override
                    public void onConnected(User user, SendBirdException e) {
                        if (e != null) {
                            e.printStackTrace();
                            return;
                        }

                        refresh();
                    }
                });
            }
        }


    }

    @Override
    public void onPause() {
        setTypingStatus(false);

        SendBird.removeChannelHandler(CHANNEL_HANDLER_ID);
        SendBird.removeConnectionHandler(CONNECTION_HANDLER_ID);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        // Save messages to cache.
        mChatAdapter.save();

        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_CHANNEL_URL, mChannelUrl);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_REQUEST_CHOOSE_MEDIA && resultCode == Activity.RESULT_OK) {
            // If user has successfully chosen the image, show a dialog to confirm upload.
            if (data == null) {
                Log.d(LOG_TAG, "data is null!");
                return;
            }

            sendFileWithThumbnail(data.getData());
        }

        // Set this as true to restore background connection management.
        SendBird.setAutoBackgroundDetection(true);
    }

    private void setUpRecyclerView() {
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mChatAdapter);
        mRecyclerView.setAdapter(mChatAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (mLayoutManager.findLastVisibleItemPosition() == mChatAdapter.getItemCount() - 1) {
                    mChatAdapter.loadPreviousMessages(30, null);
                }
            }
        });
    }

    private void setUpChatListAdapter() {
        mChatAdapter.setItemClickListener(new GroupChatAdapter.OnItemClickListener() {
            @Override
            public void onUserMessageItemClick(UserMessage message) {
                // Restore failed message and remove the failed message from list.
                if (mChatAdapter.isFailedMessage(message)) {
                    retryFailedMessage(message);
                    return;
                }

                // Message is sending. Do nothing on click event.
                if (mChatAdapter.isTempMessage(message)) {
                    return;
                }


                if (message.getCustomType().equals(GroupChatAdapter.URL_PREVIEW_CUSTOM_TYPE)) {
                    try {
                        UrlPreviewInfo info = new UrlPreviewInfo(message.getData());
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(info.getUrl()));
                        startActivity(browserIntent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFileMessageItemClick(FileMessage message) {
                // Load media chooser and remove the failed message from list.
                if (mChatAdapter.isFailedMessage(message)) {
                    retryFailedMessage(message);
                    return;
                }

                // Message is sending. Do nothing on click event.
                if (mChatAdapter.isTempMessage(message)) {
                    return;
                }


                onFileMessageClicked(message, message.getSender().getUserId() );
            }
        });

        mChatAdapter.setItemLongClickListener(new GroupChatAdapter.OnItemLongClickListener() {
            @Override
            public void onUserMessageItemLongClick(UserMessage message, int position ) {

                showMessageOptionsDialog(message, position, message.getMessage(), message.getSender().getUserId());
            }

            @Override
            public void onFileMessageItemLongClick(FileMessage message) {
            }

            @Override
            public void onAdminMessageItemLongClick(AdminMessage message) {
            }
        });
    }

    private void showMessageOptionsDialog(final BaseMessage message, final int position, final String text, final String userID) {
        String[] options = new String[] { "Copy Text", "Edit message", "Delete Message", "Dino"};
        String[] otherOptions = new String[] {"Copy Text", "View Profile", "Dino"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if(userID.equals(SendBird.getCurrentUser().getUserId())) {

            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        ClipboardManager _clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        _clipboard.setPrimaryClip(ClipData.newPlainText("Message", text));
                    } else if (which == 1) {
                        setState(STATE_EDIT, message, position);
                    } else if (which == 2) {
                        deleteMessage(message);
                    } else if (which == 3) {
                        startActivity(new Intent(getActivity(), dinosaur.class));
                    }
                }
            });
            builder.create().show();
        }else {
            builder.setItems(otherOptions, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        ClipboardManager _clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        _clipboard.setPrimaryClip(ClipData.newPlainText("Message", text));
                    } else if (which == 1) {
                        Intent i = new Intent(getActivity(), editProfileView.class);
                        i.putExtra("currentSendbird", userID);
                        startActivity(i);
                    } else if (which == 2) {
                        startActivity(new Intent(getActivity(), dinosaur.class));
                    }
                }
            });
            builder.create().show();
        }

    }

    private void showFileOptionsDialog(final BaseMessage message) {
        String[] options = new String[] { "Delete message" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteMessage(message);
            }
        });
        builder.create().show();
    }

    private void setState(int state, BaseMessage editingMessage, final int position) {
        switch (state) {
            case STATE_NORMAL:
                mCurrentState = STATE_NORMAL;
                mEditingMessage = null;

                mUploadFileButton.setVisibility(View.VISIBLE);
                mMessageSendButton.setText("SEND");
                mMessageEditText.setText("");

//                mIMM.hideSoftInputFromWindow(mMessageEditText.getWindowToken(), 0);
                break;

            case STATE_EDIT:
                mCurrentState = STATE_EDIT;
                mEditingMessage = editingMessage;

                mUploadFileButton.setVisibility(View.GONE);
                mMessageSendButton.setText("SAVE");
                String messageString = ((UserMessage)editingMessage).getMessage();
                if (messageString == null) {
                    messageString = "";
                }
                mMessageEditText.setText(messageString);
                if (messageString.length() > 0) {
                    mMessageEditText.setSelection(0, messageString.length());
                }

                mMessageEditText.requestFocus();
                mIMM.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.scrollToPosition(position);
                    }
                }, 500);
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    private void retryFailedMessage(final BaseMessage message) {
        new AlertDialog.Builder(getActivity())
                .setMessage("Retry?")
                .setPositiveButton(R.string.resend_message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            if (message instanceof UserMessage) {
                                String userInput = ((UserMessage) message).getMessage();
                                sendUserMessage(userInput);
                            } else if (message instanceof FileMessage) {
                                Uri uri = mChatAdapter.getTempFileMessageUri(message);
                                sendFileWithThumbnail(uri);
                            }
                            mChatAdapter.removeFailedMessage(message);
                        }
                    }
                })
                .setNegativeButton(R.string.delete_message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_NEGATIVE) {
                            mChatAdapter.removeFailedMessage(message);
                        }
                    }
                }).show();
    }

    /**
     * Display which users are typing.
     * If more than two users are currently typing, this will state that "multiple users" are typing.
     *
     * @param typingUsers The list of currently typing users.
     */
    private void displayTyping(List<Member> typingUsers) {

        if (typingUsers.size() > 0) {
            mCurrentEventLayout.setVisibility(View.VISIBLE);
            String string;

            if (typingUsers.size() == 1) {
                string = typingUsers.get(0).getNickname() + " is typing";
            } else if (typingUsers.size() == 2) {
                string = typingUsers.get(0).getNickname() + " " + typingUsers.get(1).getNickname() + " is typing";
            } else {
                string = "Multiple users are typing";
            }
            mCurrentEventText.setText(string);
        } else {
            mCurrentEventLayout.setVisibility(View.GONE);
        }
    }

    private void requestMedia() {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // If storage permissions are not granted, request permissions at run-time,
            // as per < API 23 guidelines.
            requestStoragePermissions();
        } else {
            Intent intent = new Intent();

            // Pick images or videos
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setType("*/*");
                String[] mimeTypes = {"image/*", "video/*"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            } else {
                intent.setType("image/* video/*");
            }

            intent.setAction(Intent.ACTION_GET_CONTENT);

            //Always show the chooser (if there are multiple options available)
            startActivityForResult(Intent.createChooser(intent, "Select Media"), INTENT_REQUEST_CHOOSE_MEDIA);
            //getActivity().getIntent().putExtra("comingFromChatURL", mChannelUrl);
            //startActivity(new Intent(getActivity(), maintenanceActivity.class));

            // Set this as false to maintain connection
            // even when an external Activity is started.
            SendBird.setAutoBackgroundDetection(false);
        }
    }

    private void requestStoragePermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            Snackbar.make(mRootLayout, "Storage access permissions are required to upload/download files.",
                    Snackbar.LENGTH_LONG)
                    .setAction("Okay", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    PERMISSION_WRITE_EXTERNAL_STORAGE);
                        }
                    })
                    .show();
        } else {
            // Permission has not been granted yet. Request it directly.
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_WRITE_EXTERNAL_STORAGE);
        }
    }

    private void onFileMessageClicked(final FileMessage message, final String userID) {


        String[] options = new String[] { "View File", "Delete File" };

        if(userID.equals(SendBird.getCurrentUser().getUserId())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        String type = message.getType().toLowerCase();
                        if (type.startsWith("image")) {
                            Intent i = new Intent(getActivity(), PhotoViewerActivity.class);
                            i.putExtra("url", message.getUrl());
                            i.putExtra("type", message.getType());
                            startActivity(i);
                        } else if (type.startsWith("video")) {
                            Intent intent = new Intent(getActivity(), MediaPlayerActivity.class);
                            intent.putExtra("url", message.getUrl());
                            startActivity(intent);
                        } else {
                            showDownloadConfirmDialog(message);
                        }
                    } else if (which == 1) {
                        deleteMessage(message);
                    }
                }
            });


            builder.create().show();

        } else {
            String type = message.getType().toLowerCase();
            if (type.startsWith("image")) {
                Intent i = new Intent(getActivity(), PhotoViewerActivity.class);
                i.putExtra("url", message.getUrl());
                i.putExtra("type", message.getType());
                startActivity(i);
            } else if (type.startsWith("video")) {
                Intent intent = new Intent(getActivity(), MediaPlayerActivity.class);
                intent.putExtra("url", message.getUrl());
                startActivity(intent);
            } else {
                showDownloadConfirmDialog(message);
            }
        }


    }

    private void showDownloadConfirmDialog(final FileMessage message) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // If storage permissions are not granted, request permissions at run-time,
            // as per < API 23 guidelines.
            requestStoragePermissions();
        } else {
            new AlertDialog.Builder(getActivity())
                    .setMessage("Download file?")
                    .setPositiveButton(R.string.download, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                FileUtils.downloadFile(getActivity(), message.getUrl(), message.getName());
                            }
                        }
                    })
                    .setNegativeButton(R.string.cancel, null).show();
        }

    }

    private void updateActionBarTitle() {

        if(mChannel.getMemberCount() <= 2) {
            List<Member> members = mChannel.getMembers();
            if (!members.get(0).getNickname().equals(SendBird.getCurrentUser().getNickname())) {
                getActivity().setTitle(members.get(1).getNickname());

            }
        }

        if(mChannel.getMemberCount() > 2) {
            List<Member> members = mChannel.getMembers();
            getActivity().setTitle(mChannel.getName());
        }

        if(mChannel.getName().equals("Announcements")) {
            SharedPreferences prefs = getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);

            if(prefs.getString("staffLevel", "").equals("admin")) {
                noCanDo.setVisibility(View.GONE);
                mMessageEditText.setVisibility(View.VISIBLE);
                mMessageSendButton.setVisibility(View.VISIBLE);
                mUploadFileButton.setVisibility(View.VISIBLE);
            } else {
                noCanDo.setVisibility(View.VISIBLE);
                mMessageEditText.setVisibility(View.GONE);
                mMessageSendButton.setVisibility(View.GONE);
                mUploadFileButton.setVisibility(View.GONE);
            }

        }



    }

    private void sendUserMessageWithUrl(final String text, String url) {
//        new WebUtils.UrlPreviewAsyncTask() {
//            @Override
//            protected void onPostExecute(UrlPreviewInfo info) {
//                UserMessage tempUserMessage = null;
//                BaseChannel.SendUserMessageHandler handler = new BaseChannel.SendUserMessageHandler() {
//                    @Override
//                    public void onSent(UserMessage userMessage, SendBirdException e) {
//                        if (e != null) {
//                            // Error!
//                            Log.e(LOG_TAG, e.toString());
//                            Toast.makeText(
//                                    getActivity(),
//                                    "Send failed with error " + e.getCode() + ": " + e.getMessage(), Toast.LENGTH_SHORT)
//                                    .show();
//                            mChatAdapter.markMessageFailed(userMessage.getRequestId());
//                            return;
//                        }
//
//                        // Update a sent message to RecyclerView
//                        mChatAdapter.markMessageSent(userMessage);
//                    }
//                };
//
//                try {
//                    // Sending a message with URL preview information and custom type.
//                    String jsonString = info.toJsonString();
//                    tempUserMessage = mChannel.sendUserMessage(text, jsonString, GroupChatAdapter.URL_PREVIEW_CUSTOM_TYPE, handler);
//                } catch (Exception e) {
//                    // Sending a message without URL preview information.
//                    tempUserMessage = mChannel.sendUserMessage(text, handler);
//                }
//
//
//                // Display a user message to RecyclerView
//                mChatAdapter.addFirst(tempUserMessage);
//            }
//        }.execute(url);

        UserMessage tempUserMessage = mChannel.sendUserMessage(text, new BaseChannel.SendUserMessageHandler() {
            @Override
            public void onSent(UserMessage userMessage, SendBirdException e) {
                if (e != null) {
                    // Error!
                    Log.e(LOG_TAG, e.toString());
                    Toast.makeText(
                            getActivity(),
                            "Send failed with error " + e.getCode() + ": " + e.getMessage(), Toast.LENGTH_SHORT)
                            .show();
                    mChatAdapter.markMessageFailed(userMessage.getRequestId());
                    return;
                }

                // Update a sent message to RecyclerView
                mChatAdapter.markMessageSent(userMessage);

            }
        });

        // Display a user message to RecyclerView
        mChatAdapter.addFirst(tempUserMessage);

    }

    private void sendUserMessage(final String text) {
//        List<String> urls = WebUtils.extractUrls(text);
//        if (urls.size() > 0) {
//            sendUserMessageWithUrl(text, urls.get(0));
//            return;
//        }




        UserMessage tempUserMessage = mChannel.sendUserMessage(text, new BaseChannel.SendUserMessageHandler() {
            @Override
            public void onSent(final UserMessage userMessage, SendBirdException e) {
                if (e != null) {
                    // Error!
                    Log.e(LOG_TAG, e.toString());
                    Toast.makeText(
                            getActivity(),
                            "Send failed with error " + e.getCode() + ": " + e.getMessage(), Toast.LENGTH_SHORT)
                            .show();
                    mChatAdapter.markMessageFailed(userMessage.getRequestId());
                    return;
                }

                // Update a sent message to RecyclerView
                mChatAdapter.markMessageSent(userMessage);
                final SharedPreferences preferences = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);


                if (mChannel.getMemberCount() > 2) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    List<Member> members = mChannel.getMembers();
                    for(int i = 0; i < members.size(); i++) {

                        if (!members.get(i).getUserId().equals(preferences.getString("sendbirdIDC", ""))) {
                            try {
                                String jsonResponse;

                                URL url = new URL("https://onesignal.com/api/v1/notifications");
                                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                                con.setUseCaches(false);
                                con.setDoOutput(true);
                                con.setDoInput(true);

                                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                                con.setRequestProperty("Authorization", "Basic NGI4NjYzMGYtYjFhMi00ZDQ1LWIzNDYtNWI2NTRhNjRhNTY3");
                                con.setRequestMethod("POST");



                                String strJsonBody = "{"
                                        + "\"app_id\": \"2144f1b0-6c2c-44e2-ab7d-80fbae543240\","
                                        + "\"ios_badgeType\": \"Increase\","
                                        + "\"ios_badgeCount\": \"1\","
                                        + "\"mutable_content\": \"true\","
                                        + "\"content_available\": \"true\","
                                        + "\"headings\": {\"en\": \""+mChannel.getName()+"\"},"
                                        + "\"subtitle\": {\"en\": \""+preferences.getString("currentFullName", "")+"\"},"
                                        + "\"filters\": [{\"field\": \"tag\", \"key\": \"sendbird\", \"relation\": \"=\", \"value\": \""+members.get(i).getUserId()+"\"}],"
                                        + "\"data\": {\"foo\": \"bar\"},"
                                        + "\"contents\": {\"en\": \""+text+"\"}"
                                        + "}";


                                System.out.println("strJsonBody:\n" + strJsonBody);

                                byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                                con.setFixedLengthStreamingMode(sendBytes.length);

                                OutputStream outputStream = con.getOutputStream();
                                outputStream.write(sendBytes);

                                int httpResponse = con.getResponseCode();
                                System.out.println("httpResponse: " + httpResponse);

                                if (httpResponse >= HttpURLConnection.HTTP_OK
                                        && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                                    Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                                    jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                                    scanner.close();
                                } else {
                                    Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                                    jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                                    scanner.close();
                                }
                                System.out.println("jsonResponse:\n" + jsonResponse);

                            } catch (Throwable t) {
                                t.printStackTrace();
                            }
                        }
                    }

                } else {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    List<Member> members = mChannel.getMembers();
                    for(int i = 0; i < members.size(); i++) {

                        if (!members.get(i).getUserId().equals(preferences.getString("sendbirdIDC", ""))) {
                            try {
                                String jsonResponse;

                                URL url = new URL("https://onesignal.com/api/v1/notifications");
                                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                                con.setUseCaches(false);
                                con.setDoOutput(true);
                                con.setDoInput(true);

                                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                                con.setRequestProperty("Authorization", "Basic NGI4NjYzMGYtYjFhMi00ZDQ1LWIzNDYtNWI2NTRhNjRhNTY3");
                                con.setRequestMethod("POST");



                                String strJsonBody = "{"
                                        + "\"app_id\": \"2144f1b0-6c2c-44e2-ab7d-80fbae543240\","
                                        + "\"ios_badgeType\": \"Increase\","
                                        + "\"ios_badgeCount\": \"1\","
                                        + "\"mutable_content\": \"true\","
                                        + "\"content_available\": \"true\","
                                        + "\"headings\": {\"en\": \"From: "+preferences.getString("currentFullName", "")+"\"},"
                                        + "\"filters\": [{\"field\": \"tag\", \"key\": \"sendbird\", \"relation\": \"=\", \"value\": \""+members.get(i).getUserId()+"\"}],"
                                        + "\"data\": {\"foo\": \"bar\"},"
                                        + "\"contents\": {\"en\": \""+text+"\"}"
                                        + "}";


                                System.out.println("strJsonBody:\n" + strJsonBody);

                                byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                                con.setFixedLengthStreamingMode(sendBytes.length);

                                OutputStream outputStream = con.getOutputStream();
                                outputStream.write(sendBytes);

                                int httpResponse = con.getResponseCode();
                                System.out.println("httpResponse: " + httpResponse);

                                if (httpResponse >= HttpURLConnection.HTTP_OK
                                        && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                                    Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                                    jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                                    scanner.close();
                                } else {
                                    Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                                    jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                                    scanner.close();
                                }
                                System.out.println("jsonResponse:\n" + jsonResponse);

                            } catch (Throwable t) {
                                t.printStackTrace();
                            }
                        }
                    }
                }
            }
        });



        if(text.equals("?help")) {
            final String botMessage = "--Al Murray Bot--\nAll Commands:\n?users : Total users\n?bots : Total bots\n" +
                    "?lastUpdate : date of last update\n?lastDatabase : last update to database\n?latestVer : latest version\n?currentVer : Your current version" +
                    "\n?currentBeta : Current beta\n?currentAlpha : Current Alpha\n?reportIssue {issue} : reports issue\n?myRole : Your role" +
                    "\n?joke : Tells a joke\n?ban {name} : Mod only, bans user\n?postsLeft : Number of GaryGram posts left\n?totalIssues : Current total issues";
            UserMessage message = mChannel.sendUserMessage(botMessage, new BaseChannel.SendUserMessageHandler() {
                @Override
                public void onSent(UserMessage userMessage, SendBirdException e) {
                    mChatAdapter.markMessageSent(userMessage);
                }
            });

            mChatAdapter.addFirst(message);
        } else if(text.equals("?totalIssues")) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("reported");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Integer total = dataSnapshot.child("lastReport").getValue(Integer.class);
                    final String botMessage = "--Al Murray Bot--\nTotal Open Issues: "+String.valueOf(total);
                    UserMessage message = mChannel.sendUserMessage(botMessage, new BaseChannel.SendUserMessageHandler() {
                        @Override
                        public void onSent(UserMessage userMessage, SendBirdException e) {
                            mChatAdapter.markMessageSent(userMessage);
                        }
                    });

                    mChatAdapter.addFirst(message);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else if(text.equals("?postsLeft")) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("globalvariables");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Integer lastPost = dataSnapshot.child("lastPost").getValue(Integer.class);
                    final String botMessage = "--Al Murray Bot--\nGaryGram posts left: "+String.valueOf(lastPost);
                    UserMessage message = mChannel.sendUserMessage(botMessage, new BaseChannel.SendUserMessageHandler() {
                        @Override
                        public void onSent(UserMessage userMessage, SendBirdException e) {
                            mChatAdapter.markMessageSent(userMessage);
                        }
                    });

                    mChatAdapter.addFirst(message);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else if (text.equals("?users")) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("botStuff");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Integer users = dataSnapshot.child("users").getValue(Integer.class);
                    final String botMessage = "--Al Murray Bot--\nCurrent users: "+String.valueOf(users);
                    UserMessage message = mChannel.sendUserMessage(botMessage, new BaseChannel.SendUserMessageHandler() {
                        @Override
                        public void onSent(UserMessage userMessage, SendBirdException e) {
                            mChatAdapter.markMessageSent(userMessage);
                        }
                    });

                    mChatAdapter.addFirst(message);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else if (text.equals("?bots")) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("botStuff");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Integer bots = dataSnapshot.child("bots").getValue(Integer.class);
                    final String botMessage = "--Al Murray Bot--\nCurrent bots: "+String.valueOf(bots);
                    UserMessage message =  mChannel.sendUserMessage(botMessage, new BaseChannel.SendUserMessageHandler() {
                        @Override
                        public void onSent(UserMessage userMessage, SendBirdException e) {
                            mChatAdapter.markMessageSent(userMessage);
                        }
                    });
                    mChatAdapter.addFirst(message);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else if (text.equals("?lastUpdate")) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("botStuff");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String lastupdate = dataSnapshot.child("lastUpdate").getValue(String.class);
                    final String botMessage = "--Al Murray Bot--\nLast Update: "+String.valueOf(lastupdate);
                    UserMessage message = mChannel.sendUserMessage(botMessage, new BaseChannel.SendUserMessageHandler() {
                        @Override
                        public void onSent(UserMessage userMessage, SendBirdException e) {
                            mChatAdapter.markMessageSent(userMessage);
                        }
                    });
                    mChatAdapter.addFirst(message);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else if (text.equals("?lastDatabase")) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("botStuff");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String lastupdate = dataSnapshot.child("lastDatabase").getValue(String.class);
                    final String botMessage = "--Al Murray Bot--\nLast Databse Update: "+String.valueOf(lastupdate);
                    UserMessage message =  mChannel.sendUserMessage(botMessage, new BaseChannel.SendUserMessageHandler() {
                        @Override
                        public void onSent(UserMessage userMessage, SendBirdException e) {
                            mChatAdapter.markMessageSent(userMessage);
                        }
                    });
                    mChatAdapter.addFirst(message);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else if (text.equals("?latestVer")) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("botStuff");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String currentVer = dataSnapshot.child("currentVer").getValue(String.class);
                    final String botMessage = "--Al Murray Bot--\nLatest Version: "+String.valueOf(currentVer);
                    UserMessage message =  mChannel.sendUserMessage(botMessage, new BaseChannel.SendUserMessageHandler() {
                        @Override
                        public void onSent(UserMessage userMessage, SendBirdException e) {
                            mChatAdapter.markMessageSent(userMessage);
                        }
                    });
                    mChatAdapter.addFirst(message);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else if(text.equals("?currentVer")) {
            final String botMessage = "--Al Murray Bot--\nCurrent Version: "+R.string.all_app_version;

            UserMessage message = mChannel.sendUserMessage(botMessage, new BaseChannel.SendUserMessageHandler() {
                @Override
                public void onSent(UserMessage userMessage, SendBirdException e) {
                    mChatAdapter.markMessageSent(userMessage);
                }
            });
            mChatAdapter.addFirst(message);


        } else if (text.equals("?currentBeta")) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("botStuff");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String beta = dataSnapshot.child("currentBeta").getValue(String.class);
                    final String botMessage = "--Al Murray Bot--\nCurrent Beta: "+String.valueOf(beta);
                    UserMessage message = mChannel.sendUserMessage(botMessage, new BaseChannel.SendUserMessageHandler() {
                        @Override
                        public void onSent(UserMessage userMessage, SendBirdException e) {
                            mChatAdapter.markMessageSent(userMessage);
                        }
                    });
                    mChatAdapter.addFirst(message);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else if (text.equals("?currentAlpha")) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("botStuff");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String alpha = dataSnapshot.child("currentAlpha").getValue(String.class);
                    final String botMessage = "--Al Murray Bot--\nCurrent Alpha: "+String.valueOf(alpha);
                    UserMessage message = mChannel.sendUserMessage(botMessage, new BaseChannel.SendUserMessageHandler() {
                        @Override
                        public void onSent(UserMessage userMessage, SendBirdException e) {
                            mChatAdapter.markMessageSent(userMessage);
                        }
                    });
                    mChatAdapter.addFirst(message);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        } else if (text.equals("?joke")) {
            Random rand = new Random();
            int i = rand.nextInt(5);
            if (i == 0) { i = 1; }
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("botStuff").child("jokes").child(String.valueOf(i));
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String joke = dataSnapshot.child("joke").getValue(String.class);
                    final String botMessage = "--Al Murray Bot--\n"+String.valueOf(joke);
                    UserMessage message = mChannel.sendUserMessage(botMessage, new BaseChannel.SendUserMessageHandler() {
                        @Override
                        public void onSent(UserMessage userMessage, SendBirdException e) {
                            mChatAdapter.markMessageSent(userMessage);
                        }
                    });
                    mChatAdapter.addFirst(message);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else if (text.equals("?myRole")) {
            SharedPreferences preferences = getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
            String role = preferences.getString("staffLevel", "member");
            final String botMessage = "--Al Murray Bot--\nYour Role: "+role;

            UserMessage message = mChannel.sendUserMessage(botMessage, new BaseChannel.SendUserMessageHandler() {
                @Override
                public void onSent(UserMessage userMessage, SendBirdException e) {
                    mChatAdapter.markMessageSent(userMessage);
                }
            });
            mChatAdapter.addFirst(message);
        }

        try {
            SharedPreferences preferences = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
            if(preferences.getString("staffLevel", "").equals("staff") || preferences.getString("staffLevel", "").equals("admin")) {
                if(text.substring(0, 4).equals("?ban")) {
                    try {

                        String rest = text.substring(5, text.length());
                        final String[] restWords = rest.split("\\s+");
                        Log.d("TAG", restWords[0]);
                        Log.d("TAG", rest.replace(restWords[0], ""));

                        try {
                            final String rest2 = rest;
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("globalvariables").child("ids");
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    try {
                                        String id  = dataSnapshot.child(String.valueOf(restWords[0].trim())).getValue(String.class);
                                        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("users").child(id);
                                        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                dataSnapshot.child("banned").getRef().setValue(true);
                                                dataSnapshot.child("bannedR").getRef().setValue(rest2.replace(restWords[0], "").trim());
                                                Log.d("TAG", restWords[1]);
                                                String botmessage = "--Al Murray Bot--\nBanned: "+restWords[0]+"\nReason: "+rest2.replace(restWords[0], "");
                                                UserMessage message = mChannel.sendUserMessage(botmessage, new BaseChannel.SendUserMessageHandler() {
                                                    @Override
                                                    public void onSent(UserMessage userMessage, SendBirdException e) {
                                                        mChatAdapter.markMessageSent(userMessage);
                                                    }
                                                });
                                                mChatAdapter.addFirst(message);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    } catch (Exception eei) {
                                        UserMessage message = mChannel.sendUserMessage("--Al Murray Bot--\n Error Banning : User not found", new BaseChannel.SendUserMessageHandler() {
                                            @Override
                                            public void onSent(UserMessage userMessage, SendBirdException e) {
                                                mChatAdapter.markMessageSent(userMessage);
                                            }
                                        });
                                        mChatAdapter.addFirst(message);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        } catch (Exception eii) {
                            UserMessage message = mChannel.sendUserMessage("--Al Murray Bot--\nError : Correct format is ?ban {id} {reason}", new BaseChannel.SendUserMessageHandler() {
                                @Override
                                public void onSent(UserMessage userMessage, SendBirdException e) {
                                    mChatAdapter.markMessageSent(userMessage);
                                }
                            });
                            mChatAdapter.addFirst(message);
                        }
                    } catch (Exception ei) {
                        UserMessage message = mChannel.sendUserMessage("--Al Murray Bot--\nError : Correct format is ?ban {id} {reason}", new BaseChannel.SendUserMessageHandler() {
                            @Override
                            public void onSent(UserMessage userMessage, SendBirdException e) {
                                mChatAdapter.markMessageSent(userMessage);
                            }
                        });
                        mChatAdapter.addFirst(message);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        }


        try {
            SharedPreferences preferences = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
            if(preferences.getString("staffLevel", "").equals("staff") || preferences.getString("staffLevel", "").equals("admin")) {
                if(text.substring(0, 6).equals("?unban")) {
                    Log.d("TAG", "GEKKI");
                    try {
                        final String rest2 = text.substring(7,text.length()).trim();
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("globalvariables").child("ids");
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                try {
                                    String id = dataSnapshot.child(rest2).getValue(String.class);
                                    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("users").child(id);
                                    ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            dataSnapshot.child("banned").getRef().setValue(false);
                                            dataSnapshot.child("bannedR").getRef().setValue("");
                                            Log.d("TAG", rest2);
                                            String botmessage = "--Al Murray Bot--\nUnbanned: "+rest2;
                                            UserMessage message = mChannel.sendUserMessage(botmessage, new BaseChannel.SendUserMessageHandler() {
                                                @Override
                                                public void onSent(UserMessage userMessage, SendBirdException e) {
                                                    mChatAdapter.markMessageSent(userMessage);
                                                }
                                            });
                                            mChatAdapter.addFirst(message);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                } catch (Exception eei) {
                                    UserMessage message = mChannel.sendUserMessage("--Al Murray Bot--\n Error Banning : User not found", new BaseChannel.SendUserMessageHandler() {
                                        @Override
                                        public void onSent(UserMessage userMessage, SendBirdException e) {
                                            mChatAdapter.markMessageSent(userMessage);
                                        }
                                    });
                                    mChatAdapter.addFirst(message);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    } catch (Exception eii) {
                        UserMessage message = mChannel.sendUserMessage("--Al Murray Bot--\nError : Correct format is ?unban {id}", new BaseChannel.SendUserMessageHandler() {
                            @Override
                            public void onSent(UserMessage userMessage, SendBirdException e) {
                                mChatAdapter.markMessageSent(userMessage);
                            }
                        });
                        mChatAdapter.addFirst(message);
                    }
                }
            }

        } catch (Exception e) {

        }

        try {
            if(text.substring(0, 12).equals("?reportIssue")) {

                try {
                    String reason = text.substring(13,text.length());
                    reason = reason.trim();
                    if (reason.equals("") || reason.equals(" ")) {
                        UserMessage message = mChannel.sendUserMessage("--Al Murray Bot--\nError : Please specify an issue", new BaseChannel.SendUserMessageHandler() {
                            @Override
                            public void onSent(UserMessage userMessage, SendBirdException e) {
                                mChatAdapter.markMessageSent(userMessage);
                            }
                        });
                        mChatAdapter.addFirst(message);
                    } else {
                        final String reason2 = reason;
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("reported");
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Log.d("TAG", "REPORT ISSUE 2");

                                Integer old = dataSnapshot.child("lastReport").getValue(Integer.class);
                                Integer newN = old  + 1;
                                dataSnapshot.child(String.valueOf(newN)).child("type").getRef().setValue("chat");
                                dataSnapshot.child(String.valueOf(newN)).child("reason").getRef().setValue(reason2);
                                dataSnapshot.child("lastReport").getRef().setValue(newN);

                                String botMessage = "--Al Murray Bot--\nIssue Reported\nIssue # : "+String.valueOf(newN)+"\nIssue : "+reason2;
                                UserMessage message = mChannel.sendUserMessage(botMessage, new BaseChannel.SendUserMessageHandler() {
                                    @Override
                                    public void onSent(UserMessage userMessage, SendBirdException e) {
                                        mChatAdapter.markMessageSent(userMessage);
                                    }
                                });
                                mChatAdapter.addFirst(message);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                } catch (Exception ei){
                    Log.d("TAG", ei.getMessage());
                    UserMessage message = mChannel.sendUserMessage("--Al Murray Bot--\nError : Please specify an issue", new BaseChannel.SendUserMessageHandler() {
                        @Override
                        public void onSent(UserMessage userMessage, SendBirdException e) {
                            mChatAdapter.markMessageSent(userMessage);
                        }
                    });
                    mChatAdapter.addFirst(message);
                }





            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Display a user message to RecyclerView
        mChatAdapter.addFirst(tempUserMessage);
    }

    /**
     * Notify other users whether the current user is typing.
     *
     * @param typing Whether the user is currently typing.
     */
    private void setTypingStatus(boolean typing) {
        if (mChannel == null) {
            return;
        }

        if (typing) {
            mIsTyping = true;
            mChannel.startTyping();
        } else {
            mIsTyping = false;
            mChannel.endTyping();
        }
    }

    /**
     * Sends a File Message containing an image file.
     * Also requests thumbnails to be generated in specified sizes.
     *
     * @param uri The URI of the image, which in this case is received through an Intent request.
     */
    private void sendFileWithThumbnail(Uri uri) {
        // Specify two dimensions of thumbnails to generate
        List<FileMessage.ThumbnailSize> thumbnailSizes = new ArrayList<>();
        thumbnailSizes.add(new FileMessage.ThumbnailSize(240, 240));
        thumbnailSizes.add(new FileMessage.ThumbnailSize(320, 320));

        Hashtable<String, Object> info = FileUtils.getFileInfo(getActivity(), uri);

        if (info == null) {
            Toast.makeText(getActivity(), "Extracting file information failed.", Toast.LENGTH_LONG).show();
            return;
        }

        final String path = (String) info.get("path");
        final File file = new File(path);
        final String name = file.getName();
        final String mime = (String) info.get("mime");
        final int size = (Integer) info.get("size");

        if (path.equals("")) {
            Toast.makeText(getActivity(), "File must be located in local storage.", Toast.LENGTH_LONG).show();
        } else {
            BaseChannel.SendFileMessageWithProgressHandler progressHandler = new BaseChannel.SendFileMessageWithProgressHandler() {
                @Override
                public void onProgress(int bytesSent, int totalBytesSent, int totalBytesToSend) {
                    FileMessage fileMessage = mFileProgressHandlerMap.get(this);
                    if (fileMessage != null && totalBytesToSend > 0) {
                        int percent = (totalBytesSent * 100) / totalBytesToSend;
                        mChatAdapter.setFileProgressPercent(fileMessage, percent);
                    }
                }

                @Override
                public void onSent(FileMessage fileMessage, SendBirdException e) {
                    if (e != null) {
                        Toast.makeText(getActivity(), "" + e.getCode() + ":" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        mChatAdapter.markMessageFailed(fileMessage.getRequestId());
                        return;
                    }

                    mChatAdapter.markMessageSent(fileMessage);
                }
            };

            // Send image with thumbnails in the specified dimensions
            FileMessage tempFileMessage = mChannel.sendFileMessage(file, name, mime, size, "", null, thumbnailSizes, progressHandler);

            mFileProgressHandlerMap.put(progressHandler, tempFileMessage);

            mChatAdapter.addTempFileMessageInfo(tempFileMessage, uri);
            mChatAdapter.addFirst(tempFileMessage);
        }
    }

    private void editMessage(final BaseMessage message, String editedMessage) {
        mChannel.updateUserMessage(message.getMessageId(), editedMessage, null, null, new BaseChannel.UpdateUserMessageHandler() {
            @Override
            public void onUpdated(UserMessage userMessage, SendBirdException e) {
                if (e != null) {
                    // Error!
                    Toast.makeText(getActivity(), "Error " + e.getCode() + ": " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                mChatAdapter.loadLatestMessages(30, new BaseChannel.GetMessagesHandler() {
                    @Override
                    public void onResult(List<BaseMessage> list, SendBirdException e) {
                        mChatAdapter.markAllMessagesAsRead();
                    }
                });
            }
        });
    }

    /**
     * Deletes a message within the channel.
     * Note that users can only delete messages sent by oneself.
     *
     * @param message The message to delete.
     */
    private void deleteMessage(final BaseMessage message) {
        mChannel.deleteMessage(message, new BaseChannel.DeleteMessageHandler() {
            @Override
            public void onResult(SendBirdException e) {
                if (e != null) {
                    // Error!
                    Toast.makeText(getActivity(), "Error: You have to be the owner of the message ", Toast.LENGTH_SHORT).show();
                    return;
                }

                mChatAdapter.loadLatestMessages(30, new BaseChannel.GetMessagesHandler() {
                    @Override
                    public void onResult(List<BaseMessage> list, SendBirdException e) {
                        mChatAdapter.markAllMessagesAsRead();
                    }
                });
            }
        });
    }
}
