package com.example.tiemchuixe.controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.content.ContentValues;
import android.database.Cursor;
import com.example.tiemchuixe.model.KhachHang;
import com.example.tiemchuixe.model.NhanVien;
import com.example.tiemchuixe.model.ThongTinNhanVien;
import com.example.tiemchuixe.model.DichVu;
import com.example.tiemchuixe.model.PhieuRuaXe;
import com.example.tiemchuixe.model.ChiTietPhieu;
import com.example.tiemchuixe.model.ChiTietPhieuDichVu;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TiemChuixe.db";
    private static final int DATABASE_VERSION = 8; // Tăng version lên 8

    // Bảng NhanVien
    public static final String TABLE_NHANVIEN = "NhanVien";
    public static final String COLUMN_MANV = "MaNV";
    public static final String COLUMN_TENNHANVIEN = "TenNhanVien";
    public static final String COLUMN_TENDANGNHAP = "TenDangNhap";
    public static final String COLUMN_MATKHAU = "MatKhau";
    public static final String COLUMN_VAITRO = "VaiTro";

    // Bảng ThongTinNhanVien (MỚI)
    public static final String TABLE_THONGTINNHANVIEN = "ThongTinNhanVien";
    public static final String COLUMN_MANV_TT = "MaNV";
    public static final String COLUMN_HOTEN = "HoTen";
    public static final String COLUMN_NGAYSINH = "NgaySinh";
    public static final String COLUMN_GIOITINH = "GioiTinh";
    public static final String COLUMN_DIACHI = "DiaChi";
    public static final String COLUMN_SODIENTHOAI = "SoDienThoai";
    public static final String COLUMN_EMAIL = "Email";
    public static final String COLUMN_CHUCVU = "ChucVu";
    public static final String COLUMN_NGAYVAOLAM = "NgayVaoLam";
    public static final String COLUMN_LUONGCOBAN = "LuongCoBan";
    public static final String COLUMN_TRANGTHAI_NV = "TrangThai";
    public static final String COLUMN_GHICHU = "GhiChu";

    // Bảng DichVu
    public static final String TABLE_DICHVU = "DichVu";
    public static final String COLUMN_MADV = "MaDV";
    public static final String COLUMN_TENDICHVU = "TenDichVu";
    public static final String COLUMN_GIATIEN = "GiaTien";
    public static final String COLUMN_LOAIXE = "LoaiXe";

    // Bảng PhieuRuaXe
    public static final String TABLE_PHIEURUAXE = "PhieuRuaXe";
    public static final String COLUMN_MAPHIEU = "MaPhieu";
    public static final String COLUMN_MAKH_PHIEU = "MaKH";
    public static final String COLUMN_MANV_TAOPHIEU = "MaNV_TaoPhieu";
    public static final String COLUMN_BIENSOXE = "BienSoXe";
    public static final String COLUMN_TRANGTHAI = "TrangThai";
    public static final String COLUMN_TONGTIEN = "TongTien";
    public static final String COLUMN_THOIGIANTAO = "ThoiGianTao";

    // Bảng ChiTietPhieu_DichVu
    public static final String TABLE_CHITIETPHIEU_DICHVU = "ChiTietPhieu_DichVu";
    public static final String COLUMN_MACHITIET = "MaChiTiet";
    public static final String COLUMN_MAPHIEU_CT = "MaPhieu";
    public static final String COLUMN_MADV_CT = "MaDV";
    public static final String COLUMN_TENDICHVU_CT = "TenDichVu";
    public static final String COLUMN_SOLUONG = "SoLuong";
    public static final String COLUMN_DONGIALUCTAO = "DonGiaLucTao";

    // Bảng KhachHang
    public static final String TABLE_KHACHHANG = "KhachHang";
    public static final String COLUMN_MAKH = "MaKH";
    public static final String COLUMN_BIENSOXE_KH = "BienSoXe";
    public static final String COLUMN_TENKHACH_KH = "TenKhach";
    public static final String COLUMN_SODIENTHOAI_KH = "SoDienThoai";

    // SQL statements to create tables
    private static final String CREATE_TABLE_NHANVIEN = "CREATE TABLE " + TABLE_NHANVIEN + "("
            + COLUMN_MANV + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TENNHANVIEN + " TEXT,"
            + COLUMN_TENDANGNHAP + " TEXT UNIQUE,"
            + COLUMN_MATKHAU + " TEXT,"
            + COLUMN_VAITRO + " TEXT" + ")";

    // Bảng ThongTinNhanVien (MỚI)
    private static final String CREATE_TABLE_THONGTINNHANVIEN = "CREATE TABLE " + TABLE_THONGTINNHANVIEN + "("
            + COLUMN_MANV_TT + " INTEGER PRIMARY KEY,"
            + COLUMN_HOTEN + " TEXT NOT NULL,"
            + COLUMN_NGAYSINH + " TEXT,"
            + COLUMN_GIOITINH + " TEXT,"
            + COLUMN_DIACHI + " TEXT,"
            + COLUMN_SODIENTHOAI + " TEXT,"
            + COLUMN_EMAIL + " TEXT,"
            + COLUMN_CHUCVU + " TEXT,"
            + COLUMN_NGAYVAOLAM + " TEXT,"
            + COLUMN_LUONGCOBAN + " REAL,"
            + COLUMN_TRANGTHAI_NV + " TEXT DEFAULT 'Đang làm việc',"
            + COLUMN_GHICHU + " TEXT,"
            + " FOREIGN KEY (" + COLUMN_MANV_TT + ") REFERENCES " + TABLE_NHANVIEN + "(" + COLUMN_MANV + ")" + ")";

    private static final String CREATE_TABLE_DICHVU = "CREATE TABLE " + TABLE_DICHVU + "("
            + COLUMN_MADV + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TENDICHVU + " TEXT UNIQUE,"
            + COLUMN_GIATIEN + " REAL,"
            + COLUMN_LOAIXE + " TEXT" + ")";

    private static final String CREATE_TABLE_PHIEURUAXE = "CREATE TABLE " + TABLE_PHIEURUAXE + "("
            + COLUMN_MAPHIEU + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_MAKH_PHIEU + " INTEGER,"
            + COLUMN_MANV_TAOPHIEU + " INTEGER,"
            + COLUMN_BIENSOXE + " TEXT,"
            + COLUMN_LOAIXE + " TEXT,"
            + COLUMN_TRANGTHAI + " TEXT,"
            + COLUMN_TONGTIEN + " REAL,"
            + COLUMN_THOIGIANTAO + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
            + " FOREIGN KEY (" + COLUMN_MAKH_PHIEU + ") REFERENCES " + TABLE_KHACHHANG + "(" + COLUMN_MAKH + "),"
            + " FOREIGN KEY (" + COLUMN_MANV_TAOPHIEU + ") REFERENCES " + TABLE_NHANVIEN + "(" + COLUMN_MANV + ")" + ")";

    private static final String CREATE_TABLE_CHITIETPHIEU_DICHVU = "CREATE TABLE " + TABLE_CHITIETPHIEU_DICHVU + "("
            + COLUMN_MACHITIET + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_MAPHIEU_CT + " INTEGER,"
            + COLUMN_MADV_CT + " INTEGER,"
            + COLUMN_TENDICHVU_CT + " TEXT,"
            + COLUMN_SOLUONG + " INTEGER DEFAULT 1,"
            + COLUMN_DONGIALUCTAO + " REAL,"
            + " FOREIGN KEY (" + COLUMN_MAPHIEU_CT + ") REFERENCES " + TABLE_PHIEURUAXE + "(" + COLUMN_MAPHIEU + "),"
            + " FOREIGN KEY (" + COLUMN_MADV_CT + ") REFERENCES " + TABLE_DICHVU + "(" + COLUMN_MADV + ")" + ")";

    private static final String CREATE_TABLE_KHACHHANG = "CREATE TABLE " + TABLE_KHACHHANG + "("
            + COLUMN_MAKH + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_BIENSOXE_KH + " TEXT,"
            + COLUMN_TENKHACH_KH + " TEXT NULLABLE,"
            + COLUMN_SODIENTHOAI_KH + " TEXT NULLABLE" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.d("DatabaseHelper", "Starting database creation...");

            // Bật foreign key constraints
            db.execSQL("PRAGMA foreign_keys = ON;");
            Log.d("DatabaseHelper", "Enabled foreign key constraints");

            // Tạo bảng NhanVien
            db.execSQL(CREATE_TABLE_NHANVIEN);
            Log.d("DatabaseHelper", "Created table NhanVien with SQL: " + CREATE_TABLE_NHANVIEN);

            // Tạo bảng ThongTinNhanVien (MỚI)
            db.execSQL(CREATE_TABLE_THONGTINNHANVIEN);
            Log.d("DatabaseHelper", "Created table ThongTinNhanVien with SQL: " + CREATE_TABLE_THONGTINNHANVIEN);

            // Tạo bảng DichVu
            db.execSQL(CREATE_TABLE_DICHVU);
            Log.d("DatabaseHelper", "Created table DichVu with SQL: " + CREATE_TABLE_DICHVU);

            // Tạo bảng KhachHang
            db.execSQL(CREATE_TABLE_KHACHHANG);
            Log.d("DatabaseHelper", "Created table KhachHang with SQL: " + CREATE_TABLE_KHACHHANG);

            // Tạo bảng PhieuRuaXe
            db.execSQL(CREATE_TABLE_PHIEURUAXE);
            Log.d("DatabaseHelper", "Created table PhieuRuaXe with SQL: " + CREATE_TABLE_PHIEURUAXE);

            // Tạo bảng ChiTietPhieu_DichVu
            db.execSQL(CREATE_TABLE_CHITIETPHIEU_DICHVU);
            Log.d("DatabaseHelper", "Created table ChiTietPhieu_DichVu with SQL: " + CREATE_TABLE_CHITIETPHIEU_DICHVU);

            // Tạo tài khoản admin mặc định
            createDefaultAdminAccount(db);

            // Thêm các dịch vụ mặc định
            addDefaultServices(db);

            Log.d("DatabaseHelper", "Database creation completed successfully");

        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error creating database: " + e.getMessage(), e);
            throw e;
        }
    }

    private void createDefaultAdminAccount(SQLiteDatabase db) {
        try {
            Log.d("DatabaseHelper", "Starting to create default admin account...");

            // Kiểm tra xem đã có tài khoản admin chưa
            Cursor cursor = db.query(TABLE_NHANVIEN,
                    new String[]{COLUMN_TENDANGNHAP, COLUMN_MATKHAU, COLUMN_VAITRO},
                    COLUMN_TENDANGNHAP + " = ?",
                    new String[]{"admin"},
                    null, null, null);

            Log.d("DatabaseHelper", "Checking for existing admin account...");

            // Nếu chưa có tài khoản admin thì tạo mới
            if (cursor == null || cursor.getCount() == 0) {
                Log.d("DatabaseHelper", "No admin account found, creating new one...");

                String password = "admin123";
                String hashedPassword = hashPassword(password);
                Log.d("DatabaseHelper", "Generated password hash for " + password + ": " + hashedPassword);

                ContentValues adminValues = new ContentValues();
                adminValues.put(COLUMN_TENNHANVIEN, "Admin");
                adminValues.put(COLUMN_TENDANGNHAP, "admin");
                adminValues.put(COLUMN_MATKHAU, hashedPassword);
                adminValues.put(COLUMN_VAITRO, "QuanLy");

                long result = db.insert(TABLE_NHANVIEN, null, adminValues);
                if (result != -1) {
                    Log.d("DatabaseHelper", "Successfully created admin account with ID: " + result);
                } else {
                    Log.e("DatabaseHelper", "Failed to create admin account");
                }
            } else {
                // Lấy thông tin tài khoản admin hiện tại
                if (cursor.moveToFirst()) {
                    String storedHash = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MATKHAU));
                    String vaiTro = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VAITRO));
                    Log.d("DatabaseHelper", "Admin account already exists with role: " + vaiTro);
                    Log.d("DatabaseHelper", "Stored password hash: " + storedHash);

                    // Nếu mật khẩu chưa được hash, cập nhật lại
                    if (!storedHash.equals(hashPassword("admin123"))) {
                        Log.d("DatabaseHelper", "Updating admin password to hashed version...");
                        ContentValues updateValues = new ContentValues();
                        updateValues.put(COLUMN_MATKHAU, hashPassword("admin123"));
                        db.update(TABLE_NHANVIEN, updateValues, COLUMN_TENDANGNHAP + " = ?", new String[]{"admin"});
                        Log.d("DatabaseHelper", "Admin password updated successfully");
                    }
                }
            }

            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error creating admin account: " + e.getMessage(), e);
            throw e;
        }
    }

    private void addDefaultServices(SQLiteDatabase db) {
        // Add some common car wash services
        String[][] defaultServices = {
                {"Rửa xe máy", "20000", "XeMay"},
                {"Rửa xe ô tô", "50000", "Oto"},
                {"Rửa xe máy + wax", "40000", "XeMay"},
                {"Rửa xe ô tô + wax", "100000", "Oto"},
                {"Rửa xe máy + đánh bóng", "60000", "XeMay"},
                {"Rửa xe ô tô + đánh bóng", "150000", "Oto"}
        };

        for (String[] service : defaultServices) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_TENDICHVU, service[0]);
            values.put(COLUMN_GIATIEN, Double.parseDouble(service[1]));
            values.put(COLUMN_LOAIXE, service[2]);
            db.insert(TABLE_DICHVU, null, values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DatabaseHelper", "Upgrading database from version " + oldVersion + " to " + newVersion);

        if (oldVersion < 5) {
            // Thêm cột LoaiXe vào bảng DichVu nếu chưa có
            try {
                db.execSQL("ALTER TABLE " + TABLE_DICHVU + " ADD COLUMN " + COLUMN_LOAIXE + " TEXT DEFAULT 'XeMay'");
                // Cập nhật dữ liệu mẫu cũ dựa trên tên dịch vụ nếu cần
                db.execSQL("UPDATE " + TABLE_DICHVU + " SET " + COLUMN_LOAIXE + " = 'XeMay' WHERE " + COLUMN_TENDICHVU + " LIKE '%xe máy%'");
                db.execSQL("UPDATE " + TABLE_DICHVU + " SET " + COLUMN_LOAIXE + " = 'Oto' WHERE " + COLUMN_TENDICHVU + " LIKE '%ô tô%'");
                Log.d("DatabaseHelper", "Successfully added LoaiXe column and updated based on service name for old versions.");
            } catch (Exception e) {
                Log.e("DatabaseHelper", "Error in upgrade for oldVersion < 5: " + e.getMessage());
                if (!e.getMessage().contains("duplicate column name")) {
                    throw e;
                }
            }
        }

        // This block ensures all existing LoaiXe values are standardized for all upgrades to version 6
        if (oldVersion < 6) {
            Log.d("DatabaseHelper", "Standardizing LoaiXe column values for new version 6.");
            try {
                // Update 'Ô tô' to 'Oto'
                ContentValues valuesOto = new ContentValues();
                valuesOto.put(COLUMN_LOAIXE, "Oto");
                db.update(TABLE_DICHVU, valuesOto, COLUMN_LOAIXE + " = ?", new String[]{"Ô tô"});
                Log.d("DatabaseHelper", "Updated 'Ô tô' to 'Oto'");

                // Update 'Xe máy' to 'XeMay'
                ContentValues valuesXeMay = new ContentValues();
                valuesXeMay.put(COLUMN_LOAIXE, "XeMay");
                db.update(TABLE_DICHVU, valuesXeMay, COLUMN_LOAIXE + " = ?", new String[]{"Xe máy"});
                Log.d("DatabaseHelper", "Updated 'Xe máy' to 'XeMay'");
            } catch (Exception e) {
                Log.e("DatabaseHelper", "Error standardizing LoaiXe values for version 6: " + e.getMessage());
                throw e;
            }
        }

        if (oldVersion < 7) {
            Log.d("DatabaseHelper", "Adding COLUMN_TENDICHVU_CT to ChiTietPhieu_DichVu for version 7.");
            try {
                db.execSQL("ALTER TABLE " + TABLE_CHITIETPHIEU_DICHVU + " ADD COLUMN " + COLUMN_TENDICHVU_CT + " TEXT");
                Log.d("DatabaseHelper", "Successfully added COLUMN_TENDICHVU_CT to ChiTietPhieu_DichVu table.");
            } catch (Exception e) {
                Log.e("DatabaseHelper", "Error adding COLUMN_TENDICHVU_CT for version 7: " + e.getMessage());
                if (!e.getMessage().contains("duplicate column name")) {
                    throw e;
                }
            }
        }

        // Thêm bảng ThongTinNhanVien cho version 8 (MỚI)
        if (oldVersion < 8) {
            try {
                db.execSQL(CREATE_TABLE_THONGTINNHANVIEN);
                Log.d("DatabaseHelper", "Created table ThongTinNhanVien with SQL: " + CREATE_TABLE_THONGTINNHANVIEN);
            } catch (Exception e) {
                Log.e("DatabaseHelper", "Error creating ThongTinNhanVien table: " + e.getMessage(), e);
                throw e;
            }
        }
    }

    // Helper method to hash password (using SHA-256 as an example)
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            String hashedPassword = hexString.toString();
            Log.d("DatabaseHelper", "Hashed password: " + password + " -> " + hashedPassword);
            return hashedPassword;
        } catch (NoSuchAlgorithmException e) {
            Log.e("DatabaseHelper", "Error hashing password", e);
            return null;
        }
    }

    // ===============================
    // PHƯƠNG THỨC CHO BẢNG THONGTINNHANVIEN (MỚI)
    // ===============================

    /**
     * Thêm thông tin chi tiết nhân viên
     */
    public long addEmployeeInfo(int maNV, String hoTen, String ngaySinh, String gioiTinh,
                                String diaChi, String soDienThoai, String email,
                                String chucVu, String ngayVaoLam, double luongCoBan,
                                String trangThai, String ghiChu) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_MANV_TT, maNV);
        values.put(COLUMN_HOTEN, hoTen);
        values.put(COLUMN_NGAYSINH, ngaySinh);
        values.put(COLUMN_GIOITINH, gioiTinh);
        values.put(COLUMN_DIACHI, diaChi);
        values.put(COLUMN_SODIENTHOAI, soDienThoai);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_CHUCVU, chucVu);
        values.put(COLUMN_NGAYVAOLAM, ngayVaoLam);
        values.put(COLUMN_LUONGCOBAN, luongCoBan);
        values.put(COLUMN_TRANGTHAI_NV, trangThai);
        values.put(COLUMN_GHICHU, ghiChu);

        long id = db.insert(TABLE_THONGTINNHANVIEN, null, values);
        db.close();
        return id;
    }

    /**
     * Lấy thông tin chi tiết nhân viên theo MaNV
     */
    public Cursor getEmployeeInfo(int maNV) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_THONGTINNHANVIEN,
                null,
                COLUMN_MANV_TT + "=?",
                new String[]{String.valueOf(maNV)},
                null, null, null);
        return cursor;
    }

    /**
     * Lấy danh sách tất cả thông tin nhân viên
     */
    public Cursor getAllEmployeeInfo() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_THONGTINNHANVIEN, null, null, null, null, null, null);
    }

    /**
     * Cập nhật thông tin chi tiết nhân viên
     */
    public int updateEmployeeInfo(int maNV, String hoTen, String ngaySinh, String gioiTinh,
                                  String diaChi, String soDienThoai, String email,
                                  String chucVu, String ngayVaoLam, double luongCoBan,
                                  String trangThai, String ghiChu) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_HOTEN, hoTen);
        values.put(COLUMN_NGAYSINH, ngaySinh);
        values.put(COLUMN_GIOITINH, gioiTinh);
        values.put(COLUMN_DIACHI, diaChi);
        values.put(COLUMN_SODIENTHOAI, soDienThoai);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_CHUCVU, chucVu);
        values.put(COLUMN_NGAYVAOLAM, ngayVaoLam);
        values.put(COLUMN_LUONGCOBAN, luongCoBan);
        values.put(COLUMN_TRANGTHAI_NV, trangThai);
        values.put(COLUMN_GHICHU, ghiChu);

        int rowsAffected = db.update(TABLE_THONGTINNHANVIEN,
                values,
                COLUMN_MANV_TT + "=?",
                new String[]{String.valueOf(maNV)});
        db.close();
        return rowsAffected;
    }

    /**
     * Xóa thông tin chi tiết nhân viên
     */
    public int deleteEmployeeInfo(int maNV) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_THONGTINNHANVIEN,
                COLUMN_MANV_TT + "=?",
                new String[]{String.valueOf(maNV)});
        db.close();
        return rowsAffected;
    }

    /**
     * Lấy danh sách tất cả nhân viên với thông tin đầy đủ (JOIN hai bảng)
     */
    public Cursor getAllEmployeesWithInfo() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT nv.*, info.* FROM " + TABLE_NHANVIEN + " nv " +
                "LEFT JOIN " + TABLE_THONGTINNHANVIEN + " info " +
                "ON nv." + COLUMN_MANV + " = info." + COLUMN_MANV_TT;
        return db.rawQuery(query, null);
    }

    /**
     * Kiểm tra xem nhân viên đã có thông tin chi tiết chưa
     */
    public boolean hasEmployeeInfo(int maNV) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_THONGTINNHANVIEN,
                new String[]{COLUMN_MANV_TT},
                COLUMN_MANV_TT + "=?",
                new String[]{String.valueOf(maNV)},
                null, null, null);

        boolean hasInfo = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return hasInfo;
    }

    /**
     * Tìm kiếm nhân viên theo tên hoặc số điện thoại
     */
    public Cursor searchEmployees(String keyword) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT nv.*, info.* FROM " + TABLE_NHANVIEN + " nv " +
                "LEFT JOIN " + TABLE_THONGTINNHANVIEN + " info " +
                "ON nv." + COLUMN_MANV + " = info." + COLUMN_MANV_TT + " " +
                "WHERE nv." + COLUMN_TENNHANVIEN + " LIKE ? " +
                "OR info." + COLUMN_HOTEN + " LIKE ? " +
                "OR info." + COLUMN_SODIENTHOAI + " LIKE ?";

        String searchPattern = "%" + keyword + "%";
        return db.rawQuery(query, new String[]{searchPattern, searchPattern, searchPattern});
    }

    /**
     * Lấy danh sách nhân viên theo trạng thái
     */
    public Cursor getEmployeesByStatus(String trangThai) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_THONGTINNHANVIEN,
                null,
                COLUMN_TRANGTHAI_NV + "=?",
                new String[]{trangThai},
                null, null, null);
    }

    /**
     * Cập nhật trạng thái nhân viên
     */
    public int updateEmployeeStatus(int maNV, String trangThai) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TRANGTHAI_NV, trangThai);

        int rowsAffected = db.update(TABLE_THONGTINNHANVIEN,
                values,
                COLUMN_MANV_TT + "=?",
                new String[]{String.valueOf(maNV)});
        db.close();
        return rowsAffected;
    }
}
