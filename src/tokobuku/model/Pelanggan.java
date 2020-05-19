package tokobuku.model;

/**
 * Model untuk Data Pelanggan
 * @author Rosyid Iz
 */
public class Pelanggan {
    private int id_pelanggan;
    private String nama_pelanggan;
    private String alamat;
    private String noTelp;

    /**
     * Mengembalikan id pelanggan
     * @return a <code>int</code>
     */
    public int getId_pelanggan() {
        return id_pelanggan;
    }

    /**
     * Mengubah id pelanggan
     * @param id_pelanggan 
     */
    public void setId_pelanggan(int id_pelanggan) {
        this.id_pelanggan = id_pelanggan;
    }

    /**
     * Mengembalikan nama pelanggan
     * @return a <code>String</code>
     */
    public String getNama_pelanggan() {
        return nama_pelanggan;
    }

    /**
     * Mengubah nama pelanggan
     * @param nama_pelanggan 
     */
    public void setNama_pelanggan(String nama_pelanggan) {
        this.nama_pelanggan = nama_pelanggan;
    }

    /**
     * Mengembalikan alamat pelanggan
     * @return a <code>String</code>
     */
    public String getAlamat() {
        return alamat;
    }

    /**
     * Mengubah alamat pelanggan
     * @param alamat 
     */
    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    /**
     * Mengembalikan Nomor telepon pelanggan
     * @return a <code>String</code>
     */
    public String getNoTelp() {
        return noTelp;
    }

    /**
     * Mengubah Nomor telepon
     * @param noTelp 
     */
    public void setNoTelp(String noTelp) {
        this.noTelp = noTelp;
    }
}