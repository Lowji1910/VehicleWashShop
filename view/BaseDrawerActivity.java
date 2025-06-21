package com.example.tiemchuixe.view;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import com.example.tiemchuixe.R;
import com.example.tiemchuixe.view.ServiceListActivity;
import com.example.tiemchuixe.view.CreateTicketActivity;
import com.example.tiemchuixe.view.ListTicketActivity;
import com.example.tiemchuixe.view.RevenueActivity;
import com.example.tiemchuixe.view.LoginActivity;
import com.example.tiemchuixe.view.EmployeeListActivity;

public abstract class BaseDrawerActivity extends AppCompatActivity {
    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Không setContentView ở đây, sẽ set ở Activity con
    }

    protected void setupDrawer(@LayoutRes int layoutResId) {
        setContentView(layoutResId);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_employee) {
                startActivity(new Intent(this, EmployeeListActivity.class));
            } else if (id == R.id.nav_service) {
                startActivity(new Intent(this, ServiceListActivity.class));
            } else if (id == R.id.nav_create_ticket) {
                startActivity(new Intent(this, CreateTicketActivity.class));
            } else if (id == R.id.nav_list_ticket) {
                startActivity(new Intent(this, ListTicketActivity.class));
            } else if (id == R.id.nav_revenue) {
                startActivity(new Intent(this, RevenueActivity.class));
            } else if (id == R.id.nav_logout) {
                LoginActivity.logout(this);
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }
} 