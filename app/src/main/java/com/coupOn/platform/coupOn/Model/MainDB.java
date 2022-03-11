package com.coupOn.platform.coupOn.Model;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executor;

public class MainDB
{
    private static MainDB mainDB;

    private MainDB(){ }

    public static synchronized MainDB getInstance( ) {
        if (mainDB == null)
            mainDB=new MainDB();
        return mainDB;
    }

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private FirebaseFirestore users = FirebaseFirestore.getInstance();

    HashMap<String, User> curUser;



    public MainDB(HashMap<String, User> curUser)
    {
        //Retrieve Current User
        users = FirebaseFirestore.getInstance(); //Connects to fireStore.
        mAuth = FirebaseAuth.getInstance(); //Connects to Authentication.
        String uid = mAuth.getCurrentUser().getUid(); //Gets the UID of the current User.
        final User[] currentUser = new User[1]; //Firebase wants to change the User to Final when retrieving the data.
        DocumentReference dr = users.collection("users").document(uid); //This is how u retrieve data from fireStore.
        dr.addSnapshotListener((Executor) this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                currentUser[0] = new User(value.getString("Email"), value.getString("FullName")); //Get from currentUser the Email and the FullName from the fireStore.
            }
        });
        this.curUser = new HashMap<>();
        this.curUser.put(uid, currentUser[0]);
    }

    public HashMap<String, User> getCurUser() {
        return curUser;
    }

    public void setCurUser(HashMap<String, User> curUser) {
        this.curUser = curUser;
    }
}

