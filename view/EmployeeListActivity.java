package com.example.tiemchuixe.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.util.List;
import java.util.ArrayList;
import com.example.tiemchuixe.R;
import com.example.tiemchuixe.model.NhanVien;
import com.example.tiemchuixe.controller.NhanVienDAO;
import com.example.tiemchuixe.view.AddEditEmployeeActivity;
import com.example.tiemchuixe.view.BaseDrawerActivity;

public class EmployeeListActivity extends BaseDrawerActivity {

    private ListView listViewEmployees;
    private Button buttonAddEmployee;
    private NhanVienDAO nhanVienDAO;
    private List<NhanVien> employeeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupDrawer(R.layout.activity_employee_list);

        // Check user role - only 'QuanLy' can access this screen
        String loggedInVaiTro = LoginActivity.getLoggedInVaiTro(this);
        if (!"QuanLy".equals(loggedInVaiTro)) {
            Toast.makeText(this, "Bạn không có quyền truy cập chức năng này.", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity
            return;
        }

        listViewEmployees = findViewById(R.id.listViewEmployees);
        buttonAddEmployee = findViewById(R.id.buttonAddEmployee);

        nhanVienDAO = new NhanVienDAO(this);

        buttonAddEmployee.setOnClickListener(v -> {
            // Navigate to Add/Edit Employee Activity
            Intent intent = new Intent(EmployeeListActivity.this, AddEditEmployeeActivity.class);
            startActivity(intent);
        });

        loadEmployees();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload employees in case changes were made in AddEditEmployeeActivity
        loadEmployees();
    }

    private void loadEmployees() {
        nhanVienDAO.open();
        employeeList = nhanVienDAO.getAllNhanVien();
        nhanVienDAO.close();

        // Prepare data for ListView
        List<String> employeeDisplayList = new ArrayList<>();
        for (NhanVien nv : employeeList) {
            employeeDisplayList.add("Tên: " + nv.getTenNhanVien() + ", Đăng nhập: " + nv.getTenDangNhap() + ", Vai trò: " + nv.getVaiTro());
        }

        // Create an ArrayAdapter and set it to the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, employeeDisplayList);
        listViewEmployees.setAdapter(adapter);

        // TODO: Implement item click listener for editing
         listViewEmployees.setOnItemClickListener((parent, view, position, id) -> {
             NhanVien selectedEmployee = employeeList.get(position);
             Intent intent = new Intent(EmployeeListActivity.this, AddEditEmployeeActivity.class);
             intent.putExtra("maNV", selectedEmployee.getMaNV()); // Pass employee ID for editing
             startActivity(intent);
         });
        // Thêm sự kiện mở thông tin chi tiết nhân viên khi long click
        /*
        listViewEmployees.setOnItemLongClickListener((parent, view, position, id) -> {
            NhanVien selectedEmployee = employeeList.get(position);
            Intent intent = new Intent(EmployeeListActivity.this, EmployeeInfoActivity.class);
            intent.putExtra("maNV", selectedEmployee.getMaNV());
            startActivity(intent);
            return true;
        });
        */
    }
} 