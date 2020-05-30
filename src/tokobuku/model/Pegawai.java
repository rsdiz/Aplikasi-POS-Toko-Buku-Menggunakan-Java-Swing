package tokobuku.model;

/**
 * Model untuk Data Pegawai
 * @author Rosyid Iz
 */
public class Pegawai {
    private int idKasir;
    private String nama;
    private String alamat;
    private String telepon;
    private String username;
    private String password;
    private String shif;
    private String salt;
    private String akses;
    
    /**
     * Mengembalikan id kasir/pegawai
     * @return a <code>int</code>
     */
    public int getIdKasir()
    {
        return this.idKasir;
    }
    
    /**
     * Mengubah id kasir/pegawai
     * @param idKasir 
     */
    public void setIdKasir(int idKasir)
    {
        this.idKasir = idKasir;
    }
    
    /**
     * Mengembalikan username dari kasir/pegawai
     * @return a <code>String</code>
     */
    public String getUsername()
    {
        return this.username;
    }
    
    /**
     * Mengubah username dari kasir/pegawai
     * @param username 
     */
    public void setUsername(String username)
    {
        this.username = username;
    }
    
    /**
     * Mengembalikan password kasir/pegawai
     * @return a <code>String</code>
     */
    public String getPassword()
    {
        return this.password;
    }
    
    /**
     * Mengubah password kasir/pegawai
     * @param password 
     */
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    /**
     * Mengembalikan nama pegawai
     * @return a <code>String</code>
     */
    public String getNama()
    {
        return this.nama;
    }
    
    /**
     * Mengubah nama pegawai
     * @param nama 
     */
    public void setNama(String nama)
    {
        this.nama = nama;
    }
    
    /**
     * Mengembalikan alamat dari pegawai
     * @return a <code>String</code>
     */
    public String getAlamat()
    {
        return this.alamat;
    }
    
    /**
     * Mengubah alamat dari pegawai
     * @param alamat 
     */
    public void setAlamat(String alamat)
    {
        this.alamat = alamat;
    }
    
    /**
     * Mengembalikan Nomor Telepon milik pegawai
     * @return a <code>String</code>
     */
    public String getTelepon()
    {
        return this.telepon;
    }
    
    /**
     * Mengubah Nomor telepon milik pegawai
     * @param telepon 
     */
    public void setTelepon(String telepon)
    {
        this.telepon = telepon;
    }

    public String getShif() {
        return shif;
    }

    public void setShif(String shif) {
        this.shif = shif;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getAkses() {
        return akses;
    }

    public void setAkses(String akses) {
        this.akses = akses;
    }
}