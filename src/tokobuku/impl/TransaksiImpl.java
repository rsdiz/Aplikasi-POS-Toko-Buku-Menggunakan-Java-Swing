package tokobuku.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import tokobuku.interf.TransaksiInterface;
import tokobuku.model.Transaksi;
import tokobuku.util.ConnectionUtil;

/**
 *
 * @author Rosyid Iz
 */
public class TransaksiImpl implements TransaksiInterface{
    
    private final Connection con = ConnectionUtil.getDB();
    public List<Transaksi> listTransaksis = new ArrayList<>();

    @Override
    public void insert(Transaksi transaksi) throws SQLException {
        String sql = "CALL insert_transaksi(?,?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, transaksi.getId());
            ps.setInt(2, transaksi.getIdPelanggan());
            ps.setInt(3, transaksi.getIdPetugas());
            ps.setString(4, transaksi.getTanggal().toString());
            ps.setInt(5, transaksi.getNominalBayar().intValue());
            ps.executeUpdate();
            listTransaksis.add(transaksi);
        }
    }

    @Override
    public void update(Transaksi transaksi) throws SQLException {
        String sql = "CALL update_transaksi(?,?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, transaksi.getId());
            ps.setInt(2, transaksi.getIdPelanggan());
            ps.setInt(3, transaksi.getIdPetugas());
            ps.setString(4, transaksi.getTanggal().toString());
            ps.setInt(5, transaksi.getNominalBayar().intValue());
            ps.executeUpdate();
            listTransaksis.set(transaksi.getId()-1, transaksi);
        }
    }

    @Override
    public void delete(Transaksi transaksi) throws SQLException {
        String sql = "CALL delete_transaksi(?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, transaksi.getId());
            ps.executeUpdate();
            listTransaksis.remove(transaksi.getId()-1);
        }
    }

    @Override
    public List<Transaksi> load() throws SQLException {
        String sql = "SELECT * FROM data_transaksi";
        try (Statement statement = con.createStatement()) {
            ResultSet res = statement.executeQuery(sql);
            while (res.next()) {
                Transaksi transaksi = new Transaksi();
                addTransaksiToList(transaksi, res);
                listTransaksis.add(transaksi);
            }
        }
        return listTransaksis;
    }
    
    private void addTransaksiToList(Transaksi transaksi, ResultSet res) throws SQLException {
        transaksi.setId(res.getInt("id_transaksi"));
        transaksi.setIdPelanggan(res.getInt("id_pelanggan"));
        transaksi.setIdPetugas(res.getInt("id_kasir"));
        transaksi.setNamaPelanggan(res.getString("nama_pelanggan"));
        transaksi.setNamaPetugas(res.getString("nama_petugas"));
        transaksi.setTanggal(res.getDate("tanggal"));
        transaksi.setNominalBayar(Double.valueOf(res.getInt("bayar")));
        transaksi.setTotalBayar(res.getInt("totalbayar"));
    }
    
}