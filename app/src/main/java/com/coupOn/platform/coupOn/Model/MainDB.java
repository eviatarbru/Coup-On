package com.coupOn.platform.coupOn.Model;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Set;

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

    private final HashMap<String, User> curUser = new HashMap<>();
    private final HashMap<String, User> chattingUsers = new HashMap<>();


    private MainDB()
    {
        currentUser();
    }

    private void currentUser()
    {
        //Retrieve Current User
        users = FirebaseFirestore.getInstance(); //Connects to fireStore.
        mAuth = FirebaseAuth.getInstance(); //Connects to Authentication.
        String uid = mAuth.getCurrentUser().getUid(); //Gets the UID of the current User.
        final User[] currentUser = new User[1]; //Firebase wants to change the User to Final when retrieving the data.
        DocumentReference dr = users.collection("users").document(uid); //This is how u retrieve data from fireStore.
        dr.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String email = value.getString("Email");
                String fullName = value.getString("FullName");
                String chatUsers = value.getString("ChatUsers");
                if(chatUsers != null)
                    curUser.put(uid, new User(email, fullName, chatUsers)); //Get from currentUser the Email and the FullName from the fireStore.
                else
                    curUser.put(uid, new User(email, fullName)); //Get from currentUser the Email and the FullName from the fireStore.
                //System.out.println(currentUser[0] + "bush1");
            }
        });
        //System.out.println(currentUser[0] + "bush2");
//        curUser.put(uid, currentUser[0]);
    }


    /*  Steps for Success
        1. new Thread(new GetUserFirebase(uid)).start();
        2. Get user.
     */
    public static class GetUserFirebase implements Runnable
    {
        User user;
        String uidFB;

        public GetUserFirebase(String uidFB) {
            this.uidFB = uidFB;
        }

        @Override
        public void run() {
            user = MainDB.getInstance().getUserFirebase(uidFB);
            System.out.println(MainDB.getInstance().getCurUser());
        }
    }

    public User getUserFirebase(String uid1)
    {
        final User[] userInfo = new User[1]; //Firebase wants to change the User to Final when retrieving the data.
        User user;
        System.out.println(uid1);

        DocumentReference dr = users.collection("users").document(uid1); //This is how u retrieve data from fireStore.
        dr.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String email = value.getString("Email");
                String fullName = value.getString("FullName");
                userInfo[0] = new User(email, fullName); //Get from currentUser the Email and the FullName from the fireStore.
            }
        });
        while(userInfo[0] == null) {
        }
        return userInfo[0];
    }

    public void getChatUsersInfo(String uid1)
    {
        final User[] userInfo = new User[1]; //Firebase wants to change the User to Final when retrieving the data.
        User user;
        System.out.println(uid1);
//        this.chattingUsers = new HashMap<>();

        DocumentReference dr = users.collection("users").document(uid1); //This is how u retrieve data from fireStore.
        dr.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String email = value.getString("Email");
                String fullName = value.getString("FullName");
                chattingUsers.put(uid1, new User(email, fullName));//Get from currentUser the Email and the FullName from the fireStore.
            }
        });
    }


    public HashMap<String, User> getCurUser() {
        return curUser;
    }

//    public void setCurUser(HashMap<String, User> curUser) {
//        curUser = curUser;
//    }

    public HashMap<String, User> getChattingUsers() {
        return chattingUsers;
    }

    public Set<String> getChattingUIDS()
    {
        return chattingUsers.keySet();
    }

//    public void setChattingUsers(HashMap<String, User> chattingUsers) {
//        chattingUsers = chattingUsers;
//    }
}

