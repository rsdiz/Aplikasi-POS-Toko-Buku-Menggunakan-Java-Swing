/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tokobuku.model;

/**
 *
 * @author Rosyid Iz
 */
public class DetailTransaksi {
    private int id_detail_trx;
    private int id_trx;
    private String isbn;
    private String judul_buku;
    private int banyak;
    private int harga;

    public int getId_detail_trx() {
        return id_detail_trx;
    }

    public void setId_detail_trx(int id_detail_trx) {
        this.id_detail_trx = id_detail_trx;
    }

    public int getId_trx() {
        return id_trx;
    }

    public void setId_trx(int id_trx) {
        this.id_trx = id_trx;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getJudul_buku() {
        return judul_buku;
    }

    public void setJudul_buku(String judul_buku) {
        this.judul_buku = judul_buku;
    }

    public int getBanyak() {
        return banyak;
    }

    public void setBanyak(int banyak) {
        this.banyak = banyak;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }
}
