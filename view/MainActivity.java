package com.example.tiemchuixe.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.widget.LinearLayout;
import android.widget.FrameLayout;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Toast;
import com.example.tiemchuixe.R;
import com.example.tiemchuixe.controller.NhanVienDAO;
import com.example.tiemchuixe.model.NhanVien;
import com.example.tiemchuixe.view.LoginActivity;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Kiểm tra đăng nhập
        String loggedInVaiTro = LoginActivity.getLoggedInVaiTro(this);
        if (loggedInVaiTro == null) {
            LoginActivity.logout(this);
            return;
        }

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Lấy thông tin user
        int maNV = LoginActivity.getLoggedInMaNV(this);
        String vaiTro = LoginActivity.getLoggedInVaiTro(this);
        String tenHienThi = "";
        NhanVienDAO nhanVienDAO = new NhanVienDAO(this);
        nhanVienDAO.open();
        NhanVien nhanVien = nhanVienDAO.getNhanVienById(maNV);
        nhanVienDAO.close();
        if (nhanVien != null) {
            tenHienThi = nhanVien.getTenNhanVien();
        }
        // Chào mừng
        TextView textViewWelcome = findViewById(R.id.textViewWelcome);
        textViewWelcome.setText("Xin chào " + vaiTro + " " + tenHienThi + "!");

        // Hiển thị thông tin khác
        LinearLayout layoutUserInfo = findViewById(R.id.layoutUserInfo);
        layoutUserInfo.removeAllViews();
        if (nhanVien != null) {
            addInfoRow(layoutUserInfo, "Mã NV", String.valueOf(nhanVien.getMaNV()));
            addInfoRow(layoutUserInfo, "Tên đăng nhập", nhanVien.getTenDangNhap());
            addInfoRow(layoutUserInfo, "Chức vụ", nhanVien.getVaiTro());
        }
        // Có thể thêm các thông tin khác nếu cần

        // Chuẩn bị chỗ cho biểu đồ thống kê (chart)
        FrameLayout chartContainer = findViewById(R.id.chartContainer);
        // TODO: Thêm code hiển thị biểu đồ thống kê ở đây (ví dụ dùng thư viện MPAndroidChart)

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
            } else if (id == R.id.nav_profile) {
                // Mở trang thông tin nhân viên
                Intent intent = new Intent(this, EmployeeInfoActivity.class);
                intent.putExtra("maNV", maNV);
                startActivity(intent);
            } else if (id == R.id.nav_logout) {
                LoginActivity.logout(this);
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void addInfoRow(LinearLayout parent, String label, String value) {
        TextView tv = new TextView(this);
        tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv.setText(label + ": " + value);
        tv.setTextSize(16f);
        parent.addView(tv);
    }
}