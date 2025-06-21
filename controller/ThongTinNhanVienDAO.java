package com.example.tiemchuixe.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;
import com.example.tiemchuixe.model.ThongTinNhanVien;
import com.example.tiemchuixe.controller.DatabaseHelper;

public class ThongTinNhanVienDAO {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public ThongTinNhanVienDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long addThongTinNhanVien(ThongTinNhanVien info) {
        return dbHelper.addEmployeeInfo(
            info.getMaNV(),
            info.getHoTen(),
            info.getNgaySinh(),
            info.getGioiTinh(),
            info.getDiaChi(),
            info.getSoDienThoai(),
            info.getEmail(),
            info.getChucVu(),
            info.getNgayVaoLam(),
            info.getLuongCoBan(),
            info.getTrangThai(),
            info.getGhiChu()
        );
    }

    public int updateThongTinNhanVien(ThongTinNhanVien info) {
        return dbHelper.updateEmployeeInfo(
            info.getMaNV(),
            info.getHoTen(),
            info.getNgaySinh(),
            info.getGioiTinh(),
            info.getDiaChi(),
            info.getSoDienThoai(),
            info.getEmail(),
            info.getChucVu(),
            info.getNgayVaoLam(),
            info.getLuongCoBan(),
            info.getTrangThai(),
            info.getGhiChu()
        );
    }

    public int deleteThongTinNhanVien(int maNV) {
        return dbHelper.deleteEmployeeInfo(maNV);
    }

    public ThongTinNhanVien getThongTinNhanVien(int maNV) {
        Cursor cursor = dbHelper.getEmployeeInfo(maNV);
        ThongTinNhanVien info = null;
        if (cursor != null && cursor.moveToFirst()) {
            info = cursorToThongTinNhanVien(cursor);
        }
        if (cursor != null) cursor.close();
        return info;
    }

    public List<ThongTinNhanVien> getAllThongTinNhanVien() {
        Cursor cursor = dbHelper.getAllEmployeeInfo();
        List<ThongTinNhanVien> list = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(cursorToThongTinNhanVien(cursor));
            } while (cursor.moveToNext());
        }
        if (cursor != null) cursor.close();
        return list;
    }

    private ThongTinNhanVien cursorToThongTinNhanVien(Cursor cursor) {
        ThongTinNhanVien info = new ThongTinNhanVien();
        info.setMaNV(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MANV_TT)));
        info.setHoTen(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_HOTEN)));
        info.setNgaySinh(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NGAYSINH)));
        info.setGioiTinh(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_GIOITINH)));
        info.setDiaChi(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DIACHI)));
        info.setSoDienThoai(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SODIENTHOAI)));
        info.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMAIL)));
        info.setChucVu(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CHUCVU)));
        info.setNgayVaoLam(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NGAYVAOLAM)));
        info.setLuongCoBan(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LUONGCOBAN)));
        info.setTrangThai(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANGTHAI_NV)));
        info.setGhiChu(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_GHICHU)));
        return info;
    }
} 