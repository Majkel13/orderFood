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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {

    MaterialEditText edtPhone, edtPassword, edtEmail;
    Button btnSignIn;
    private FirebaseAuth mAuth;
    TextView goToRegister;

    FirebaseUser user;
    DatabaseReference reference;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtPassword = (MaterialEditText)findViewById(R.id.edtPassword);
        edtEmail = (MaterialEditText)findViewById(R.id.edtMail);

        btnSignIn = (Button)findViewById(R.id.btnSignIn);

        goToRegister = (TextView)findViewById(R.id.goToRegister);
        mAuth = FirebaseAuth.getInstance();

        //Firebase
//        FirebaseDatabase datebase = FirebaseDatabase.getInstance();
//        DatabaseReference table_user = datebase.getReference("User");

        goToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(SignIn.this,SignUp.class);
                startActivity(homeIntent);
                finish();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = edtPassword.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();

                if(pass.isEmpty()|| email.isEmpty() ){
                    Toast.makeText(SignIn.this,"Wypełnij wszystkie pola", Toast.LENGTH_SHORT).show();
                }
                else {

                    mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){



                                user = FirebaseAuth.getInstance().getCurrentUser();
                                reference = FirebaseDatabase.getInstance().getReference("Users");
                                userID = user.getUid();

                                reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        User userProfile = snapshot.getValue(User.class);

                                        if(userProfile != null){
                                            if(!userProfile.getAdmin()){
                                                Toast.makeText(SignIn.this, "Zalogowano", Toast.LENGTH_SHORT).show();
                                                Intent homeIntent = new Intent(SignIn.this,HomeActivity.class);
                                                startActivity(homeIntent);
                                                finish();
                                            }else{
                                                Toast.makeText(SignIn.this, "Brak uprawnień", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(SignIn.this,"Cos poszło nie tak",Toast.LENGTH_SHORT).show();
                                    }
                                });



                            }
                            else {
                                Toast.makeText(SignIn.this, "Błędne dane logowania", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }

            }
        });
    }
}