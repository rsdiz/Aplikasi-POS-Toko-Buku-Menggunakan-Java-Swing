package tokobuku.model;

/**
 * Model untuk Data Transaksi
 * @author Rosyid Iz
 */
public class Transaksi {
    private int id = 0;
    private int idPelanggan = 0;
    private String namaPelanggan = "";
    private int idPetugas = 0;
    private String namaPetugas = "";
    private String tanggal;
    private Double nominalBayar = 0D;
    private int totalBayar = 0;
    
    /**
     * Mengembalikan Nama Pelanggan
     * @return a <code>String</code>
     */
    public String getNamaPelanggan() {
        return namaPelanggan;
    }

    /**
     * Mengubah Nama Pelanggan
     * @param namaPelanggan 
     */
    public void setNamaPelanggan(String namaPelanggan) {
        this.namaPelanggan = namaPelanggan;
    }

    /**
     * Mengembalikan Nama Petugas atau Kasir
     * @return a <code>String</code>
     */
    public String getNamaPetugas() {
        return namaPetugas;
    }

    /**
     * Mengubah Nama Petugas atau Kasir
     * @param namaPetugas 
     */
    public void setNamaPetugas(String namaPetugas) {
        this.namaPetugas = namaPetugas;
    }

    /**
     * Mengembalikan Tanggal terjadinya transaksi
     * @return a <code>Date</code>
     */
    public String getTanggal() {
        return tanggal;
    }

    /**
     * Mengubah tanggal terjadinya transaksi
     * @param tanggal 
     */
    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    /**
     * Mengembalikan jumlah uang yang
     * dibayarkan oleh pelanggan
     * @return a <code>Double</code>
     */
    public Double getNominalBayar() {
        return nominalBayar;
    }

    /**
     * Mengubah jumlah uang yang dibayar
     * @param nominalBayar 
     */
    public void setNominalBayar(Double nominalBayar) {
        this.nominalBayar = nominalBayar;
    }

    /**
     * Mengembalikan Nomer Transaksi
     * @return a <code>int</code>
     */
    public int getId() {
        return id;
    }

    /**
     * Mengubah nomer transaksi
     * @param id 
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Mengembalikan id pelanggan
     * @return a <code>int</code>
     */
    public int getIdPelanggan() {
        return idPelanggan;
    }

    /**
     * Mengubah id pelanggan
     * @param idPelanggan 
     */
    public void setIdPelanggan(int idPelanggan) {
        this.idPelanggan = idPelanggan;
    }

    /**
     * Mengembalikan id petugas / kasir
     * @return a <code>int</code>
     */
    public int getIdPetugas() {
        return idPetugas;
    }

    /**
     * Mengubah id petugas/kasir
     * @param idPetugas 
     */
    public void setIdPetugas(int idPetugas) {
        this.idPetugas = idPetugas;
    }

    /**
     * Mengembalikan jumlah uang yang harus dibayarkan
     * @return a <code>int</code>
     */
    public int getTotalBayar() {
        return totalBayar;
    }

    /**
     * Mengubah jumlah uang yang harus dibayar
     * @param totalBayar 
     */
    public void setTotalBayar(int totalBayar) {
        this.totalBayar = totalBayar;
    }
}
