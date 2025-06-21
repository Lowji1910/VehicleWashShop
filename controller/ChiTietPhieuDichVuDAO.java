package com.example.tiemchuixe.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;
import com.example.tiemchuixe.model.ChiTietPhieuDichVu;
import com.example.tiemchuixe.controller.DatabaseHelper;

public class ChiTietPhieuDichVuDAO {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public ChiTietPhieuDichVuDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long addChiTietPhieuDichVu(ChiTietPhieuDichVu chiTiet) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_MAPHIEU_CT, chiTiet.getMaPhieu());
        values.put(DatabaseHelper.COLUMN_MADV_CT, chiTiet.getMaDV());
        values.put(DatabaseHelper.COLUMN_SOLUONG, chiTiet.getSoLuong());
        values.put(DatabaseHelper.COLUMN_DONGIALUCTAO, chiTiet.getDonGiaLucTao());

        long insertedId = database.insert(DatabaseHelper.TABLE_CHITIETPHIEU_DICHVU, null, values);
        return insertedId;
    }

    public ChiTietPhieuDichVu getChiTietPhieuDichVuById(int maChiTiet) {
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_CHITIETPHIEU_DICHVU,
                new String[]{DatabaseHelper.COLUMN_MACHITIET, DatabaseHelper.COLUMN_MAPHIEU_CT, DatabaseHelper.COLUMN_MADV_CT, DatabaseHelper.COLUMN_SOLUONG, DatabaseHelper.COLUMN_DONGIALUCTAO},
                DatabaseHelper.COLUMN_MACHITIET + " = ?",
                new String[]{String.valueOf(maChiTiet)},
                null, null, null);

        ChiTietPhieuDichVu chiTiet = null;
        if (cursor != null && cursor.moveToFirst()) {
            chiTiet = new ChiTietPhieuDichVu();
            chiTiet.setMaChiTiet(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MACHITIET)));
            chiTiet.setMaPhieu(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MAPHIEU_CT)));
            chiTiet.setMaDV(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MADV_CT)));
            chiTiet.setSoLuong(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SOLUONG)));
            chiTiet.setDonGiaLucTao(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DONGIALUCTAO)));
        }
        if (cursor != null) {
            cursor.close();
        }
        return chiTiet;
    }

    public List<ChiTietPhieuDichVu> getChiTietPhieuByMaPhieu(int maPhieu) {
        List<ChiTietPhieuDichVu> chiTietList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_CHITIETPHIEU_DICHVU + " WHERE " + DatabaseHelper.COLUMN_MAPHIEU_CT + " = ?";

        Cursor cursor = database.rawQuery(selectQuery, new String[]{String.valueOf(maPhieu)});

        if (cursor.moveToFirst()) {
            do {
                ChiTietPhieuDichVu chiTiet = new ChiTietPhieuDichVu();
                chiTiet.setMaChiTiet(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MACHITIET)));
                chiTiet.setMaPhieu(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MAPHIEU_CT)));
                chiTiet.setMaDV(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MADV_CT)));
                chiTiet.setSoLuong(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SOLUONG)));
                chiTiet.setDonGiaLucTao(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DONGIALUCTAO)));
                chiTietList.add(chiTiet);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }
        return chiTietList;
    }

    public int updateChiTietPhieuDichVu(ChiTietPhieuDichVu chiTiet) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_MAPHIEU_CT, chiTiet.getMaPhieu());
        values.put(DatabaseHelper.COLUMN_MADV_CT, chiTiet.getMaDV());
        values.put(DatabaseHelper.COLUMN_SOLUONG, chiTiet.getSoLuong());
        values.put(DatabaseHelper.COLUMN_DONGIALUCTAO, chiTiet.getDonGiaLucTao());

        // updating row
        return database.update(DatabaseHelper.TABLE_CHITIETPHIEU_DICHVU,
                              values,
                              DatabaseHelper.COLUMN_MACHITIET + " = ?",
                              new String[]{String.valueOf(chiTiet.getMaChiTiet())});
    }

     public int deleteChiTietPhieuDichVu(int maChiTiet) {
        return database.delete(DatabaseHelper.TABLE_CHITIETPHIEU_DICHVU, DatabaseHelper.COLUMN_MACHITIET + " = ?",
                new String[]{String.valueOf(maChiTiet)});
    }

    public int deleteChiTietPhieuByMaPhieu(int maPhieu) {
        return database.delete(DatabaseHelper.TABLE_CHITIETPHIEU_DICHVU, DatabaseHelper.COLUMN_MAPHIEU_CT + " = ?",
                new String[]{String.valueOf(maPhieu)});
    }
} 