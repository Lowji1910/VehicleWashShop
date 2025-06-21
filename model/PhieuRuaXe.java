package com.example.tiemchuixe.model;

public class PhieuRuaXe {
    private int maPhieu;
    private int maKH;
    private int maNV;
    private String bienSoXe;
    private String loaiXe;
    private String trangThai;
    private double tongTien;
    private String ngayTao;

    public PhieuRuaXe() {
    }

    public PhieuRuaXe(int maPhieu, int maKH, int maNV, String bienSoXe, String loaiXe, 
                      String trangThai, double tongTien, String ngayTao) {
        this.maPhieu = maPhieu;
        this.maKH = maKH;
        this.maNV = maNV;
        this.bienSoXe = bienSoXe;
        this.loaiXe = loaiXe;
        this.trangThai = trangThai;
        this.tongTien = tongTien;
        this.ngayTao = ngayTao;
    }

    public int getMaPhieu() {
        return maPhieu;
    }

    public void setMaPhieu(int maPhieu) {
        this.maPhieu = maPhieu;
    }

    public int getMaKH() {
        return maKH;
    }

    public void setMaKH(int maKH) {
        this.maKH = maKH;
    }

    public int getMaNV() {
        return maNV;
    }

    public void setMaNV(int maNV) {
        this.maNV = maNV;
    }

    public String getBienSoXe() {
        return bienSoXe;
    }

    public void setBienSoXe(String bienSoXe) {
        this.bienSoXe = bienSoXe;
    }

    public String getLoaiXe() {
        return loaiXe;
    }

    public void setLoaiXe(String loaiXe) {
        this.loaiXe = loaiXe;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public String getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(String ngayTao) {
        this.ngayTao = ngayTao;
    }
}