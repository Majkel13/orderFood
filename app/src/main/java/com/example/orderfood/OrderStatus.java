package com.example.orderfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.orderfood.Database.Database;
import com.example.orderfood.Model.Food;
import com.example.orderfood.Model.Request;
import com.example.orderfood.Model.User;
import com.example.orderfood.ViewHolder.MenuViewHolder;
import com.example.orderfood.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class OrderStatus extends AppCompatActivity {


    public RecyclerView recyclerView;
    public  RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    FirebaseUser user;

    DrawerLayout drawerLayout;

    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        database = FirebaseDatabase.getInstance();
        requests  = database.getReference("Requests");

        drawerLayout = findViewById(R.id.drawer_layout);

        recyclerView = (RecyclerView)findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        user = FirebaseAuth.getInstance().getCurrentUser();


        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Requests")
                .orderByChild("email")
                .equalTo(user.getEmail())
                .limitToLast(50);
        FirebaseRecyclerOptions<Request> options = new FirebaseRecyclerOptions.Builder<Request>().setQuery(query, Request.class).build();

        adapter =new FirebaseRecyclerAdapter<Request, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder orderViewHolder, int i, @NonNull Request request) {
                orderViewHolder.txtOrderId.setText(adapter.getRef(i).getKey());
                orderViewHolder.txtOrderStatus.setText(convertCodeToStatus(request.getStatus()));
                orderViewHolder.txtOrderAddress.setText(request.getAddress());
                orderViewHolder.txtOrderPhone.setText(request.getPhone());
                orderViewHolder.txtOrderMethod.setText(request.getPayment_method());
                orderViewHolder.txtTotalPrice.setText(request.getTotal());
            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout, parent, false);
                return  new OrderViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);


    }

    private void loadOrders(String email){

    }

    private String convertCodeToStatus(String status){
        if(status.equals("0"))
            return "W trakcie realizacji";
        else if(status.equals("1"))
            return "W drodze";
        else
            return "Dostarczony";
    }

    public void ClickCart(View view){
        HomeActivity.redirectActivity(this, Cart.class);
    }

    public void ClickMenu(View view){
        HomeActivity.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view){
        HomeActivity.closeDrawer(drawerLayout);
    }

    public  void ClickProfil(View view){
        HomeActivity.redirectActivity(this,Profil.class);
    }

    public void ClickOrderStatus(View view){
        recreate();
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
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}