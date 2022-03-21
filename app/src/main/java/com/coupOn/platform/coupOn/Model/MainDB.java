package com.coupOn.platform.coupOn.Model;

import static com.coupOn.platform.coupOn.InterestsScreen.TAG;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;

public class MainDB
{
    private static MainDB instance;

    public static MainDB getInstance() {
        if(instance == null)
            instance = new MainDB();
        return instance;
    }

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private FirebaseFirestore users = FirebaseFirestore.getInstance();

    private HashMap<String, User> curUser;
    private HashMap<String, User> chattingUsers;


    public MainDB()
    {
        currentUser();
//        chattingUsers();

    }

    public void currentUser()
    {
        //Retrieve Current User
        users = FirebaseFirestore.getInstance(); //Connects to fireStore.
        mAuth = FirebaseAuth.getInstance(); //Connects to Authentication.
        String uid = mAuth.getCurrentUser().getUid(); //Gets the UID of the current User.
        System.out.println(uid);
        final User[] currentUser = new User[1]; //Firebase wants to change the User to Final when retrieving the data.
        DocumentReference dr = users.collection("users").document(uid); //This is how u retrieve data from fireStore.
        dr.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                currentUser[0] = new User(value.getString("Email"), value.getString("FullName")); //Get from currentUser the Email and the FullName from the fireStore.
            }
        });
        this.curUser = new HashMap<>();
        this.curUser.put(uid, currentUser[0]);
    }

//    public void chattingUsers()
//    {
//        this.chattingUsers = new HashMap<>();
//        //Retrieve Current User
//        users = FirebaseFirestore.getInstance(); //Connects to fireStore.
//        mAuth = FirebaseAuth.getInstance(); //Connects to Authentication.
//        String uid = mAuth.getCurrentUser().getUid(); //Gets the UID of the current User.
//        final User[] userChat = new User[1]; //Firebase wants to change the User to Final when retrieving the data.
//        ArrayList<String> uids2 = new ArrayList<>();
//        DocumentReference dr = users.collection("users").document(uid); //This is how u retrieve data from fireStore.
//        dr.addSnapshotListener((Executor) this, new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                String uid = value.getString("chatUID");
//                HashMap<String, User> usersInfo = new HashMap<>();
//                String[] uids = uid.split(", ");
//                for(int i = 0; i < uids.length; i++)
//                {
//                    uids2.add(uids[i]);
//                }
//            }
//        });
//        for(String uid1: uids2)
//        {
//            DocumentReference dr2 = users.collection("users").document(uid1); //This is how u retrieve data from fireStore.
//            dr2.addSnapshotListener((Executor) this, new EventListener<DocumentSnapshot>() {
//                @Override
//                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error)
//                {
//                    userChat[0] = new User(value.getString("Email"), value.getString("FullName")); //Get from currentUser the Email and the FullName from the fireStore.
//                }
//            });
//            this.chattingUsers.put(uid1, userChat[0]);
//        }
//    }


    public void getUserChat(String uid) //Use it for UserChatList to get the users that chat with the current user.
    {
        final User[] chatUser = new User[1]; //Firebase wants to change the User to Final when retrieving the data.
        DocumentReference dr = users.collection("users").document(uid); //This is how u retrieve data from fireStore.
        dr.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String uids = value.getString("userUids");
                String [] userUid = uids.split(", ");
                for(int i = 0; i < userUid.length; i++)
                {
                    DocumentReference dr = users.collection("users").document(userUid[i]); //This is how u retrieve data from fireStore.
                    dr.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            chatUser[0] = new User(value.getString("Email"), value.getString("FullName")); //Get from currentUser the Email and the FullName from the fireStore.
                        }
                    });
                    chattingUsers.put(userUid[i], chatUser[0]);
                }
            }
        });
    }

    public boolean insertChatToFirebase(String uid1, String uid2)
    {

        return true;
    }



    public HashMap<String, User> getCurUser() {
        return curUser;
    }

    public void setCurUser(HashMap<String, User> curUser) {
        this.curUser = curUser;
    }

    public HashMap<String, User> getChattingUsers() {
        return chattingUsers;
    }

    public void setChattingUsers(HashMap<String, User> chattingUsers) {
        this.chattingUsers = chattingUsers;
    }
}
