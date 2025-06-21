package com.example.tiemchuixe.model;

public class ChiTietPhieuDichVu {
    private int maChiTiet;
    private int maPhieu;
    private int maDV;
    private int soLuong;
    private double donGiaLucTao;

    // Constructors
    public ChiTietPhieuDichVu() {
    }

    public ChiTietPhieuDichVu(int maPhieu, int maDV, int soLuong, double donGiaLucTao) {
        this.maPhieu = maPhieu;
        this.maDV = maDV;
        this.soLuong = soLuong;
        this.donGiaLucTao = donGiaLucTao;
    }

    // Getters and Setters
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

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getDonGiaLucTao() {
        return donGiaLucTao;
    }

    public void setDonGiaLucTao(double donGiaLucTao) {
        this.donGiaLucTao = donGiaLucTao;
    }
} 