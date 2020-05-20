package tokobuku.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import tokobuku.interf.PelangganInterface;
import tokobuku.model.Pelanggan;
import tokobuku.util.ConnectionUtil;

/**
 *
 * @author Rosyid Iz
 */
public class PelangganImpl implements PelangganInterface {

    private final Connection con = ConnectionUtil.getDB();
    public final List<Pelanggan> listPelanggans = new ArrayList<>();

    @Override
    public void insert(Pelanggan pelanggan) throws SQLException {
        String sql = "CALL insert_pelanggan(?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, pelanggan.getId_pelanggan());
            ps.setString(2, pelanggan.getNama_pelanggan());
            ps.setString(3, pelanggan.getAlamat());
            ps.setString(4, pelanggan.getNoTelp());
            ps.executeUpdate();
            listPelanggans.add(pelanggan);
        }
    }

    @Override
    public void update(Pelanggan pelanggan) throws SQLException {
        String sql = "CALL update_pelanggan(?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, pelanggan.getId_pelanggan());
            ps.setString(2, pelanggan.getNama_pelanggan());
            ps.setString(3, pelanggan.getAlamat());
            ps.setString(4, pelanggan.getNoTelp());
            ps.executeUpdate();
            listPelanggans.set(pelanggan.getId_pelanggan()-1, pelanggan);
        }
    }

    @Override
    public void delete(Pelanggan pelanggan) throws SQLException {
        String sql = "CALL delete_pelanggan(?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, pelanggan.getId_pelanggan());
            ps.executeUpdate();
            listPelanggans.remove(pelanggan.getId_pelanggan()-1);
        }
    }

    @Override
    public List<Pelanggan> load() throws SQLException {
        String sql = "SELECT * FROM tb_pelanggan";
        try (Statement statement = con.createStatement()) {
            ResultSet res = statement.executeQuery(sql);
            while (res.next()) {
                Pelanggan pelanggan = new Pelanggan();
                addPelangganToList(pelanggan, res);
                listPelanggans.add(pelanggan);
            }
        }
        return listPelanggans;
    }

    private void addPelangganToList(Pelanggan pelanggan, ResultSet res) throws SQLException {
        pelanggan.setId_pelanggan(res.getInt("id_pelanggan"));
        pelanggan.setNama_pelanggan(res.getString("nama_pelanggan"));
        pelanggan.setAlamat(res.getString("alamat"));
        pelanggan.setNoTelp(res.getString("telepon"));
    }

}