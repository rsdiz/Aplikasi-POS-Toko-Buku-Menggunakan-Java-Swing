package tokobuku.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import tokobuku.interf.LoginInterface;
import tokobuku.model.Pegawai;
import tokobuku.util.ConnectionUtil;
import tokobuku.util.PreferencedHelper;

/**
 *
 * @author Rosyid Iz
 */
public class LoginImpl implements LoginInterface {

    private final static PreferencedHelper PREFS = new PreferencedHelper();
    private final static Pegawai DATA = new Pegawai();

    @Override
    public String doLogin(String username, String password) throws SQLException {
        String result;
        try (Connection con = ConnectionUtil.getDB()) {
            String sql = "SELECT * FROM tb_pegawai WHERE username=? AND password=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet res = ps.executeQuery();
            if (res.next()) {
                result = "SUCCESS";
                saveDataUser(
                        res.getInt("id_kasir"),
                        res.getString("nama"),
                        res.getString("alamat"),
                        res.getString("telepon"),
                        res.getString("username"));
            } else {
                result = "FAILED";
            }
        }
        return result;
    }

    private void saveDataUser(
            int id,
            String nama,
            String alamat,
            String telepon,
            String username
    ) throws SQLException {
        PREFS.setLogin(true);
        PREFS.setId(id);
        PREFS.setName(nama);
        PREFS.setAddress(alamat);
        PREFS.setTel(telepon);
        PREFS.setUname(username);
    }

}
