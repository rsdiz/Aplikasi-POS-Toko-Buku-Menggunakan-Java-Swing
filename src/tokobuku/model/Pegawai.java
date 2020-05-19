package tokobuku.model;

/**
 * Model untuk Data Pegawai
 * @author Rosyid Iz
 */
public class Pegawai {
    private int id_kasir;
    private String nama;
    private String alamat;
    private String telepon;
    private String username;
    private String password;
    
    /**
     * Mengembalikan id kasir/pegawai
     * @return a <code>int</code>
     */
    public int getId()
    {
        return this.id_kasir;
    }
    
    /**
     * Mengubah id kasir/pegawai
     * @param id_kasir 
     */
    public void setId(int id_kasir)
    {
        this.id_kasir = id_kasir;
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
}