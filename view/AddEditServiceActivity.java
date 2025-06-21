package com.example.tiemchuixe.view;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import com.example.tiemchuixe.R;
import com.example.tiemchuixe.controller.DichVuDAO;
import com.example.tiemchuixe.model.DichVu;
import com.example.tiemchuixe.view.LoginActivity;

public class AddEditServiceActivity extends AppCompatActivity {

    private EditText editTextServiceName;
    private EditText editTextServicePrice;
    private RadioGroup radioGroupVehicleType;
    private Button buttonSaveService;
    private Button buttonDeleteService;
    private TextView textViewTitle;

    private DichVuDAO dichVuDAO;
    private int serviceId = -1; // -1 indicates adding a new service

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_service);

        // Check user role - only 'QuanLy' can access this screen
        String loggedInVaiTro = LoginActivity.getLoggedInVaiTro(this);
        if (!"QuanLy".equals(loggedInVaiTro)) {
            Toast.makeText(this, "Bạn không có quyền truy cập chức năng này.", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity
            return;
        }

        editTextServiceName = findViewById(R.id.editTextServiceName);
        editTextServicePrice = findViewById(R.id.editTextServicePrice);
        radioGroupVehicleType = findViewById(R.id.radioGroupVehicleType);
        buttonSaveService = findViewById(R.id.buttonSaveService);
        buttonDeleteService = findViewById(R.id.buttonDeleteService);
        textViewTitle = findViewById(R.id.textViewTitle);

        dichVuDAO = new DichVuDAO(this);

        // Check if editing an existing service
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("maDV")) {
            serviceId = extras.getInt("maDV", -1);
            if (serviceId != -1) {
                textViewTitle.setText("Sửa Dịch vụ");
                loadServiceData(serviceId);
                buttonDeleteService.setVisibility(View.VISIBLE); // Show delete button for existing services
            }
        }

        buttonSaveService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveService();
            }
        });

        buttonDeleteService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog();
            }
        });
    }

    private void loadServiceData(int maDV) {
        dichVuDAO.open();
        DichVu dichVu = dichVuDAO.getDichVuById(maDV);
        dichVuDAO.close();

        if (dichVu != null) {
            editTextServiceName.setText(dichVu.getTenDichVu());
            editTextServicePrice.setText(String.valueOf(dichVu.getGiaTien()));
            
            // Set vehicle type
            RadioButton radioButton = dichVu.getLoaiXe().equals("XeMay") ? 
                findViewById(R.id.radioButtonMotorcycle) : findViewById(R.id.radioButtonCar);
            radioButton.setChecked(true);
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin dịch vụ.", Toast.LENGTH_SHORT).show();
            finish(); // Close activity if service not found
        }
    }

    private void saveService() {
        String name = editTextServiceName.getText().toString().trim();
        String priceStr = editTextServicePrice.getText().toString().trim();

        if (name.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Giá tiền không hợp lệ.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get selected vehicle type
        int selectedId = radioGroupVehicleType.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectedId);
        String vehicleType = radioButton.getText().toString().equals("Xe máy") ? "XeMay" : "Oto";

        DichVu dichVu = new DichVu();
        dichVu.setTenDichVu(name);
        dichVu.setGiaTien(price);
        dichVu.setLoaiXe(vehicleType);

        dichVuDAO.open();
        long result;
        try {
            if (serviceId == -1) {
                // Add new service
                result = dichVuDAO.addDichVu(dichVu);
                if (result > 0) {
                    Toast.makeText(this, "Thêm dịch vụ thành công.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Lỗi khi thêm dịch vụ.", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Update existing service
                dichVu.setMaDV(serviceId);
                result = dichVuDAO.updateDichVu(dichVu);
                if (result > 0) {
                    Toast.makeText(this, "Cập nhật dịch vụ thành công.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Không có thông tin nào được cập nhật hoặc lỗi.", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (SQLiteConstraintException e) {
            // Handle unique service name constraint violation
            Toast.makeText(this, "Tên dịch vụ đã tồn tại.", Toast.LENGTH_SHORT).show();
        } finally {
            dichVuDAO.close();
        }
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa dịch vụ này không?")
                .setPositiveButton("Xóa", (dialog, which) -> deleteService())
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteService() {
        dichVuDAO.open();
        dichVuDAO.deleteDichVu(serviceId);
        dichVuDAO.close();
        Toast.makeText(this, "Dịch vụ đã được xóa.", Toast.LENGTH_SHORT).show();
        finish(); // Close the activity after deletion
    }
} 