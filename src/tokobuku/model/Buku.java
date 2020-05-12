package tokobuku.model;

/**
 *
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

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getJudul_buku() {
        return judul_buku;
    }

    public void setJudul_buku(String judul_buku) {
        this.judul_buku = judul_buku;
    }

    public String getPenulis() {
        return penulis;
    }

    public void setPenulis(String penulis) {
        this.penulis = penulis;
    }

    public String getPenerbit() {
        return penerbit;
    }

    public void setPenerbit(String penerbit) {
        this.penerbit = penerbit;
    }

    public String getTahun() {
        return tahun;
    }

    public void setTahun(String tahun) {
        this.tahun = tahun;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public float getHarga_pokok() {
        return harga_pokok;
    }

    public void setHarga_pokok(float harga_pokok) {
        this.harga_pokok = harga_pokok;
    }

    public float getHarga_jual() {
        return harga_jual;
    }

    public void setHarga_jual(float harga_jual) {
        this.harga_jual = harga_jual;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
    
}
