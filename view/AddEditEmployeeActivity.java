package com.example.tiemchuixe.view;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.tiemchuixe.R;
import com.example.tiemchuixe.model.NhanVien;
import com.example.tiemchuixe.controller.NhanVienDAO;
import com.example.tiemchuixe.view.LoginActivity;

public class AddEditEmployeeActivity extends AppCompatActivity {

    private EditText editTextEmployeeName;
    private EditText editTextEmployeeUsername;
    private EditText editTextEmployeePassword;
    private Spinner spinnerEmployeeRole;
    private Button buttonSaveEmployee;
    private TextView textViewTitle;

    private NhanVienDAO nhanVienDAO;
    private int employeeId = -1; // -1 indicates adding a new employee

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_employee);

        // Check user role - only 'QuanLy' can access this screen
        String loggedInVaiTro = LoginActivity.getLoggedInVaiTro(this);
        if (!"QuanLy".equals(loggedInVaiTro)) {
            Toast.makeText(this, "Bạn không có quyền truy cập chức năng này.", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity
            return;
        }

        editTextEmployeeName = findViewById(R.id.editTextEmployeeName);
        editTextEmployeeUsername = findViewById(R.id.editTextEmployeeUsername);
        editTextEmployeePassword = findViewById(R.id.editTextEmployeePassword);
        spinnerEmployeeRole = findViewById(R.id.spinnerEmployeeRole);
        buttonSaveEmployee = findViewById(R.id.buttonSaveEmployee);
        textViewTitle = findViewById(R.id.textViewTitle);

        nhanVienDAO = new NhanVienDAO(this);

        // Setup Role Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.employee_roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEmployeeRole.setAdapter(adapter);

        // Check if editing an existing employee
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("maNV")) {
            employeeId = extras.getInt("maNV", -1);
            if (employeeId != -1) {
                textViewTitle.setText("Sửa Thông tin Nhân viên");
                loadEmployeeData(employeeId);
                 // Password is not loaded for security, hint updated in layout
            }
        }

        buttonSaveEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEmployee();
            }
        });
    }

    private void loadEmployeeData(int maNV) {
        nhanVienDAO.open();
        NhanVien nhanVien = nhanVienDAO.getNhanVienById(maNV);
        nhanVienDAO.close();

        if (nhanVien != null) {
            editTextEmployeeName.setText(nhanVien.getTenNhanVien());
            editTextEmployeeUsername.setText(nhanVien.getTenDangNhap());
            // Do not load password for security

            // Set spinner selection
            String vaiTro = nhanVien.getVaiTro();
            ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinnerEmployeeRole.getAdapter();
            if (adapter != null) {
                int spinnerPosition = adapter.getPosition(vaiTro);
                spinnerEmployeeRole.setSelection(spinnerPosition);
            }
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin nhân viên.", Toast.LENGTH_SHORT).show();
            finish(); // Close activity if employee not found
        }
    }

    private void saveEmployee() {
        String name = editTextEmployeeName.getText().toString().trim();
        String username = editTextEmployeeUsername.getText().toString().trim();
        String password = editTextEmployeePassword.getText().toString().trim();
        String role = spinnerEmployeeRole.getSelectedItem().toString();

        if (name.isEmpty() || username.isEmpty() || (employeeId == -1 && password.isEmpty())) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin (bao gồm mật khẩu cho nhân viên mới).", Toast.LENGTH_SHORT).show();
            return;
        }

        NhanVien nhanVien = new NhanVien();
        nhanVien.setTenNhanVien(name);
        nhanVien.setTenDangNhap(username);
        nhanVien.setVaiTro(role);

        nhanVienDAO.open();
        long result;
        try {
            if (employeeId == -1) {
                // Add new employee
                nhanVien.setMatKhau(password); // Password hashing happens in DAO
                result = nhanVienDAO.addNhanVien(nhanVien);
                if (result > 0) {
                    Toast.makeText(this, "Thêm nhân viên thành công.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Lỗi khi thêm nhân viên.", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Update existing employee
                nhanVien.setMaNV(employeeId);
                if (!password.isEmpty()) {
                    nhanVien.setMatKhau(password); // Password hashing happens in DAO
                } else {
                     // If password field is empty, retain existing password
                     // This requires retrieving the old password hash which is not ideal
                     // A better approach would be a separate password change feature
                     // For now, if empty, the update method in DAO will not change the password field.
                }
                result = nhanVienDAO.updateNhanVien(nhanVien);
                 if (result > 0) { // update returns number of affected rows
                    Toast.makeText(this, "Cập nhật thông tin nhân viên thành công.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Không có thông tin nào được cập nhật hoặc lỗi.", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (SQLiteConstraintException e) {
             // Handle unique username constraint violation
             Toast.makeText(this, "Tên đăng nhập đã tồn tại.", Toast.LENGTH_SHORT).show();
        } finally {
            nhanVienDAO.close();
        }
    }
} 