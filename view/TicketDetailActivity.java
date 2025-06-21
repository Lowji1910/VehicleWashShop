package com.example.tiemchuixe.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import com.example.tiemchuixe.R;
import com.example.tiemchuixe.model.PhieuRuaXe;
import com.example.tiemchuixe.model.KhachHang;
import com.example.tiemchuixe.model.ChiTietPhieu;
import com.example.tiemchuixe.controller.PhieuRuaXeDAO;
import com.example.tiemchuixe.controller.KhachHangDAO;

public class TicketDetailActivity extends AppCompatActivity {
    private TextView textViewTicketId;
    private TextView textViewCustomerName;
    private TextView textViewCustomerPhone;
    private TextView textViewLicensePlate;
    private TextView textViewVehicleType;
    private TextView textViewTotalAmount;
    private TextView textViewCreatedDate;
    private TextView textViewCurrentStatus;
    private RecyclerView recyclerViewServices;
    private Button buttonStartWashing;
    private Button buttonComplete;
    private Button buttonCancel;

    private PhieuRuaXeDAO phieuRuaXeDAO;
    private KhachHangDAO khachHangDAO;
    private PhieuRuaXe currentPhieu;
    private ServiceDetailAdapter serviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);

        // Thêm nút back
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Chi tiết phiếu");
        }

        // Khởi tạo các view
        initializeViews();

        // Khởi tạo DAO
        phieuRuaXeDAO = new PhieuRuaXeDAO(this);
        khachHangDAO = new KhachHangDAO(this);

        // Thiết lập RecyclerView
        recyclerViewServices.setLayoutManager(new LinearLayoutManager(this));
        serviceAdapter = new ServiceDetailAdapter();
        recyclerViewServices.setAdapter(serviceAdapter);

        // Lấy thông tin phiếu từ Intent
        int maPhieu = getIntent().getIntExtra("MA_PHIEU", -1);
        if (maPhieu != -1) {
            loadTicketDetails(maPhieu);
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin phiếu", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initializeViews() {
        textViewTicketId = findViewById(R.id.textViewTicketId);
        textViewCustomerName = findViewById(R.id.textViewCustomerName);
        textViewCustomerPhone = findViewById(R.id.textViewCustomerPhone);
        textViewLicensePlate = findViewById(R.id.textViewLicensePlate);
        textViewVehicleType = findViewById(R.id.textViewVehicleType);
        textViewTotalAmount = findViewById(R.id.textViewTotalAmount);
        textViewCreatedDate = findViewById(R.id.textViewCreatedDate);
        textViewCurrentStatus = findViewById(R.id.textViewCurrentStatus);
        recyclerViewServices = findViewById(R.id.recyclerViewServices);
        buttonStartWashing = findViewById(R.id.buttonStartWashing);
        buttonComplete = findViewById(R.id.buttonComplete);
        buttonCancel = findViewById(R.id.buttonCancel);

        // Thiết lập các sự kiện click
        buttonStartWashing.setOnClickListener(v -> updateStatus("Đang rửa"));
        buttonComplete.setOnClickListener(v -> updateStatus("Hoàn thành"));
        buttonCancel.setOnClickListener(v -> updateStatus("Đã hủy"));
    }

    private void loadTicketDetails(int maPhieu) {
        phieuRuaXeDAO.open();
        khachHangDAO.open();

        currentPhieu = phieuRuaXeDAO.getPhieuRuaXeById(maPhieu);
        if (currentPhieu != null) {
            KhachHang khachHang = khachHangDAO.getKhachHangById(currentPhieu.getMaKH());

            // Hiển thị thông tin phiếu
            textViewTicketId.setText("Phiếu số: " + currentPhieu.getMaPhieu());
            textViewCustomerName.setText("Khách hàng: " + (khachHang != null ? khachHang.getTenKhachHang() : "Không xác định"));
            textViewCustomerPhone.setText("SĐT: " + (khachHang != null ? khachHang.getSoDienThoai() : "Không xác định"));
            textViewLicensePlate.setText("Biển số xe: " + currentPhieu.getBienSoXe());
            textViewVehicleType.setText("Loại xe: " + currentPhieu.getLoaiXe());
            
            // Format số tiền
            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            textViewTotalAmount.setText("Tổng tiền: " + formatter.format(currentPhieu.getTongTien()));
            
            textViewCreatedDate.setText("Ngày tạo: " + currentPhieu.getNgayTao());

            // Load chi tiết dịch vụ
            List<ChiTietPhieu> chiTietList = phieuRuaXeDAO.getChiTietPhieu(maPhieu);
            serviceAdapter.updateData(chiTietList);

            // Hiển thị trạng thái hiện tại
            updateStatusDisplay(currentPhieu.getTrangThai());

            // Hiển thị các nút cập nhật trạng thái tùy theo trạng thái hiện tại
            updateButtonVisibility(currentPhieu.getTrangThai());
        }

        phieuRuaXeDAO.close();
        khachHangDAO.close();
    }

    private void updateStatusDisplay(String trangThai) {
        switch (trangThai) {
            case "Chưa hoàn thành":
                textViewCurrentStatus.setText(R.string.status_waiting);
                textViewCurrentStatus.setBackgroundResource(android.R.color.holo_orange_light);
                break;
            case "Đang rửa":
                textViewCurrentStatus.setText(R.string.status_washing);
                textViewCurrentStatus.setBackgroundResource(android.R.color.holo_blue_light);
                break;
            case "Hoàn thành":
                textViewCurrentStatus.setText(R.string.status_completed);
                textViewCurrentStatus.setBackgroundResource(android.R.color.holo_green_light);
                break;
            case "Đã hủy":
                textViewCurrentStatus.setText(R.string.status_cancelled);
                textViewCurrentStatus.setBackgroundResource(android.R.color.holo_red_light);
                break;
            default:
                textViewCurrentStatus.setBackgroundResource(android.R.color.darker_gray);
        }
    }

    private void updateButtonVisibility(String currentStatus) {
        // Ẩn tất cả các nút
        buttonStartWashing.setVisibility(View.GONE);
        buttonComplete.setVisibility(View.GONE);
        buttonCancel.setVisibility(View.GONE);

        // Hiển thị các nút tùy theo trạng thái hiện tại
        switch (currentStatus) {
            case "Chưa hoàn thành":
                buttonStartWashing.setVisibility(View.VISIBLE);
                buttonCancel.setVisibility(View.VISIBLE);
                break;
            case "Đang rửa":
                buttonComplete.setVisibility(View.VISIBLE);
                buttonCancel.setVisibility(View.VISIBLE);
                break;
            case "Hoàn thành":
            case "Đã hủy":
                // Không hiển thị nút nào khi đã hoàn thành hoặc hủy
                break;
        }
    }

    private void updateStatus(String newStatus) {
        if (currentPhieu != null) {
            phieuRuaXeDAO.open();
            currentPhieu.setTrangThai(newStatus);
            int result = phieuRuaXeDAO.updatePhieuRuaXe(currentPhieu);
            phieuRuaXeDAO.close();

            if (result > 0) {
                updateStatusDisplay(newStatus);
                updateButtonVisibility(newStatus);
                Toast.makeText(this, "Cập nhật trạng thái thành công", Toast.LENGTH_SHORT).show();
                // Thông báo cho ListTicketActivity biết đã cập nhật trạng thái
                setResult(RESULT_OK);
            } else {
                Toast.makeText(this, "Lỗi khi cập nhật trạng thái", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class ServiceDetailAdapter extends RecyclerView.Adapter<ServiceDetailAdapter.ServiceViewHolder> {
        private List<ChiTietPhieu> chiTietList = new ArrayList<>();

        public void updateData(List<ChiTietPhieu> newList) {
            this.chiTietList = newList;
            notifyDataSetChanged();
        }

        @Override
        public ServiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_service_detail, parent, false);
            return new ServiceViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ServiceViewHolder holder, int position) {
            ChiTietPhieu chiTiet = chiTietList.get(position);
            holder.textViewServiceName.setText(chiTiet.getTenDichVu());
            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            holder.textViewServicePrice.setText(formatter.format(chiTiet.getDonGia()));
        }

        @Override
        public int getItemCount() {
            return chiTietList.size();
        }

        class ServiceViewHolder extends RecyclerView.ViewHolder {
            TextView textViewServiceName;
            TextView textViewServicePrice;

            ServiceViewHolder(View itemView) {
                super(itemView);
                textViewServiceName = itemView.findViewById(R.id.textViewServiceName);
                textViewServicePrice = itemView.findViewById(R.id.textViewServicePrice);
            }
        }
    }
} 