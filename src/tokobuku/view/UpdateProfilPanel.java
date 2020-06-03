package tokobuku.view;

import java.awt.Dimension;
import java.awt.Font;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import tokobuku.SistemTokoBuku;
import tokobuku.impl.PegawaiImpl;
import tokobuku.model.Pegawai;
import tokobuku.util.CustomFont;
import tokobuku.util.PasswordUtils;
import tokobuku.util.PreferencedHelper;

/**
 *
 * @author Rosyid Iz
 */
public class UpdateProfilPanel {

    private final Font DEFAULT = CustomFont.getFont("Bahnschrift", 18);

    private final JDialog dialog = new JDialog();
    private final Pegawai pegawai;
    private final PegawaiImpl pegawaiImpl;

    private JPanel basePanel;
    private JTextField tf_namaPegawai;
    private JTextField tf_alamatPegawai;
    private JTextField tf_notelpPegawai;
    private JTextField tf_unamePegawai;
    private JPasswordField jpf_passlama;
    private JButton btnShowPassLama;
    private JPasswordField jpf_passbaru;
    private JButton btnShowPassBaru;
    private JButton btnBatal;
    private JButton btnSimpan;

    public UpdateProfilPanel(Pegawai pegawai, PegawaiImpl pegawaiImpl) {
        this.pegawai = pegawai;
        this.pegawaiImpl = pegawaiImpl;
    }

    private void apply() {
        basePanel.add(tf_namaPegawai, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 280, 60));
        basePanel.add(tf_alamatPegawai, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 280, 60));
        basePanel.add(tf_notelpPegawai, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 220, 280, 60));
        basePanel.add(tf_unamePegawai, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 40, 280, 60));
        basePanel.add(jpf_passlama, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 130, 230, 60));
        basePanel.add(btnShowPassLama, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 137, 50, 50));
        basePanel.add(jpf_passbaru, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 220, 230, 60));
        basePanel.add(btnShowPassBaru, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 227, 50, 50));
        basePanel.add(btnBatal, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 320, 140, 50));
        basePanel.add(btnSimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 320, 140, 50));
    }

    private void setText() {
        tf_namaPegawai.setText(pegawai.getNama());
        tf_alamatPegawai.setText(pegawai.getAlamat());
        tf_notelpPegawai.setText(pegawai.getTelepon());
        tf_unamePegawai.setText(pegawai.getUsername());
    }

    public void display() {
        JOptionPane optionPane = new JOptionPane(getPanel(), JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
        dialog.setTitle("Ubah Profil");
        dialog.setBackground(new java.awt.Color(55, 55, 55));
        dialog.setModal(true);
        dialog.setLocationByPlatform(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setContentPane(optionPane);
        dialog.pack();
        dialog.setVisible(true);
    }

    private JPanel getPanel() {
        basePanel = new javax.swing.JPanel();
        basePanel.setBackground(new java.awt.Color(65, 65, 65));
        basePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        basePanel.setPreferredSize(new Dimension(690, 410));

        tf_namaPegawai = new javax.swing.JTextField();
        tf_namaPegawai.setBackground(new java.awt.Color(65, 65, 65));
        tf_namaPegawai.setFont(DEFAULT);
        tf_namaPegawai.setForeground(new java.awt.Color(255, 255, 255));
        tf_namaPegawai.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Nama", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Bahnschrift", 0, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        tf_namaPegawai.setCaretColor(new java.awt.Color(255, 255, 102));
        tf_namaPegawai.setDisabledTextColor(new java.awt.Color(200, 200, 200));
        tf_namaPegawai.setMargin(new java.awt.Insets(2, 6, 2, 6));

        tf_alamatPegawai = new javax.swing.JTextField();
        tf_alamatPegawai.setBackground(new java.awt.Color(65, 65, 65));
        tf_alamatPegawai.setFont(DEFAULT);
        tf_alamatPegawai.setForeground(new java.awt.Color(255, 255, 255));
        tf_alamatPegawai.setToolTipText("");
        tf_alamatPegawai.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Alamat", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Bahnschrift", 0, 14), new java.awt.Color(255, 255, 255)));
        tf_alamatPegawai.setCaretColor(new java.awt.Color(255, 255, 102));
        tf_alamatPegawai.setDisabledTextColor(new java.awt.Color(200, 200, 200));
        tf_alamatPegawai.setMargin(new java.awt.Insets(2, 6, 2, 6));

        tf_notelpPegawai = new javax.swing.JTextField();
        tf_notelpPegawai.setBackground(new java.awt.Color(65, 65, 65));
        tf_notelpPegawai.setFont(DEFAULT);
        tf_notelpPegawai.setForeground(new java.awt.Color(255, 255, 255));
        tf_notelpPegawai.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "No. Telepon", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Bahnschrift", 0, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        tf_notelpPegawai.setCaretColor(new java.awt.Color(255, 255, 102));
        tf_notelpPegawai.setDisabledTextColor(new java.awt.Color(200, 200, 200));
        tf_notelpPegawai.setMargin(new java.awt.Insets(2, 6, 2, 6));

        tf_unamePegawai = new javax.swing.JTextField();
        tf_unamePegawai.setBackground(new java.awt.Color(65, 65, 65));
        tf_unamePegawai.setFont(DEFAULT);
        tf_unamePegawai.setForeground(new java.awt.Color(255, 255, 255));
        tf_unamePegawai.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Username", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Bahnschrift", 0, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        tf_unamePegawai.setCaretColor(new java.awt.Color(255, 255, 102));
        tf_unamePegawai.setDisabledTextColor(new java.awt.Color(200, 200, 200));
        tf_unamePegawai.setEnabled(false);
        tf_unamePegawai.setMargin(new java.awt.Insets(2, 6, 2, 6));

        jpf_passlama = new javax.swing.JPasswordField();
        jpf_passlama.setBackground(new java.awt.Color(65, 65, 65));
        jpf_passlama.setFont(new java.awt.Font("Tahoma", 0, 18));
        jpf_passlama.setForeground(new java.awt.Color(255, 255, 255));
        jpf_passlama.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Password Lama", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Bahnschrift", 0, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jpf_passlama.setCaretColor(new java.awt.Color(255, 255, 102));
        jpf_passlama.setDisabledTextColor(new java.awt.Color(200, 200, 200));

        btnShowPassLama = new javax.swing.JButton();
        btnShowPassLama.setBackground(new java.awt.Color(70, 70, 70));
        btnShowPassLama.setForeground(new java.awt.Color(255, 255, 255));
        btnShowPassLama.setText("SHOW");
        btnShowPassLama.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnShowPassLama.addActionListener((evt) -> {
            if (btnShowPassLama.getText().equalsIgnoreCase("show")) {
                btnShowPassLama.setText("HIDE");
                jpf_passlama.setEchoChar((char) 0);
            } else {
                btnShowPassLama.setText("SHOW");
                jpf_passlama.setEchoChar('\u25cf');
            }
        });

        jpf_passbaru = new javax.swing.JPasswordField();
        jpf_passbaru.setBackground(new java.awt.Color(65, 65, 65));
        jpf_passbaru.setFont(new java.awt.Font("Tahoma", 0, 18));
        jpf_passbaru.setForeground(new java.awt.Color(255, 255, 255));
        jpf_passbaru.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Password Baru", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Bahnschrift", 0, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jpf_passbaru.setCaretColor(new java.awt.Color(255, 255, 102));
        jpf_passbaru.setDisabledTextColor(new java.awt.Color(200, 200, 200));

        btnShowPassBaru = new javax.swing.JButton();
        btnShowPassBaru.setBackground(new java.awt.Color(70, 70, 70));
        btnShowPassBaru.setForeground(new java.awt.Color(255, 255, 255));
        btnShowPassBaru.setText("SHOW");
        btnShowPassBaru.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnShowPassBaru.addActionListener((evt) -> {
            if (btnShowPassBaru.getText().equalsIgnoreCase("show")) {
                btnShowPassBaru.setText("HIDE");
                jpf_passbaru.setEchoChar((char) 0);
            } else {
                btnShowPassBaru.setText("SHOW");
                jpf_passbaru.setEchoChar('\u25cf');
            }
        });

        btnBatal = new javax.swing.JButton();
        btnBatal.setBackground(new java.awt.Color(100, 100, 100));
        btnBatal.setFont(DEFAULT);
        btnBatal.setForeground(new java.awt.Color(255, 255, 255));
        btnBatal.setText("BATAL");
        btnBatal.addActionListener((evt) -> {
            dialog.dispose();
        });

        btnSimpan = new javax.swing.JButton();
        btnSimpan.setBackground(new java.awt.Color(0, 255, 0));
        btnSimpan.setFont(DEFAULT);
        btnSimpan.setText("SIMPAN");
        btnSimpan.addActionListener((evt) -> {
            CustomJPanelView jOpt = new CustomJPanelView();
            jOpt.setPanel("black");
            pegawai.setNama(tf_namaPegawai.getText());
            pegawai.setAlamat(tf_alamatPegawai.getText());
            pegawai.setTelepon(tf_notelpPegawai.getText());
            if (jpf_passlama.getPassword().length > 0) {
                if (PasswordUtils.veriifyPassword(String.valueOf(jpf_passlama.getPassword()), pegawai.getPassword(), pegawai.getSalt())) {
                    if (jpf_passbaru.getPassword().length > 0) {
                        String securePassword = PasswordUtils.generateSecurePassword(String.valueOf(jpf_passbaru.getPassword()), pegawai.getSalt());
                        pegawai.setPassword(securePassword);
                        try {
                            pegawaiImpl.update(pegawai);
                            PreferencedHelper.setName(pegawai.getNama());
                            PreferencedHelper.setAddress(pegawai.getAlamat());
                            PreferencedHelper.setTel(pegawai.getTelepon());
                            dialog.dispose();
                        } catch (SQLException ex) {
                            jOpt.displayError(basePanel, "Gagal Menambah Pegawai!", "Error");
                            SistemTokoBuku.logger.warning(ex.getMessage());
                        }
                    }
                } else {
                    jOpt.displayError(basePanel, "Password Lama Anda salah!", "Error");
                }
            } else {
                try {
                    pegawaiImpl.update(pegawai);
                    PreferencedHelper.setName(pegawai.getNama());
                    PreferencedHelper.setAddress(pegawai.getAlamat());
                    PreferencedHelper.setTel(pegawai.getTelepon());
                    dialog.dispose();
                } catch (SQLException ex) {
                    jOpt.displayError(basePanel, "Gagal Menambah Pegawai!", "Error");
                    SistemTokoBuku.logger.warning(ex.getMessage());
                }
            }
        });

        setText();
        apply();

        return basePanel;
    }

}
