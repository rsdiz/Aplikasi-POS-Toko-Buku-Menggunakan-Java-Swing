package tokobuku.view;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import tokobuku.impl.PegawaiImpl;
import tokobuku.model.Pegawai;
import tokobuku.util.CustomFont;
import tokobuku.util.PasswordUtils;

/**
 *
 * @author Rosyid Iz
 */
public class ChangePasswordPanel {

    private static final JDialog DIALOG = new JDialog();
    private static Pegawai pegawai;
    private static PegawaiImpl pegawaiImpl;

    private static JPanel basePane;
    private static JLabel jLabel1;
    private static JPasswordField jpfUbahPass;
    private static JButton btnShowPwd;
    private static JButton btnSavePwd;

    /**
     *
     */
    public ChangePasswordPanel() {
    }

    private static void apply() {
        basePane.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, 460, 30));
        basePane.add(jpfUbahPass, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 70, 250, 50));
        basePane.add(btnShowPwd, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 70, 50, 50));
        basePane.add(btnSavePwd, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 140, 130, 40));
    }

    public static void display(Pegawai pegawai) {
        ChangePasswordPanel.pegawai = pegawai;
        JOptionPane optionPane = new JOptionPane(getPanel(), JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
        DIALOG.setTitle("Ubah Password");
        DIALOG.setBackground(new java.awt.Color(55, 55, 55));
        DIALOG.setModal(true);
        DIALOG.setLocationByPlatform(true);
        DIALOG.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        DIALOG.setContentPane(optionPane);
        DIALOG.pack();
        DIALOG.setVisible(true);
    }

    public static void setImpl(PegawaiImpl pegawaiImpl) {
        ChangePasswordPanel.pegawaiImpl = pegawaiImpl;
    }

    private static JPanel getPanel() {
        basePane = new javax.swing.JPanel();
        basePane.setBackground(new java.awt.Color(55, 55, 55));
        basePane.setPreferredSize(new java.awt.Dimension(460, 230));
        basePane.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1 = new javax.swing.JLabel();
        jLabel1.setFont(CustomFont.getFont("bahnschrift", 18));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Masukkan Password Baru");
        jLabel1.setIconTextGap(2);

        jpfUbahPass = new javax.swing.JPasswordField();
        jpfUbahPass.setBackground(new java.awt.Color(75, 75, 75));
        jpfUbahPass.setFont(CustomFont.getFont(18));
        jpfUbahPass.setForeground(new java.awt.Color(102, 255, 102));
        jpfUbahPass.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpfUbahPass.setText("password");
        jpfUbahPass.setBorder(null);
        jpfUbahPass.setCaretColor(new java.awt.Color(150, 150, 150));
        jpfUbahPass.setMargin(new java.awt.Insets(8, 8, 8, 8));

        btnShowPwd = new javax.swing.JButton();
        btnShowPwd.setBackground(new java.awt.Color(75, 75, 75));
        btnShowPwd.setFont(CustomFont.getFont(12));
        btnShowPwd.setForeground(new java.awt.Color(180, 180, 180));
        btnShowPwd.setText("show");
        btnShowPwd.setBorder(null);
        btnShowPwd.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnShowPwd.setText("hide");
                jpfUbahPass.setEchoChar((char) 0);
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnShowPwd.setText("show");
                jpfUbahPass.setEchoChar('\u25cf');
            }
        });

        btnSavePwd = new javax.swing.JButton();
        btnSavePwd.setBackground(new java.awt.Color(0, 204, 51));
        btnSavePwd.setFont(CustomFont.getFont("bebas", 18));
        btnSavePwd.setForeground(new java.awt.Color(0, 102, 0));
        btnSavePwd.setText("SIMPAN");
        btnSavePwd.setBorderPainted(false);
        btnSavePwd.addActionListener((click) -> {
            try {
                String securePassword = PasswordUtils.generateSecurePassword(String.valueOf(jpfUbahPass.getPassword()), pegawai.getSalt());
                pegawai.setPassword(securePassword);
                pegawaiImpl.updatePwd(pegawai);
                DIALOG.dispose();
            } catch (SQLException ex) {
                Logger.getLogger(ChangePasswordPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        apply();

        return basePane;
    }

}
