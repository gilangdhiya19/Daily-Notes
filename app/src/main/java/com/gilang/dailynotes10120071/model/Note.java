package com.gilang.dailynotes10120071.model;
//10120071, Gilang Dhiya Ulhaq, IF2, gilang.dhiya19@gmail.com

public class Note {
    public String id;
    public String tanggal;
    public String waktu;
    public String judul;
    public String isi;
    public String id_kategori;

    public Note() {
        // Default constructor required for Firebase
    }

    public Note(String id, String tanggal, String waktu, String judul, String isi, String id_kategori) {
        this.id = id;
        this.tanggal = tanggal;
        this.waktu = waktu;
        this.judul = judul;
        this.isi = isi;
        this.id_kategori = id_kategori;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

    public String getId_kategori() {
        return id_kategori;
    }

    public void setId_kategori(String id_kategori) {
        this.id_kategori = id_kategori;
    }
}
