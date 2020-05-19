package tokobuku.model;

/**
 * Model untuk Data Buku
 * @author Rosyid Iz
 */
public class Buku {
    private String isbn;
    private String kategori;
    private String judul_buku;
    private String penulis;
    private String penerbit;
    private String tahun;
    private int stok;
    private float harga_pokok;
    private float harga_jual;
    private byte[] image;

    public String getIsbn() {
        return isbn;
    }

    /**
     * Mengubah nomer ISBN buku
     * @param isbn 
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Mengembalikan nama nama kategori
     * @return a <code>String</code>
     */
    public String getKategori() {
        return kategori;
    }

    /**
     * Mengubah kategori buku
     * @param kategori 
     */
    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    /**
     * Mengembalikan nama judul buku
     * @return a <code>String</code>
     */
    public String getJudul_buku() {
        return judul_buku;
    }

    /**
     * Mengubah judul buku
     * @param judul_buku 
     */
    public void setJudul_buku(String judul_buku) {
        this.judul_buku = judul_buku;
    }

    /**
     * Mengembalikan nama penulis buku
     * @return a <code>String</code>
     */
    public String getPenulis() {
        return penulis;
    }

    /**
     * Mengubah nama penulis buku
     * @param penulis 
     */
    public void setPenulis(String penulis) {
        this.penulis = penulis;
    }

    /**
     * Mengembalikan nama penerbit
     * @return a <code>String</code>
     */
    public String getPenerbit() {
        return penerbit;
    }

    /**
     * Mengubah nama penerbit buku
     * @param penerbit 
     */
    public void setPenerbit(String penerbit) {
        this.penerbit = penerbit;
    }

    /**
     * Mengembalikan tahun terbit buku
     * @return a <code>String</code>
     */
    public String getTahun() {
        return tahun;
    }

    /**
     * Mengubah tahun terbit buku
     * @param tahun 
     */
    public void setTahun(String tahun) {
        this.tahun = tahun;
    }

    /**
     * Mengembalikan jumlah stok
     * @return a <code>int</code>
     */
    public int getStok() {
        return stok;
    }

    /**
     * Mengubah jumlah stok buku
     * @param stok 
     */
    public void setStok(int stok) {
        this.stok = stok;
    }

    /**
     * Mengembalikan harga pokok buku
     * @return a <code>float</code>
     */
    public float getHarga_pokok() {
        return harga_pokok;
    }
    
    /**
     * Mengubah Harga pokok buku
     * @param harga_pokok 
     */
    public void setHarga_pokok(float harga_pokok) {
        this.harga_pokok = harga_pokok;
    }

    /**
     * Mengembalikan harga jual buku
     * @return a <code>float</code>
     */
    public float getHarga_jual() {
        return harga_jual;
    }

    /**
     * Mengubah harga jual buku
     * @param harga_jual 
     */
    public void setHarga_jual(float harga_jual) {
        this.harga_jual = harga_jual;
    }

    /**
     * Mengembalikan gambar sampul buku
     * @return a <code>byte[]</code>
     */
    public byte[] getImage() {
        return image;
    }

    /**
     * Mengubah gambar buku
     * @param image 
     */
    public void setImage(byte[] image) {
        this.image = image;
    }
    
}
