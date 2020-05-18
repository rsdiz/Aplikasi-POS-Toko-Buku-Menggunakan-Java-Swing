/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tokobuku.interf;

import java.sql.SQLException;
import java.util.List;
import tokobuku.model.DetailTransaksi;

/**
 *
 * @author Rosyid Iz
 */
public interface DetailTransaksiInterface {
    public void insert(DetailTransaksi detailtrx) throws SQLException;
    public void update(DetailTransaksi detailtrx) throws SQLException;
    public void delete(DetailTransaksi detailtrx) throws SQLException;
    public List<DetailTransaksi> load(int select_id) throws SQLException;
}