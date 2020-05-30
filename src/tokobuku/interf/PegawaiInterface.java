package tokobuku.interf;

import java.sql.SQLException;
import java.util.List;
import tokobuku.model.Pegawai;

/**
 *
 * @author Rosyid Iz
 */
public interface PegawaiInterface {
    public void insert(Pegawai pegawai) throws SQLException;
    public void update(Pegawai pegawai) throws SQLException;
    public void updatePwd(Pegawai pegawai) throws SQLException;
    public void delete(Pegawai pegawai) throws SQLException;
    public List<Pegawai> load() throws SQLException;
}