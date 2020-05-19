package tokobuku.interf;

import java.sql.SQLException;
import java.util.List;
import tokobuku.model.Pelanggan;

/**
 *
 * @author Rosyid Iz
 */
public interface PelangganInterface {
    public void insert(Pelanggan pelanggan) throws SQLException;
    public void update(Pelanggan pelanggan) throws SQLException;
    public void delete(Pelanggan pelanggan) throws SQLException;
    public List<Pelanggan> load() throws SQLException;
}
