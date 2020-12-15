package com.example.orderfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.orderfood.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth mAuth;

    MaterialEditText edtPhone, edtName,edtPassword, edtAddress,edtMail;
    TextView goToLogin;
    Button btnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtName = (MaterialEditText)findViewById(R.id.edtName);
        edtPassword = (MaterialEditText)findViewById(R.id.edtPassword);
        edtPhone = (MaterialEditText)findViewById(R.id.edtPhone);
        edtAddress = (MaterialEditText)findViewById(R.id.edtAddress);
        edtMail = (MaterialEditText)findViewById(R.id.edtMail);

        goToLogin =(TextView)findViewById(R.id.goToLogin);

        btnSignUp = (Button)findViewById(R.id.btnSignUp);

        mAuth = FirebaseAuth.getInstance();

//        FirebaseDatabase datebase = FirebaseDatabase.getInstance();
//        DatabaseReference table_user = datebase.getReference("User");

        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(SignUp.this,SignIn.class);
                startActivity(homeIntent);
                finish();
            }
        });


        btnSignUp.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                String name  = edtName.getText().toString().trim();
                String pass = edtPassword.getText().toString().trim();
                String email = edtMail.getText().toString().trim();
                String phone = edtPhone.getText().toString().trim();
                String address = edtAddress.getText().toString().trim();

                if(pass.isEmpty() || phone.isEmpty() || name.isEmpty() || address.isEmpty() || email.isEmpty()){
                    Toast.makeText(SignUp.this,"Wypełnij wszystkie pola"  , Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(pass.length()<8 || pass.length()>20)
                    {
                        Toast.makeText(SignUp.this,"Hasło musi mieć od 8 do 20 znaków"  , Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        mAuth.createUserWithEmailAndPassword(email, pass)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            User user = new User(name,pass,address,email,phone,10);
                                            FirebaseDatabase.getInstance().getReference("Users")
                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Toast.makeText( SignUp.this,"Rejestracja powiodła się ", Toast.LENGTH_SHORT).show();
                                                        Intent homeIntent = new Intent(SignUp.this,SignIn.class);
                                                        startActivity(homeIntent);
                                                        finish();
                                                    }else
                                                    {
                                                        Toast.makeText(SignUp.this,"Rejestracja nie powiodła się 1", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }else {
                                            Toast.makeText(SignUp.this,"Rejestracja nie powiodła się, adres e-mail musi byc unikalny", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                    }


//                    ProgressDialog mDialog = new ProgressDialog(SignUp.this);
//                    mDialog.setMessage("Prosze czekac..");
//                    mDialog.show();
//
//                    table_user.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                            if(snapshot.child(edtPhone.getText().toString()).exists())
//                            {
//                                mDialog.dismiss();
//                                Toast.makeText(SignUp.this,"Numer juz istnieje", Toast.LENGTH_SHORT).show();
//
//                            }
//                            else
//                            {
//                                mDialog.dismiss();
//                                User user = new User(edtName.getText().toString(),edtPassword.getText().toString(),edtName.getText().toString(),edtMail.getText().toString(),edtPhone.getText().toString(),10);
//                                table_user.child(edtPhone.getText().toString()).setValue(user);
//                                Toast.makeText( SignUp.this,"Register succes", Toast.LENGTH_SHORT).show();
//                                finish();
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });

                }


            }
        });
    }
}