package tokobuku.interf;

import java.sql.SQLException;
import java.util.List;
import tokobuku.model.Kategori;

/**
 *
 * @author Rosyid Iz
 */
public interface KategoriInterface {

    /**
     * Method untuk menambah (insert) data kategori
     *
     * @param kategori - Data kategori
     * @throws SQLException
     */
    public void insert(Kategori kategori) throws SQLException;

    /**
     * Method untuk merubah (update) data kategori
     *
     * @param kategori - Data kategori
     * @param new_kode - ISBN baru
     * @throws SQLException
     */
    public void update(Kategori kategori, int new_kode) throws SQLException;

    /**
     * Method untuk menghapus (delete) data kategori
     *
     * @param kategori - Data Kategori
     * @throws SQLException
     */
    public void delete(Kategori kategori) throws SQLException;

    /**
     * Method untuk mengambil data kategori dari database
     * @return <code>List Kategori</code>
     * @throws SQLException 
     */
    public List<Kategori> load() throws SQLException;
}
