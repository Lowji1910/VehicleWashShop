package com.example.tiemchuixe.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import android.util.Log;
import com.example.tiemchuixe.model.PhieuRuaXe;
import com.example.tiemchuixe.model.ChiTietPhieu;
import com.example.tiemchuixe.controller.DatabaseHelper;

public class PhieuRuaXeDAO {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public PhieuRuaXeDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long addPhieuRuaXe(PhieuRuaXe phieu) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_MAKH_PHIEU, phieu.getMaKH());
        values.put(DatabaseHelper.COLUMN_MANV_TAOPHIEU, phieu.getMaNV());
        values.put(DatabaseHelper.COLUMN_BIENSOXE, phieu.getBienSoXe());
        values.put(DatabaseHelper.COLUMN_LOAIXE, phieu.getLoaiXe());
        values.put(DatabaseHelper.COLUMN_TRANGTHAI, phieu.getTrangThai());
        values.put(DatabaseHelper.COLUMN_TONGTIEN, phieu.getTongTien());
        
        // Thêm ngày tạo phiếu
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        values.put(DatabaseHelper.COLUMN_THOIGIANTAO, currentDate);

        return database.insert(DatabaseHelper.TABLE_PHIEURUAXE, null, values);
    }

    public int updatePhieuRuaXe(PhieuRuaXe phieu) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_MAKH_PHIEU, phieu.getMaKH());
        values.put(DatabaseHelper.COLUMN_MANV_TAOPHIEU, phieu.getMaNV());
        values.put(DatabaseHelper.COLUMN_BIENSOXE, phieu.getBienSoXe());
        values.put(DatabaseHelper.COLUMN_LOAIXE, phieu.getLoaiXe());
        values.put(DatabaseHelper.COLUMN_TRANGTHAI, phieu.getTrangThai());
        values.put(DatabaseHelper.COLUMN_TONGTIEN, phieu.getTongTien());

        return database.update(DatabaseHelper.TABLE_PHIEURUAXE, values, 
                DatabaseHelper.COLUMN_MAPHIEU + " = ?",
                new String[]{String.valueOf(phieu.getMaPhieu())});
    }

    public PhieuRuaXe getPhieuRuaXeById(int maPhieu) {
        Cursor cursor = database.query(DatabaseHelper.TABLE_PHIEURUAXE,
                new String[]{DatabaseHelper.COLUMN_MAPHIEU, DatabaseHelper.COLUMN_MAKH_PHIEU, 
                           DatabaseHelper.COLUMN_MANV_TAOPHIEU, DatabaseHelper.COLUMN_BIENSOXE,
                           DatabaseHelper.COLUMN_LOAIXE, DatabaseHelper.COLUMN_TRANGTHAI,
                           DatabaseHelper.COLUMN_TONGTIEN, DatabaseHelper.COLUMN_THOIGIANTAO},
                DatabaseHelper.COLUMN_MAPHIEU + " = ?",
                new String[]{String.valueOf(maPhieu)},
                null, null, null);

        PhieuRuaXe phieu = null;
        if (cursor != null && cursor.moveToFirst()) {
            phieu = new PhieuRuaXe();
            phieu.setMaPhieu(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MAPHIEU)));
            phieu.setMaKH(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MAKH_PHIEU)));
            phieu.setMaNV(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MANV_TAOPHIEU)));
            phieu.setBienSoXe(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BIENSOXE)));
            phieu.setLoaiXe(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LOAIXE)));
            phieu.setTrangThai(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANGTHAI)));
            phieu.setTongTien(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TONGTIEN)));
            phieu.setNgayTao(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_THOIGIANTAO)));
        }
        if (cursor != null) {
            cursor.close();
        }
        return phieu;
    }

    public List<PhieuRuaXe> getAllTickets() {
        List<PhieuRuaXe> ticketList = new ArrayList<>();
        String selection = DatabaseHelper.COLUMN_TRANGTHAI + " != ?";
        String[] selectionArgs = {"Hoàn thành"};
        Cursor cursor = database.query(DatabaseHelper.TABLE_PHIEURUAXE,
                new String[]{DatabaseHelper.COLUMN_MAPHIEU, DatabaseHelper.COLUMN_MAKH_PHIEU, 
                           DatabaseHelper.COLUMN_MANV_TAOPHIEU, DatabaseHelper.COLUMN_BIENSOXE,
                           DatabaseHelper.COLUMN_LOAIXE, DatabaseHelper.COLUMN_TRANGTHAI,
                           DatabaseHelper.COLUMN_TONGTIEN, DatabaseHelper.COLUMN_THOIGIANTAO},
                selection, selectionArgs, null, null, DatabaseHelper.COLUMN_THOIGIANTAO + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                PhieuRuaXe phieu = new PhieuRuaXe();
                phieu.setMaPhieu(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MAPHIEU)));
                phieu.setMaKH(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MAKH_PHIEU)));
                phieu.setMaNV(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MANV_TAOPHIEU)));
                phieu.setBienSoXe(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BIENSOXE)));
                phieu.setLoaiXe(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LOAIXE)));
                phieu.setTrangThai(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANGTHAI)));
                phieu.setTongTien(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TONGTIEN)));
                phieu.setNgayTao(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_THOIGIANTAO)));
                ticketList.add(phieu);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return ticketList;
    }

    public List<PhieuRuaXe> getTicketsByStatusExcludingCompleted(String excludedStatus) {
        List<PhieuRuaXe> ticketList = new ArrayList<>();
        String selection = DatabaseHelper.COLUMN_TRANGTHAI + " != ?";
        String[] selectionArgs = {"Hoàn thành"};
        Cursor cursor = database.query(DatabaseHelper.TABLE_PHIEURUAXE,
                new String[]{DatabaseHelper.COLUMN_MAPHIEU, DatabaseHelper.COLUMN_MAKH_PHIEU,
                        DatabaseHelper.COLUMN_MANV_TAOPHIEU, DatabaseHelper.COLUMN_BIENSOXE,
                        DatabaseHelper.COLUMN_LOAIXE, DatabaseHelper.COLUMN_TRANGTHAI,
                        DatabaseHelper.COLUMN_TONGTIEN, DatabaseHelper.COLUMN_THOIGIANTAO},
                selection, selectionArgs, null, null, DatabaseHelper.COLUMN_THOIGIANTAO + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                PhieuRuaXe phieu = new PhieuRuaXe();
                phieu.setMaPhieu(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MAPHIEU)));
                phieu.setMaKH(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MAKH_PHIEU)));
                phieu.setMaNV(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MANV_TAOPHIEU)));
                phieu.setBienSoXe(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BIENSOXE)));
                phieu.setLoaiXe(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LOAIXE)));
                phieu.setTrangThai(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANGTHAI)));
                phieu.setTongTien(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TONGTIEN)));
                phieu.setNgayTao(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_THOIGIANTAO)));
                ticketList.add(phieu);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return ticketList;
    }

    public long addChiTietPhieu(int maPhieu, int maDV, String tenDichVu, double donGia) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_MAPHIEU_CT, maPhieu);
        values.put(DatabaseHelper.COLUMN_MADV_CT, maDV);
        values.put(DatabaseHelper.COLUMN_TENDICHVU_CT, tenDichVu);
        values.put(DatabaseHelper.COLUMN_DONGIALUCTAO, donGia);
        values.put(DatabaseHelper.COLUMN_SOLUONG, 1);
        return database.insert(DatabaseHelper.TABLE_CHITIETPHIEU_DICHVU, null, values);
    }

    public List<ChiTietPhieu> getChiTietPhieu(int maPhieu) {
        List<ChiTietPhieu> chiTietList = new ArrayList<>();
        String query = "SELECT ct.* FROM " + DatabaseHelper.TABLE_CHITIETPHIEU_DICHVU + " ct" +
                       " WHERE ct." + DatabaseHelper.COLUMN_MAPHIEU_CT + " = ?";

        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(maPhieu)});

        if (cursor.moveToFirst()) {
            do {
                ChiTietPhieu chiTiet = new ChiTietPhieu();
                chiTiet.setMaChiTiet(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MACHITIET)));
                chiTiet.setMaPhieu(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MAPHIEU_CT)));
                chiTiet.setMaDV(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MADV_CT)));
                chiTiet.setTenDichVu(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TENDICHVU_CT)));
                chiTiet.setDonGia(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DONGIALUCTAO)));
                chiTiet.setSoLuong(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SOLUONG)));
                chiTietList.add(chiTiet);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return chiTietList;
    }

    public double getDailyRevenue(String date) {
        double revenue = 0;
        try {
            String query = "SELECT SUM(ct.SoLuong * ct.DonGiaLucTao) as Total " +
                    "FROM " + DatabaseHelper.TABLE_CHITIETPHIEU_DICHVU + " ct " +
                    "JOIN " + DatabaseHelper.TABLE_PHIEURUAXE + " p ON ct.MaPhieu = p.MaPhieu " +
                    "WHERE strftime('%Y-%m-%d', p.ThoiGianTao) = ? AND p.TrangThai = 'Hoàn thành'";
            Cursor cursor = database.rawQuery(query, new String[]{date});
            if (cursor != null && cursor.moveToFirst()) {
                revenue = cursor.getDouble(0);
                cursor.close();
            }
        } catch (Exception e) {
            Log.e("Revenue", "Error getting daily revenue", e);
        }
        return revenue;
    }

    public List<PhieuRuaXe> getCompletedTicketsForDate(String date) {
        List<PhieuRuaXe> tickets = new ArrayList<>();
        try {
            String query = "SELECT p.*, k." + DatabaseHelper.COLUMN_TENKHACH_KH + ", k." + DatabaseHelper.COLUMN_SODIENTHOAI_KH + " " +
                    "FROM " + DatabaseHelper.TABLE_PHIEURUAXE + " p " +
                    "JOIN " + DatabaseHelper.TABLE_KHACHHANG + " k ON p.MaKH = k.MaKH " +
                    "WHERE strftime('%Y-%m-%d', p.ThoiGianTao) = ? AND p.TrangThai = 'Hoàn thành'";
            Cursor cursor = database.rawQuery(query, new String[]{date});
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    tickets.add(cursorToPhieuRuaXe(cursor));
                }
                cursor.close();
            }
        } catch (Exception e) {
            Log.e("Revenue", "Error getting completed tickets for date", e);
        }
        return tickets;
    }

    public double getMonthlyRevenue(String month) {
        double revenue = 0;
        try {
            String query = "SELECT SUM(ct.SoLuong * ct.DonGiaLucTao) as Total " +
                    "FROM " + DatabaseHelper.TABLE_CHITIETPHIEU_DICHVU + " ct " +
                    "JOIN " + DatabaseHelper.TABLE_PHIEURUAXE + " p ON ct.MaPhieu = p.MaPhieu " +
                    "WHERE strftime('%Y-%m', p.ThoiGianTao) = ? AND p.TrangThai = 'Hoàn thành'";
            Cursor cursor = database.rawQuery(query, new String[]{month});
            if (cursor != null && cursor.moveToFirst()) {
                revenue = cursor.getDouble(0);
                cursor.close();
            }
        } catch (Exception e) {
            Log.e("Revenue", "Error getting monthly revenue", e);
        }
        return revenue;
    }

    public List<PhieuRuaXe> getCompletedTicketsForMonth(String month) {
        List<PhieuRuaXe> tickets = new ArrayList<>();
        try {
            String query = "SELECT p.*, k." + DatabaseHelper.COLUMN_TENKHACH_KH + ", k." + DatabaseHelper.COLUMN_SODIENTHOAI_KH + " " +
                    "FROM " + DatabaseHelper.TABLE_PHIEURUAXE + " p " +
                    "JOIN " + DatabaseHelper.TABLE_KHACHHANG + " k ON p.MaKH = k.MaKH " +
                    "WHERE strftime('%Y-%m', p.ThoiGianTao) = ? AND p.TrangThai = 'Hoàn thành'";
            Cursor cursor = database.rawQuery(query, new String[]{month});
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    tickets.add(cursorToPhieuRuaXe(cursor));
                }
                cursor.close();
            }
        } catch (Exception e) {
            Log.e("Revenue", "Error getting completed tickets for month", e);
        }
        return tickets;
    }

    public double getYearlyRevenue(String year) {
        double revenue = 0;
        try {
            String query = "SELECT SUM(ct.SoLuong * ct.DonGiaLucTao) as Total " +
                    "FROM " + DatabaseHelper.TABLE_CHITIETPHIEU_DICHVU + " ct " +
                    "JOIN " + DatabaseHelper.TABLE_PHIEURUAXE + " p ON ct.MaPhieu = p.MaPhieu " +
                    "WHERE strftime('%Y', p.ThoiGianTao) = ? AND p.TrangThai = 'Hoàn thành'";
            Cursor cursor = database.rawQuery(query, new String[]{year});
            if (cursor != null && cursor.moveToFirst()) {
                revenue = cursor.getDouble(0);
                cursor.close();
            }
        } catch (Exception e) {
            Log.e("Revenue", "Error getting yearly revenue", e);
        }
        return revenue;
    }

    public List<PhieuRuaXe> getCompletedTicketsForYear(String year) {
        List<PhieuRuaXe> tickets = new ArrayList<>();
        try {
            String query = "SELECT p.*, k." + DatabaseHelper.COLUMN_TENKHACH_KH + ", k." + DatabaseHelper.COLUMN_SODIENTHOAI_KH + " " +
                    "FROM " + DatabaseHelper.TABLE_PHIEURUAXE + " p " +
                    "JOIN " + DatabaseHelper.TABLE_KHACHHANG + " k ON p.MaKH = k.MaKH " +
                    "WHERE strftime('%Y', p.ThoiGianTao) = ? AND p.TrangThai = 'Hoàn thành'";
            Cursor cursor = database.rawQuery(query, new String[]{year});
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    tickets.add(cursorToPhieuRuaXe(cursor));
                }
                cursor.close();
            }
        } catch (Exception e) {
            Log.e("Revenue", "Error getting completed tickets for year", e);
        }
        return tickets;
    }

    private PhieuRuaXe cursorToPhieuRuaXe(Cursor cursor) {
        PhieuRuaXe phieu = new PhieuRuaXe();
        try {
            phieu.setMaPhieu(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MAPHIEU)));
            phieu.setMaKH(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MAKH_PHIEU)));
            phieu.setMaNV(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MANV_TAOPHIEU)));
            phieu.setBienSoXe(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BIENSOXE)));
            phieu.setLoaiXe(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LOAIXE)));
            phieu.setTrangThai(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANGTHAI)));
            phieu.setTongTien(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TONGTIEN)));
            phieu.setNgayTao(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_THOIGIANTAO)));

            // Lấy thông tin khách hàng nếu có
            try {
                String tenKhach = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TENKHACH_KH));
                String soDienThoai = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SODIENTHOAI_KH));
                Log.d("Revenue", "Customer info - Name: " + tenKhach + ", Phone: " + soDienThoai);
            } catch (Exception e) {
                Log.e("Revenue", "Error getting customer info", e);
            }
        } catch (Exception e) {
            Log.e("Revenue", "Error converting cursor to PhieuRuaXe", e);
        }
        return phieu;
    }
} 