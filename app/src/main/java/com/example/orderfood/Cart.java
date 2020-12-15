package com.example.orderfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.orderfood.Database.Database;
import com.example.orderfood.Model.Order;
import com.example.orderfood.Model.Request;
import com.example.orderfood.Model.User;
import com.example.orderfood.ViewHolder.CartAdapter;
import com.google.android.gms.common.internal.service.Common;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference reference, reference_user;

    TextView txtTotalPrice;
    Button btnPlace,btnCancelOrder;

    List<Order> cart = new ArrayList<>();
    CartAdapter adapter ;

    FirebaseUser user;
    String userID;

    RadioGroup radioGroup, radioGroupPoints;
    RadioButton radioButton, radioButtonPoints, radioButtonTenPoints, radioButtonTwentyPoints;
    EditText  editOrderAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        database = FirebaseDatabase.getInstance();
        reference=database.getReference("Requests");

        recyclerView = (RecyclerView)findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        txtTotalPrice = (TextView)findViewById(R.id.total);
        btnPlace = (Button)findViewById(R.id.btnPlaceOrder);
        btnCancelOrder = (Button)findViewById(R.id.btnCancelOrder);
        editOrderAddress =(EditText)findViewById(R.id.editOrderAddress);

        radioButtonTenPoints = (RadioButton)findViewById(R.id.tenPoints);
        radioButtonTwentyPoints = (RadioButton)findViewById(R.id.twentyPoints);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference_user = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        reference_user.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                editOrderAddress.setText(userProfile.getAddress());
                if (userProfile.getPoints()>=10){
                    radioButtonTenPoints.setEnabled(true);
                    radioButtonTenPoints.setText("10 punktów = -10%");
                }else if(userProfile.getPoints()>=20){
                    radioButtonTwentyPoints.setEnabled(true);
                    radioButtonTwentyPoints.setText("20 punktów = -20%");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        loadListFood("0");
        radioGroup = findViewById(R.id.radioGroup);
        radioGroupPoints = findViewById(R.id.radioGroupPoints);

        radioGroupPoints.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb=(RadioButton)findViewById(checkedId);
                Toast.makeText(getApplicationContext(), rb.getText(), Toast.LENGTH_SHORT).show();

               loadListFood(convertPoints(rb.getText().toString()));
            }
        });

        btnCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).cleanCart();
                Toast.makeText(Cart.this,"Wyczyszczono koszyk",Toast.LENGTH_LONG).show();
                recreate();
            }
        });

        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recyclerView.getAdapter().getItemCount() == 0){
                    Toast.makeText(Cart.this,"Dodaj coś do koszyka i złóż zamówienie",Toast.LENGTH_LONG).show();
                }else{
                    int radioId = radioGroup.getCheckedRadioButtonId();
                    radioButton = findViewById(radioId);

                    int radioIdPoints = radioGroupPoints.getCheckedRadioButtonId();
                    radioButtonPoints = findViewById(radioIdPoints);

                    reference_user.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User userProfile = snapshot.getValue(User.class);

                            if(userProfile != null){

                                if(radioButtonPoints!=null){
                                    int points = userProfile.getPoints();
                                    String p  = convertPoints(radioButtonPoints.getText().toString());
                                    points = points - Integer.parseInt(p);

                                    reference_user.child(userID).child("points").setValue(points);
                                }else{
                                    int points = userProfile.getPoints();
                                    int point = convertPriceToPoints();
                                    points = points+point;
                                    reference_user.child(userID).child("points").setValue(points);
                                }


                                Request request = new Request(
                                        userProfile.getPhone(),
                                        userProfile.getName(),
                                        editOrderAddress.getText().toString(),
                                        txtTotalPrice.getText().toString(),
                                        cart,
                                        userProfile.getMail(),
                                        radioButton.getText().toString()
                                );

                                reference.child(String.valueOf(System.currentTimeMillis())).setValue(request);
                                new Database(getBaseContext()).cleanCart();
                                Toast.makeText(Cart.this,"Dziękujemy za zamówienie",Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Cart.this,"Cos poszło nie tak",Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }
        });

    }

    private int convertPriceToPoints(){
        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart,this);
        recyclerView.setAdapter(adapter);

        double total = 0;
        for(Order  order : cart)
            total+=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));

        double points = total/10;
        int p = new Double(points).intValue();

        return p;

    }

    private void loadListFood(String points) {

        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart,this);
        recyclerView.setAdapter(adapter);

        double total = 0;
        for(Order  order : cart)
            total+=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));

        if(points.equals("10")){
            total = total*0.9;
        }else if(points.equals("20")){
            total = total*0.8;
        }

        Locale locale = new Locale("pl", "PL");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        txtTotalPrice.setText(fmt.format(total));
    }

    public String convertPoints (String text)
    {
        if(text.equals("10 punktów = -10%"))
            return "10";
        else if(text.equals("20 punktów = -20%"))
            return "20";
        else
            return "0";
    }

    public void CheckPayment(View view) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        Toast.makeText(this,"Wybrano metode platnosc  " , Toast.LENGTH_LONG);
    }
}