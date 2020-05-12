package tokobuku.util;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 *
 * @author Rosyid Iz
 */
public class PreferencedHelper {
    Preferences prefs = Preferences.userNodeForPackage(tokobuku.SistemTokoBuku.class);
    final String ID_USER = "id_user";
    final String UNAME = "username";
    final String NAME = "nama";
    final String TELP = "telepon";
    final String ADDRESS = "alamat";
    final String isLogin = "isLogin";

    public PreferencedHelper() {
        prefs.node("Pegawai");
    }
    
    public void clear() throws BackingStoreException{
        prefs.clear();
    }
    
    public boolean getLogin(){
        return prefs.getBoolean(isLogin, false);
    }
    
    public void setLogin(boolean status){
        prefs.putBoolean(isLogin, status);
    }
    
    public int getId(){
        return prefs.getInt(ID_USER, -1);
    }
    
    public void setId(int id){
        prefs.putInt(ID_USER, id);
    }
    
    public String getUname(){
        return prefs.get(UNAME, "");
    }
    
    public void setUname(String uname){
        prefs.put(UNAME, uname);
    }
    
    public String getName(){
        return prefs.get(NAME, "");
    }
    
    public void setName(String name){
        prefs.put(NAME, name);
    }
    
    public String getTel(){
        return prefs.get(TELP, "");
    }
    
    public void setTel(String tel){
        prefs.put(TELP, tel);
    }
    
    public String getAddress(){
        return prefs.get(ADDRESS, "");
    }
    
    public void setAddress(String address){
        prefs.put(ADDRESS, address);
    }
    
}
