package tokobuku.interf;

import java.sql.SQLException;
import java.util.List;
import tokobuku.model.Transaksi;

/**
 *
 * @author Rosyid Iz
 */
public interface TransaksiInterface {
    public void insert(Transaksi transaksi) throws SQLException;
    public void update(Transaksi transaksi) throws SQLException;
    public void delete(Transaksi transaksi) throws SQLException;
    public List<Transaksi> load() throws SQLException;
}
