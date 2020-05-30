package tokobuku.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import tokobuku.interf.PegawaiInterface;
import tokobuku.model.Pegawai;
import tokobuku.util.ConnectionUtil;

/**
 *
 * @author Rosyid Iz
 */
public class PegawaiImpl implements PegawaiInterface {
    
    private final Connection con = ConnectionUtil.getDB();
    public List<Pegawai> listPegawais = new ArrayList<>();

    @Override
    public void insert(Pegawai pegawai) throws SQLException {
        String sql = "{ CALL insert_pegawai(?,?,?,?,?,?,?,?,?) }";
        try (PreparedStatement ps = con.prepareCall(sql)) {
            ps.setInt(1, pegawai.getIdKasir());
            ps.setString(2, pegawai.getNama());
            ps.setString(3, pegawai.getAlamat());
            ps.setString(4, pegawai.getTelepon());
            ps.setString(5, pegawai.getShif());
            ps.setString(6, pegawai.getUsername());
            ps.setString(7, pegawai.getPassword());
            ps.setString(8, pegawai.getSalt());
            ps.setString(9, pegawai.getAkses());
            ps.executeUpdate();
            listPegawais.add(pegawai);
        }
    }

    @Override
    public void update(Pegawai pegawai) throws SQLException {
        String sql = "{ CALL update_pegawai(?,?,?,?,?,?,?,?) }";
        try (PreparedStatement ps = con.prepareCall(sql)) {
            ps.setInt(1, pegawai.getIdKasir());
            ps.setString(2, pegawai.getNama());
            ps.setString(3, pegawai.getAlamat());
            ps.setString(4, pegawai.getTelepon());
            ps.setString(5, pegawai.getShif());
            ps.setString(6, pegawai.getUsername());
            ps.setString(7, pegawai.getPassword());
            ps.setString(8, pegawai.getAkses());
            ps.executeUpdate();
            listPegawais.set(pegawai.getIdKasir()-1, pegawai);
        }
    }

    @Override
    public void updatePwd(Pegawai pegawai) throws SQLException {
        String sql = "{ CALL update_password(?,?) }";
        try (PreparedStatement ps = con.prepareCall(sql)) {
            ps.setInt(1, pegawai.getIdKasir());
            ps.setString(2, pegawai.getPassword());
            ps.executeUpdate();
            listPegawais.set(pegawai.getIdKasir()-1, pegawai);
        }
    }

    @Override
    public void delete(Pegawai pegawai) throws SQLException {
        String sql = "{ CALL delete_pegawai(?) }";
        try (PreparedStatement ps = con.prepareCall(sql)) {
            ps.setInt(1, pegawai.getIdKasir());
            ps.executeUpdate();
            listPegawais.remove(pegawai.getIdKasir()-1);
        }
    }

    @Override
    public List<Pegawai> load() throws SQLException {
        String sql = "SELECT * FROM tb_pegawai";
        try (Statement statement = con.createStatement()) {
            ResultSet res = statement.executeQuery(sql);
            while(res.next()) {
                Pegawai pegawai = new Pegawai();
                addPegawaiToList(pegawai, res);
                listPegawais.add(pegawai);
            }
        }
        return listPegawais;
    }
    
    private void addPegawaiToList(Pegawai pegawai, ResultSet res) throws SQLException {
        pegawai.setIdKasir(res.getInt("id_kasir"));
        pegawai.setNama(res.getString("nama"));
        pegawai.setAlamat(res.getString("alamat"));
        pegawai.setTelepon(res.getString("telepon"));
        pegawai.setShif(res.getString("shif"));
        pegawai.setUsername(res.getString("username"));
        pegawai.setPassword(res.getString("pwd"));
        pegawai.setSalt(res.getString("salt"));
        pegawai.setAkses(res.getString("akses"));
    }
    
}
