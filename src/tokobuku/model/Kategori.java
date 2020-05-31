package tokobuku.model;

/**
 *
 * @author Rosyid Iz
 */
public class Kategori {
    private int kode_kategori;
    private String nama_kategori;
    private String rak;

    public int getKode_kategori() {
        return kode_kategori;
    }

    public void setKode_kategori(int kode_kategori) {
        this.kode_kategori = kode_kategori;
    }

    public String getNama_kategori() {
        return nama_kategori;
    }

    public void setNama_kategori(String nama_kategori) {
        this.nama_kategori = nama_kategori;
    }

    public String getRak() {
        return rak;
    }

    public void setRak(String rak) {
        this.rak = rak;
    }
}
