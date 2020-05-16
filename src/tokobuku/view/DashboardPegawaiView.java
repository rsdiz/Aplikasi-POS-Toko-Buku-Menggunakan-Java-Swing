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

/**
 *
 * @author Rosyid Iz
 */
public class DashboardPegawaiView extends javax.swing.JFrame {

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
    }

    private synchronized void searchBuku(String keywords) {
        panelListBuku.removeAll();
        try {
            listBuku = buku.searchBuku(keywords);
            totalBuku = listBuku.size();
        } catch (SQLException ex) {
            System.out.println("Error Fetching Data Buku!");
        }
        setListBukuPanel();
    }
    
    private void setListBukuPanel() {
        final int columns = 2;
        // Operator ternary menghitung jumlah baris
        int rows = totalBuku % columns != 0 ? (totalBuku / columns) + 1 : (totalBuku / columns);
        // Menghitung panjang dari panel listBukuPanel
        float height = (rows * 150) + (rows * 5);
        panelListBuku.setLayout(new java.awt.GridLayout(rows, columns, 10, 10));
        if (rows < 4) {
            scrollPanelListBuku.setPreferredSize(new Dimension(1030, (int) height + 5));
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
        try {
            listTransaksi = transaksi.load();
            totalTransaksi = listTransaksi.size();
        } catch (SQLException e) {
            System.out.println("Error Fetching Data Transaksi!");
        }
        setListTransaksiPanel();
    }
    
    private void setListTransaksiPanel() {
        int rows = totalTransaksi;
        float height = (rows * 100);
        panelListTransaksi.setLayout(new GridLayout(rows, 1, 0, 0));
        if (rows < 6) {
            scrollPanelTransaksi.setPreferredSize(new Dimension(410, (int) height + 5));
        }
        panelListTransaksi.setPreferredSize(new Dimension(407, (int) height));
        for (int i = 0; i < totalTransaksi; i++) {
            TransaksiPanelView transaksiPanelView = TransaksiPanelView.getInsance();
            int j = i;
            try {
                transaksiPanelView.setProperty(
                        j,
                        listTransaksi.get(i).getTanggal().toString(),
                        listTransaksi.get(i).getNamaPelanggan());
            } catch (Exception ex) {
                Logger.getLogger(DashboardPegawaiView.class.getName()).log(Level.SEVERE, null, ex);
            }
            transaksiPanelView.apply(panelListTransaksi);
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

        dragPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        minimizedIcon = new javax.swing.JLabel();
        closeButton = new javax.swing.JLabel();
        basePanel = new javax.swing.JPanel();
        tabbedPanel = new javax.swing.JTabbedPane();
        dashboardPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        bukuPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        listBukuText = new javax.swing.JLabel();
        pencarianText = new javax.swing.JLabel();
        fieldPencarian = new javax.swing.JTextField();
        buttonPencarian = new javax.swing.JButton();
        sortingText = new javax.swing.JLabel();
        sep2 = new javax.swing.JSeparator();
        sortingComboBox = new javax.swing.JComboBox<>();
        scrollPanelListBuku = new javax.swing.JScrollPane();
        panelListBuku = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        panelTransaksi = new javax.swing.JPanel();
        scrollPanelTransaksi = new javax.swing.JScrollPane();
        panelListTransaksi = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel6 = new javax.swing.JPanel();
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

        jLabel1.setFont(new java.awt.Font("Metropolis Medium", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("K-SIR Book: Dashboard");
        dragPanel.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 0, 178, 36));

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

        dashboardPanel.setBackground(new java.awt.Color(75, 75, 75));
        dashboardPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Montserrat SemiBold", 0, 32)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(240, 240, 240));
        jLabel2.setText("Tinjauan");
        dashboardPanel.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, -1, -1));

        jPanel4.setPreferredSize(new java.awt.Dimension(250, 250));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        dashboardPanel.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 140, 370, 220));

        tabbedPanel.addTab("  Dashboard  ", dashboardPanel);

        bukuPanel.setBackground(new java.awt.Color(75, 75, 75));
        bukuPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(75, 75, 75));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        listBukuText.setFont(new CustomFont().getFont("bahnschrift", 36));
        listBukuText.setForeground(new java.awt.Color(255, 255, 255));
        listBukuText.setText("Daftar Buku");
        jPanel2.add(listBukuText, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, -1, -1));

        pencarianText.setFont(new CustomFont().getFont("tahoma",18));
        pencarianText.setForeground(new java.awt.Color(255, 255, 255));
        pencarianText.setText("Pencarian:");
        jPanel2.add(pencarianText, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 20, -1, -1));

        fieldPencarian.setBackground(new java.awt.Color(35, 35, 35));
        fieldPencarian.setFont(new CustomFont().getFont("tahoma",14));
        fieldPencarian.setForeground(new java.awt.Color(255, 255, 255));
        fieldPencarian.setText("ISBN / Judul Buku / Pengarang / Penerbit");
        fieldPencarian.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        fieldPencarian.setSelectedTextColor(new java.awt.Color(51, 51, 51));
        fieldPencarian.setSelectionColor(new java.awt.Color(255, 205, 158));
        fieldPencarian.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fieldPencarianKeyPressed(evt);
            }
        });
        jPanel2.add(fieldPencarian, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 50, 190, 40));

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
        jPanel2.add(buttonPencarian, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 50, 60, 40));

        sortingText.setFont(new CustomFont().getFont(18));
        sortingText.setForeground(new java.awt.Color(255, 255, 255));
        sortingText.setText("Urutkan berdasarkan:");
        jPanel2.add(sortingText, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 20, -1, -1));

        sep2.setBackground(new java.awt.Color(255, 194, 149));
        sep2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel2.add(sep2, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 20, 10, 80));

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
        jPanel2.add(sortingComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 50, 170, 40));

        bukuPanel.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1045, 120));

        scrollPanelListBuku.setBackground(new java.awt.Color(75, 75, 75));
        scrollPanelListBuku.setBorder(null);
        scrollPanelListBuku.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPanelListBuku.setViewportBorder(javax.swing.BorderFactory.createEtchedBorder());
        scrollPanelListBuku.setPreferredSize(new java.awt.Dimension(1030, 545));

        panelListBuku.setBackground(new java.awt.Color(75, 75, 75));
        panelListBuku.setMinimumSize(new java.awt.Dimension(1045, 0));
        panelListBuku.setPreferredSize(new java.awt.Dimension(1045, 1045));
        panelListBuku.setLayout(new java.awt.GridLayout(1, 0, 20, 20));
        scrollPanelListBuku.setViewportView(panelListBuku);

        bukuPanel.add(scrollPanelListBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 120, -1, -1));

        tabbedPanel.addTab("  Daftar Buku  ", bukuPanel);

        jPanel1.setBackground(new java.awt.Color(85, 85, 85));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelTransaksi.setBackground(new java.awt.Color(51, 51, 51));
        panelTransaksi.setMaximumSize(new java.awt.Dimension(410, 670));
        panelTransaksi.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        scrollPanelTransaksi.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        scrollPanelTransaksi.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPanelTransaksi.setMinimumSize(new java.awt.Dimension(410, 570));
        scrollPanelTransaksi.setPreferredSize(new java.awt.Dimension(410, 570));

        panelListTransaksi.setLayout(new java.awt.GridLayout());
        scrollPanelTransaksi.setViewportView(panelListTransaksi);

        panelTransaksi.add(scrollPanelTransaksi, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, -1, -1));

        jLabel3.setFont(new CustomFont().getFont("bahnschrift", Font.BOLD, 24));
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Transaksi Pembelian");
        panelTransaksi.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 260, 80));

        jPanel1.add(panelTransaksi, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 410, 670));

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanel6.setBackground(new java.awt.Color(75, 75, 75));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jScrollPane2.setViewportView(jPanel6);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 0, 630, 670));

        tabbedPanel.addTab("  Transaksi  ", jPanel1);

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
    }//GEN-LAST:event_sortingComboBoxActionPerformed

    private void fieldPencarianKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fieldPencarianKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            buttonPencarianActionPerformed(null);
        }
    }//GEN-LAST:event_fieldPencarianKeyPressed

    private void buttonPencarianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPencarianActionPerformed
        String keywords = fieldPencarian.getText();
        searchBuku(keywords);
    }//GEN-LAST:event_buttonPencarianActionPerformed

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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel backgroundPanel;
    private javax.swing.JPanel basePanel;
    private javax.swing.JPanel bukuPanel;
    private javax.swing.JButton buttonPencarian;
    private javax.swing.JLabel closeButton;
    private javax.swing.JLabel curTime;
    private javax.swing.JPanel dashboardPanel;
    private javax.swing.JPanel dragPanel;
    private javax.swing.JTextField fieldPencarian;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JLabel listBukuText;
    private javax.swing.JButton logoutButton;
    private javax.swing.JLabel minimizedIcon;
    private javax.swing.JLabel namaKasirText;
    private javax.swing.JLabel noTeleponText;
    private javax.swing.JPanel panelListBuku;
    private javax.swing.JPanel panelListTransaksi;
    private javax.swing.JPanel panelTransaksi;
    private javax.swing.JLabel pencarianText;
    private javax.swing.JScrollPane scrollPanelListBuku;
    private javax.swing.JScrollPane scrollPanelTransaksi;
    private javax.swing.JLabel selamatDatangText;
    private javax.swing.JSeparator sep1;
    private javax.swing.JSeparator sep2;
    private javax.swing.JComboBox<String> sortingComboBox;
    private javax.swing.JLabel sortingText;
    private javax.swing.JTabbedPane tabbedPanel;
    private javax.swing.JLabel titleText;
    private javax.swing.JButton ubahProfilButton;
    private javax.swing.JLabel versionText;
    // End of variables declaration//GEN-END:variables
}
