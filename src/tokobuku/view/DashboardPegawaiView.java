package tokobuku.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import java.util.prefs.BackingStoreException;
import javax.swing.SwingUtilities;
import tokobuku.impl.BukuImpl;
import tokobuku.impl.TransaksiImpl;
import tokobuku.model.Buku;
import tokobuku.model.Transaksi;
import tokobuku.util.DragWindow;
import tokobuku.util.PreferencedHelper;
import tokobuku.util.CustomFont;
import tokobuku.util.Formatter;

/**
 *
 * @author Rosyid Iz
 */
public class DashboardPegawaiView extends javax.swing.JFrame {
    
    private Formatter<Date> tanggal = new Formatter<>();

    private static final PreferencedHelper PREFS = new PreferencedHelper();
    private final BukuImpl buku = new BukuImpl();
    private List<Buku> listBuku = new ArrayList<>();
    private final TransaksiImpl transaksi = new TransaksiImpl();
    private List<Transaksi> listTransaksi = new ArrayList<>();
    private int totalBuku = 0;
    private int totalTransaksi = 0;

    /**
     * Creates new form DashboardPegawaiView
     */
    public DashboardPegawaiView() {
        initComponents();
        panelLoadingBuku.setVisible(false);
        panelDetailTransaksi.setVisible(false);
        panelTambahTransaksi.setVisible(false);
        panelInfo.setVisible(true);
        getCurTime();
        DragWindow dragWindow = new DragWindow(DashboardPegawaiView.this);
        namaKasirText.setText(PREFS.getName());
        noTeleponText.setText(PREFS.getTel());

        Thread threadBuku = new Thread("thread-buku") {
            @Override
            public void run() {
                loadDataBuku(0);
            }
        };
        Thread threadTransaksi = new Thread("thread-transaksi") {
            @Override
            public void run() {
                loadDataTransaksi();
            }
        };
        threadBuku.start();
        threadTransaksi.start();
    }

    private synchronized void loadDataBuku(int opt) {
        panelListBuku.removeAll();
        listBuku.clear();
        Thread t = new Thread("thread-loadDataBuku") {
            @Override
            public void run() {
                scrollPanelListBuku.setVisible(false);
                textLoadingDataBuku.setText("Mengambil Data Buku...");
                panelLoadingBuku.setVisible(true);
                try {
                    switch (opt) {
                        case 1:
                            listBuku = buku.loadAllBy("isbn");
                            break;
                        case 2:
                            listBuku = buku.loadAllBy("judul_buku");
                            break;
                        case 3:
                            listBuku = buku.loadAllBy("penulis");
                            break;
                        case 4:
                            listBuku = buku.loadAllBy("penerbit");
                            break;
                        default:
                            listBuku = buku.loadAll();
                    }
                    totalBuku = listBuku.size();
                } catch (SQLException ex) {
                    System.out.println("Error Fetching Data Buku!");
                }
                setListBukuPanel();
                panelLoadingBuku.setVisible(false);
                scrollPanelListBuku.setVisible(true);
            }
        };
        t.start();
    }

    private synchronized void searchBuku(String keywords) {
        panelListBuku.removeAll();
        listBuku.clear();
        Thread t = new Thread("thread-searchBuku") {
            @Override
            public void run() {
                scrollPanelListBuku.setVisible(false);
                textLoadingDataBuku.setText("Mencari Data Buku...");
                panelLoadingBuku.setVisible(true);
                try {
                    listBuku = buku.searchBuku(keywords);
                    totalBuku = listBuku.size();
                } catch (SQLException ex) {
                    System.out.println("Error Fetching Data Buku!");
                }
                setListBukuPanel();
                panelLoadingBuku.setVisible(false);
                scrollPanelListBuku.setVisible(true);
            }
        };
        t.start();
    }

    private synchronized void setListBukuPanel() {
        final int columns = 2;
        // Operator ternary menghitung jumlah baris
        int rows = totalBuku % columns != 0 ? (totalBuku / columns) + 1 : (totalBuku / columns);
        // Menghitung panjang dari panel panelListBuku
        float height = (rows * 150) + (rows * 5);
        panelListBuku.setLayout(new java.awt.GridLayout(rows, columns, 10, 10));
        if (rows < 4) {
            scrollPanelListBuku.setPreferredSize(new Dimension(1030, (int) height + 5));
        } else {
            scrollPanelListBuku.setPreferredSize(new Dimension(1030, 545));
        }
        panelListBuku.setPreferredSize(new java.awt.Dimension(1045, (int) height));
        for (int i = 0; i < totalBuku; i++) {
            BookPanelView bookPanelView = new BookPanelView();
            try {
                bookPanelView.setProperty(
                        listBuku.get(i).getImage(),
                        listBuku.get(i).getJudul_buku(),
                        listBuku.get(i).getIsbn(),
                        listBuku.get(i).getKategori(),
                        listBuku.get(i).getPenulis(),
                        listBuku.get(i).getPenerbit(),
                        listBuku.get(i).getTahun(),
                        listBuku.get(i).getStok(),
                        listBuku.get(i).getHarga_jual());
            } catch (Exception ex) {
                Logger.getLogger(DashboardPegawaiView.class.getName()).log(Level.SEVERE, null, ex);
            }
            bookPanelView.apply(panelListBuku);
        }
        SwingUtilities.updateComponentTreeUI(panelListBuku);
    }

    private synchronized void loadDataTransaksi() {
        panelListTransaksi.removeAll();
        Thread t = new Thread("thread-loadTransaksi") {
            @Override
            public void run() {
                panelListTransaksi.setVisible(false);
                try {
                    listTransaksi = transaksi.load();
                    totalTransaksi = listTransaksi.size();
                } catch (SQLException e) {
                    System.out.println("Error Fetching Data Transaksi!");
                }
                setListTransaksiPanel();
                panelListTransaksi.setVisible(true);
            }
        };
        t.start();
    }

    private void setListTransaksiPanel() {
        int rows = totalTransaksi;
        float height = (rows * 100);
        panelListTransaksi.setLayout(new GridLayout(rows, 1, 0, 5));
        if (rows < 5) {
            scrollPanelTransaksi.setPreferredSize(new Dimension(410, (int) height + 10));
        } else {
            scrollPanelTransaksi.setPreferredSize(new Dimension(410, 470));
        }
        panelListTransaksi.setPreferredSize(new Dimension(407, (int) height));

        for (int i = 0, j = i + 1; i < totalTransaksi; i++, j++) {
            TransaksiPanelView transaksiPanelView = new TransaksiPanelView(j);
            try {
                transaksiPanelView.setTransaksi(listTransaksi.get(i));
                transaksiPanelView.initJLabel(textNoTransaksiDT, textNamaPelangganDT, textNamaPegawaiDT, textBayarDT, textTotalDT, textTanggalDT);
                transaksiPanelView.initBase(panelDetailTransaksi, scrollPanelDT, panelDT, panelInfo, panelTambahTransaksi, buttonAddTransaksi);
                transaksiPanelView.apply(panelListTransaksi);
            } catch (Exception ex) {
                Logger.getLogger(DashboardPegawaiView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        SwingUtilities.updateComponentTreeUI(panelListTransaksi);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        frameTambahTransaksi = new javax.swing.JFrame();
        dragPanel = new javax.swing.JPanel();
        textTitleBar = new javax.swing.JLabel();
        minimizedIcon = new javax.swing.JLabel();
        closeButton = new javax.swing.JLabel();
        basePanel = new javax.swing.JPanel();
        tabbedPanel = new javax.swing.JTabbedPane();
        tabDashboard = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        tabDaftarBuku = new javax.swing.JPanel();
        headerDaftarBuku = new javax.swing.JPanel();
        listBukuText = new javax.swing.JLabel();
        pencarianText = new javax.swing.JLabel();
        fieldPencarian = new javax.swing.JTextField();
        buttonPencarian = new javax.swing.JButton();
        sortingText = new javax.swing.JLabel();
        sep2 = new javax.swing.JSeparator();
        sortingComboBox = new javax.swing.JComboBox<>();
        panelLoadingBuku = new javax.swing.JPanel();
        textLoadingDataBuku = new javax.swing.JLabel();
        scrollPanelListBuku = new javax.swing.JScrollPane();
        panelListBuku = new javax.swing.JPanel();
        tabTransaksi = new javax.swing.JPanel();
        panelTransaksi = new javax.swing.JPanel();
        scrollPanelTransaksi = new javax.swing.JScrollPane();
        panelListTransaksi = new javax.swing.JPanel();
        textTransaksiPembelian = new javax.swing.JLabel();
        buttonAddTransaksi = new javax.swing.JButton();
        panelTambahTransaksi = new javax.swing.JPanel();
        scrollPanelDTtambah = new javax.swing.JScrollPane();
        panelDT1 = new javax.swing.JPanel();
        panelTableHeaderTambah = new javax.swing.JPanel();
        panelHeadHarga1 = new javax.swing.JPanel();
        textHarga1 = new javax.swing.JLabel();
        panelHeadQty1 = new javax.swing.JPanel();
        textQty1 = new javax.swing.JLabel();
        panelHeadTotal1 = new javax.swing.JPanel();
        textTotal1 = new javax.swing.JLabel();
        panelHeadNo1 = new javax.swing.JPanel();
        textNo1 = new javax.swing.JLabel();
        panelHeadIsbn1 = new javax.swing.JPanel();
        textIsbn1 = new javax.swing.JLabel();
        textHeaderAddTrx = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        tf_uangTunai = new javax.swing.JFormattedTextField();
        textUangTunaiAdd = new javax.swing.JLabel();
        cb_namaPelanggan = new javax.swing.JComboBox<>();
        textNamaPelangganAdd = new javax.swing.JLabel();
        btnTambahDataTrx = new javax.swing.JButton();
        tf_tanggal = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        panelTotalTambah = new javax.swing.JLabel();
        tf_total = new javax.swing.JTextField();
        textTanggalTambah = new javax.swing.JLabel();
        panelNoTrx = new javax.swing.JPanel();
        textNoTransaksiAdd = new javax.swing.JLabel();
        textIDTrx = new javax.swing.JLabel();
        panelInfo = new javax.swing.JPanel();
        textNoTransaksiDT1 = new javax.swing.JLabel();
        panelDetailTransaksi = new javax.swing.JPanel();
        textTanggalDT = new javax.swing.JLabel();
        textNoTransaksiDT = new javax.swing.JLabel();
        textNamaPelangganDT = new javax.swing.JLabel();
        textNamaPegawaiDT = new javax.swing.JLabel();
        textBayarDT = new javax.swing.JLabel();
        textTotalDT = new javax.swing.JLabel();
        scrollPanelDT = new javax.swing.JScrollPane();
        panelDT = new javax.swing.JPanel();
        panelTableHeader = new javax.swing.JPanel();
        panelHeadHarga = new javax.swing.JPanel();
        textHarga = new javax.swing.JLabel();
        panelHeadQty = new javax.swing.JPanel();
        textQty = new javax.swing.JLabel();
        panelHeadTotal = new javax.swing.JPanel();
        textTotal = new javax.swing.JLabel();
        panelHeadNo = new javax.swing.JPanel();
        textNo = new javax.swing.JLabel();
        panelHeadIsbn = new javax.swing.JPanel();
        textIsbn = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        tabPelanggan = new javax.swing.JPanel();
        leftPanel = new javax.swing.JPanel();
        versionText = new javax.swing.JLabel();
        titleText = new javax.swing.JLabel();
        ubahProfilButton = new javax.swing.JButton();
        logoutButton = new javax.swing.JButton();
        selamatDatangText = new javax.swing.JLabel();
        namaKasirText = new javax.swing.JLabel();
        noTeleponText = new javax.swing.JLabel();
        sep1 = new javax.swing.JSeparator();
        curTime = new javax.swing.JLabel();
        backgroundPanel = new javax.swing.JLabel();

        frameTambahTransaksi.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        frameTambahTransaksi.setTitle("Tambah Transaksi");
        frameTambahTransaksi.setName("frameAddTransaksi"); // NOI18N
        frameTambahTransaksi.setUndecorated(true);
        frameTambahTransaksi.setType(java.awt.Window.Type.POPUP);

        javax.swing.GroupLayout frameTambahTransaksiLayout = new javax.swing.GroupLayout(frameTambahTransaksi.getContentPane());
        frameTambahTransaksi.getContentPane().setLayout(frameTambahTransaksiLayout);
        frameTambahTransaksiLayout.setHorizontalGroup(
            frameTambahTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        frameTambahTransaksiLayout.setVerticalGroup(
            frameTambahTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        frameTambahTransaksi.getAccessibleContext().setAccessibleParent(buttonAddTransaksi);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("K-SIR Book: Dashboard");
        setLocationByPlatform(true);
        setMaximumSize(new java.awt.Dimension(1280, 2147483647));
        setName("frameDashboard"); // NOI18N
        setUndecorated(true);

        dragPanel.setBackground(new java.awt.Color(35, 35, 35));
        dragPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        dragPanel.setMaximumSize(new java.awt.Dimension(1024, 40));
        dragPanel.setMinimumSize(new java.awt.Dimension(1024, 40));
        dragPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        textTitleBar.setFont(new java.awt.Font("Metropolis Medium", 0, 14)); // NOI18N
        textTitleBar.setForeground(new java.awt.Color(255, 255, 255));
        textTitleBar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textTitleBar.setText("K-SIR Book: Dashboard");
        dragPanel.add(textTitleBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 0, 1120, 36));

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

        basePanel.setBackground(new java.awt.Color(65, 65, 65));
        basePanel.setMaximumSize(new java.awt.Dimension(1280, 32767));
        basePanel.setMinimumSize(new java.awt.Dimension(1280, 768));
        basePanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                basePanelMouseClicked(evt);
            }
        });
        basePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tabbedPanel.setBackground(new java.awt.Color(51, 51, 51));
        tabbedPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 2, 2, 2));
        tabbedPanel.setForeground(new java.awt.Color(255, 255, 255));
        tabbedPanel.setToolTipText("");
        tabbedPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tabbedPanel.setFont(new CustomFont().getFont("bahnschrift", 0, 18)
        );
        tabbedPanel.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabbedPanelStateChanged(evt);
            }
        });

        tabDashboard.setBackground(new java.awt.Color(75, 75, 75));
        tabDashboard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Montserrat SemiBold", 0, 32)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(240, 240, 240));
        jLabel2.setText("Tinjauan");
        tabDashboard.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, -1, -1));

        jPanel4.setPreferredSize(new java.awt.Dimension(250, 250));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        tabDashboard.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 140, 370, 220));

        tabbedPanel.addTab("  Dashboard  ", tabDashboard);

        tabDaftarBuku.setBackground(new java.awt.Color(75, 75, 75));
        tabDaftarBuku.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        headerDaftarBuku.setBackground(new java.awt.Color(75, 75, 75));
        headerDaftarBuku.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        listBukuText.setFont(new CustomFont().getFont("bahnschrift", 36));
        listBukuText.setForeground(new java.awt.Color(255, 255, 255));
        listBukuText.setText("Daftar Buku");
        headerDaftarBuku.add(listBukuText, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, -1, -1));

        pencarianText.setFont(new CustomFont().getFont("tahoma",18));
        pencarianText.setForeground(new java.awt.Color(255, 255, 255));
        pencarianText.setText("Pencarian:");
        headerDaftarBuku.add(pencarianText, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 20, -1, -1));

        fieldPencarian.setBackground(new java.awt.Color(35, 35, 35));
        fieldPencarian.setFont(new CustomFont().getFont("tahoma",14));
        fieldPencarian.setForeground(new java.awt.Color(255, 255, 255));
        fieldPencarian.setText("ISBN / Judul Buku / Pengarang / Penerbit");
        fieldPencarian.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        fieldPencarian.setCaretColor(new java.awt.Color(255, 255, 0));
        fieldPencarian.setSelectedTextColor(new java.awt.Color(51, 51, 51));
        fieldPencarian.setSelectionColor(new java.awt.Color(255, 205, 158));
        fieldPencarian.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fieldPencarianKeyPressed(evt);
            }
        });
        headerDaftarBuku.add(fieldPencarian, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 50, 190, 40));

        buttonPencarian.setBackground(new java.awt.Color(51, 51, 51));
        buttonPencarian.setFont(new CustomFont().getFont(1, 12)
        );
        buttonPencarian.setForeground(new java.awt.Color(255, 255, 255));
        buttonPencarian.setText("CARI");
        buttonPencarian.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        buttonPencarian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPencarianActionPerformed(evt);
            }
        });
        headerDaftarBuku.add(buttonPencarian, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 50, 60, 40));

        sortingText.setFont(new CustomFont().getFont(18));
        sortingText.setForeground(new java.awt.Color(255, 255, 255));
        sortingText.setText("Urutkan berdasarkan:");
        headerDaftarBuku.add(sortingText, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 20, -1, -1));

        sep2.setBackground(new java.awt.Color(255, 194, 149));
        sep2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        headerDaftarBuku.add(sep2, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 20, 10, 80));

        sortingComboBox.setBackground(new java.awt.Color(35, 35, 35));
        sortingComboBox.setFont(new CustomFont().getFont(14));
        sortingComboBox.setForeground(new java.awt.Color(255, 255, 255));
        sortingComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "ISBN", "Judul Buku", "Penulis", "Penerbit" }));
        sortingComboBox.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        sortingComboBox.setInheritsPopupMenu(true);
        sortingComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortingComboBoxActionPerformed(evt);
            }
        });
        headerDaftarBuku.add(sortingComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 50, 170, 40));

        tabDaftarBuku.add(headerDaftarBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1045, 120));

        panelLoadingBuku.setBackground(new java.awt.Color(51, 51, 51));
        panelLoadingBuku.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        textLoadingDataBuku.setFont(new CustomFont().getFont("bahnschrift", 1, 36));
        textLoadingDataBuku.setForeground(new java.awt.Color(255, 255, 255));
        textLoadingDataBuku.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textLoadingDataBuku.setText("Mengambil Data Buku...");
        panelLoadingBuku.add(textLoadingDataBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 1010, 70));

        tabDaftarBuku.add(panelLoadingBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 115, 1040, 560));

        scrollPanelListBuku.setBackground(new java.awt.Color(75, 75, 75));
        scrollPanelListBuku.setBorder(null);
        scrollPanelListBuku.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPanelListBuku.setViewportBorder(javax.swing.BorderFactory.createEtchedBorder());
        scrollPanelListBuku.setPreferredSize(new java.awt.Dimension(1030, 545));

        panelListBuku.setBackground(new java.awt.Color(75, 75, 75));
        panelListBuku.setMinimumSize(new java.awt.Dimension(1030, 0));
        panelListBuku.setPreferredSize(new java.awt.Dimension(1030, 540));
        panelListBuku.setLayout(new java.awt.GridLayout(1, 0, 20, 20));
        scrollPanelListBuku.setViewportView(panelListBuku);

        tabDaftarBuku.add(scrollPanelListBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 120, -1, -1));

        tabbedPanel.addTab("  Daftar Buku  ", tabDaftarBuku);

        tabTransaksi.setBackground(new java.awt.Color(85, 85, 85));
        tabTransaksi.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelTransaksi.setBackground(new java.awt.Color(77, 77, 77));
        panelTransaksi.setMaximumSize(new java.awt.Dimension(410, 670));
        panelTransaksi.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        scrollPanelTransaksi.setBackground(new java.awt.Color(60, 60, 60));
        scrollPanelTransaksi.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        scrollPanelTransaksi.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPanelTransaksi.setMinimumSize(new java.awt.Dimension(410, 570));
        scrollPanelTransaksi.setPreferredSize(new java.awt.Dimension(410, 470));

        panelListTransaksi.setBackground(new java.awt.Color(51, 51, 51));
        panelListTransaksi.setLayout(new java.awt.GridLayout(1, 0));
        scrollPanelTransaksi.setViewportView(panelListTransaksi);

        panelTransaksi.add(scrollPanelTransaksi, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, -1, -1));

        textTransaksiPembelian.setFont(new CustomFont().getFont("bahnschrift", Font.BOLD, 24));
        textTransaksiPembelian.setForeground(new java.awt.Color(255, 255, 255));
        textTransaksiPembelian.setText("Transaksi Pembelian");
        panelTransaksi.add(textTransaksiPembelian, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 260, 80));

        buttonAddTransaksi.setBackground(new java.awt.Color(30, 30, 30));
        buttonAddTransaksi.setFont(new CustomFont().getFont("bahnschrift", 1, 24));
        buttonAddTransaksi.setForeground(new java.awt.Color(200, 200, 200));
        buttonAddTransaksi.setText("TAMBAH TRANSAKSI");
        buttonAddTransaksi.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        buttonAddTransaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAddTransaksiActionPerformed(evt);
            }
        });
        panelTransaksi.add(buttonAddTransaksi, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 577, 396, 86));

        tabTransaksi.add(panelTransaksi, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 410, 670));

        panelTambahTransaksi.setBackground(new java.awt.Color(87, 87, 87));
        panelTambahTransaksi.setMinimumSize(new java.awt.Dimension(630, 670));
        panelTambahTransaksi.setName(""); // NOI18N
        panelTambahTransaksi.setPreferredSize(new java.awt.Dimension(630, 675));
        panelTambahTransaksi.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        scrollPanelDTtambah.setBackground(new java.awt.Color(51, 51, 51));
        scrollPanelDTtambah.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        scrollPanelDTtambah.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPanelDTtambah.setPreferredSize(new java.awt.Dimension(610, 410));

        panelDT1.setBackground(new java.awt.Color(40, 40, 40));
        panelDT1.setLayout(new java.awt.GridLayout());
        scrollPanelDTtambah.setViewportView(panelDT1);

        panelTambahTransaksi.add(scrollPanelDTtambah, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, -1, 340));

        panelTableHeaderTambah.setBackground(new java.awt.Color(70, 70, 70));
        panelTableHeaderTambah.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelTableHeaderTambah.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelHeadHarga1.setBackground(new java.awt.Color(51, 51, 51));
        panelHeadHarga1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelHeadHarga1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        textHarga1.setFont(new java.awt.Font("Bebas", 0, 14)); // NOI18N
        textHarga1.setForeground(new java.awt.Color(255, 255, 255));
        textHarga1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textHarga1.setText("HARGA");
        panelHeadHarga1.add(textHarga1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 130, 40));

        panelTableHeaderTambah.add(panelHeadHarga1, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 5, -1, -1));

        panelHeadQty1.setBackground(new java.awt.Color(51, 51, 51));
        panelHeadQty1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelHeadQty1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        textQty1.setFont(new java.awt.Font("Bebas", 0, 14)); // NOI18N
        textQty1.setForeground(new java.awt.Color(255, 255, 255));
        textQty1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textQty1.setText("QTY");
        textQty1.setPreferredSize(new java.awt.Dimension(80, 40));
        panelHeadQty1.add(textQty1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 80, 40));

        panelTableHeaderTambah.add(panelHeadQty1, new org.netbeans.lib.awtextra.AbsoluteConstraints(235, 5, -1, -1));

        panelHeadTotal1.setBackground(new java.awt.Color(51, 51, 51));
        panelHeadTotal1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelHeadTotal1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        textTotal1.setFont(new java.awt.Font("Bebas", 0, 14)); // NOI18N
        textTotal1.setForeground(new java.awt.Color(255, 255, 255));
        textTotal1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textTotal1.setText("TOTAL");
        panelHeadTotal1.add(textTotal1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 155, 40));

        panelTableHeaderTambah.add(panelHeadTotal1, new org.netbeans.lib.awtextra.AbsoluteConstraints(455, 5, 150, -1));

        panelHeadNo1.setBackground(new java.awt.Color(51, 51, 51));
        panelHeadNo1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelHeadNo1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        textNo1.setFont(new java.awt.Font("Bebas", 0, 14)); // NOI18N
        textNo1.setForeground(new java.awt.Color(255, 255, 255));
        textNo1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textNo1.setText("No.");
        panelHeadNo1.add(textNo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 40, 40));

        panelTableHeaderTambah.add(panelHeadNo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 5, -1, -1));

        panelHeadIsbn1.setBackground(new java.awt.Color(51, 51, 51));
        panelHeadIsbn1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelHeadIsbn1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        textIsbn1.setFont(new java.awt.Font("Bebas", 0, 14)); // NOI18N
        textIsbn1.setForeground(new java.awt.Color(255, 255, 255));
        textIsbn1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textIsbn1.setText("ISBN");
        panelHeadIsbn1.add(textIsbn1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 180, 40));

        panelTableHeaderTambah.add(panelHeadIsbn1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 5, -1, -1));

        panelTambahTransaksi.add(panelTableHeaderTambah, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 175, 610, 50));

        textHeaderAddTrx.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        textHeaderAddTrx.setForeground(new java.awt.Color(255, 255, 255));
        textHeaderAddTrx.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textHeaderAddTrx.setText("Tambah Data Transaksi");
        panelTambahTransaksi.add(textHeaderAddTrx, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 610, 50));

        jSeparator2.setBackground(new java.awt.Color(153, 153, 153));
        jSeparator2.setForeground(new java.awt.Color(102, 102, 102));
        panelTambahTransaksi.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 590, -1));

        tf_uangTunai.setBackground(new java.awt.Color(97, 97, 97));
        tf_uangTunai.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        tf_uangTunai.setForeground(new java.awt.Color(255, 255, 255));
        tf_uangTunai.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("¤#,##0"))));
        tf_uangTunai.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tf_uangTunai.setText("0");
        tf_uangTunai.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        panelTambahTransaksi.add(tf_uangTunai, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 110, 210, 40));

        textUangTunaiAdd.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        textUangTunaiAdd.setForeground(new java.awt.Color(255, 255, 150));
        textUangTunaiAdd.setText("Uang Tunai:");
        panelTambahTransaksi.add(textUangTunaiAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 80, -1, -1));

        cb_namaPelanggan.setBackground(new java.awt.Color(97, 97, 97));
        cb_namaPelanggan.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        cb_namaPelanggan.setForeground(new java.awt.Color(255, 255, 255));
        cb_namaPelanggan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cb_namaPelanggan.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        panelTambahTransaksi.add(cb_namaPelanggan, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 210, 40));

        textNamaPelangganAdd.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        textNamaPelangganAdd.setForeground(new java.awt.Color(255, 255, 150));
        textNamaPelangganAdd.setText("Nama Pelanggan:");
        panelTambahTransaksi.add(textNamaPelangganAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, -1));

        btnTambahDataTrx.setBackground(new java.awt.Color(102, 255, 51));
        btnTambahDataTrx.setFont(new java.awt.Font("Bebas", 1, 18)); // NOI18N
        btnTambahDataTrx.setForeground(new java.awt.Color(30, 30, 30));
        btnTambahDataTrx.setText("TAMBAH");
        btnTambahDataTrx.setMargin(new java.awt.Insets(2, 2, 2, 2));
        panelTambahTransaksi.add(btnTambahDataTrx, new org.netbeans.lib.awtextra.AbsoluteConstraints(508, 580, 110, 80));

        tf_tanggal.setEditable(false);
        tf_tanggal.setBackground(new java.awt.Color(80, 80, 80));
        tf_tanggal.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        tf_tanggal.setForeground(new java.awt.Color(200, 200, 200));
        tf_tanggal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tf_tanggal.setText("24/05/2000");
        tf_tanggal.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tf_tanggal.setMargin(new java.awt.Insets(2, 14, 2, 14));
        panelTambahTransaksi.add(tf_tanggal, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 610, 190, 50));

        jSeparator3.setBackground(new java.awt.Color(80, 80, 80));
        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        panelTambahTransaksi.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 580, 10, 80));

        panelTotalTambah.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        panelTotalTambah.setForeground(new java.awt.Color(255, 255, 255));
        panelTotalTambah.setText("Total:");
        panelTambahTransaksi.add(panelTotalTambah, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 580, 250, 30));

        tf_total.setEditable(false);
        tf_total.setBackground(new java.awt.Color(80, 80, 80));
        tf_total.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        tf_total.setForeground(new java.awt.Color(200, 200, 200));
        tf_total.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tf_total.setText("Rp.00,-");
        tf_total.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelTambahTransaksi.add(tf_total, new org.netbeans.lib.awtextra.AbsoluteConstraints(239, 610, 250, 50));

        textTanggalTambah.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        textTanggalTambah.setForeground(new java.awt.Color(255, 255, 255));
        textTanggalTambah.setText("Tanggal:");
        panelTambahTransaksi.add(textTanggalTambah, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 580, 190, 30));

        panelNoTrx.setBackground(new java.awt.Color(77, 77, 77));
        panelNoTrx.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        textNoTransaksiAdd.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        textNoTransaksiAdd.setForeground(new java.awt.Color(255, 255, 255));
        textNoTransaksiAdd.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textNoTransaksiAdd.setText("No Transaksi");
        panelNoTrx.add(textNoTransaksiAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 120, 30));

        textIDTrx.setFont(new java.awt.Font("Bebas", 0, 18)); // NOI18N
        textIDTrx.setForeground(new java.awt.Color(255, 255, 255));
        textIDTrx.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textIDTrx.setText("NO");
        panelNoTrx.add(textIDTrx, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 100, 60));

        panelTambahTransaksi.add(panelNoTrx, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 65, 120, 100));

        tabTransaksi.add(panelTambahTransaksi, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 0, -1, -1));

        panelInfo.setBackground(new java.awt.Color(87, 87, 87));
        panelInfo.setMinimumSize(new java.awt.Dimension(630, 670));
        panelInfo.setName(""); // NOI18N
        panelInfo.setPreferredSize(new java.awt.Dimension(630, 675));
        panelInfo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        textNoTransaksiDT1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        textNoTransaksiDT1.setForeground(new java.awt.Color(255, 255, 255));
        textNoTransaksiDT1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textNoTransaksiDT1.setText("Pilih salah satu transaksi untuk melihat detail transaksi");
        panelInfo.add(textNoTransaksiDT1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 440, 440, 30));

        tabTransaksi.add(panelInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 0, -1, -1));

        panelDetailTransaksi.setBackground(new java.awt.Color(87, 87, 87));
        panelDetailTransaksi.setMinimumSize(new java.awt.Dimension(630, 670));
        panelDetailTransaksi.setName(""); // NOI18N
        panelDetailTransaksi.setPreferredSize(new java.awt.Dimension(630, 675));
        panelDetailTransaksi.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        textTanggalDT.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        textTanggalDT.setForeground(new java.awt.Color(255, 255, 255));
        textTanggalDT.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        textTanggalDT.setText("Tanggal");
        panelDetailTransaksi.add(textTanggalDT, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 80, 280, 30));

        textNoTransaksiDT.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        textNoTransaksiDT.setForeground(new java.awt.Color(255, 255, 255));
        textNoTransaksiDT.setText("No Transaksi: idtrx");
        panelDetailTransaksi.add(textNoTransaksiDT, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 240, 30));

        textNamaPelangganDT.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        textNamaPelangganDT.setForeground(new java.awt.Color(255, 255, 255));
        textNamaPelangganDT.setText("Pelanggan: nama_pelanggan");
        panelDetailTransaksi.add(textNamaPelangganDT, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 240, 30));

        textNamaPegawaiDT.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        textNamaPegawaiDT.setForeground(new java.awt.Color(255, 255, 255));
        textNamaPegawaiDT.setText("Kasir: nama_pegawai");
        panelDetailTransaksi.add(textNamaPegawaiDT, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 240, 30));

        textBayarDT.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        textBayarDT.setForeground(new java.awt.Color(255, 255, 255));
        textBayarDT.setText("Uang Tunai: bayar");
        panelDetailTransaksi.add(textBayarDT, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 240, 30));

        textTotalDT.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        textTotalDT.setForeground(new java.awt.Color(255, 255, 255));
        textTotalDT.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        textTotalDT.setText("Total Beli");
        panelDetailTransaksi.add(textTotalDT, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 20, 280, 50));

        scrollPanelDT.setBackground(new java.awt.Color(51, 51, 51));
        scrollPanelDT.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        scrollPanelDT.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPanelDT.setPreferredSize(new java.awt.Dimension(610, 410));

        panelDT.setBackground(new java.awt.Color(40, 40, 40));
        panelDT.setLayout(new java.awt.GridLayout(1, 0));
        scrollPanelDT.setViewportView(panelDT);

        panelDetailTransaksi.add(scrollPanelDT, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, -1, -1));

        panelTableHeader.setBackground(new java.awt.Color(70, 70, 70));
        panelTableHeader.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelTableHeader.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelHeadHarga.setBackground(new java.awt.Color(51, 51, 51));
        panelHeadHarga.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelHeadHarga.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        textHarga.setFont(new java.awt.Font("Bebas", 0, 14)); // NOI18N
        textHarga.setForeground(new java.awt.Color(255, 255, 255));
        textHarga.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textHarga.setText("HARGA");
        panelHeadHarga.add(textHarga, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 130, 40));

        panelTableHeader.add(panelHeadHarga, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 5, -1, -1));

        panelHeadQty.setBackground(new java.awt.Color(51, 51, 51));
        panelHeadQty.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelHeadQty.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        textQty.setFont(new java.awt.Font("Bebas", 0, 14)); // NOI18N
        textQty.setForeground(new java.awt.Color(255, 255, 255));
        textQty.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textQty.setText("QTY");
        textQty.setPreferredSize(new java.awt.Dimension(80, 40));
        panelHeadQty.add(textQty, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 80, 40));

        panelTableHeader.add(panelHeadQty, new org.netbeans.lib.awtextra.AbsoluteConstraints(235, 5, -1, -1));

        panelHeadTotal.setBackground(new java.awt.Color(51, 51, 51));
        panelHeadTotal.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelHeadTotal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        textTotal.setFont(new java.awt.Font("Bebas", 0, 14)); // NOI18N
        textTotal.setForeground(new java.awt.Color(255, 255, 255));
        textTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textTotal.setText("TOTAL");
        panelHeadTotal.add(textTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 155, 40));

        panelTableHeader.add(panelHeadTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(455, 5, 150, -1));

        panelHeadNo.setBackground(new java.awt.Color(51, 51, 51));
        panelHeadNo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelHeadNo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        textNo.setFont(new java.awt.Font("Bebas", 0, 14)); // NOI18N
        textNo.setForeground(new java.awt.Color(255, 255, 255));
        textNo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textNo.setText("No.");
        panelHeadNo.add(textNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 40, 40));

        panelTableHeader.add(panelHeadNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 5, -1, -1));

        panelHeadIsbn.setBackground(new java.awt.Color(51, 51, 51));
        panelHeadIsbn.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelHeadIsbn.setPreferredSize(new java.awt.Dimension(180, 40));
        panelHeadIsbn.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        textIsbn.setFont(new java.awt.Font("Bebas", 0, 14)); // NOI18N
        textIsbn.setForeground(new java.awt.Color(255, 255, 255));
        textIsbn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textIsbn.setText("ISBN");
        panelHeadIsbn.add(textIsbn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 180, 40));

        panelTableHeader.add(panelHeadIsbn, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 5, -1, -1));

        panelDetailTransaksi.add(panelTableHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 195, 610, 50));
        panelDetailTransaksi.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 70, 250, 10));

        tabTransaksi.add(panelDetailTransaksi, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 0, -1, -1));

        tabbedPanel.addTab("  Transaksi  ", tabTransaksi);

        javax.swing.GroupLayout tabPelangganLayout = new javax.swing.GroupLayout(tabPelanggan);
        tabPelanggan.setLayout(tabPelangganLayout);
        tabPelangganLayout.setHorizontalGroup(
            tabPelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1041, Short.MAX_VALUE)
        );
        tabPelangganLayout.setVerticalGroup(
            tabPelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 676, Short.MAX_VALUE)
        );

        tabbedPanel.addTab("  Pelanggan  ", tabPelanggan);

        basePanel.add(tabbedPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 0, 1050, 708));
        tabbedPanel.getAccessibleContext().setAccessibleName("tabDashboard");

        leftPanel.setBackground(new java.awt.Color(51, 51, 51));
        leftPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        versionText.setFont(new CustomFont().getFont("tahoma", 14)
        );
        versionText.setForeground(new java.awt.Color(204, 204, 204));
        versionText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        versionText.setText("V 1.0");
        leftPanel.add(versionText, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 250, 30));

        titleText.setBackground(new java.awt.Color(255, 205, 158));
        titleText.setFont(new CustomFont().getFont("bebas",36));
        titleText.setForeground(new java.awt.Color(255, 205, 158));
        titleText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleText.setText("K-SIR   BOOK");
        leftPanel.add(titleText, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, 250, -1));

        ubahProfilButton.setBackground(new java.awt.Color(51, 51, 51));
        ubahProfilButton.setFont(new CustomFont().getFont("bahnschrift", 1, 18)
        );
        ubahProfilButton.setForeground(new java.awt.Color(255, 255, 255));
        ubahProfilButton.setText("UBAH PROFILE");
        ubahProfilButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        leftPanel.add(ubahProfilButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 563, 190, 50));

        logoutButton.setBackground(new java.awt.Color(51, 51, 51));
        logoutButton.setFont(new CustomFont().getFont("bahnschrift", 1, 18)
        );
        logoutButton.setForeground(new java.awt.Color(255, 255, 255));
        logoutButton.setText("LOGOUT");
        logoutButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        logoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutButtonActionPerformed(evt);
            }
        });
        leftPanel.add(logoutButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 630, 190, 50));

        selamatDatangText.setFont(new CustomFont().getFont("bahnschrift", 18)
        );
        selamatDatangText.setForeground(new java.awt.Color(255, 255, 255));
        selamatDatangText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        selamatDatangText.setText("Selamat Datang");
        leftPanel.add(selamatDatangText, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 250, -1));

        namaKasirText.setBackground(new java.awt.Color(250, 250, 250));
        namaKasirText.setFont(new CustomFont().getFont("bebas", Font.BOLD, 36));
        namaKasirText.setForeground(new java.awt.Color(250, 250, 250));
        namaKasirText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        namaKasirText.setText("Nama Kasir");
        leftPanel.add(namaKasirText, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 230, -1));

        noTeleponText.setFont(new CustomFont().getFont("bahnshrift",1,14));
        noTeleponText.setForeground(new java.awt.Color(204, 204, 204));
        noTeleponText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        noTeleponText.setText("Nomor Telepon");
        leftPanel.add(noTeleponText, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, 210, -1));
        leftPanel.add(sep1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 210, 10));

        curTime.setFont(new CustomFont().getFont("bebas", 1, 18)
        );
        curTime.setForeground(new java.awt.Color(255, 255, 255));
        curTime.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        curTime.setText("00:00");
        leftPanel.add(curTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 500, 250, 40));

        backgroundPanel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        backgroundPanel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tokobuku/images/library-background-2.png"))); // NOI18N
        backgroundPanel.setText("Ubah ");
        backgroundPanel.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        leftPanel.add(backgroundPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 250, 710));

        basePanel.add(leftPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 250, 708));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dragPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(basePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(dragPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(basePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 708, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        basePanel.getAccessibleContext().setAccessibleName("");

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutButtonActionPerformed
        this.setVisible(false);
        LoginView login = new LoginView();
        this.dispose();
        login.setVisible(true);
        try {
            PREFS.clear();
        } catch (BackingStoreException ex) {
            System.out.println("Error BackingStoreException: " + ex);
        }
    }//GEN-LAST:event_logoutButtonActionPerformed

    private void minimizedIconMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_minimizedIconMouseClicked
        this.setState(DashboardPegawaiView.ICONIFIED);
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
        // TODO add your handling code here:
    }//GEN-LAST:event_basePanelMouseClicked

    private void sortingComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortingComboBoxActionPerformed
        // Mengubah sorting berdasarkan pilihan pada comboBox
        int item = sortingComboBox.getSelectedIndex();
        if (!panelLoadingBuku.isVisible()) {
            switch (item) {
                case 0:
                    loadDataBuku(item);
                    break;
                case 1:
                    loadDataBuku(item);
                    break;
                case 2:
                    loadDataBuku(item);
                    break;
                case 3:
                    loadDataBuku(item);
                    break;
                case 4:
                    loadDataBuku(item);
                    break;
            }
        }
    }//GEN-LAST:event_sortingComboBoxActionPerformed

    private void fieldPencarianKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fieldPencarianKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            buttonPencarianActionPerformed(null);
        }
    }//GEN-LAST:event_fieldPencarianKeyPressed

    private void buttonPencarianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPencarianActionPerformed
        if (!panelLoadingBuku.isVisible()) {
            String keywords = fieldPencarian.getText();
            searchBuku(keywords);
        }
    }//GEN-LAST:event_buttonPencarianActionPerformed

    private void tabbedPanelStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabbedPanelStateChanged
        String title;
        switch (tabbedPanel.getSelectedIndex()) {
            case 0:
                title = "K-SIR Book: Dashboard";
                break;
            case 1:
                title = "K-SIR Book: Data Buku";
                break;
            case 2:
                title = "K-SIR Book: Data Transaksi";
                break;
            default:
                title = "K-SIR Book";
        }
        setTitle(title);
        textTitleBar.setText(title);
    }//GEN-LAST:event_tabbedPanelStateChanged

    private void buttonAddTransaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAddTransaksiActionPerformed
        if (panelInfo.isVisible()) {
            buttonAddTransaksi.setText("BATAL");
            panelInfo.setVisible(false);
            panelDetailTransaksi.setVisible(false);
            panelTambahTransaksi.setVisible(true);
        } else if (panelDetailTransaksi.isVisible()) {
            buttonAddTransaksi.setText("BATAL");
            panelDetailTransaksi.setVisible(false);
            panelInfo.setVisible(false);
            panelTambahTransaksi.setVisible(true);
        } else if (panelTambahTransaksi.isVisible()) {
            buttonAddTransaksi.setText("TAMBAH TRANSAKSI");
            panelTambahTransaksi.setVisible(false);
            panelInfo.setVisible(true);
            panelDetailTransaksi.setVisible(false);
        }
    }//GEN-LAST:event_buttonAddTransaksiActionPerformed

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
            java.util.logging.Logger.getLogger(DashboardPegawaiView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new DashboardPegawaiView().setVisible(true);

        });
    }
    // Get Current Time and Update on Interface every 1 seconds
    private Timer timer;

    private void getCurTime() {
        curTime.setText(String.valueOf(new SimpleDateFormat("HH:mm").format(new Date())));
        ActionListener list = (ActionEvent ae) -> {
            if (DashboardPegawaiView.this.isVisible()) {
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                curTime.setText(String.valueOf(formatter.format(new Date())));
            } else {
                timer.stop();
            }
        };
        timer = new Timer(1000, list);
        timer.start();
    }
    
    private synchronized void setInfoTambahTrx() {
        tf_tanggal.setText(String.valueOf(tanggal.tanggal(new Date())));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel backgroundPanel;
    private javax.swing.JPanel basePanel;
    private javax.swing.JButton btnTambahDataTrx;
    private javax.swing.JButton buttonAddTransaksi;
    private javax.swing.JButton buttonPencarian;
    private javax.swing.JComboBox<String> cb_namaPelanggan;
    private javax.swing.JLabel closeButton;
    private javax.swing.JLabel curTime;
    private javax.swing.JPanel dragPanel;
    private javax.swing.JTextField fieldPencarian;
    private javax.swing.JFrame frameTambahTransaksi;
    private javax.swing.JPanel headerDaftarBuku;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JLabel listBukuText;
    private javax.swing.JButton logoutButton;
    private javax.swing.JLabel minimizedIcon;
    private javax.swing.JLabel namaKasirText;
    private javax.swing.JLabel noTeleponText;
    private javax.swing.JPanel panelDT;
    private javax.swing.JPanel panelDT1;
    private javax.swing.JPanel panelDetailTransaksi;
    private javax.swing.JPanel panelHeadHarga;
    private javax.swing.JPanel panelHeadHarga1;
    private javax.swing.JPanel panelHeadIsbn;
    private javax.swing.JPanel panelHeadIsbn1;
    private javax.swing.JPanel panelHeadNo;
    private javax.swing.JPanel panelHeadNo1;
    private javax.swing.JPanel panelHeadQty;
    private javax.swing.JPanel panelHeadQty1;
    private javax.swing.JPanel panelHeadTotal;
    private javax.swing.JPanel panelHeadTotal1;
    private javax.swing.JPanel panelInfo;
    private javax.swing.JPanel panelListBuku;
    private javax.swing.JPanel panelListTransaksi;
    private javax.swing.JPanel panelLoadingBuku;
    private javax.swing.JPanel panelNoTrx;
    private javax.swing.JPanel panelTableHeader;
    private javax.swing.JPanel panelTableHeaderTambah;
    private javax.swing.JPanel panelTambahTransaksi;
    private javax.swing.JLabel panelTotalTambah;
    private javax.swing.JPanel panelTransaksi;
    private javax.swing.JLabel pencarianText;
    private javax.swing.JScrollPane scrollPanelDT;
    private javax.swing.JScrollPane scrollPanelDTtambah;
    private javax.swing.JScrollPane scrollPanelListBuku;
    private javax.swing.JScrollPane scrollPanelTransaksi;
    private javax.swing.JLabel selamatDatangText;
    private javax.swing.JSeparator sep1;
    private javax.swing.JSeparator sep2;
    private javax.swing.JComboBox<String> sortingComboBox;
    private javax.swing.JLabel sortingText;
    private javax.swing.JPanel tabDaftarBuku;
    private javax.swing.JPanel tabDashboard;
    private javax.swing.JPanel tabPelanggan;
    private javax.swing.JPanel tabTransaksi;
    private javax.swing.JTabbedPane tabbedPanel;
    private javax.swing.JLabel textBayarDT;
    private javax.swing.JLabel textHarga;
    private javax.swing.JLabel textHarga1;
    private javax.swing.JLabel textHeaderAddTrx;
    private javax.swing.JLabel textIDTrx;
    private javax.swing.JLabel textIsbn;
    private javax.swing.JLabel textIsbn1;
    private javax.swing.JLabel textLoadingDataBuku;
    private javax.swing.JLabel textNamaPegawaiDT;
    private javax.swing.JLabel textNamaPelangganAdd;
    private javax.swing.JLabel textNamaPelangganDT;
    private javax.swing.JLabel textNo;
    private javax.swing.JLabel textNo1;
    private javax.swing.JLabel textNoTransaksiAdd;
    private javax.swing.JLabel textNoTransaksiDT;
    private javax.swing.JLabel textNoTransaksiDT1;
    private javax.swing.JLabel textQty;
    private javax.swing.JLabel textQty1;
    private javax.swing.JLabel textTanggalDT;
    private javax.swing.JLabel textTanggalTambah;
    private javax.swing.JLabel textTitleBar;
    private javax.swing.JLabel textTotal;
    private javax.swing.JLabel textTotal1;
    private javax.swing.JLabel textTotalDT;
    private javax.swing.JLabel textTransaksiPembelian;
    private javax.swing.JLabel textUangTunaiAdd;
    private javax.swing.JTextField tf_tanggal;
    private javax.swing.JTextField tf_total;
    private javax.swing.JFormattedTextField tf_uangTunai;
    private javax.swing.JLabel titleText;
    private javax.swing.JButton ubahProfilButton;
    private javax.swing.JLabel versionText;
    // End of variables declaration//GEN-END:variables
}
