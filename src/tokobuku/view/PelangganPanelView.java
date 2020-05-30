package tokobuku.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import tokobuku.impl.PelangganImpl;
import tokobuku.model.Pelanggan;
import tokobuku.util.CustomFont;

/**
 * Tampilan Panel pada Pelanggan
 *
 * @author Rosyid Iz
 */
public class PelangganPanelView{
    private static final Font DEFAULT_FONT = CustomFont.getFont("bahnschrift", 14);

    private JPanel basePanel;
    private final JPanel panelColumn;
    private final JTextField columnNomor;
    private final JTextField columnNama;
    private final JTextField columnAlamat;
    private final JTextField columnNoTelp;
    private final JTextField columnEdit;
    private final JTextField columnHapus;
    private final int index;

    private Pelanggan pelanggan;
    private final PelangganImpl pelangganImpl;
    private boolean verify = true;
    private boolean showDelete = false;

    private final InputVerifier verifyNoTelp = new InputVerifier() {
        @Override
        public boolean verify(JComponent input) {
            JTextField textField = (JTextField) input;
            if (textField.getText().length() > 13 || textField.getText().isEmpty()) {
                columnNoTelp.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.LOWERED, Color.RED, Color.DARK_GRAY));
                verify = false;
                return false;
            } else {
                Pattern pattern = Pattern.compile("^[\\+]?[(]?\\d{3}[)]?[-\\s\\.]?\\d{3}[-\\s\\.]?\\d{4,6}$");
                Matcher matcher = pattern.matcher(textField.getText());
                if (matcher.matches()) {
                    input.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                    verify = true;
                    return true;
                } else {
                    input.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.LOWERED, Color.RED, Color.DARK_GRAY));
                    verify = false;
                    return false;
                }
            }
        }
    };

    private final InputVerifier verifyString = new InputVerifier() {
        @Override
        public boolean verify(JComponent input) {
            JTextField textField = (JTextField) input;
            if (textField.getText().length() > 50 || textField.getText().isEmpty()) {
                input.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.LOWERED, Color.RED, Color.DARK_GRAY));
                verify = false;
                return false;
            } else {
                input.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                verify = true;
                return true;
            }
        }
    };

    public PelangganPanelView(int index, PelangganImpl pelangganImpl) {
        this.pelangganImpl = pelangganImpl;
        this.index = index;

        panelColumn = new javax.swing.JPanel();
        panelColumn.setBackground(new java.awt.Color(65, 65, 65));
        panelColumn.setPreferredSize(new java.awt.Dimension(565, 45));
        panelColumn.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        columnNomor = new javax.swing.JTextField();
        columnNomor.setEditable(false);
        columnNomor.setBackground(new java.awt.Color(60, 60, 60));
        columnNomor.setFont(DEFAULT_FONT);
        columnNomor.setText(String.valueOf(index));
        columnNomor.setForeground(new java.awt.Color(255, 255, 255));
        columnNomor.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        columnNomor.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        columnNomor.setPreferredSize(new java.awt.Dimension(60, 35));

        columnNama = new javax.swing.JTextField();
        columnNama.setEditable(false);
        columnNama.setBackground(new java.awt.Color(60, 60, 60));
        columnNama.setFont(DEFAULT_FONT);
        columnNama.setForeground(new java.awt.Color(255, 255, 255));
        columnNama.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        columnNama.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        columnNama.setPreferredSize(new java.awt.Dimension(60, 35));
        columnNama.setCaretColor(Color.GREEN);
        columnNama.setInputVerifier(verifyString);

        columnAlamat = new javax.swing.JTextField();
        columnAlamat.setEditable(false);
        columnAlamat.setBackground(new java.awt.Color(60, 60, 60));
        columnAlamat.setFont(DEFAULT_FONT);
        columnAlamat.setForeground(new java.awt.Color(255, 255, 255));
        columnAlamat.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        columnAlamat.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        columnAlamat.setPreferredSize(new java.awt.Dimension(60, 35));
        columnAlamat.setCaretColor(Color.GREEN);
        columnAlamat.setInputVerifier(verifyString);

        columnNoTelp = new javax.swing.JTextField();
        columnNoTelp.setEditable(false);
        columnNoTelp.setBackground(new java.awt.Color(60, 60, 60));
        columnNoTelp.setFont(DEFAULT_FONT);
        columnNoTelp.setForeground(new java.awt.Color(255, 255, 255));
        columnNoTelp.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        columnNoTelp.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        columnNoTelp.setPreferredSize(new java.awt.Dimension(60, 35));
        columnNoTelp.setCaretColor(Color.GREEN);
        columnNoTelp.setInputVerifier(verifyNoTelp);

        columnEdit = new javax.swing.JTextField();
        columnEdit.setEditable(false);
        columnEdit.setBackground(new java.awt.Color(60, 180, 60));
        columnEdit.setFont(CustomFont.getFont("bebas", 14));
        columnEdit.setForeground(new java.awt.Color(255, 255, 255));
        columnEdit.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        columnEdit.setText("Edit");
        columnEdit.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        columnEdit.setPreferredSize(new java.awt.Dimension(60, 35));

        columnEdit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (columnEdit.getText().equals("Edit")) {
                    enableEdit();
                } else {
                    if (verify) {
                        disableEdit();
                    }
                }
                SwingUtilities.updateComponentTreeUI(panelColumn);
            }
        });

        columnHapus = new javax.swing.JTextField();
        columnHapus.setEditable(false);
        columnHapus.setBackground(new java.awt.Color(255, 60, 60));
        columnHapus.setFont(CustomFont.getFont("bebas", 14));
        columnHapus.setForeground(new java.awt.Color(255, 255, 255));
        columnHapus.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        columnHapus.setText("Hapus");
        columnHapus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        columnHapus.setPreferredSize(new java.awt.Dimension(60, 35));

        columnHapus.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!showDelete) {
                    deletePelanggan();
                }
            }
        });

        addComponent(60);
    }

    private void addComponent(int width) {
        panelColumn.add(columnHapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(625, 5, 65, -1));
        panelColumn.add(columnEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 5, width, -1));
        panelColumn.add(columnNoTelp, new org.netbeans.lib.awtextra.AbsoluteConstraints(425, 5, 130, -1));
        panelColumn.add(columnAlamat, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 5, 150, -1));
        panelColumn.add(columnNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 5, 210, -1));
        panelColumn.add(columnNomor, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 5, 40, -1));
    }

    /**
     * Menambah list ke panel
     *
     * @param panel
     */
    public void apply(JPanel panel) {
        this.basePanel = panel;
        basePanel.add(panelColumn);
    }

    /**
     * Setter untuk Model Pelanggan
     *
     * @param pelanggan
     */
    public void setPelanggan(Pelanggan pelanggan) {
        this.pelanggan = pelanggan;
        setTextComponent();
    }

    /**
     * Mengubah text pada tiap component
     */
    private void setTextComponent() {
        columnNama.setText(pelanggan.getNama_pelanggan());
        columnAlamat.setText(pelanggan.getAlamat());
        columnNoTelp.setText(pelanggan.getNoTelp());
    }

    private void enableEdit() {
        panelColumn.removeAll();
        columnEdit.setText("Simpan");
        columnHapus.setVisible(false);
        columnEdit.setBackground(new java.awt.Color(80, 200, 80));
        columnEdit.setPreferredSize(new java.awt.Dimension(130, 35));
        addComponent(130);
        columnNama.setEditable(true);
        columnNama.setBorder(javax.swing.BorderFactory.createEtchedBorder(0));
        columnAlamat.setEditable(true);
        columnAlamat.setBorder(javax.swing.BorderFactory.createEtchedBorder(0));
        columnNoTelp.setEditable(true);
        columnNoTelp.setBorder(javax.swing.BorderFactory.createEtchedBorder(0));
    }

    private void disableEdit() {
        if (columnNama.getText().equals(pelanggan.getNama_pelanggan())
                && columnAlamat.getText().equals(pelanggan.getAlamat())
                && columnNoTelp.getText().equals(pelanggan.getNoTelp())) {

        } else {
            String oldNama = pelanggan.getNama_pelanggan();
            pelanggan.setNama_pelanggan(columnNama.getText());
            String oldAlamat = pelanggan.getAlamat();
            pelanggan.setAlamat(columnAlamat.getText());
            String oldNoTelp = pelanggan.getNoTelp();
            pelanggan.setNoTelp(columnNoTelp.getText());
            Thread t = new Thread("update-pelanggan") {
                @Override
                public void run() {
                    try {
                        pelangganImpl.update(pelanggan);
                    } catch (SQLException ex) {
                        pelanggan.setNama_pelanggan(oldNama);
                        columnNama.setText(oldNama);
                        pelanggan.setAlamat(oldAlamat);
                        columnAlamat.setText(oldAlamat);
                        pelanggan.setNoTelp(oldNoTelp);
                        columnNoTelp.setText(oldNoTelp);
                        Logger.getLogger(PelangganPanelView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
            t.start();
        }
        panelColumn.removeAll();
        columnEdit.setText("Edit");
        columnHapus.setVisible(true);
        columnEdit.setBackground(new java.awt.Color(60, 180, 60));
        columnEdit.setPreferredSize(new java.awt.Dimension(60, 35));
        addComponent(60);
        columnNama.setEditable(false);
        columnNama.setBorder(javax.swing.BorderFactory.createEtchedBorder(1));
        columnAlamat.setEditable(false);
        columnAlamat.setBorder(javax.swing.BorderFactory.createEtchedBorder(1));
        columnNoTelp.setEditable(false);
        columnNoTelp.setBorder(javax.swing.BorderFactory.createEtchedBorder(1));
    }

    private void deletePelanggan() {
        CustomJPanelView jOpt = new CustomJPanelView();
        jOpt.setPanel("black");
        int aksi = jOpt.displayConfirmDialog(basePanel, "Apakah anda ingin menghapus " + pelanggan.getNama_pelanggan() + " dari daftar pelanggan?", "Konfirmasi");
        switch (aksi) {
            case 0: // Opsi yes
            {
                Thread t = new Thread() {
                    @Override
                    public void run() {
                        try {
                            pelangganImpl.delete(pelanggan);
                            basePanel.remove(index - 1);
                            SwingUtilities.updateComponentTreeUI(basePanel);
                        } catch (SQLException ex) {
                            Logger.getLogger(PelangganPanelView.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                };
                t.start();
                break;
            }
            default:
                showDelete = false;
        }
    }
}
