/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tokobuku.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tokobuku.interf.DetailTransaksiInterface;
import tokobuku.model.DetailTransaksi;
import tokobuku.util.ConnectionUtil;

/**
 *
 * @author Rosyid Iz
 */
public class DetailTransaksiImpl implements DetailTransaksiInterface{
    
    private final Connection con = ConnectionUtil.getDB();
    public List<DetailTransaksi> listDetailTransaksis = new ArrayList<>();

    @Override
    public void insert(DetailTransaksi detailtrx) throws SQLException {
        String sql = "CALL insert_detailtrx(?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, detailtrx.getId_trx());
            ps.setString(2, detailtrx.getIsbn());
            ps.setInt(3, detailtrx.getBanyak());
            ps.executeUpdate();
            listDetailTransaksis.add(detailtrx);
        }
    }

    @Override
    public void update(DetailTransaksi detailtrx) throws SQLException {
        String sql = "CALL update_detailtrx(?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, detailtrx.getId_detail_trx());
            ps.setInt(2, detailtrx.getId_trx());
            ps.setString(3, detailtrx.getIsbn());
            ps.setInt(4, detailtrx.getBanyak());
            ps.executeUpdate();
            listDetailTransaksis.set(detailtrx.getId_detail_trx()-1, detailtrx);
        }
    }

    @Override
    public void delete(DetailTransaksi detailtrx) throws SQLException {
        String sql = "CALL delete_detailtrx(?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, detailtrx.getId_detail_trx());
            ps.executeUpdate();
            listDetailTransaksis.remove(detailtrx.getId_detail_trx()-1);
        }
    }

    @Override
    public List<DetailTransaksi> load(int select_id) throws SQLException {
        String sql = "SELECT * FROM data_detail_trx WHERE id_trx = ?";
        try(PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, select_id);
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                DetailTransaksi detailTransaksi = new DetailTransaksi();
                addDetailTrxToList(detailTransaksi, res);
                listDetailTransaksis.add(detailTransaksi);
            }
        }
        return listDetailTransaksis;
    }
    
    private void addDetailTrxToList(DetailTransaksi detailTransaksi, ResultSet res) throws SQLException {
        detailTransaksi.setId_detail_trx(res.getInt("id_detail"));
        detailTransaksi.setId_trx(res.getInt("id_trx"));
        detailTransaksi.setIsbn(res.getString("isbn"));
        detailTransaksi.setJudul_buku("judul_buku");
        detailTransaksi.setBanyak(res.getInt("banyak"));
        detailTransaksi.setHarga(res.getInt("harga"));
    }
}
