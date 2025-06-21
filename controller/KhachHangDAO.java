package com.example.tiemchuixe.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;
import com.example.tiemchuixe.model.KhachHang;
import com.example.tiemchuixe.controller.DatabaseHelper;

public class KhachHangDAO {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public KhachHangDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long addKhachHang(KhachHang khachHang) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TENKHACH_KH, khachHang.getTenKhachHang());
        values.put(DatabaseHelper.COLUMN_SODIENTHOAI_KH, khachHang.getSoDienThoai());
        values.put(DatabaseHelper.COLUMN_BIENSOXE_KH, khachHang.getBienSoXe());

        return database.insert(DatabaseHelper.TABLE_KHACHHANG, null, values);
    }

    public int updateKhachHang(KhachHang khachHang) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TENKHACH_KH, khachHang.getTenKhachHang());
        values.put(DatabaseHelper.COLUMN_SODIENTHOAI_KH, khachHang.getSoDienThoai());
        values.put(DatabaseHelper.COLUMN_BIENSOXE_KH, khachHang.getBienSoXe());

        return database.update(DatabaseHelper.TABLE_KHACHHANG, values, 
                DatabaseHelper.COLUMN_MAKH + " = ?",
                new String[]{String.valueOf(khachHang.getMaKH())});
    }

    public KhachHang getKhachHangByPhone(String phone) {
        Cursor cursor = database.query(DatabaseHelper.TABLE_KHACHHANG,
                new String[]{DatabaseHelper.COLUMN_MAKH, DatabaseHelper.COLUMN_TENKHACH_KH, 
                           DatabaseHelper.COLUMN_SODIENTHOAI_KH, DatabaseHelper.COLUMN_BIENSOXE_KH},
                DatabaseHelper.COLUMN_SODIENTHOAI_KH + " = ?",
                new String[]{phone},
                null, null, null);

        KhachHang khachHang = null;
        if (cursor != null && cursor.moveToFirst()) {
            khachHang = new KhachHang();
            khachHang.setMaKH(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MAKH)));
            khachHang.setTenKhachHang(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TENKHACH_KH)));
            khachHang.setSoDienThoai(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SODIENTHOAI_KH)));
            khachHang.setBienSoXe(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BIENSOXE_KH)));
        }
        if (cursor != null) {
            cursor.close();
        }
        return khachHang;
    }

    public KhachHang getKhachHangById(int maKH) {
        Cursor cursor = database.query(DatabaseHelper.TABLE_KHACHHANG,
                new String[]{DatabaseHelper.COLUMN_MAKH, DatabaseHelper.COLUMN_TENKHACH_KH, 
                           DatabaseHelper.COLUMN_SODIENTHOAI_KH, DatabaseHelper.COLUMN_BIENSOXE_KH},
                DatabaseHelper.COLUMN_MAKH + " = ?",
                new String[]{String.valueOf(maKH)},
                null, null, null);

        KhachHang khachHang = null;
        if (cursor != null && cursor.moveToFirst()) {
            khachHang = new KhachHang();
            khachHang.setMaKH(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MAKH)));
            khachHang.setTenKhachHang(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TENKHACH_KH)));
            khachHang.setSoDienThoai(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SODIENTHOAI_KH)));
            khachHang.setBienSoXe(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BIENSOXE_KH)));
        }
        if (cursor != null) {
            cursor.close();
        }
        return khachHang;
    }

    public List<KhachHang> getAllKhachHang() {
        List<KhachHang> khachHangList = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_KHACHHANG,
                new String[]{DatabaseHelper.COLUMN_MAKH, DatabaseHelper.COLUMN_TENKHACH_KH, 
                           DatabaseHelper.COLUMN_SODIENTHOAI_KH, DatabaseHelper.COLUMN_BIENSOXE_KH},
                null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                KhachHang khachHang = new KhachHang();
                khachHang.setMaKH(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MAKH)));
                khachHang.setTenKhachHang(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TENKHACH_KH)));
                khachHang.setSoDienThoai(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SODIENTHOAI_KH)));
                khachHang.setBienSoXe(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BIENSOXE_KH)));
                khachHangList.add(khachHang);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return khachHangList;
    }

    public void deleteKhachHang(int maKH) {
        database.delete(DatabaseHelper.TABLE_KHACHHANG, DatabaseHelper.COLUMN_MAKH + " = ?",
                new String[]{String.valueOf(maKH)});
    }
}