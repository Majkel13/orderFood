package com.example.orderfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.orderfood.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditAddress extends AppCompatActivity {

    FirebaseUser user;
    DatabaseReference reference;
    String userID;
    EditText editAdress;
    Button saveUpdateAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        editAdress = (EditText)findViewById(R.id.editAddress);
        saveUpdateAddress = (Button)findViewById(R.id.saveUpdateAddress);

        saveUpdateAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editAdress.getText().toString().isEmpty()){
                    Toast.makeText(EditAddress.this,"Pole nie może być puste",Toast.LENGTH_SHORT).show();
                }
                else{
                    reference.child(userID).child("address").setValue(editAdress.getText().toString().trim());
                    Toast.makeText(EditAddress.this,"Edycja zakonczona pomyślnie",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EditAddress.this,Profil.class));
                    finish();
                }


            }
        });

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    editAdress.setText(userProfile.getAddress());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditAddress.this,"Cos poszło nie tak",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void ClickCancelUpdate(View view){
        startActivity(new Intent(EditAddress.this,Profil.class));
        finish();
    }
}