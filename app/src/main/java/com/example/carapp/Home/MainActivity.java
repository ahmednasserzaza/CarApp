package com.example.carapp.Home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.carapp.CarDetails.CarDetailsActivity;
import com.example.carapp.Database.DatabaseAccess;
import com.example.carapp.R;
import com.example.carapp.RecyclerViewPackage.Car;
import com.example.carapp.RecyclerViewPackage.CarRvAdapter;
import com.example.carapp.RecyclerViewPackage.OnRecyclerClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final int ADD_CAR_REQ_CODE = 3;
    public static final int EDIT_CAR_REQ_CODE = 4;
    public static final String CAR_KEY = "car_key";

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private RecyclerView rv;
    private CarRvAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Car> cars;
    private DatabaseAccess db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        db = DatabaseAccess.getInstance(this);

        db.open();
        cars = db.getAllRows();
        db.close();

        adapter = new CarRvAdapter(this,cars, new OnRecyclerClickListener() {
            @Override
            public void onItemClick(int carId) {
                Intent intent = new Intent(getBaseContext(), CarDetailsActivity.class);
                intent.putExtra(CAR_KEY, carId);
                startActivityForResult(intent, EDIT_CAR_REQ_CODE);
            }
        });
        layoutManager = new GridLayoutManager(this , 2);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
        rv.setHasFixedSize(true);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), CarDetailsActivity.class);
                startActivityForResult(intent, ADD_CAR_REQ_CODE);
            }
        });

    }

    private void initViews() {
        toolbar = findViewById(R.id.main_tool_bar);
        fab = findViewById(R.id.fb_add_new_car);
        rv = findViewById(R.id.main_recycler_view);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                db.open();
                cars = db.searchByModel(query) ;
                db.close();
                adapter.setCars(cars);
                adapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                db.open();
                cars = db.searchByModel(newText) ;
                db.close();
                adapter.setCars(cars);
                adapter.notifyDataSetChanged();
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                db.open();
                cars = db.getAllRows() ;
                db.close();
                adapter.setCars(cars);
                adapter.notifyDataSetChanged();
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_CAR_REQ_CODE || requestCode == EDIT_CAR_REQ_CODE) {
            db.open();
            cars = db.getAllRows();
            db.close();
            adapter.setCars(cars);
            adapter.notifyDataSetChanged();
        }
    }
}