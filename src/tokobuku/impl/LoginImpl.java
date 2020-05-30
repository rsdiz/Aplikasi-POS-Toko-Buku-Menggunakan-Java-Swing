package tokobuku.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import tokobuku.interf.LoginInterface;
import tokobuku.util.ConnectionUtil;
import tokobuku.util.PasswordUtils;
import tokobuku.util.PreferencedHelper;

/**
 *
 * @author Rosyid Iz
 */
public class LoginImpl implements LoginInterface {

    @Override
    public String doLogin(String username, String password) throws SQLException {
        String result;
        try (Connection con = ConnectionUtil.getDB()) {
            String sql = "SELECT * FROM tb_pegawai WHERE username=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet res = ps.executeQuery();
            if (res.next()) {
                boolean isMatch = PasswordUtils.veriifyPassword(password, res.getString("pwd"), res.getString("salt"));
                if (isMatch) {
                    result = "200";
                    saveDataUser(
                            res.getInt("id_kasir"),
                            res.getString("nama"),
                            res.getString("alamat"),
                            res.getString("telepon"),
                            res.getString("username"),
                            res.getString("akses"));
                } else {
                    result = "406";
                }
            } else {
                result = "404";
            }
        }
        return result;
    }

    private void saveDataUser(
            int id,
            String nama,
            String alamat,
            String telepon,
            String username,
            String akses
    ) throws SQLException {
        PreferencedHelper.setLogin(true);
        PreferencedHelper.setId(id);
        PreferencedHelper.setName(nama);
        PreferencedHelper.setAddress(alamat);
        PreferencedHelper.setTel(telepon);
        PreferencedHelper.setUname(username);
        PreferencedHelper.setAkses(akses);
    }
}