package com.example.tiemchuixe.model;

public class DichVu {
    private int maDV;
    private String tenDichVu;
    private double giaTien;
    private String loaiXe;

    // Constructors
    public DichVu() {
    }

    public DichVu(String tenDichVu, double giaTien) {
        this.tenDichVu = tenDichVu;
        this.giaTien = giaTien;
    }

    // Getters and Setters
    public int getMaDV() {
        return maDV;
    }

    public void setMaDV(int maDV) {
        this.maDV = maDV;
    }

    public String getTenDichVu() {
        return tenDichVu;
    }

    public void setTenDichVu(String tenDichVu) {
        this.tenDichVu = tenDichVu;
    }

    public double getGiaTien() {
        return giaTien;
    }

    public void setGiaTien(double giaTien) {
        this.giaTien = giaTien;
    }

    public String getLoaiXe() {
        return loaiXe;
    }

    public void setLoaiXe(String loaiXe) {
        this.loaiXe = loaiXe;
    }
} 