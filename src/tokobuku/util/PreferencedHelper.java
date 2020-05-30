package tokobuku.util;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 *
 * @author Rosyid Iz
 */
public class PreferencedHelper {
    static Preferences prefs = Preferences.userNodeForPackage(tokobuku.SistemTokoBuku.class);
    static final String ID_USER = "id_user";
    static final String UNAME = "username";
    static final String NAME = "nama";
    static final String TELP = "telepon";
    static final String ADDRESS = "alamat";
    static final String IS_LOGIN = "isLogin";
    static final String AKSES = "akses";

    public PreferencedHelper() {
        prefs.node("Pegawai");
    }
    
    public static String getAkses() {
        return prefs.get(AKSES, "");
    }
    
    public static void setAkses(String akses) {
        prefs.put(AKSES, akses);
    }
    
    public static void clear() throws BackingStoreException{
        prefs.clear();
    }
    
    public static boolean getLogin(){
        return prefs.getBoolean(IS_LOGIN, false);
    }
    
    public static void setLogin(boolean status){
        prefs.putBoolean(IS_LOGIN, status);
    }
    
    public static int getId(){
        return prefs.getInt(ID_USER, -1);
    }
    
    public static void setId(int id){
        prefs.putInt(ID_USER, id);
    }
    
    public static String getUname(){
        return prefs.get(UNAME, "");
    }
    
    public static void setUname(String uname){
        prefs.put(UNAME, uname);
    }
    
    public static String getName(){
        return prefs.get(NAME, "");
    }
    
    public static void setName(String name){
        prefs.put(NAME, name);
    }
    
    public static String getTel(){
        return prefs.get(TELP, "");
    }
    
    public static void setTel(String tel){
        prefs.put(TELP, tel);
    }
    
    public static String getAddress(){
        return prefs.get(ADDRESS, "");
    }
    
    public static void setAddress(String address){
        prefs.put(ADDRESS, address);
    }
    
}
