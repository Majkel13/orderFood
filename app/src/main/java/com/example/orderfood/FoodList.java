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
import android.widget.TextView;
import android.widget.Toast;

import com.example.orderfood.Interface.ItemClickListener;
import com.example.orderfood.Model.Category;
import com.example.orderfood.Model.Food;
import com.example.orderfood.ViewHolder.FoodViewHolder;
import com.example.orderfood.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class FoodList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    DrawerLayout drawerLayout;
    DatabaseReference menu_data;
    FirebaseDatabase database;

    String categoryId="";
    FirebaseRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        database = FirebaseDatabase.getInstance();
        menu_data = database.getReference("Food");

        TextView textView = (TextView)findViewById(R.id.titleContent);
        textView.setText("MENU");

        recyclerView = (RecyclerView)findViewById(R.id.recyclerFood);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.btnFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cartIntent = new Intent(FoodList.this, Cart.class);
                startActivity(cartIntent);
            }
        });

        if(getIntent() != null)
            categoryId= getIntent().getStringExtra("CategoryId");
        if(!categoryId.isEmpty() && categoryId !=null)
        {
            Query query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Food")
                    .orderByChild("MenuID")
                    .equalTo(categoryId)
                    .limitToLast(50);

            FirebaseRecyclerOptions<Food> options = new FirebaseRecyclerOptions.Builder<Food>().setQuery(query, Food.class).build();

            FoodViewHolder menuViewHolder = new FoodViewHolder(recyclerView);
            adapter= new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {

                @Override
                protected void onBindViewHolder(@NonNull FoodViewHolder menuViewHolder, int i, @NonNull Food category) {

                    menuViewHolder.food_name.setText(category.getName());
                    Picasso.get().load(category.getImage()).into(menuViewHolder.food_image);
                    final Food local = category;

                    menuViewHolder.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void onClick(View view, int position, boolean isLongClick) {
                           Intent foodDetail = new Intent(FoodList.this, FoodDetail.class);
                           foodDetail.putExtra("FoodId",adapter.getRef(position).getKey());
                           startActivity(foodDetail);
                        }
                    });

                }
                @NonNull
                @Override
                public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
                    return  new FoodViewHolder(view);
                }


            };
            recyclerView.setAdapter(adapter);
        }

    }

    private void loadListFood(String categoryId) {
    }

    public void ClickMenu(View view){
        HomeActivity.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view){
        HomeActivity.closeDrawer(drawerLayout);
    }

    public  void ClickProfil(View view){
        HomeActivity.redirectActivity(this, Profil.class);
    }

    public void ClickHome(View view){
        HomeActivity.redirectActivity(this,HomeActivity.class);
    }

    public void ClickMenuFood(View view){
        recreate();
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

