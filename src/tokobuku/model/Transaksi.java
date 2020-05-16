package tokobuku.model;

import java.util.Date;

/**
 *
 * @author Rosyid Iz
 */
public class Transaksi {
    private int id;
    private int idPelanggan;
    private String namaPelanggan;
    private int idPetugas;
    private String namaPetugas;
    private Date tanggal;
    private Double nominalBayar;

    public String getNamaPelanggan() {
        return namaPelanggan;
    }

    public void setNamaPelanggan(String namaPelanggan) {
        this.namaPelanggan = namaPelanggan;
    }

    public String getNamaPetugas() {
        return namaPetugas;
    }

    public void setNamaPetugas(String namaPetugas) {
        this.namaPetugas = namaPetugas;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    public Double getNominalBayar() {
        return nominalBayar;
    }

    public void setNominalBayar(Double nominalBayar) {
        this.nominalBayar = nominalBayar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPelanggan() {
        return idPelanggan;
    }

    public void setIdPelanggan(int idPelanggan) {
        this.idPelanggan = idPelanggan;
    }

    public int getIdPetugas() {
        return idPetugas;
    }

    public void setIdPetugas(int idPetugas) {
        this.idPetugas = idPetugas;
    }
}
