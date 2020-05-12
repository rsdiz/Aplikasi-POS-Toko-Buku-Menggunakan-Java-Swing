package tokobuku.impl;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import tokobuku.interf.BukuInterface;
import tokobuku.model.Buku;
import tokobuku.util.ConnectionUtil;

/**
 *
 * @author Rosyid Iz
 */
public class BukuImpl implements BukuInterface {

    private final Connection con = ConnectionUtil.getDB();

    @Override
    public void insert(Buku buku) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(Buku buku) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Buku buku) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Buku> searchBuku(String keywords) throws SQLException {
        List<Buku> listBuku = new ArrayList<>();
        try {
            String sql = "CALL search_buku(?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, keywords);
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                Buku buku = new Buku();
                addBukuToList(buku, res);
                listBuku.add(buku);
            }
        } catch (SQLException e) {
            System.out.println("Error Search!\n" + e);
        }
        return listBuku;
    }

    @Override
    public List<Buku> loadAll() throws SQLException {
        List<Buku> listBuku = new ArrayList<>();
        try {
            String sql = "SELECT * FROM data_buku";
            Statement statement = con.createStatement();
            ResultSet res = statement.executeQuery(sql);
            while (res.next()) {
                Buku buku = new Buku();
                addBukuToList(buku, res);
                listBuku.add(buku);
            }
        } catch (SQLException e) {
            System.out.println("Error Fetching Data Buku!\n" + e);
        }
        return listBuku;
    }

    @Override
    public List<Buku> loadAllBy(String type) throws SQLException {
        List<Buku> listBuku = new ArrayList<>();
        if ("judul_buku".equalsIgnoreCase(type) | "isbn".equalsIgnoreCase(type) | "penulis".equalsIgnoreCase(type) | "penerbit".equalsIgnoreCase(type)) {
            try {
                String sql = "SELECT * FROM data_buku ORDER BY " + type;
                Statement statement = con.createStatement();
                ResultSet res = statement.executeQuery(sql);
                while (res.next()) {
                    Buku buku = new Buku();
                    addBukuToList(buku, res);
                    listBuku.add(buku);
                }
            } catch (SQLException e) {
                System.out.println("Error Fetching Data Buku!\n" + e);
            }
        } else {
            
        }
        return listBuku;
    }

    private void addBukuToList(Buku buku, ResultSet res) throws SQLException {
        buku.setIsbn(res.getString("isbn"));
        buku.setJudul_buku(res.getString("judul_buku"));
        buku.setKategori(res.getString("nama_kategori"));
        buku.setPenulis(res.getString("penulis"));
        buku.setPenerbit(res.getString("penerbit"));
        buku.setTahun(res.getString("tahun"));
        buku.setStok(res.getInt("stok"));
        buku.setHarga_pokok(res.getFloat("harga_pokok"));
        buku.setHarga_jual(res.getFloat("harga_jual"));
        buku.setIsbn(res.getString("isbn"));
        Blob gambarBlob = res.getBlob("gambar");
        if (gambarBlob != null) {
            byte gambarByte[] = gambarBlob.getBytes(1, (int) gambarBlob.length());
            buku.setImage(gambarByte);
        } else {
            buku.setImage(null);
        }
    }

}
