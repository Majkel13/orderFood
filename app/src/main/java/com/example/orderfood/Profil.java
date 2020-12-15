package com.example.orderfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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

public class Profil extends AppCompatActivity {

    DrawerLayout drawerLayout;

    FirebaseUser user;
    DatabaseReference reference;
    String userID;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        drawerLayout = findViewById(R.id.drawer_layout);
        TextView textView = (TextView)findViewById(R.id.titleContent);
        textView.setText("PROFIL");


        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        final TextView fullNameText = (TextView)findViewById(R.id.fullName);
        final TextView PhoneText = (TextView)findViewById(R.id.phone);
        final TextView AddressText = (TextView)findViewById(R.id.address);
        final TextView PointsText = (TextView)findViewById(R.id.points);
        final TextView EmailText = (TextView)findViewById(R.id.email);
        progressBar = (ProgressBar)findViewById(R.id.progressProfil) ;


        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    progressBar.setVisibility(View.GONE);
                    String fullName = userProfile.getName();
                    String phone = userProfile.getPhone();
                    String address = userProfile.getAddress();
                    Integer points = userProfile.getPoints();
                    String email = userProfile.getMail();

                    fullNameText.setText(fullName);
                    PhoneText.setText(phone);
                    AddressText.setText(address);
                    PointsText.setText(points.toString());
                    EmailText.setText(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profil.this,"Cos poszło nie tak",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public  void GotoEditPhone(View view){
        startActivity(new Intent(Profil.this,edit_phone.class));
        finish();
    }

    public  void GotoEditAdress(View view){
        startActivity(new Intent(Profil.this,EditAddress.class));
        finish();
    }

    public void ClickMenu(View view){
        HomeActivity.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view){
        HomeActivity.closeDrawer(drawerLayout);
    }

    public  void ClickProfil(View view){
        recreate();

    }
    public void ClickOrderStatus(View view){
        HomeActivity.redirectActivity(this,OrderStatus.class);
    }

    public void ClickCart(View view){
        HomeActivity.redirectActivity(this, Cart.class);
    }

    public void ClickHome(View view){
        HomeActivity.redirectActivity(this,HomeActivity.class);
    }

    public void ClickMenuFood(View view){
        HomeActivity.redirectActivity(this, Menu.class);
    }

    public void ClickLogout(View view){
        logout(this);
    }

    public  void logout(Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle("Wylogowanie");

        builder.setMessage("Jesteś pewien że chcesz się wylogować?");

        builder.setPositiveButton("TAK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //activity.finishAffinity();
                // System.exit(0);
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(activity,"Wylogowano"  , Toast.LENGTH_SHORT).show();

                finishAffinity();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("NIE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        HomeActivity.closeDrawer(drawerLayout);
    }

}