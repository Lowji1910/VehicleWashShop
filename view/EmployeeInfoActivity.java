package com.example.tiemchuixe.view;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;
import com.example.tiemchuixe.model.ThongTinNhanVien;
import com.example.tiemchuixe.controller.ThongTinNhanVienDAO;
import android.app.AlertDialog;
import android.widget.EditText;
import com.example.tiemchuixe.model.NhanVien;
import com.example.tiemchuixe.controller.NhanVienDAO;
import com.example.tiemchuixe.R;

public class EmployeeInfoActivity extends Activity {
    private EditText editTextHoTen, editTextNgaySinh, editTextGioiTinh, editTextDiaChi, editTextSoDienThoai, editTextEmail, editTextChucVu, editTextNgayVaoLam, editTextLuongCoBan, editTextTrangThai, editTextGhiChu;
    private EditText editTextUsername, editTextPassword;
    private Button buttonSaveInfo, buttonDeleteInfo;
    private TextView labelUsername, labelPassword;
    private int maNV;
    private ThongTinNhanVienDAO thongTinNhanVienDAO;
    private ThongTinNhanVien thongTin;
    private NhanVienDAO nhanVienDAO;
    private NhanVien nhanVien;
    private String vaiTro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_info);

        // Lấy vai trò hiện tại
        vaiTro = LoginActivity.getLoggedInVaiTro(this);

        // Ánh xạ view
        editTextHoTen = findViewById(R.id.editTextHoTen);
        editTextNgaySinh = findViewById(R.id.editTextNgaySinh);
        editTextGioiTinh = findViewById(R.id.editTextGioiTinh);
        editTextDiaChi = findViewById(R.id.editTextDiaChi);
        editTextSoDienThoai = findViewById(R.id.editTextSoDienThoai);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextChucVu = findViewById(R.id.editTextChucVu);
        editTextNgayVaoLam = findViewById(R.id.editTextNgayVaoLam);
        editTextLuongCoBan = findViewById(R.id.editTextLuongCoBan);
        editTextTrangThai = findViewById(R.id.editTextTrangThai);
        editTextGhiChu = findViewById(R.id.editTextGhiChu);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        labelUsername = findViewById(R.id.labelUsername);
        labelPassword = findViewById(R.id.labelPassword);
        buttonSaveInfo = findViewById(R.id.buttonSaveInfo);
        buttonDeleteInfo = findViewById(R.id.buttonDeleteInfo);

        maNV = getIntent().getIntExtra("maNV", -1);
        thongTinNhanVienDAO = new ThongTinNhanVienDAO(this);
        nhanVienDAO = new NhanVienDAO(this);
        thongTinNhanVienDAO.open();
        nhanVienDAO.open();
        thongTin = thongTinNhanVienDAO.getThongTinNhanVien(maNV);
        nhanVien = nhanVienDAO.getNhanVienById(maNV);
        thongTinNhanVienDAO.close();
        nhanVienDAO.close();

        // Nếu là admin thì cho phép sửa username/password
        if ("QuanLy".equals(vaiTro)) {
            labelUsername.setVisibility(View.VISIBLE);
            editTextUsername.setVisibility(View.VISIBLE);
            labelPassword.setVisibility(View.VISIBLE);
            editTextPassword.setVisibility(View.VISIBLE);
            if (nhanVien != null) {
                editTextUsername.setText(nhanVien.getTenDangNhap());
            }
        } else {
            labelUsername.setVisibility(View.GONE);
            editTextUsername.setVisibility(View.GONE);
            labelPassword.setVisibility(View.GONE);
            editTextPassword.setVisibility(View.GONE);
        }
        // Luôn cho phép sửa thông tin profile
        if (thongTin != null) {
            editTextHoTen.setText(thongTin.getHoTen());
            editTextNgaySinh.setText(thongTin.getNgaySinh());
            editTextGioiTinh.setText(thongTin.getGioiTinh());
            editTextDiaChi.setText(thongTin.getDiaChi());
            editTextSoDienThoai.setText(thongTin.getSoDienThoai());
            editTextEmail.setText(thongTin.getEmail());
            editTextChucVu.setText(thongTin.getChucVu());
            editTextNgayVaoLam.setText(thongTin.getNgayVaoLam());
            editTextLuongCoBan.setText(String.valueOf(thongTin.getLuongCoBan()));
            editTextTrangThai.setText(thongTin.getTrangThai());
            editTextGhiChu.setText(thongTin.getGhiChu());
        } else if (nhanVien != null) {
            editTextHoTen.setText(nhanVien.getTenNhanVien());
            editTextChucVu.setText(nhanVien.getVaiTro());
        }

        // Lưu thông tin
        buttonSaveInfo.setOnClickListener(v -> {
            thongTinNhanVienDAO.open();
            ThongTinNhanVien info = new ThongTinNhanVien();
            info.setMaNV(maNV);
            info.setHoTen(editTextHoTen.getText().toString().trim());
            info.setNgaySinh(editTextNgaySinh.getText().toString().trim());
            info.setGioiTinh(editTextGioiTinh.getText().toString().trim());
            info.setDiaChi(editTextDiaChi.getText().toString().trim());
            info.setSoDienThoai(editTextSoDienThoai.getText().toString().trim());
            info.setEmail(editTextEmail.getText().toString().trim());
            info.setChucVu(editTextChucVu.getText().toString().trim());
            info.setNgayVaoLam(editTextNgayVaoLam.getText().toString().trim());
            try {
                info.setLuongCoBan(Double.parseDouble(editTextLuongCoBan.getText().toString().trim()));
            } catch (Exception e) {
                info.setLuongCoBan(0);
            }
            info.setTrangThai(editTextTrangThai.getText().toString().trim());
            info.setGhiChu(editTextGhiChu.getText().toString().trim());
            boolean isUpdate = thongTin != null;
            long result;
            if (isUpdate) {
                result = thongTinNhanVienDAO.updateThongTinNhanVien(info);
            } else {
                result = thongTinNhanVienDAO.addThongTinNhanVien(info);
            }
            thongTinNhanVienDAO.close();
            // Nếu là admin thì cho phép sửa username/password
            if ("QuanLy".equals(vaiTro)) {
                nhanVienDAO.open();
                if (nhanVien != null) {
                    nhanVien.setTenDangNhap(editTextUsername.getText().toString().trim());
                    String newPassword = editTextPassword.getText().toString().trim();
                    if (!newPassword.isEmpty()) {
                        nhanVien.setMatKhau(newPassword);
                    }
                    nhanVienDAO.updateNhanVien(nhanVien);
                }
                nhanVienDAO.close();
            }
            if (result > 0) {
                Toast.makeText(this, "Lưu thông tin thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Lỗi khi lưu thông tin", Toast.LENGTH_SHORT).show();
            }
        });

        // Xóa thông tin
        buttonDeleteInfo.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa thông tin chi tiết nhân viên này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    thongTinNhanVienDAO.open();
                    int result = thongTinNhanVienDAO.deleteThongTinNhanVien(maNV);
                    thongTinNhanVienDAO.close();
                    if (result > 0) {
                        Toast.makeText(this, "Đã xóa thông tin chi tiết nhân viên", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Không thể xóa thông tin hoặc chưa có thông tin để xóa", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
        });
    }
} 