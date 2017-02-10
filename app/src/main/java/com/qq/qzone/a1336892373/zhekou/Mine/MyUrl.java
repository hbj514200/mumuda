package com.qq.qzone.a1336892373.zhekou.Mine;

import android.os.Handler;
import android.os.Message;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.qq.qzone.a1336892373.zhekou.tools.httpConn;

public class MyUrl {

    public void getUrl(final String source, final MineCallback callback){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("myData");

        myRef.child(httpConn.stringToMD5(source)).child("me").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null)
                    callback.setMineUrl( dataSnapshot.getValue().toString() );
                else {
                    DatabaseReference myRef2 = database.getReference("newURL");
                    myRef2.push().setValue( source );
                }
            }
            @Override public void onCancelled(DatabaseError databaseError) {}
        });
    }

}
