package com.example.tiemchuixe.model;

public class NhanVien {
    private int maNV;
    private String tenNhanVien;
    private String tenDangNhap;
    private String matKhau;
    private String vaiTro;

    // Constructors
    public NhanVien() {
    }

    public NhanVien(String tenNhanVien, String tenDangNhap, String matKhau, String vaiTro) {
        this.tenNhanVien = tenNhanVien;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau; // Store hashed password here
        this.vaiTro = vaiTro;
    }

    // Getters and Setters
    public int getMaNV() {
        return maNV;
    }

    public void setMaNV(int maNV) {
        this.maNV = maNV;
    }

    public String getTenNhanVien() {
        return tenNhanVien;
    }

    public void setTenNhanVien(String tenNhanVien) {
        this.tenNhanVien = tenNhanVien;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }
} 