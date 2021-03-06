package tokobuku.view;

import java.awt.event.KeyEvent;
import java.sql.SQLException;
import tokobuku.impl.LoginImpl;
import tokobuku.interf.LoginInterface;
import tokobuku.util.DragWindow;
import tokobuku.util.CustomFont;

/**
 *
 * @author Rosyid Iz
 */
public class LoginView extends javax.swing.JFrame {

    private final LoginInterface login;
    private String username;
    private String password;

    /**
     * Creates new form LoginView
     */
    public LoginView() {
        initComponents();
        DragWindow dragWindow = new DragWindow(this);
        login = new LoginImpl();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dragPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        minimizedIcon = new javax.swing.JLabel();
        closeButton = new javax.swing.JLabel();
        basePanel = new javax.swing.JPanel();
        backgroundPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        loginPanel = new javax.swing.JPanel();
        loginButton = new javax.swing.JButton();
        panelUsername = new javax.swing.JPanel();
        iconUser = new javax.swing.JLabel();
        fieldUsername = new javax.swing.JTextField();
        panelPassword = new javax.swing.JPanel();
        iconPass = new javax.swing.JLabel();
        fieldPassword = new javax.swing.JPasswordField();
        titleText = new javax.swing.JLabel();
        descriptionText = new javax.swing.JLabel();
        versionText = new javax.swing.JLabel();
        background = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("K-SIR Book: Login");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setName("frameLogin"); // NOI18N
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        dragPanel.setBackground(new java.awt.Color(35, 35, 35));
        dragPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        dragPanel.setMaximumSize(new java.awt.Dimension(1024, 40));
        dragPanel.setMinimumSize(new java.awt.Dimension(1024, 40));
        dragPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Metropolis Medium", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("K-SIR Book: Login");
        dragPanel.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(416, 2, 178, 36));

        minimizedIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        minimizedIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tokobuku/images/minimized~1.png"))); // NOI18N
        minimizedIcon.setToolTipText("Minimize");
        minimizedIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                minimizedIconMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                minimizedIconMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                minimizedIconMouseExited(evt);
            }
        });
        dragPanel.add(minimizedIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 0, 30, 40));

        closeButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        closeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tokobuku/images/close~1.png"))); // NOI18N
        closeButton.setToolTipText("Close");
        closeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                closeButtonMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                closeButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                closeButtonMouseExited(evt);
            }
        });
        dragPanel.add(closeButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 30, 40));

        getContentPane().add(dragPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1020, 40));

        basePanel.setBackground(new java.awt.Color(51, 51, 51));
        basePanel.setMaximumSize(new java.awt.Dimension(1024, 520));
        basePanel.setMinimumSize(new java.awt.Dimension(1024, 520));
        basePanel.setPreferredSize(new java.awt.Dimension(1024, 520));
        basePanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                basePanelMouseClicked(evt);
            }
        });
        basePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        backgroundPanel.setBackground(new java.awt.Color(51, 51, 51));
        backgroundPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        backgroundPanel.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, 150, 140));

        loginPanel.setBackground(new java.awt.Color(243, 243, 243));
        loginPanel.setPreferredSize(new java.awt.Dimension(512, 512));
        loginPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        loginButton.setBackground(new java.awt.Color(51, 51, 51));
        loginButton.setFont(new CustomFont().getFont("tahoma", 1, 12)
        );
        loginButton.setForeground(new java.awt.Color(255, 255, 255));
        loginButton.setText("SIGN IN");
        loginButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        loginButton.setBorderPainted(false);
        loginButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });
        loginPanel.add(loginButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 310, 110, 40));

        panelUsername.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        panelUsername.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tokobuku/images/user~1.png"))); // NOI18N
        iconUser.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        iconUser.setFocusable(false);
        iconUser.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        panelUsername.add(iconUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 15, -1, -1));

        fieldUsername.setBackground(new java.awt.Color(240, 240, 240));
        fieldUsername.setFont(new CustomFont().getFont("asap", 14)
        );
        fieldUsername.setForeground(new java.awt.Color(51, 51, 51));
        fieldUsername.setText("Username");
        fieldUsername.setToolTipText("Masukkan Username");
        fieldUsername.setBorder(null);
        fieldUsername.setCaretColor(new java.awt.Color(51, 51, 51));
        fieldUsername.setMargin(new java.awt.Insets(6, 6, 6, 6));
        fieldUsername.setNextFocusableComponent(fieldPassword);
        fieldUsername.setSelectedTextColor(new java.awt.Color(127, 42, 0));
        fieldUsername.setSelectionColor(new java.awt.Color(225, 225, 225));
        panelUsername.add(fieldUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 5, 210, 50));

        loginPanel.add(panelUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 140, 270, 60));

        panelPassword.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        panelPassword.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconPass.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tokobuku/images/unlocked~1.png"))); // NOI18N
        iconPass.setFocusable(false);
        panelPassword.add(iconPass, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 15, -1, -1));

        fieldPassword.setBackground(new java.awt.Color(240, 240, 240));
        fieldPassword.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        fieldPassword.setForeground(new java.awt.Color(51, 51, 51));
        fieldPassword.setText("Password");
        fieldPassword.setBorder(null);
        fieldPassword.setMargin(new java.awt.Insets(6, 6, 6, 6));
        fieldPassword.setNextFocusableComponent(loginButton);
        fieldPassword.setSelectedTextColor(new java.awt.Color(127, 42, 0));
        fieldPassword.setSelectionColor(new java.awt.Color(225, 225, 225));
        fieldPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fieldPasswordKeyPressed(evt);
            }
        });
        panelPassword.add(fieldPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 5, 210, 50));

        loginPanel.add(panelPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 220, 270, 60));

        titleText.setFont(new CustomFont().getFont("bebas", 36)
        );
        titleText.setForeground(new java.awt.Color(51, 51, 51));
        titleText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleText.setText("K-SIR   BOOK");
        loginPanel.add(titleText, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, 250, 60));

        descriptionText.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        descriptionText.setForeground(new java.awt.Color(99, 20, 0));
        descriptionText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        descriptionText.setText("Login");
        loginPanel.add(descriptionText, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 80, 250, 40));

        backgroundPanel.add(loginPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 50, 420, 410));

        versionText.setFont(new CustomFont().getFont("tahoma", 14)
        );
        versionText.setForeground(new java.awt.Color(255, 255, 255));
        versionText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        versionText.setText("V 1.0");
        backgroundPanel.add(versionText, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 460, 70, 40));

        background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tokobuku/images/library-background.jpg"))); // NOI18N
        background.setToolTipText("");
        backgroundPanel.add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 1000, 500));

        basePanel.add(backgroundPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1020, 510));

        getContentPane().add(basePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 1020, 520));

        setSize(new java.awt.Dimension(1020, 560));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void minimizedIconMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_minimizedIconMouseClicked
        this.setState(LoginView.ICONIFIED);
    }//GEN-LAST:event_minimizedIconMouseClicked

    private void minimizedIconMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_minimizedIconMouseEntered
        minimizedIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tokobuku/images/minimized-hover~1.png")));
    }//GEN-LAST:event_minimizedIconMouseEntered

    private void minimizedIconMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_minimizedIconMouseExited
        minimizedIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tokobuku/images/minimized~1.png")));
    }//GEN-LAST:event_minimizedIconMouseExited

    private void closeButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_closeButtonMouseClicked
//        this.dispose();
        System.exit(0);
    }//GEN-LAST:event_closeButtonMouseClicked

    private void closeButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_closeButtonMouseEntered
        closeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tokobuku/images/close-hover~1.png")));
    }//GEN-LAST:event_closeButtonMouseEntered

    private void closeButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_closeButtonMouseExited
        closeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tokobuku/images/close~1.png")));
    }//GEN-LAST:event_closeButtonMouseExited

    private void basePanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_basePanelMouseClicked
        // NOTHING
    }//GEN-LAST:event_basePanelMouseClicked

    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButtonActionPerformed
        username = fieldUsername.getText();
        password = String.valueOf(fieldPassword.getPassword());
        CustomJPanelView jOpt = new CustomJPanelView();
        jOpt.setPanel("black");
        try {
            String resultCode = login.doLogin(username, password);
            switch (resultCode) {
                case "200":
                    jOpt.displayInfo(this, "Berhasil Login!", "LOGIN");
                    this.setVisible(false);
                    DashboardPegawaiView dash = DashboardPegawaiView.getInstance();
                    this.dispose();
                    dash.setVisible(true);
                    break;
                case "406":
                    jOpt.displayError(this, "Username atau Password yang dimasukkan salah, harap ulangi kembali!", "LOGIN");
                    break;
                case "404":
                    jOpt.displayError(this, "Username tidak ditemukan, harap ulangi kembali!", "LOGIN");
                    break;
                default:
                    break;
            }
        } catch (SQLException ex) {
            jOpt.displayError(this, "Terjadi masalah dengan database!", "Database Error!");
        }
    }//GEN-LAST:event_loginButtonActionPerformed

    private void fieldPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fieldPasswordKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            loginButtonActionPerformed(null);
        }
    }//GEN-LAST:event_fieldPasswordKeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows Classic".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new LoginView().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel background;
    private javax.swing.JPanel backgroundPanel;
    private javax.swing.JPanel basePanel;
    private javax.swing.JLabel closeButton;
    private javax.swing.JLabel descriptionText;
    private javax.swing.JPanel dragPanel;
    private javax.swing.JPasswordField fieldPassword;
    private javax.swing.JTextField fieldUsername;
    private javax.swing.JLabel iconPass;
    private javax.swing.JLabel iconUser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JButton loginButton;
    private javax.swing.JPanel loginPanel;
    private javax.swing.JLabel minimizedIcon;
    private javax.swing.JPanel panelPassword;
    private javax.swing.JPanel panelUsername;
    private javax.swing.JLabel titleText;
    private javax.swing.JLabel versionText;
    // End of variables declaration//GEN-END:variables
}
