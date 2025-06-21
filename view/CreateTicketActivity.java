package com.example.tiemchuixe.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.example.tiemchuixe.R;
import com.example.tiemchuixe.model.KhachHang;
import com.example.tiemchuixe.model.DichVu;
import com.example.tiemchuixe.model.PhieuRuaXe;
import com.example.tiemchuixe.controller.KhachHangDAO;
import com.example.tiemchuixe.controller.DichVuDAO;
import com.example.tiemchuixe.controller.PhieuRuaXeDAO;

public class CreateTicketActivity extends AppCompatActivity implements ServiceCheckboxAdapter.OnServiceSelectedListener {
    private EditText editTextCustomerName;
    private EditText editTextCustomerPhone;
    private EditText editTextLicensePlate;
    private RadioGroup radioGroupVehicleType;
    private RecyclerView recyclerViewServices;
    private TextView textViewTotal;
    private Button buttonCreateTicket;

    private KhachHangDAO khachHangDAO;
    private DichVuDAO dichVuDAO;
    private PhieuRuaXeDAO phieuRuaXeDAO;
    private List<DichVu> allServices;
    private List<DichVu> selectedServices = new ArrayList<>();
    private ServiceCheckboxAdapter serviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ticket);

        khachHangDAO = new KhachHangDAO(this);
        dichVuDAO = new DichVuDAO(this);
        phieuRuaXeDAO = new PhieuRuaXeDAO(this);

        khachHangDAO.open();
        dichVuDAO.open();
        phieuRuaXeDAO.open();

        editTextCustomerName = findViewById(R.id.editTextCustomerName);
        editTextCustomerPhone = findViewById(R.id.editTextCustomerPhone);
        editTextLicensePlate = findViewById(R.id.editTextLicensePlate);
        radioGroupVehicleType = findViewById(R.id.radioGroupVehicleType);
        recyclerViewServices = findViewById(R.id.recyclerViewServices);
        textViewTotal = findViewById(R.id.textViewTotal);
        buttonCreateTicket = findViewById(R.id.buttonCreateTicket);

        recyclerViewServices.setLayoutManager(new LinearLayoutManager(this));

        loadServices();

        // Handle vehicle type selection to filter services
        radioGroupVehicleType.setOnCheckedChangeListener((group, checkedId) -> {
            filterServicesByVehicleType();
        });

        buttonCreateTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTicket();
            }
        });
    }

    private void loadServices() {
        allServices = dichVuDAO.getAllDichVu();
        filterServicesByVehicleType();
    }

    private void filterServicesByVehicleType() {
        String selectedVehicleType;
        int checkedRadioButtonId = radioGroupVehicleType.getCheckedRadioButtonId();

        if (checkedRadioButtonId == R.id.radioButtonMotorbike) {
            selectedVehicleType = "XeMay";
        } else if (checkedRadioButtonId == R.id.radioButtonCar) {
            selectedVehicleType = "Oto";
        } else {
            selectedVehicleType = ""; // Default or error case
        }

        List<DichVu> filteredServices = new ArrayList<>();
        for (DichVu service : allServices) {
            if (service.getLoaiXe() != null && service.getLoaiXe().equalsIgnoreCase(selectedVehicleType)) {
                filteredServices.add(service);
            }
        }

        selectedServices.clear(); // Clear previously selected services
        serviceAdapter = new ServiceCheckboxAdapter(this, filteredServices, selectedServices);
        recyclerViewServices.setAdapter(serviceAdapter);
        updateTotal();
    }

    private void createTicket() {
        String customerName = editTextCustomerName.getText().toString().trim();
        String phone = editTextCustomerPhone.getText().toString().trim();
        String licensePlate = editTextLicensePlate.getText().toString().trim();

        String selectedVehicleType;
        int checkedRadioButtonId = radioGroupVehicleType.getCheckedRadioButtonId();
        if (checkedRadioButtonId == R.id.radioButtonMotorbike) {
            selectedVehicleType = "XeMay";
        } else if (checkedRadioButtonId == R.id.radioButtonCar) {
            selectedVehicleType = "Oto";
        } else {
            Toast.makeText(this, "Vui lòng chọn loại xe", Toast.LENGTH_SHORT).show();
            return;
        }

        if (customerName.isEmpty() || phone.isEmpty() || licensePlate.isEmpty() || selectedServices.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin và chọn ít nhất một dịch vụ", Toast.LENGTH_SHORT).show();
            return;
        }

        KhachHang khachHang = khachHangDAO.getKhachHangByPhone(phone);
        if (khachHang == null) {
            // Create new customer if not found
            khachHang = new KhachHang();
            khachHang.setTenKhachHang(customerName);
            khachHang.setSoDienThoai(phone);
            khachHang.setBienSoXe(licensePlate);
            long newKhachHangId = khachHangDAO.addKhachHang(khachHang);
            if (newKhachHangId > 0) {
                khachHang.setMaKH((int) newKhachHangId);
            } else {
                Toast.makeText(this, "Lỗi khi tạo khách hàng mới", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            // Update existing customer's name and license plate
            khachHang.setTenKhachHang(customerName);
            khachHang.setBienSoXe(licensePlate);
            khachHangDAO.updateKhachHang(khachHang);
        }

        // Create ticket
        PhieuRuaXe phieu = new PhieuRuaXe();
        phieu.setMaKH(khachHang.getMaKH());
        phieu.setMaNV(getIntent().getIntExtra("maNV", 1)); // Assuming maNV is passed via Intent
        phieu.setBienSoXe(licensePlate);
        phieu.setLoaiXe(selectedVehicleType);
        phieu.setTrangThai("Chưa hoàn thành"); // Initial status
        phieu.setTongTien(calculateTotal());
        phieu.setNgayTao(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));

        long maPhieu = phieuRuaXeDAO.addPhieuRuaXe(phieu);
        if (maPhieu > 0) {
            // Add service details
            for (DichVu dv : selectedServices) {
                phieuRuaXeDAO.addChiTietPhieu((int)maPhieu, dv.getMaDV(), dv.getTenDichVu(), dv.getGiaTien());
            }
            Toast.makeText(this, "Tạo phiếu thành công", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Lỗi khi tạo phiếu", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onServiceSelectionChanged() {
        updateTotal();
    }

    private void updateTotal() {
        double total = calculateTotal();
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        textViewTotal.setText("Tổng tiền: " + formatter.format(total));
    }

    private double calculateTotal() {
        double total = 0;
        for (DichVu dv : selectedServices) {
            total += dv.getGiaTien();
        }
        return total;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (khachHangDAO != null) khachHangDAO.close();
        if (dichVuDAO != null) dichVuDAO.close();
        if (phieuRuaXeDAO != null) phieuRuaXeDAO.close();
    }
} 