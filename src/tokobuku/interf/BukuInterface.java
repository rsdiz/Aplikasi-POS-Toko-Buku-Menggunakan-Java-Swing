package tokobuku.interf;

import java.sql.SQLException;
import java.util.List;
import tokobuku.model.Buku;

/**
 *
 * @author Rosyid Iz
 */
public interface BukuInterface {
    public void insert(Buku buku) throws SQLException;
    public void update(Buku buku, String isbn_new) throws SQLException;
    public void delete(Buku buku) throws SQLException;
    public List<Buku> searchBuku(String keywords) throws SQLException;
    public List<Buku> loadAll() throws SQLException;
    public List<Buku> loadAllBy(String type) throws SQLException;
}
