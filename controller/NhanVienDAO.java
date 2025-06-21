package com.example.tiemchuixe.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import com.example.tiemchuixe.model.NhanVien;
import com.example.tiemchuixe.controller.DatabaseHelper;

public class NhanVienDAO {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public NhanVienDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        try {
            database = dbHelper.getWritableDatabase();
            Log.d("NhanVienDAO", "Database opened successfully");
        } catch (Exception e) {
            Log.e("NhanVienDAO", "Error opening database: " + e.getMessage(), e);
            throw e;
        }
    }

    public void close() {
        try {
            dbHelper.close();
            Log.d("NhanVienDAO", "Database closed successfully");
        } catch (Exception e) {
            Log.e("NhanVienDAO", "Error closing database: " + e.getMessage(), e);
        }
    }

    public long addNhanVien(NhanVien nhanVien) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TENNHANVIEN, nhanVien.getTenNhanVien());
        values.put(DatabaseHelper.COLUMN_TENDANGNHAP, nhanVien.getTenDangNhap());
        values.put(DatabaseHelper.COLUMN_MATKHAU, DatabaseHelper.hashPassword(nhanVien.getMatKhau())); // Store hashed password
        values.put(DatabaseHelper.COLUMN_VAITRO, nhanVien.getVaiTro());

        long insertedId = database.insert(DatabaseHelper.TABLE_NHANVIEN, null, values);
        return insertedId;
    }

    public NhanVien getNhanVienById(int maNV) {
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_NHANVIEN,
                new String[]{DatabaseHelper.COLUMN_MANV, DatabaseHelper.COLUMN_TENNHANVIEN, DatabaseHelper.COLUMN_TENDANGNHAP, DatabaseHelper.COLUMN_VAITRO},
                DatabaseHelper.COLUMN_MANV + " = ?",
                new String[]{String.valueOf(maNV)},
                null, null, null);

        NhanVien nhanVien = null;
        if (cursor != null && cursor.moveToFirst()) {
            nhanVien = new NhanVien();
            nhanVien.setMaNV(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MANV)));
            nhanVien.setTenNhanVien(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TENNHANVIEN)));
            nhanVien.setTenDangNhap(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TENDANGNHAP)));
            nhanVien.setVaiTro(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_VAITRO)));
            // Note: We don't retrieve the hashed password here for security reasons
        }
        if (cursor != null) {
            cursor.close();
        }
        return nhanVien;
    }

     public NhanVien getNhanVienByTenDangNhap(String tenDangNhap) {
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_NHANVIEN,
                new String[]{DatabaseHelper.COLUMN_MANV, DatabaseHelper.COLUMN_TENNHANVIEN, DatabaseHelper.COLUMN_TENDANGNHAP, DatabaseHelper.COLUMN_VAITRO},
                DatabaseHelper.COLUMN_TENDANGNHAP + " = ?",
                new String[]{tenDangNhap},
                null, null, null);

        NhanVien nhanVien = null;
        if (cursor != null && cursor.moveToFirst()) {
            nhanVien = new NhanVien();
            nhanVien.setMaNV(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MANV)));
            nhanVien.setTenNhanVien(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TENNHANVIEN)));
            nhanVien.setTenDangNhap(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TENDANGNHAP)));
            nhanVien.setVaiTro(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_VAITRO)));
            // Note: We don't retrieve the hashed password here for security reasons
        }
        if (cursor != null) {
            cursor.close();
        }
        return nhanVien;
    }

    public List<NhanVien> getAllNhanVien() {
        List<NhanVien> nhanVienList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DatabaseHelper.TABLE_NHANVIEN;

        Cursor cursor = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                NhanVien nhanVien = new NhanVien();
                nhanVien.setMaNV(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MANV)));
                nhanVien.setTenNhanVien(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TENNHANVIEN)));
                nhanVien.setTenDangNhap(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TENDANGNHAP)));
                nhanVien.setVaiTro(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_VAITRO)));
                // Note: We don't retrieve the hashed password here for security reasons
                nhanVienList.add(nhanVien);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }
        return nhanVienList;
    }

    public int updateNhanVien(NhanVien nhanVien) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TENNHANVIEN, nhanVien.getTenNhanVien());
        values.put(DatabaseHelper.COLUMN_TENDANGNHAP, nhanVien.getTenDangNhap());
        // Only update password if a new one is provided and hashed
        if (nhanVien.getMatKhau() != null && !nhanVien.getMatKhau().isEmpty()) {
             values.put(DatabaseHelper.COLUMN_MATKHAU, DatabaseHelper.hashPassword(nhanVien.getMatKhau()));
        }
        values.put(DatabaseHelper.COLUMN_VAITRO, nhanVien.getVaiTro());

        // updating row
        return database.update(DatabaseHelper.TABLE_NHANVIEN,
                              values,
                              DatabaseHelper.COLUMN_MANV + " = ?",
                              new String[]{String.valueOf(nhanVien.getMaNV())});
    }

    public void deleteNhanVien(int maNV) {
        database.delete(DatabaseHelper.TABLE_NHANVIEN, DatabaseHelper.COLUMN_MANV + " = ?",
                new String[]{String.valueOf(maNV)});
    }

    public boolean checkLogin(String tenDangNhap, String matKhau) {
        try {
            Log.d("NhanVienDAO", "Starting login check for user: " + tenDangNhap);
            
            // Query to retrieve the hashed password for the given username
            String query = "SELECT * FROM " + DatabaseHelper.TABLE_NHANVIEN + " WHERE " + DatabaseHelper.COLUMN_TENDANGNHAP + " = ?";
            Log.d("NhanVienDAO", "Executing query: " + query + " with username: " + tenDangNhap);
            
            Cursor cursor = database.rawQuery(query, new String[]{tenDangNhap});
            Log.d("NhanVienDAO", "Query returned " + (cursor != null ? cursor.getCount() : 0) + " rows");

            boolean loginSuccessful = false;
            if (cursor != null && cursor.moveToFirst()) {
                String hashedPassword = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MATKHAU));
                String inputHashedPassword = DatabaseHelper.hashPassword(matKhau);
                String vaiTro = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_VAITRO));
                int maNV = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MANV));
                
                Log.d("NhanVienDAO", "Found user - MaNV: " + maNV + ", VaiTro: " + vaiTro);
                Log.d("NhanVienDAO", "Stored hash: " + hashedPassword);
                Log.d("NhanVienDAO", "Input hash: " + inputHashedPassword);
                
                // Hash the provided password and compare with the stored hashed password
                if (hashedPassword != null && hashedPassword.equals(inputHashedPassword)) {
                    loginSuccessful = true;
                    Log.d("NhanVienDAO", "Login successful - Password match");
                } else {
                    Log.d("NhanVienDAO", "Login failed - Password mismatch");
                }
            } else {
                Log.d("NhanVienDAO", "Login failed - User not found");
            }

            if (cursor != null) {
                cursor.close();
            }
            return loginSuccessful;
        } catch (Exception e) {
            Log.e("NhanVienDAO", "Error during login check: " + e.getMessage(), e);
            throw e;
        }
    }

    public String getNhanVienVaiTro(String tenDangNhap) {
        String vaiTro = null;
        String query = "SELECT " + DatabaseHelper.COLUMN_VAITRO + " FROM " + DatabaseHelper.TABLE_NHANVIEN + " WHERE " + DatabaseHelper.COLUMN_TENDANGNHAP + " = ?";
        Cursor cursor = database.rawQuery(query, new String[]{tenDangNhap});

        if (cursor != null && cursor.moveToFirst()) {
            vaiTro = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_VAITRO));
        }

        if (cursor != null) {
            cursor.close();
        }
        return vaiTro;
    }

} 