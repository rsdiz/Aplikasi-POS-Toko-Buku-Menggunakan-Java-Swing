/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tokobuku.model;

/**
 * Model untuk Data Detail Transaksi
 * @author Rosyid Iz
 */
public class DetailTransaksi {
    private int id_detail_trx;
    private int id_trx;
    private String isbn;
    private String judul_buku;
    private int banyak;
    private int harga;

    /**
     * Mengembalikan id detail transaksi
     * @return a <code>int</code>
     */
    public int getId_detail_trx() {
        return id_detail_trx;
    }

    /**
     * Mengubah id detail transaksi
     * @param id_detail_trx 
     */
    public void setId_detail_trx(int id_detail_trx) {
        this.id_detail_trx = id_detail_trx;
    }

    /**
     * Mengembalikan id transaksi
     * @return a <code>int</code>
     */
    public int getId_trx() {
        return id_trx;
    }

    /**
     * Mengubah id transaksi
     * @param id_trx 
     */
    public void setId_trx(int id_trx) {
        this.id_trx = id_trx;
    }

    /**
     * Mengembalikan nomor ISBN buku yang dibeli
     * @return a <code>String</code>
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Mengubah nomor ISBN buku yang dibeli
     * @param isbn 
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Mengembalikan judul buku berdasarkan ISBN
     * @return a <code>String</code>
     */
    public String getJudul_buku() {
        return judul_buku;
    }

    /**
     * Mengubah judul buku dari ISBN
     * @param judul_buku 
     */
    public void setJudul_buku(String judul_buku) {
        this.judul_buku = judul_buku;
    }

    /**
     * Mengembalikan kuantitas buku yang dibeli tiap ISBN
     * @return a <code>int</code>
     */
    public int getBanyak() {
        return banyak;
    }

    /**
     * Mengubah kuantitas buku yang dibeli tiap ISBN
     * @param banyak 
     */
    public void setBanyak(int banyak) {
        this.banyak = banyak;
    }

    /**
     * Mengembalikan harga dari buku yang dibeli
     * @return 
     */
    public int getHarga() {
        return harga;
    }

    /**
     * Mengubah harga buku yang dibeli
     * @param harga 
     */
    public void setHarga(int harga) {
        this.harga = harga;
    }
}
