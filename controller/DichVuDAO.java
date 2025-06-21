package com.example.tiemchuixe.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;
import com.example.tiemchuixe.model.DichVu;
import com.example.tiemchuixe.controller.DatabaseHelper;

public class DichVuDAO {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public DichVuDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long addDichVu(DichVu dichVu) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TENDICHVU, dichVu.getTenDichVu());
        values.put(DatabaseHelper.COLUMN_GIATIEN, dichVu.getGiaTien());
        values.put(DatabaseHelper.COLUMN_LOAIXE, dichVu.getLoaiXe());

        long insertedId = database.insert(DatabaseHelper.TABLE_DICHVU, null, values);
        return insertedId;
    }

    public DichVu getDichVuById(int maDV) {
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_DICHVU,
                new String[]{DatabaseHelper.COLUMN_MADV, DatabaseHelper.COLUMN_TENDICHVU, 
                           DatabaseHelper.COLUMN_GIATIEN, DatabaseHelper.COLUMN_LOAIXE},
                DatabaseHelper.COLUMN_MADV + " = ?",
                new String[]{String.valueOf(maDV)},
                null, null, null);

        DichVu dichVu = null;
        if (cursor != null && cursor.moveToFirst()) {
            dichVu = new DichVu();
            dichVu.setMaDV(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MADV)));
            dichVu.setTenDichVu(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TENDICHVU)));
            dichVu.setGiaTien(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_GIATIEN)));
            dichVu.setLoaiXe(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LOAIXE)));
        }
        if (cursor != null) {
            cursor.close();
        }
        return dichVu;
    }

    public DichVu getDichVuByTen(String tenDichVu) {
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_DICHVU,
                new String[]{DatabaseHelper.COLUMN_MADV, DatabaseHelper.COLUMN_TENDICHVU, 
                           DatabaseHelper.COLUMN_GIATIEN, DatabaseHelper.COLUMN_LOAIXE},
                DatabaseHelper.COLUMN_TENDICHVU + " = ?",
                new String[]{tenDichVu},
                null, null, null);

        DichVu dichVu = null;
        if (cursor != null && cursor.moveToFirst()) {
            dichVu = new DichVu();
            dichVu.setMaDV(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MADV)));
            dichVu.setTenDichVu(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TENDICHVU)));
            dichVu.setGiaTien(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_GIATIEN)));
            dichVu.setLoaiXe(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LOAIXE)));
        }
        if (cursor != null) {
            cursor.close();
        }
        return dichVu;
    }

    public List<DichVu> getAllDichVu() {
        List<DichVu> dichVuList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_DICHVU;

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DichVu dichVu = new DichVu();
                dichVu.setMaDV(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MADV)));
                dichVu.setTenDichVu(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TENDICHVU)));
                dichVu.setGiaTien(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_GIATIEN)));
                dichVu.setLoaiXe(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LOAIXE)));
                dichVuList.add(dichVu);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }
        return dichVuList;
    }

    public int updateDichVu(DichVu dichVu) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TENDICHVU, dichVu.getTenDichVu());
        values.put(DatabaseHelper.COLUMN_GIATIEN, dichVu.getGiaTien());
        values.put(DatabaseHelper.COLUMN_LOAIXE, dichVu.getLoaiXe());

        return database.update(DatabaseHelper.TABLE_DICHVU,
                              values,
                              DatabaseHelper.COLUMN_MADV + " = ?",
                              new String[]{String.valueOf(dichVu.getMaDV())});
    }

    public void deleteDichVu(int maDV) {
        database.delete(DatabaseHelper.TABLE_DICHVU, DatabaseHelper.COLUMN_MADV + " = ?",
                new String[]{String.valueOf(maDV)});
    }

    public List<DichVu> getDichVuByLoaiXe(String loaiXe) {
        List<DichVu> dichVuList = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_DICHVU,
                new String[]{DatabaseHelper.COLUMN_MADV, DatabaseHelper.COLUMN_TENDICHVU, 
                           DatabaseHelper.COLUMN_GIATIEN, DatabaseHelper.COLUMN_LOAIXE},
                DatabaseHelper.COLUMN_LOAIXE + " = ?",
                new String[]{loaiXe},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                DichVu dichVu = new DichVu();
                dichVu.setMaDV(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MADV)));
                dichVu.setTenDichVu(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TENDICHVU)));
                dichVu.setGiaTien(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_GIATIEN)));
                dichVu.setLoaiXe(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LOAIXE)));
                dichVuList.add(dichVu);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return dichVuList;
    }
} 