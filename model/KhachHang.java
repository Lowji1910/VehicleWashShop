package com.example.tiemchuixe.model;

public class KhachHang {
    private int maKH;
    private String tenKhachHang;
    private String soDienThoai;
    private String bienSoXe;

    // Constructors
    public KhachHang() {
    }

    public KhachHang(int maKH, String tenKhachHang, String soDienThoai, String bienSoXe) {
        this.maKH = maKH;
        this.tenKhachHang = tenKhachHang;
        this.soDienThoai = soDienThoai;
        this.bienSoXe = bienSoXe;
    }

    // Getters and Setters
    public int getMaKH() {
        return maKH;
    }

    public void setMaKH(int maKH) {
        this.maKH = maKH;
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getBienSoXe() {
        return bienSoXe;
    }

    public void setBienSoXe(String bienSoXe) {
        this.bienSoXe = bienSoXe;
    }
} 