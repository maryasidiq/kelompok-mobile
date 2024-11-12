package com.example.pamkelompok;

public class Laptop {
    private String id;
    private String nama;
    private String merk;
    private String harga;
    private String tahunRilis;

    public Laptop(String id, String nama, String merk, String harga, String tahunRilis) {
        this.nama = nama;
        this.merk = merk;
        this.harga = harga;
        this.tahunRilis = tahunRilis;
        this.id = id;
    }

    public String getId() { return id; }
    public String getNama() { return nama; }
    public String getMerk() { return merk; }
    public String getHarga() { return harga; }
    public String getTahunRilis() { return tahunRilis; }
}
