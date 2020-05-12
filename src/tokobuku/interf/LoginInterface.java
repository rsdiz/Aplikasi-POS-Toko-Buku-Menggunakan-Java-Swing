package tokobuku.interf;

import java.sql.SQLException;

/**
 *
 * @author Rosyid Iz
 */
public interface LoginInterface {

    public String doLogin(String username, String password) throws SQLException;
    
}
