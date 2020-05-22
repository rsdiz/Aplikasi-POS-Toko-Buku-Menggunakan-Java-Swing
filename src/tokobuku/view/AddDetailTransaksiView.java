package tokobuku.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import tokobuku.impl.DetailTransaksiImpl;
import tokobuku.model.Buku;
import tokobuku.model.DetailTransaksi;
import tokobuku.util.CustomFont;
import tokobuku.util.Formatter;

/**
 *
 * @author Rosyid Iz
 */
public class AddDetailTransaksiView {

    /**
     * Font Bahnscrift, Style Plain, size 14
     */
    private final static Font DEFAULT_FONT = new CustomFont().getFont("Bahnschrift", 14);
    /**
     * Black 35,35,35
     */
    private final static java.awt.Color COLOR35 = new java.awt.Color(35, 35, 35);
    /**
     * White 255,255,255
     */
    private final static java.awt.Color COLOR255 = new java.awt.Color(255, 255, 255);

    private final static Formatter<Float> FLOAT_F = new Formatter<>();
    private final static Formatter<Integer> INT_F = new Formatter<>();

    private final int index;
    private final Formatter<Integer> uang;
    private JPanel parentPanel;
    private final JPanel basePanel;
    private final JTextField tf_noDetail;
    private final JTextField tf_isbn;
    private final JTextField tf_qty;
    private final JTextField tf_harga;
    private final JTextField tf_total;
    private JTextField tf_totalBayar;

    private final DetailTransaksiImpl detailImpl;
    private final DetailTransaksi detailTransaksi;
    private final List<Buku> listBukus;

    private Buku buku = null;

    /**
     * Verifikasi apakah inputan merupakan angka atau bukan
     */
    private final InputVerifier ivDigit = new InputVerifier() {
        @Override
        public boolean verify(JComponent input) {
            JTextField textField = (JTextField) input;
            try {
                int number = Integer.parseInt(textField.getText());
                input.setBorder(javax.swing.BorderFactory.createEtchedBorder(0));
                return true;
            } catch (NumberFormatException e) {
                input.setBorder(javax.swing.BorderFactory.createEtchedBorder(0, Color.RED, Color.DARK_GRAY));
                return false;
            }
        }
    };

    public AddDetailTransaksiView(List<Buku> listBukus, DetailTransaksiImpl inpDetailImpl, int index) {
        // Inisialisasi
        this.index = index;
        this.detailImpl = inpDetailImpl;
        this.uang = new Formatter<>();
        this.detailTransaksi = inpDetailImpl.listDetailTransaksis.get(index - 1);
        this.listBukus = listBukus;
        this.basePanel = new javax.swing.JPanel();
        this.tf_noDetail = new javax.swing.JTextField();
        this.tf_isbn = new javax.swing.JTextField();
        this.tf_qty = new javax.swing.JTextField();
        this.tf_harga = new javax.swing.JTextField();
        this.tf_total = new javax.swing.JTextField();
        detailImpl.listDetailTransaksis.get(index - 1).setId_detail_trx(index);
        // panelCustomized
        basePanel.setBackground(new java.awt.Color(45, 45, 45));
        basePanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        basePanel.setMinimumSize(new java.awt.Dimension(610, 50));
        basePanel.setPreferredSize(new java.awt.Dimension(605, 50));
        basePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        // setText
        tf_isbn.setText(detailTransaksi.getIsbn());
        tf_noDetail.setText(String.valueOf(index));
        tf_qty.setText(String.valueOf(detailTransaksi.getBanyak()));
        tf_harga.setText(String.valueOf(detailTransaksi.getHarga()));
        tf_total.setText(String.valueOf(INT_F.rupiah(Integer.parseInt(tf_qty.getText()) * Integer.parseInt(tf_harga.getText()))));
        // setBackground
        tf_noDetail.setBackground(COLOR35);
        tf_isbn.setBackground(COLOR35);
        tf_qty.setBackground(COLOR35);
        tf_harga.setBackground(COLOR35);
        tf_total.setBackground(COLOR35);
        // setEditable
        tf_noDetail.setEditable(false);
        tf_isbn.setEditable(false);
        tf_qty.setEditable(true);
        tf_harga.setEditable(false);
        tf_total.setEditable(false);
        // setFont
        tf_noDetail.setFont(DEFAULT_FONT);
        tf_isbn.setFont(DEFAULT_FONT);
        tf_qty.setFont(DEFAULT_FONT);
        tf_harga.setFont(DEFAULT_FONT);
        tf_total.setFont(DEFAULT_FONT);
        // setForeground
        tf_noDetail.setForeground(COLOR255);
        tf_isbn.setForeground(COLOR255);
        tf_qty.setForeground(COLOR255);
        tf_harga.setForeground(COLOR255);
        tf_total.setForeground(COLOR255);
        // setHorizontalAlignment, 0 = Center
        tf_noDetail.setHorizontalAlignment(0);
        tf_isbn.setHorizontalAlignment(0);
        tf_qty.setHorizontalAlignment(0);
        tf_harga.setHorizontalAlignment(0);
        tf_total.setHorizontalAlignment(0);
        // setBorder
        tf_noDetail.setBorder(javax.swing.BorderFactory.createEtchedBorder(1));
        tf_isbn.setBorder(javax.swing.BorderFactory.createEtchedBorder(0));
        tf_qty.setBorder(javax.swing.BorderFactory.createEtchedBorder(0));
        tf_harga.setBorder(javax.swing.BorderFactory.createEtchedBorder(1));
        tf_total.setBorder(javax.swing.BorderFactory.createEtchedBorder(1));
        // setCaretColor
        tf_noDetail.setCaretColor(COLOR255);
        tf_isbn.setCaretColor(COLOR255);
        tf_qty.setCaretColor(COLOR255);
        tf_harga.setCaretColor(COLOR255);
        tf_total.setCaretColor(COLOR255);
        // setPrefferedSize
        tf_noDetail.setPreferredSize(new java.awt.Dimension(40, 40));
        tf_isbn.setPreferredSize(new java.awt.Dimension(180, 40));
        tf_qty.setPreferredSize(new java.awt.Dimension(80, 40));
        tf_harga.setPreferredSize(new java.awt.Dimension(130, 40));
        tf_total.setPreferredSize(new java.awt.Dimension(140, 40));
        // setInputVerifier
        tf_qty.setInputVerifier(ivDigit);
        // ActionListener
        tf_isbn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new Dialog(listBukus).display(); // Menampilkan dialog untuk memilih buku
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!tf_isbn.getText().equals("") && buku != null) {
                    tf_harga.setText(String.valueOf(buku.getHarga_jual()));
                    try {
                        //mengubah harga pada list<DetailTransaksi> sesuai dengan buku yang dipilih
                        inpDetailImpl.listDetailTransaksis.get(index - 1).setHarga((int) buku.getHarga_jual());
                    } catch (IndexOutOfBoundsException ex) {

                    }
                }
            }
        });
        tf_noDetail.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Membuat tampilan panel menjadi warna merah
                tf_noDetail.setBackground(new java.awt.Color(255, 60, 60));
                tf_noDetail.setText("-");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Mengembalikan tampilan seperti biasa
                tf_noDetail.setBackground(COLOR35);
                tf_noDetail.setText(String.valueOf(index));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // Menampilkan dialog peringatan apakah ingin menghapus atau tidak
                deleteItem();
            }
        });
        
        tf_qty.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                SwingUtilities.invokeLater(() -> {
                    int total;
                    try {
                        inpDetailImpl.listDetailTransaksis.get(index - 1).setBanyak(Integer.parseInt(tf_qty.getText()));
                        total = inpDetailImpl.listDetailTransaksis.get(index - 1).getBanyak() * inpDetailImpl.listDetailTransaksis.get(index - 1).getHarga();
                    } catch (NumberFormatException ex) {
                        inpDetailImpl.listDetailTransaksis.get(index - 1).setBanyak(0);
                        total = 0;
                    }
                    tf_total.setText(String.valueOf(INT_F.rupiah(total)) + ",-");
                    int totalBayar = 0;
                    totalBayar = inpDetailImpl.listDetailTransaksis.stream().map((t) -> t.getBanyak() * t.getHarga()).reduce(totalBayar, Integer::sum);
                    tf_totalBayar.setText(String.valueOf(INT_F.rupiah(totalBayar)) + ",-");
                });
            }
        });
    }

    /**
     * Fungsi untuk menerapkan penempatan komponen komponen ke dalam panel
     *
     * @param jPanel
     * @param tf_totalBayar
     */
    public void apply(JPanel jPanel, JTextField tf_totalBayar) {
        basePanel.add(tf_noDetail, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 5, -1, -1));
        basePanel.add(tf_isbn, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 5, -1, -1));
        basePanel.add(tf_qty, new org.netbeans.lib.awtextra.AbsoluteConstraints(235, 5, -1, -1));
        basePanel.add(tf_harga, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 5, -1, -1));
        basePanel.add(tf_total, new org.netbeans.lib.awtextra.AbsoluteConstraints(455, 5, -1, -1));
        jPanel.add(basePanel, index - 1);
        SwingUtilities.updateComponentTreeUI(jPanel);
        this.parentPanel = jPanel;
        this.tf_totalBayar = tf_totalBayar;
    }

    /**
     * Menampilkan dialog peringatan apakah ingin menghapus kolom tersebut
     * dan juga kode untuk menghapus baris
     */
    private void deleteItem() {
        CustomJPanelView jOpt = new CustomJPanelView();
        jOpt.setPanel("black");
        int aksi = jOpt.displayConfirmDialog(basePanel, "Hapus item?", "Konfirmasi");
        switch (aksi) {
            case 0: {
                SwingUtilities.updateComponentTreeUI(parentPanel);
                int size = detailImpl.listDetailTransaksis.size();
                for (int i = size, j = 1; i >= 0; i--, j++) {
                    if (i - 1 <= size && parentPanel.getComponent(size - j).equals(basePanel)) {
                        if (parentPanel.getComponent(size - j).equals(basePanel)) {
                            parentPanel.remove(size - j);
                            detailImpl.listDetailTransaksis.remove(size - j);
                            SwingUtilities.updateComponentTreeUI(parentPanel);
                            break;
                        }
                    }
                }
                break;
            }
        }
    }

    /**
     * Class dialog untuk menampilkan panel daftar buku
     */
    class Dialog {

        private final JDialog dialog;
        private final List<Buku> listBukus;

        public Dialog(List<Buku> listBukus) {
            this.dialog = new JDialog();
            this.listBukus = listBukus;
        }

        public void display() {
            JOptionPane optionPane = new JOptionPane(getPanel(), JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
            dialog.setTitle("Pilih ISBN");
            dialog.setBackground(new java.awt.Color(51, 51, 51));
            dialog.setModal(true);
            dialog.setLocationByPlatform(true);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setContentPane(optionPane);
            dialog.pack();
            dialog.setVisible(true);
        }

        private JPanel getPanel() {
            List<String> listIsbn = new ArrayList<>();

            JPanel panel = new JPanel();
            panel.setBackground(new java.awt.Color(51, 51, 51));
            panel.setPreferredSize(new java.awt.Dimension(580, 220));
            panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

            JTextField textISBN = new javax.swing.JTextField();
            textISBN.setEditable(false);
            textISBN.setBackground(new java.awt.Color(255, 255, 255));
            textISBN.setFont(new CustomFont().getFont("bahnschrift", 24));
            textISBN.setForeground(new java.awt.Color(222, 222, 222));
            textISBN.setHorizontalAlignment(javax.swing.JTextField.CENTER);
            textISBN.setText("ISBN:");
            textISBN.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
            textISBN.setCaretColor(new java.awt.Color(80, 80, 80));
            textISBN.setDisabledTextColor(new java.awt.Color(51, 51, 51));
            textISBN.setEnabled(false);
            textISBN.setHighlighter(null);
            textISBN.setPreferredSize(new java.awt.Dimension(70, 40));
            panel.add(textISBN, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 80, -1));

            JComboBox<String> cb_isbn = new javax.swing.JComboBox<>();
            cb_isbn.setBackground(new java.awt.Color(80, 80, 80));
            cb_isbn.setEditable(true);
            cb_isbn.setFont(new CustomFont().getFont("bahnschrift", 24));
            cb_isbn.setForeground(new java.awt.Color(222, 222, 222));
            cb_isbn.setMaximumRowCount(10);
            cb_isbn.removeAllItems();
            listBukus.forEach((p) -> {
                listIsbn.add(p.getIsbn());
                cb_isbn.addItem(p.getIsbn());
            });
            cb_isbn.setSelectedIndex(-1);
            cb_isbn.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
            cb_isbn.setPreferredSize(new java.awt.Dimension(330, 40));
            panel.add(cb_isbn, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 30, 320, -1));

            JButton buttonSelect = new javax.swing.JButton();
            buttonSelect.setBackground(new java.awt.Color(51, 255, 51));
            buttonSelect.setFont(new CustomFont().getFont("bahnschrift", 18));
            buttonSelect.setText("PILIH");
            panel.add(buttonSelect, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 30, 120, 40));

            JLabel imageBuku = new javax.swing.JLabel();
            imageBuku.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tokobuku/images/book.png")));
            panel.add(imageBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, -1, -1));

            JLabel judulBuku = new javax.swing.JLabel();
            judulBuku.setFont(new CustomFont().getFont("bahnschrift", 16));
            judulBuku.setForeground(new java.awt.Color(255, 255, 255));
            judulBuku.setText("judul_buku");
            panel.add(judulBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 100, 410, 40));

            JLabel penulisBuku = new javax.swing.JLabel();
            penulisBuku.setFont(new CustomFont().getFont("bahnschrift", 14));
            penulisBuku.setForeground(new java.awt.Color(255, 255, 255));
            penulisBuku.setText("penulis");
            panel.add(penulisBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 150, 240, 20));

            JLabel penerbit_tahun = new javax.swing.JLabel();
            penerbit_tahun.setFont(new CustomFont().getFont("bahnschrift", 14));
            penerbit_tahun.setForeground(new java.awt.Color(255, 255, 255));
            penerbit_tahun.setText("penerbit, tahun");
            panel.add(penerbit_tahun, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 180, 240, 20));

            JTextField tf_hargaBuku = new javax.swing.JTextField();
            tf_hargaBuku.setEditable(false);
            tf_hargaBuku.setBackground(new java.awt.Color(40, 40, 40));
            tf_hargaBuku.setFont(new CustomFont().getFont("bebas", 18));
            tf_hargaBuku.setForeground(new java.awt.Color(255, 255, 255));
            tf_hargaBuku.setHorizontalAlignment(javax.swing.JTextField.CENTER);
            tf_hargaBuku.setText("Harga");
            tf_hargaBuku.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            tf_hargaBuku.setDisabledTextColor(new java.awt.Color(222, 222, 222));
            tf_hargaBuku.setEnabled(false);
            panel.add(tf_hargaBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 160, 170, 40));

            cb_isbn.addActionListener((ActionEvent e) -> {
                String item = (String) cb_isbn.getSelectedItem();
            });

            JTextField textField = (JTextField) cb_isbn.getEditor().getEditorComponent();
            textField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    SwingUtilities.invokeLater(() -> {
                        isbnFilter(cb_isbn, listIsbn, textField.getText());
                    });
                }
            });

            cb_isbn.addActionListener((ActionEvent e) -> {
                for (Buku bukus : listBukus) {
                    if (textField.getText().equalsIgnoreCase(bukus.getIsbn())) {
                        buku = bukus;
                        if (bukus.getImage() != null) {
                            imageBuku.setIcon(new ImageIcon(bukus.getImage()));
                        }
                        judulBuku.setText(bukus.getJudul_buku());
                        penulisBuku.setText(bukus.getPenulis());
                        penerbit_tahun.setText(bukus.getPenerbit() + ", " + bukus.getTahun());
                        tf_hargaBuku.setText(String.valueOf(FLOAT_F.rupiah(bukus.getHarga_jual())));
                        break;
                    }
                }
            });

            buttonSelect.addActionListener((ActionEvent e) -> {
                tf_isbn.setText(textField.getText());
                detailImpl.listDetailTransaksis.get(index - 1).setIsbn(textField.getText());
                dialog.dispose();
            });

            return panel;
        }

        private synchronized void isbnFilter(JComboBox<String> isbn, List<String> array, String keywords) {
            if (!isbn.isPopupVisible()) {
                isbn.showPopup();
            }

            List<String> filterArray = new ArrayList<>();

            for (int i = 0; i < array.size(); i++) {
                if (array.get(i).toLowerCase().contains(keywords.toLowerCase())) {
                    filterArray.add(array.get(i));
                }
            }

            if (filterArray.size() > 0) {
                DefaultComboBoxModel model = (DefaultComboBoxModel) isbn.getModel();
                model.removeAllElements();
                filterArray.forEach((s) -> {
                    model.addElement(s);
                });
                JTextField textField = (JTextField) isbn.getEditor().getEditorComponent();
                textField.setText(keywords);
            }
        }
    }
}
