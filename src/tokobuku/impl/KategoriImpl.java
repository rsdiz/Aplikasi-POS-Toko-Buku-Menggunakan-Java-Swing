package tokobuku.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import tokobuku.interf.KategoriInterface;
import tokobuku.model.Kategori;
import tokobuku.util.ConnectionUtil;

/**
 *
 * @author Rosyid Iz
 */
public class KategoriImpl implements KategoriInterface {
    
    private final Connection con = ConnectionUtil.getDB();
    public List<Kategori> listKategoris = new ArrayList<>();

    @Override
    public void insert(Kategori kategori) throws SQLException {
        String sql = "{ CALL insert_kategori(?,?,?) }";
        try (PreparedStatement ps = con.prepareCall(sql)) {
            ps.setInt(1, kategori.getKode_kategori());
            ps.setString(2, kategori.getNama_kategori());
            ps.setString(3, kategori.getRak());
            ps.executeUpdate();
            listKategoris.add(kategori);
        }
    }

    @Override
    public void update(Kategori kategori, int new_kode) throws SQLException {
        String sql = "{ CALL update_kategori(?,?,?,?) }";
        try (PreparedStatement ps = con.prepareCall(sql)) {
            ps.setInt(1, kategori.getKode_kategori());
            ps.setInt(2, new_kode);
            ps.setString(3, kategori.getNama_kategori());
            ps.setString(4, kategori.getRak());
            ps.executeUpdate();
            for (Kategori listKategori : listKategoris) {
                if (listKategori.getKode_kategori() == kategori.getKode_kategori()) {
                    listKategori.setKode_kategori(new_kode);
                    listKategori.setNama_kategori(kategori.getNama_kategori());
                    listKategori.setRak(kategori.getRak());
                    break;
                }
            }
        }
    }

    @Override
    public void delete(Kategori kategori) throws SQLException {
        String sql = "{ CALL delete_kategori(?) }";
        try (PreparedStatement ps = con.prepareCall(sql)) {
            ps.setInt(1, kategori.getKode_kategori());
            ps.executeUpdate();
            listKategoris.remove(kategori);
        }
    }

    @Override
    public List<Kategori> load() throws SQLException {
        String sql = "SELECT * FROM tb_kategori";
        try (Statement statement = con.createStatement()) {
            ResultSet res = statement.executeQuery(sql);
            while(res.next()) {
                Kategori kategori = new Kategori();
                addKategoriToList(kategori, res);
                listKategoris.add(kategori);
            }
        }
        return listKategoris;
    }
    
    private void addKategoriToList(Kategori kategori, ResultSet res) throws SQLException {
        kategori.setKode_kategori(res.getInt("kode_kategori"));
        kategori.setNama_kategori(res.getString("nama_kategori"));
        kategori.setRak(res.getString("rak"));
    }
    
}
