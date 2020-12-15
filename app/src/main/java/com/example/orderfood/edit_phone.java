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

public class edit_phone extends AppCompatActivity {

    FirebaseUser user;
    DatabaseReference reference;
    String userID;
    EditText editPhone;
    Button saveUpdatePhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phone);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        editPhone = (EditText)findViewById(R.id.editPhone);
        saveUpdatePhone = (Button)findViewById(R.id.saveUpdatePhone);

        saveUpdatePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editPhone.getText().toString().isEmpty()){
                    Toast.makeText(edit_phone.this,"Pole nie moze byc puste",Toast.LENGTH_SHORT).show();
                }else{
                    reference.child(userID).child("phone").setValue(editPhone.getText().toString().trim());
                    Toast.makeText(edit_phone.this,"Edycja zakonczona pomyślnie",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(edit_phone.this,Profil.class));
                    finish();
                }


            }
        });

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    editPhone.setText(userProfile.getPhone());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(edit_phone.this,"Cos poszło nie tak",Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void ClickCancelUpdate(View view){
        startActivity(new Intent(edit_phone.this,Profil.class));
        finish();
    }
}