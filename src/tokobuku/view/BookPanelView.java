package tokobuku.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import tokobuku.impl.BukuImpl;
import tokobuku.impl.KategoriImpl;
import tokobuku.model.Buku;
import tokobuku.util.CustomFont;
import tokobuku.util.Formatter;
import tokobuku.util.PreferencedHelper;

/**
 *
 * @author Rosyid Iz
 */
public class BookPanelView {

    private final Formatter<Integer> intF;
    private final BukuImpl bukuImpl;
    private final Buku buku;

    private JLabel imageBuku = new JLabel();
    private final JLabel judulBukuText;
    private final JLabel penulisText;
    private final JLabel isbnText;
    private final JPanel bookPanel;

    private int stok;

    private final KategoriImpl kategoriImpl;

    private final JDialog dialog = new JDialog();

    public BookPanelView(BukuImpl bukuImpl, Buku buku) {
        this.bukuImpl = bukuImpl;
        this.kategoriImpl = new KategoriImpl();
        this.buku = buku;
        this.intF = new Formatter<>();
        this.judulBukuText = new JLabel();
        this.isbnText = new JLabel();
        this.penulisText = new JLabel();
        this.bookPanel = new JPanel(true);
        this.stok = buku.getStok();
        try {
            kategoriImpl.load();
        } catch (SQLException ex) {
        }
        bookPanel.setLayout(new AbsoluteLayout());
        bookPanel.setBounds(0, 0, 200, 200);
        bookPanel.setBackground(new Color(65, 65, 65));
        bookPanel.setForeground(Color.WHITE);
    }

    public void setProperty() throws Exception {
        // Set image from db
        if (buku.getImage() != null) {
            imageBuku = new JLabel(new ImageIcon(Formatter.buffBytes(buku.getImage())));
        } else {
            imageBuku.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tokobuku/images/book.png")));
        }
        imageBuku.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        bookPanel.add(imageBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 20, 100, 100));

        judulBukuText.setFont(CustomFont.getFont("bahnschrift", 18));
        judulBukuText.setText("<html>" + buku.getJudul_buku() + "</html>");
        judulBukuText.setToolTipText(buku.getJudul_buku());
        judulBukuText.setForeground(Color.WHITE);
        bookPanel.add(judulBukuText, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 350, -1));

        isbnText.setFont(CustomFont.getFont("asap", 1, 14)); // NOI18N
        isbnText.setText("ISBN: " + Formatter.structIsbn(buku.getIsbn()));
        isbnText.setForeground(new Color(222, 222, 222));
        bookPanel.add(isbnText, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 75, 230, -1));

        penulisText.setFont(CustomFont.getFont("tahoma", 1, 14));
        penulisText.setText("Penulis: " + buku.getPenulis());
        penulisText.setForeground(Color.WHITE);
        bookPanel.add(penulisText, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 100, 350, 30));
    }

    public void apply(JPanel panel) {
        panel.add(bookPanel);
        bookPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                bookPanel.setBackground(new Color(55, 55, 55));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                bookPanel.setBackground(new Color(65, 65, 65));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane optionPane = new JOptionPane(getPanel(), JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
                dialog.setTitle("BUKU " + buku.getJudul_buku());
                dialog.setBackground(new java.awt.Color(55, 55, 55));
                dialog.setModal(true);
                dialog.setLocationByPlatform(true);
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                dialog.setContentPane(optionPane);
                dialog.pack();
                dialog.setVisible(true);
            }
        });
    }

    private JPanel getPanel() {
        JPanel basePanel = new javax.swing.JPanel();
        basePanel.setBackground(new java.awt.Color(50, 50, 50));
        basePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        basePanel.setPreferredSize(new Dimension(770, 495));

        JButton btnKembali = new javax.swing.JButton();
        btnKembali.setBackground(new java.awt.Color(55, 55, 55));
        btnKembali.setForeground(new java.awt.Color(255, 255, 255));
        btnKembali.setFont(new java.awt.Font("Bebas", 0, 18));
        btnKembali.setText("Kembali");
        btnKembali.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnKembali.setMargin(new java.awt.Insets(0, 0, 0, 0));

        btnKembali.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnKembali.setBackground(new java.awt.Color(255, 25, 25));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnKembali.setBackground(new java.awt.Color(55, 55, 55));
            }
        });
        basePanel.add(btnKembali, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 430, 120, 50));

        JButton btnEdit = new javax.swing.JButton();
        btnEdit.setBackground(new java.awt.Color(55, 55, 55));
        btnEdit.setFont(new java.awt.Font("Bebas", 0, 18));
        btnEdit.setForeground(new java.awt.Color(255, 255, 255));
        btnEdit.setText("EDIT");
        btnEdit.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnEdit.setMargin(new java.awt.Insets(0, 0, 0, 0));

        btnEdit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnEdit.setBackground(new java.awt.Color(25, 200, 25));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnEdit.setBackground(new java.awt.Color(55, 55, 55));
            }
        });
        basePanel.add(btnEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 430, 120, 50));

        JSeparator sep1 = new javax.swing.JSeparator();
        sep1.setBackground(new java.awt.Color(100, 100, 100));
        sep1.setForeground(new java.awt.Color(120, 120, 120));
        JSeparator sep2 = new javax.swing.JSeparator();
        sep2.setBackground(new java.awt.Color(100, 100, 100));
        sep2.setForeground(new java.awt.Color(120, 120, 120));
        JSeparator sep3 = new javax.swing.JSeparator();
        sep3.setBackground(new java.awt.Color(100, 100, 100));
        sep3.setForeground(new java.awt.Color(120, 120, 120));
        basePanel.add(sep1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 433, 430, 10));
        basePanel.add(sep2, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 453, 430, 10));
        basePanel.add(sep3, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 473, 430, 10));

        JPanel panelBuku = new javax.swing.JPanel();
        panelBuku.setBackground(new java.awt.Color(55, 55, 55));
        panelBuku.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelBuku.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        basePanel.add(panelBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 730, 400));

        JLabel labelJudulBuku = new javax.swing.JLabel();
        labelJudulBuku.setFont(new java.awt.Font("Bahnschrift", 0, 14));
        labelJudulBuku.setForeground(new java.awt.Color(255, 255, 153));
        labelJudulBuku.setText("Judul Buku:");
        panelBuku.add(labelJudulBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, -1, -1));

        JTextField tf_judulBuku = new javax.swing.JTextField();
        tf_judulBuku.setBackground(new java.awt.Color(55, 55, 55));
        tf_judulBuku.setFont(new java.awt.Font("Bahnschrift", 1, 18));
        tf_judulBuku.setForeground(new java.awt.Color(255, 255, 255));
        tf_judulBuku.setText(buku.getJudul_buku());
        tf_judulBuku.setBorder(null);
        tf_judulBuku.setCaretColor(new java.awt.Color(255, 255, 153));
        tf_judulBuku.setMargin(new java.awt.Insets(2, 16, 2, 16));
        tf_judulBuku.setEnabled(false);
        panelBuku.add(tf_judulBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 510, 40));

        JLabel labelISBN = new javax.swing.JLabel();
        labelISBN.setFont(new java.awt.Font("Bahnschrift", 0, 18));
        labelISBN.setForeground(new java.awt.Color(222, 222, 222));
        labelISBN.setText("ISBN:");
        panelBuku.add(labelISBN, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, -1, -1));

        JTextField tf_isbnBuku = new javax.swing.JTextField();
        tf_isbnBuku.setBackground(new java.awt.Color(55, 55, 55));
        tf_isbnBuku.setFont(new java.awt.Font("Bahnschrift", 1, 18));
        tf_isbnBuku.setForeground(new java.awt.Color(222, 222, 222));
        tf_isbnBuku.setText(Formatter.structIsbn(buku.getIsbn()));
        tf_isbnBuku.setBorder(null);
        tf_isbnBuku.setCaretColor(new java.awt.Color(255, 255, 153));
        tf_isbnBuku.setMargin(new java.awt.Insets(2, 16, 2, 16));
        tf_isbnBuku.setEnabled(false);
        panelBuku.add(tf_isbnBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 100, 230, 40));

        JLabel labelKategoriBuku = new javax.swing.JLabel();
        labelKategoriBuku.setFont(new java.awt.Font("Bahnschrift", 0, 14));
        labelKategoriBuku.setForeground(new java.awt.Color(255, 255, 153));
        labelKategoriBuku.setText("Kategori:");
        panelBuku.add(labelKategoriBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, -1, -1));

        JComboBox cb_kategoriBuku = new javax.swing.JComboBox<>();
        cb_kategoriBuku.setBackground(new java.awt.Color(55, 55, 55));
        cb_kategoriBuku.setForeground(new java.awt.Color(0, 0, 0));
        cb_kategoriBuku.setBorder(null);
        cb_kategoriBuku.setEnabled(false);
        cb_kategoriBuku.removeAllItems();
        kategoriImpl.listKategoris.forEach((listKategori) -> {
            cb_kategoriBuku.addItem(listKategori.getNama_kategori());
            if (buku.getKategori().equals(listKategori.getNama_kategori())) {
                cb_kategoriBuku.setSelectedItem(listKategori.getNama_kategori());
            }
        });
        cb_kategoriBuku.getEditor().getEditorComponent().setBackground(new java.awt.Color(55, 55, 55));
        panelBuku.add(cb_kategoriBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, 270, 40));

        JButton btnMoreKategori = new javax.swing.JButton();
        btnMoreKategori.setBackground(new java.awt.Color(65, 65, 65));
        btnMoreKategori.setForeground(new java.awt.Color(255, 255, 255));
        btnMoreKategori.setText("...");
        btnMoreKategori.setBorder(null);
        btnMoreKategori.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnMoreKategori.setBackground(new java.awt.Color(75, 75, 75));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnMoreKategori.setBackground(new java.awt.Color(65, 65, 65));
            }
        });
        panelBuku.add(btnMoreKategori, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 190, 40, 40));

        JLabel labelPenulisBuku = new javax.swing.JLabel();
        labelPenulisBuku.setFont(new java.awt.Font("Bahnschrift", 0, 14));
        labelPenulisBuku.setForeground(new java.awt.Color(255, 255, 153));
        labelPenulisBuku.setText("Penulis:");
        panelBuku.add(labelPenulisBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 250, -1, -1));

        JTextField tf_penulisBuku = new javax.swing.JTextField();
        tf_penulisBuku.setBackground(new java.awt.Color(55, 55, 55));
        tf_penulisBuku.setFont(new java.awt.Font("Bahnschrift", 1, 18));
        tf_penulisBuku.setForeground(new java.awt.Color(255, 255, 255));
        tf_penulisBuku.setText(buku.getPenulis());
        tf_penulisBuku.setBorder(null);
        tf_penulisBuku.setCaretColor(new java.awt.Color(255, 255, 153));
        tf_penulisBuku.setMargin(new java.awt.Insets(2, 16, 2, 16));
        tf_penulisBuku.setEnabled(false);
        panelBuku.add(tf_penulisBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 270, 470, 40));

        JLabel labelPenerbitBuku = new javax.swing.JLabel();
        labelPenerbitBuku.setFont(new java.awt.Font("Bahnschrift", 0, 14));
        labelPenerbitBuku.setForeground(new java.awt.Color(255, 255, 153));
        labelPenerbitBuku.setText("Penerbit:");
        panelBuku.add(labelPenerbitBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 320, -1, -1));

        JTextField tf_penerbitBuku = new javax.swing.JTextField();
        tf_penerbitBuku.setBackground(new java.awt.Color(55, 55, 55));
        tf_penerbitBuku.setFont(new java.awt.Font("Bahnschrift", 1, 18));
        tf_penerbitBuku.setForeground(new java.awt.Color(255, 255, 255));
        tf_penerbitBuku.setText(buku.getPenerbit());
        tf_penerbitBuku.setBorder(null);
        tf_penerbitBuku.setCaretColor(new java.awt.Color(255, 255, 153));
        tf_penerbitBuku.setMargin(new java.awt.Insets(2, 16, 2, 16));
        tf_penerbitBuku.setEnabled(false);
        panelBuku.add(tf_penerbitBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 340, 310, 40));

        JPanel panelCoverBuku = new javax.swing.JPanel();
        panelCoverBuku.setBackground(new java.awt.Color(188, 186, 184));
        panelCoverBuku.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelCoverBuku.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        panelBuku.add(panelCoverBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 30, 140, 130));

        JLabel coverBuku = new javax.swing.JLabel();
        coverBuku.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        coverBuku.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tokobuku/images/book.png"))); // NOI18N
        panelCoverBuku.add(coverBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 15, 110, -1));

        JLabel labelStokBuku = new javax.swing.JLabel();
        labelStokBuku.setFont(new java.awt.Font("Bahnschrift", 0, 14));
        labelStokBuku.setForeground(new java.awt.Color(255, 255, 153));
        labelStokBuku.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelStokBuku.setText("Stok:");
        panelBuku.add(labelStokBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 167, 140, 20));

        JTextField tf_stokBuku = new javax.swing.JTextField();
        tf_stokBuku.setBackground(new java.awt.Color(55, 55, 55));
        tf_stokBuku.setFont(new java.awt.Font("Bebas", 0, 20));
        tf_stokBuku.setForeground(new java.awt.Color(255, 255, 255));
        tf_stokBuku.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tf_stokBuku.setText(String.valueOf(buku.getStok()));
        tf_stokBuku.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tf_stokBuku.setCaretColor(new java.awt.Color(255, 255, 153));
        tf_stokBuku.setMargin(new java.awt.Insets(2, 16, 2, 16));
        tf_stokBuku.setEnabled(false);
        panelBuku.add(tf_stokBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 190, 60, 50));

        JButton btnMinStok = new javax.swing.JButton();
        btnMinStok.setBackground(new java.awt.Color(65, 65, 65));
        btnMinStok.setFont(new java.awt.Font("Bahnschrift", 0, 18));
        btnMinStok.setText("-");
        btnMinStok.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnMinStok.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnMinStok.setForeground(new java.awt.Color(255, 255, 255));
        btnMinStok.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnMinStok.setBackground(new java.awt.Color(100, 100, 100));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnMinStok.setBackground(new java.awt.Color(65, 65, 65));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (!tf_stokBuku.getText().equals("0")) {
                    stok -= 1;
                    tf_stokBuku.setText(String.valueOf(stok));
                }
            }
        });
        panelBuku.add(btnMinStok, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 190, 40, 50));

        JButton btnPlusStok = new javax.swing.JButton();
        btnPlusStok.setBackground(new java.awt.Color(65, 65, 65));
        btnPlusStok.setFont(new java.awt.Font("Bahnschrift", 0, 18));
        btnPlusStok.setText("+");
        btnPlusStok.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnPlusStok.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnPlusStok.setForeground(new java.awt.Color(255, 255, 255));
        btnPlusStok.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnPlusStok.setBackground(new java.awt.Color(100, 100, 100));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnPlusStok.setBackground(new java.awt.Color(65, 65, 65));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                stok += 1;
                tf_stokBuku.setText(String.valueOf(stok));
            }
        });
        panelBuku.add(btnPlusStok, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 190, 40, 50));

        JLabel labelHargaPokokBuku = new javax.swing.JLabel();
        labelHargaPokokBuku.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        labelHargaPokokBuku.setForeground(new java.awt.Color(255, 255, 153));
        labelHargaPokokBuku.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelHargaPokokBuku.setText("Harga Pokok:");
        panelBuku.add(labelHargaPokokBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 240, 140, 30));

        JTextField tf_hargaPokokBuku = new javax.swing.JTextField();
        tf_hargaPokokBuku.setBackground(new java.awt.Color(55, 55, 55));
        tf_hargaPokokBuku.setFont(new java.awt.Font("Bebas", 0, 18));
        tf_hargaPokokBuku.setForeground(new java.awt.Color(255, 255, 255));
        tf_hargaPokokBuku.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tf_hargaPokokBuku.setText(String.valueOf(intF.rupiah(buku.getHarga_pokok())));
        tf_hargaPokokBuku.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tf_hargaPokokBuku.setCaretColor(new java.awt.Color(255, 255, 153));
        tf_hargaPokokBuku.setMargin(new java.awt.Insets(2, 16, 2, 16));
        tf_hargaPokokBuku.setEnabled(false);
        panelBuku.add(tf_hargaPokokBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 270, 140, 40));

        JLabel labelHargaJualBuku = new javax.swing.JLabel();
        labelHargaJualBuku.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        labelHargaJualBuku.setForeground(new java.awt.Color(255, 255, 153));
        labelHargaJualBuku.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelHargaJualBuku.setText("Harga Jual:");
        panelBuku.add(labelHargaJualBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 310, 140, 30));

        JTextField tf_hargaJualBuku = new javax.swing.JTextField();
        tf_hargaJualBuku.setBackground(new java.awt.Color(55, 55, 55));
        tf_hargaJualBuku.setFont(new java.awt.Font("Bebas", 0, 18)); // NOI18N
        tf_hargaJualBuku.setForeground(new java.awt.Color(255, 255, 255));
        tf_hargaJualBuku.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tf_hargaJualBuku.setText(String.valueOf(intF.rupiah(buku.getHarga_jual())));
        tf_hargaJualBuku.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tf_hargaJualBuku.setCaretColor(new java.awt.Color(255, 255, 153));
        tf_hargaJualBuku.setMargin(new java.awt.Insets(2, 16, 2, 16));
        tf_hargaJualBuku.setEnabled(false);
        panelBuku.add(tf_hargaJualBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 340, 140, 40));

        JLabel labelTahunBuku = new javax.swing.JLabel();
        labelTahunBuku.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        labelTahunBuku.setForeground(new java.awt.Color(255, 255, 153));
        labelTahunBuku.setText("Tahun:");
        panelBuku.add(labelTahunBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 320, -1, -1));

        JTextField tf_tahunBuku = new javax.swing.JTextField();
        tf_tahunBuku.setBackground(new java.awt.Color(55, 55, 55));
        tf_tahunBuku.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        tf_tahunBuku.setForeground(new java.awt.Color(255, 255, 255));
        tf_tahunBuku.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        if (buku.getTahun().length() > 4) {
            String[] tahun = buku.getTahun().split("-");
            tf_tahunBuku.setText(tahun[0]);
        } else {
            tf_tahunBuku.setText(buku.getTahun());
        }
        tf_tahunBuku.setBorder(null);
        tf_tahunBuku.setCaretColor(new java.awt.Color(255, 255, 153));
        tf_tahunBuku.setEnabled(false);
        tf_tahunBuku.setMargin(new java.awt.Insets(2, 2, 2, 2));
        panelBuku.add(tf_tahunBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 340, 70, 40));

        btnMinStok.setVisible(false);
        btnPlusStok.setVisible(false);
        btnMoreKategori.setVisible(false);

        if (PreferencedHelper.getAkses().equalsIgnoreCase("kasir")) {
            btnEdit.setVisible(false);

        }

        btnEdit.addActionListener((l) -> {
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "jpeg");
            JFileChooser fc = new JFileChooser(FileSystemView.getFileSystemView());
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fc.removeChoosableFileFilter(fc.getFileFilter());
            fc.setFileFilter(filter);

            MouseAdapter m = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (btnEdit.getText().equalsIgnoreCase("SIMPAN")) {
                        int aksi = fc.showOpenDialog(dialog);
                        if (aksi == JFileChooser.APPROVE_OPTION) {
                            try {
                                File img = fc.getSelectedFile();
                                buku.setImage(Formatter.extractBytes(img));
                                coverBuku.setIcon(new ImageIcon(Formatter.buffBytes(buku.getImage())));
                            } catch (FileNotFoundException ex) {
                                System.out.println("Error (FileNotFound): " + ex.getMessage());
                            } catch (IOException ex) {
                                System.out.println("Error (IO): " + ex.getMessage());
                            }
                        }
                    }
                }
            };

            panelCoverBuku.addMouseListener(m);

            if (btnEdit.getText().equalsIgnoreCase("edit")) {
                btnEdit.setText("Simpan");
                btnKembali.setText("Batal");
                tf_stokBuku.setEnabled(true);
                tf_penulisBuku.setEnabled(true);
                tf_penerbitBuku.setEnabled(true);
                tf_judulBuku.setEnabled(true);
                tf_isbnBuku.setEnabled(true);
                tf_hargaPokokBuku.setEnabled(true);
                tf_hargaJualBuku.setEnabled(true);
                cb_kategoriBuku.setEnabled(true);
                cb_kategoriBuku.setForeground(new java.awt.Color(255, 255, 255));
                btnMinStok.setVisible(true);
                btnPlusStok.setVisible(true);
                btnMoreKategori.setVisible(true);
            } else {
                btnEdit.setText("Edit");
                btnKembali.setText("Kembali");
                try {
                    buku.setJudul_buku(tf_judulBuku.getText());
                    buku.setPenerbit(tf_penerbitBuku.getText());
                    buku.setPenulis(tf_penulisBuku.getText());
                    Object kategori = cb_kategoriBuku.getSelectedItem();
                    buku.setKategori(kategori.toString());
                    buku.setStok(stok);
                    String hargaJual = tf_hargaJualBuku.getText().replace(".", "");
                    String[] split = hargaJual.split(" ");
                    hargaJual = split[1];
                    buku.setHarga_jual(Integer.parseInt(hargaJual));
                    String hargaPokok = tf_hargaPokokBuku.getText().replace(".", "");
                    split = hargaPokok.split(" ");
                    hargaPokok = split[1];
                    buku.setHarga_pokok(Integer.parseInt(hargaPokok));
                    buku.setTahun(tf_tahunBuku.getText());
                    bukuImpl.update(buku, Formatter.destructIsbn(tf_isbnBuku.getText()));
                } catch (SQLException ex) {
                    System.out.println("Error : " + ex.getMessage());
                }

                tf_stokBuku.setEnabled(false);
                tf_penulisBuku.setEnabled(false);
                tf_penerbitBuku.setEnabled(false);
                tf_judulBuku.setEnabled(false);
                tf_isbnBuku.setEnabled(false);
                tf_hargaPokokBuku.setEnabled(false);
                tf_hargaJualBuku.setEnabled(false);
                cb_kategoriBuku.setEnabled(false);
                cb_kategoriBuku.setForeground(new java.awt.Color(0, 0, 0));
                btnMinStok.setVisible(false);
                btnPlusStok.setVisible(false);
                btnMoreKategori.setVisible(false);
            }
        });

        btnKembali.addActionListener((l) -> {
            if (btnKembali.getText().equalsIgnoreCase("kembali")) {
                dialog.dispose();
                DashboardPegawaiView.getInstance().loadDataBuku(0);
            } else {
                btnEdit.setText("Edit");
                tf_stokBuku.setEnabled(false);
                tf_penulisBuku.setEnabled(false);
                tf_penerbitBuku.setEnabled(false);
                tf_judulBuku.setEnabled(false);
                tf_isbnBuku.setEnabled(false);
                tf_hargaPokokBuku.setEnabled(false);
                tf_hargaJualBuku.setEnabled(false);
                cb_kategoriBuku.setEnabled(false);
                cb_kategoriBuku.setForeground(new java.awt.Color(0, 0, 0));
                btnMinStok.setVisible(false);
                btnPlusStok.setVisible(false);
                btnMoreKategori.setVisible(false);

                btnKembali.setText("KEMBALI");
                tf_judulBuku.setText(buku.getJudul_buku());
                tf_isbnBuku.setText(Formatter.structIsbn(buku.getIsbn()));
                cb_kategoriBuku.setSelectedItem(buku.getKategori());
                tf_penulisBuku.setText(buku.getPenulis());
                tf_penerbitBuku.setText(buku.getPenerbit());
                tf_stokBuku.setText(String.valueOf(buku.getStok()));
                tf_hargaPokokBuku.setText(String.valueOf(intF.rupiah(buku.getHarga_pokok())));
                tf_hargaJualBuku.setText(String.valueOf(intF.rupiah(buku.getHarga_jual())));
                if (buku.getTahun().length() > 4) {
                    String[] tahun = buku.getTahun().split("-");
                    tf_tahunBuku.setText(tahun[0]);
                } else {
                    tf_tahunBuku.setText(buku.getTahun());
                }
            }
        });

        return basePanel;
    }

}
