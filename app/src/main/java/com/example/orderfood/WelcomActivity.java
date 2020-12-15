package com.example.orderfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.orderfood.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WelcomActivity extends AppCompatActivity {

    Button logout;

    FirebaseUser user;
    DatabaseReference reference;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom);



        //Log Out
        logout = (Button)findViewById(R.id.wyloguj);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(WelcomActivity.this,MainActivity.class));
                finish();
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        final TextView fullNameText = (TextView)findViewById(R.id.fullName);
        final TextView PhoneText = (TextView)findViewById(R.id.phone);
        final TextView AddressText = (TextView)findViewById(R.id.address);
        final TextView PointsText = (TextView)findViewById(R.id.points);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    String fullName = userProfile.getName();
                    String phone = userProfile.getPhone();
                    String address = userProfile.getAddress();
                    Integer points = userProfile.getPoints();

                    fullNameText.setText(fullName);
                    PhoneText.setText(phone);
                    AddressText.setText(address);
                    PointsText.setText(points.toString());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WelcomActivity.this,"Cos poszło nie tak",Toast.LENGTH_SHORT).show();
            }
        });
    }
}