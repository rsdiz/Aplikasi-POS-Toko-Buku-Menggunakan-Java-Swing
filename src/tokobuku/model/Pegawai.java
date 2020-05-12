package tokobuku.model;

/**
 *
 * @author Rosyid Iz
 */
public class Pegawai {
    private int id_user;
    private String nama;
    private String alamat;
    private String telepon;
    private String username;
    private String password;
    
    public int getId()
    {
        return this.id_user;
    }
    
    public void setId(int id_user)
    {
        this.id_user = id_user;
    }
    
    public String getUsername()
    {
        return this.username;
    }
    
    public void setUsername(String username)
    {
        this.username = username;
    }
    
    public String getPassword()
    {
        return this.password;
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    public String getNama()
    {
        return this.nama;
    }
    
    public void setNama(String nama)
    {
        this.nama = nama;
    }
    
    public String getAlamat()
    {
        return this.alamat;
    }
    
    public void setAlamat(String alamat)
    {
        this.alamat = alamat;
    }
    
    public String getTelepon()
    {
        return this.telepon;
    }
    
    public void setTelepon(String telepon)
    {
        this.telepon = telepon;
    }
}