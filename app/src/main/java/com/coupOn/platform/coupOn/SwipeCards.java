package com.coupOn.platform.coupOn;

import android.content.Context;
import android.os.Bundle;

import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.shashank.platform.coup_on.R;

import java.util.ArrayList;
import java.util.List;

//MainActivity
public class SwipeCards extends AppCompatActivity {

    private Cards cards_data[];

    //private ArrayList<String> al;
    private ArrayAdapterCoupon arrayAdapter;// <String> || ArrayAdapter --> arrayAdapter
    private int i;

    ListView listView;
    private ArrayList<Cards> rowItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_cards);


        this.rowItems = new ArrayList<Cards>();
        this.rowItems.add(new Cards("Ido", "Laser"));
        //al.add("c");
        //al.add("python");
        //al.add("java");
        //al.add("html");
        //al.add("c++");
        //al.add("css");
        //al.add("javascript");

        arrayAdapter = new ArrayAdapterCoupon(this, R.layout.item, rowItems);

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0); //changed
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                Toast.makeText(SwipeCards.this, "Left!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Toast.makeText(SwipeCards.this, "Right!", Toast.LENGTH_SHORT).show();
            }

            @Override //required DataSnapShot
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                //Cards item = new Cards( dataSnapshot);
                Cards item = new Cards("id", "No Coupons");// need to be changed based on DB
                rowItems.add(item);// "XML ".concat(String.valueOf(i))
                arrayAdapter.notifyDataSetChanged();
                Log.d("LIST", "notified");
                i++;
            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(SwipeCards.this, "Clicked!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /*public void checkUser(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("Users");
        userDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if(snapshot.exists()){
                    Cards item = new Cards(snapshot.getKey(), snapshot.child("name").getValue().toString());
                    rowItems.add(item);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/

    static void makeToast(Context ctx, String s){
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

}