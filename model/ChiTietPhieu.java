package com.example.tiemchuixe.model;

public class ChiTietPhieu {
    private int maChiTiet;
    private int maPhieu;
    private int maDV;
    private String tenDichVu;
    private double donGia;
    private int soLuong;

    public ChiTietPhieu() {
    }

    public ChiTietPhieu(int maPhieu, int maDV, int soLuong, double donGiaLucTao, String tenDichVu) {
        this.maPhieu = maPhieu;
        this.maDV = maDV;
        this.soLuong = soLuong;
        this.donGia = donGiaLucTao;
        this.tenDichVu = tenDichVu;
    }

    public int getMaChiTiet() {
        return maChiTiet;
    }

    public void setMaChiTiet(int maChiTiet) {
        this.maChiTiet = maChiTiet;
    }

    public int getMaPhieu() {
        return maPhieu;
    }

    public void setMaPhieu(int maPhieu) {
        this.maPhieu = maPhieu;
    }

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

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
} 