package tokobuku.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyAdapter;
import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import java.util.prefs.BackingStoreException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import tokobuku.impl.BukuImpl;
import tokobuku.impl.DetailTransaksiImpl;
import tokobuku.impl.KategoriImpl;
import tokobuku.impl.PegawaiImpl;
import tokobuku.impl.PelangganImpl;
import tokobuku.impl.TransaksiImpl;
import tokobuku.model.Buku;
import tokobuku.model.DetailTransaksi;
import tokobuku.model.Kategori;
import tokobuku.model.Pegawai;
import tokobuku.model.Pelanggan;
import tokobuku.model.Transaksi;
import tokobuku.util.DragWindow;
import tokobuku.util.PreferencedHelper;
import tokobuku.util.CustomFont;
import tokobuku.util.Formatter;
import tokobuku.util.PasswordUtils;

/**
 *
 * @author Rosyid Iz
 */
public class DashboardPegawaiView extends javax.swing.JFrame {

    private static DashboardPegawaiView dashboardPegawaiView;

    private final Formatter<Date> tanggal = new Formatter<>();

    private final BukuImpl buku = new BukuImpl();
    private List<Buku> listBuku = new ArrayList<>();
    private final TransaksiImpl transaksi = new TransaksiImpl();
    private List<Transaksi> listTransaksi = transaksi.listTransaksis;
    private final PelangganImpl pelanggan = new PelangganImpl();
    private List<Pelanggan> listPelanggans = pelanggan.listPelanggans;
    private final PegawaiImpl pegawai = new PegawaiImpl();
    private List<Pegawai> listPegawais = pegawai.listPegawais;
    private final DetailTransaksiImpl detailTrx = new DetailTransaksiImpl();
    private List<DetailTransaksi> listDetailTransaksis = detailTrx.listDetailTransaksis;
    protected final KategoriImpl kategoriImpl = new KategoriImpl();
    private List<Kategori> listKategoris = kategoriImpl.listKategoris;

    private int totalBuku = 0;
    private int totalTransaksi = 0;
    private int totalPelanggan = 0;
    private int totalPegawai = 0;

    private Boolean inputNamaP = false;
    private Boolean inputAlamatP = false;
    private Boolean inputTelpP = false;

    private Boolean inputNamaT = false;
    private Boolean inputUangT = false;

    /**
     * Verifikasi input nomor ponsel
     */
    private InputVerifier ivPhoneNumber = new InputVerifier() {
        @Override
        public boolean verify(JComponent input) {
            JTextField textField = (JTextField) input;
            if (!"Masukkan Nomor Telepon".equals(textField.getText())) {
                Pattern pattern = Pattern.compile("^[\\+]?[(]?\\d{3}[)]?[-\\s\\.]?\\d{3}[-\\s\\.]?\\d{4,6}$");
                Matcher matcher = pattern.matcher(textField.getText());
                if (matcher.matches()) {
                    input.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, Color.GREEN, Color.DARK_GRAY));
                    inputTelpP = true;
                    return true;
                } else {
                    input.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, Color.RED, Color.DARK_GRAY));
                    return false;
                }
            } else {
                input.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
                return true;
            }
        }
    };

    /**
     * Verifikasi input nama dam alamat
     */
    private InputVerifier ivBlank = new InputVerifier() {
        @Override
        public boolean verify(JComponent input) {
            JTextField textField = (JTextField) input;
            if (textField.getText().equals("Masukkan Nama Pelanggan") || textField.getText().equals("Masukkan Alamat")) {
                input.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
                return true;
            } else if (textField.getText().isEmpty()) {
                input.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, Color.RED, Color.DARK_GRAY));
                return false;
            } else {
                switch (input.getName()) {
                    case "tf_nama":
                        inputNamaP = true;
                        break;
                    case "tf_alamat":
                        inputAlamatP = true;
                        break;
                }
                input.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, Color.GREEN, Color.DARK_GRAY));
                return true;
            }
        }
    };

    /**
     * Verifikasi input uang
     */
    private final InputVerifier ivUang = new InputVerifier() {
        @Override
        public boolean verify(JComponent input) {
            JTextField textField = (JTextField) input;
            try {
                int number = Integer.parseInt(textField.getText());
                String tb[] = tf_totalBayar.getText().split(" ");
                tb = tb[1].split(",");
                String tb1 = tb[0].replace(".", "");
                input.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
                if (number < Integer.parseInt(tb1)) {
                    input.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, Color.RED, Color.DARK_GRAY));
                    inputUangT = false;
                    return false;
                } else {
                    inputUangT = number != 0;
                    return true;
                }
            } catch (NumberFormatException e) {
                input.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, Color.RED, Color.DARK_GRAY));
                inputUangT = false;
                return false;
            }
        }
    };

    /**
     * Creates new form DashboardPegawaiView
     */
    private DashboardPegawaiView() {
        initComponents();
        if (!PreferencedHelper.getAkses().equalsIgnoreCase("admin")) {
            sep3.setVisible(false);
            btnTambahBuku.setVisible(false);
            tabbedPanel.remove(4);
        }
        panelTambahBuku.setVisible(false);
        panelLoadingBuku.setVisible(false);
        panelTambahPelanggan.setVisible(false);
        panelDetailTransaksi.setVisible(false);
        panelTambahTransaksi.setVisible(false);
        panelDTPegawai.setVisible(false);
        panelTambahPegawai.setVisible(false);
        labelWarningPwd.setVisible(false);
        labelWarningUname.setVisible(false);
        labelWarningUpwd.setVisible(false);
        panelInfo.setVisible(true);
        getCurTime();
        setDataPegawai();
        DragWindow dragWindow = new DragWindow(DashboardPegawaiView.this);

        Thread threadBuku = new Thread("thread-buku") {
            @Override
            public void run() {
                loadDataBuku(0);
                loadDataKategori();
            }
        };
        threadBuku.start();
        Thread threadPelanggan = new Thread("thread-pelanggan") {
            @Override
            public void run() {
                syncDataPelanggan();
                loadDataPelanggan();
            }
        };
        threadPelanggan.start();
        Thread threadTransaksi = new Thread("thread-transaksi") {
            @Override
            public void run() {
                syncDataTransaksi();
                loadDataTransaksi();
            }
        };
        threadTransaksi.start();
        Thread threadPegawai = new Thread("thread-pegawai") {
            @Override
            public void run() {
                syncDataPegawai();
                loadDataPegawai();
            }
        };
        threadPegawai.start();
    }

    public static DashboardPegawaiView getInstance() {
        if (dashboardPegawaiView == null) {
            dashboardPegawaiView = new DashboardPegawaiView();
        }
        return dashboardPegawaiView;
    }
    
    public static void destroyInstance() {
        if (dashboardPegawaiView != null) {
            dashboardPegawaiView = null;
        }
    }

    protected synchronized void loadDataKategori() {
        try {
            listKategoris.clear();
            kategoriImpl.listKategoris = new ArrayList<>();
            listKategoris = kategoriImpl.load();
            cb_newKategoriBuku.removeAllItems();
            cb_newKategoriBuku.addItem("");
            listKategoris.forEach((listKategori) -> {
                cb_newKategoriBuku.addItem(listKategori.getNama_kategori());
            });
            SwingUtilities.updateComponentTreeUI(tabDaftarBuku);
        } catch (SQLException ex) {
            System.out.println("Error Fetching Data Kategori!");
        }
    }

    /**
     * Load data buku dari database, lalu menyimpan ke List Buku.
     *
     * @param opt - Pilihan untuk sorting buku
     */
    protected synchronized void loadDataBuku(int opt) {
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

    /**
     * Fungsi untuk mencari buku berdasarkan kata kunci
     *
     * @param keywords
     */
    private synchronized void searchBuku(String keywords) {
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

    /**
     * Menampilkan Data List Buku ke panel
     */
    private synchronized void setListBukuPanel() {
        panelListBuku.removeAll();
        final int columns = 2;
        // Operator ternary menghitung jumlah baris
        int rows = totalBuku % columns != 0 ? (totalBuku / columns) + 1 : (totalBuku / columns);
        // Menghitung panjang dari panel panelListBuku
        float height = (rows * 150) + (rows * 5);
        panelListBuku.setLayout(new java.awt.GridLayout(rows, columns, 10, 10));
        if (rows < 4) {
            scrollPanelListBuku.setPreferredSize(new Dimension(1030, (int) height + 5));
            scrollPanelListBuku.getVerticalScrollBar().setUnitIncrement(0);
        } else {
            scrollPanelListBuku.setPreferredSize(new Dimension(1030, 545));
            scrollPanelListBuku.getVerticalScrollBar().setUnitIncrement(20);
        }
        panelListBuku.setPreferredSize(new java.awt.Dimension(1045, (int) height));
        for (int i = 0; i < totalBuku; i++) {
            BookPanelView bookPanelView = new BookPanelView(buku, listBuku.get(i));
            try {
                bookPanelView.setProperty();
            } catch (Exception ex) {
                Logger.getLogger(DashboardPegawaiView.class.getName()).log(Level.SEVERE, null, ex);
            }
            bookPanelView.apply(panelListBuku);
        }
        SwingUtilities.updateComponentTreeUI(tabDaftarBuku);
    }

    /**
     * Load Data Transaksi dari database, kemudian menyimpan ke List Transaksi.
     */
    private synchronized void syncDataTransaksi() {
        try {
            transaksi.listTransaksis = new ArrayList<>();
            listTransaksi = transaksi.load();
        } catch (SQLException e) {
            System.out.println("Error Fetching Data Transaksi!");
        }
    }

    /**
     * Mengambil data transaksi dari list, kemudian ditampilkan ke daftar
     * transaksi
     */
    protected synchronized void loadDataTransaksi() {
        listTransaksi = transaksi.listTransaksis;
        totalTransaksi = listTransaksi.size();
        setListTransaksiPanel();
    }

    /**
     * Menampilkan Data List Transaksi ke panel.
     */
    private synchronized void setListTransaksiPanel() {
        panelListTransaksi.removeAll();
        int rows = totalTransaksi;
        float height = (rows * 100);
        panelListTransaksi.setLayout(new GridLayout(rows, 1, 0, 5));
        if (rows < 5) {
            scrollPanelTransaksi.setPreferredSize(new Dimension(410, (int) height + 10));
            scrollPanelTransaksi.getVerticalScrollBar().setUnitIncrement(1);
        } else {
            scrollPanelTransaksi.setPreferredSize(new Dimension(410, 470));
            scrollPanelTransaksi.getVerticalScrollBar().setUnitIncrement(16);
        }
        panelListTransaksi.setPreferredSize(new Dimension(407, (int) height));

        for (int i = 0, j = i + 1; i < totalTransaksi; i++, j++) {
            TransaksiPanelView transaksiPanelView = new TransaksiPanelView(j);
            transaksiPanelView.setTransaksi(listTransaksi.get(i));
            transaksiPanelView.initJLabel(textNoTransaksiDT, textNamaPelangganDT, textNamaPegawaiDT, textBayarDT, textTotalDT, textTanggalDT);
            transaksiPanelView.initBase(panelDetailTransaksi, scrollPanelDT, panelDT, panelInfo, panelTambahTransaksi, buttonAddTransaksi);
            transaksiPanelView.apply(panelListTransaksi);
        }
        SwingUtilities.updateComponentTreeUI(tabTransaksi);
    }

    /**
     * Load data Pelanggan dari database, kemudian menyimpan data ke list
     * Pelanggan
     */
    private synchronized void syncDataPelanggan() {
        try {
            pelanggan.listPelanggans = new ArrayList<>();
            listPelanggans = pelanggan.load();
        } catch (SQLException ex) {
            System.out.println("Gagal menyinkronkan data pelanggan!\n"
                    + "Error: " + ex.getMessage());
        }
    }

    /**
     * Mengambil data Pelanggan dari list Transaksi, kemudian ditampilkan ke
     * daftar pelanggan
     */
    protected synchronized void loadDataPelanggan() {
        listPelanggans = pelanggan.listPelanggans;
        totalPelanggan = listPelanggans.size();
        setListPelanggan();
    }

    /**
     * Menampilkan data Pelanggan ke Panel
     */
    private synchronized void setListPelanggan() {
        panelListPelanggan.removeAll();
        int rows = totalPelanggan;
        float height = (rows * 45);
        panelListPelanggan.setLayout(new GridLayout(rows, 1, 0, 0));
        if (rows < 14) {
            scrolPanellListPelanggan.setPreferredSize(new Dimension(700, (int) height + 5));
            scrolPanellListPelanggan.getVerticalScrollBar().setUnitIncrement(1);
        } else {
            scrolPanellListPelanggan.setPreferredSize(new Dimension(700, 610));
            scrolPanellListPelanggan.getVerticalScrollBar().setUnitIncrement(16);
        }
        panelListPelanggan.setPreferredSize(new Dimension(700, (int) height));

        for (int i = 0, j = i + 1; i < totalPelanggan; i++, j++) {
            PelangganPanelView pelangganPanelView = new PelangganPanelView(j, pelanggan);
            pelangganPanelView.setPelanggan(listPelanggans.get(i));
            pelangganPanelView.apply(panelListPelanggan);
        }
        setCBpelanggan();
        SwingUtilities.updateComponentTreeUI(tabPelanggan);
    }

    /**
     * Load Data Pegawai dari database, kemudian menyimpan ke List Pegawai
     */
    private synchronized void syncDataPegawai() {
        try {
            pegawai.listPegawais = new ArrayList<>();
            listPegawais = pegawai.load();
        } catch (SQLException ex) {
            System.out.println("Gagal menyinkronkan data pegawai!\n"
                    + "Error: " + ex.getMessage());
        }
    }

    /**
     * Mengambil data Pegawai dari list Pegawai, kemudian ditampilkan ke daftar
     * pegawai
     */
    protected synchronized void loadDataPegawai() {
        listPegawais = pegawai.listPegawais;
        totalPegawai = listPegawais.size();
        setListPegawai();
    }

    /**
     * Menampilkan data Pegawai ke Panel
     */
    private synchronized void setListPegawai() {
        panelListPegawai.removeAll();
        int column = totalPegawai;
        float width = (column * 210);
        if (column < 5) {
            scrollPaneListlPegawai.setPreferredSize(new Dimension((int) width + 10, 210));
            scrollPaneListlPegawai.getHorizontalScrollBar().setUnitIncrement(1);
        } else {
            scrollPaneListlPegawai.setPreferredSize(new Dimension(1030, 210));
            scrollPaneListlPegawai.getHorizontalScrollBar().setUnitIncrement(16);
        }
        panelListPegawai.setLayout(new GridLayout(1, column, 0, 0));
        panelListPegawai.setPreferredSize(new Dimension((int) width, 210));
        for (int i = 0; i < totalPegawai; i++) {
            final int index = i;
            AddPegawaiPanelView p = new AddPegawaiPanelView(index, pegawai, listPegawais.get(i), panelDTPegawai);
            p.init(tf_namaPegawai,
                    tf_alamatPegawai,
                    tf_notelpPegawai,
                    tf_unamePegawai,
                    labelIDPegawai,
                    rb_kasir, rb_admin,
                    rb_pagi, rb_sore,
                    btnEditPegawai,
                    btnHapusPegawai,
                    btnEditPassword);
            panelListPegawai.add(p.pegawaiPanelV);
        }
        SwingUtilities.updateComponentTreeUI(tabPegawai);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGroupShif = new javax.swing.ButtonGroup();
        btnGroupAkses = new javax.swing.ButtonGroup();
        btnGroupNewShif = new javax.swing.ButtonGroup();
        btnGroupNewAkses = new javax.swing.ButtonGroup();
        dragPanel = new javax.swing.JPanel();
        textTitleBar = new javax.swing.JLabel();
        minimizedIcon = new javax.swing.JLabel();
        closeButton = new javax.swing.JLabel();
        basePanel = new javax.swing.JPanel();
        tabbedPanel = new javax.swing.JTabbedPane();
        tabDashboard = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        tabDaftarBuku = new javax.swing.JPanel();
        panelTambahBuku = new javax.swing.JPanel();
        tf_newJudulBuku = new javax.swing.JTextField();
        labelWarningIsbn = new javax.swing.JLabel();
        ftf_newIsbn = new javax.swing.JFormattedTextField();
        labelWarningJudulBuku = new javax.swing.JLabel();
        tf_newPenulisBuku = new javax.swing.JTextField();
        labelWarningPenulis = new javax.swing.JLabel();
        tf_newPenerbitBuku = new javax.swing.JTextField();
        labelWarningPenerbitBuku = new javax.swing.JLabel();
        labelInformasiBuku = new javax.swing.JLabel();
        cb_newKategoriBuku = new javax.swing.JComboBox<>();
        labelKategoriBuku = new javax.swing.JLabel();
        btnMoreKategori = new javax.swing.JButton();
        ftf_newTahunBuku = new javax.swing.JTextField();
        labelWarningTahun = new javax.swing.JLabel();
        panelCoverBuku = new javax.swing.JPanel();
        newCoverBuku = new javax.swing.JLabel();
        btnDeleteCoverBuku = new javax.swing.JButton();
        btnSelectCoverBuku = new javax.swing.JButton();
        ftf_newHargaPokok = new javax.swing.JFormattedTextField();
        labelWarningHargaPokok = new javax.swing.JLabel();
        ftf_newHargaJual = new javax.swing.JFormattedTextField();
        labelWarningHargaJual = new javax.swing.JLabel();
        sep5 = new javax.swing.JSeparator();
        btnTambahkanBuku = new javax.swing.JButton();
        btnResetNewBuku = new javax.swing.JButton();
        sep4 = new javax.swing.JSeparator();
        labelWarningKategori = new javax.swing.JLabel();
        headerDaftarBuku = new javax.swing.JPanel();
        sep3 = new javax.swing.JSeparator();
        listBukuText = new javax.swing.JLabel();
        pencarianText = new javax.swing.JLabel();
        fieldPencarian = new javax.swing.JTextField();
        buttonPencarian = new javax.swing.JButton();
        sortingText = new javax.swing.JLabel();
        sep2 = new javax.swing.JSeparator();
        sortingComboBox = new javax.swing.JComboBox<>();
        btnTambahBuku = new javax.swing.JButton();
        scrollPanelListBuku = new javax.swing.JScrollPane();
        panelListBuku = new javax.swing.JPanel();
        panelLoadingBuku = new javax.swing.JPanel();
        textLoadingDataBuku = new javax.swing.JLabel();
        tabTransaksi = new javax.swing.JPanel();
        panelTransaksi = new javax.swing.JPanel();
        scrollPanelTransaksi = new javax.swing.JScrollPane();
        panelListTransaksi = new javax.swing.JPanel();
        textTransaksiPembelian = new javax.swing.JLabel();
        buttonAddTransaksi = new javax.swing.JButton();
        panelTambahTransaksi = new javax.swing.JPanel();
        scrollPanelDTtambah = new javax.swing.JScrollPane();
        panelDTtambah = new javax.swing.JPanel();
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
        textUangTunaiAdd = new javax.swing.JLabel();
        cb_namaPelanggan = new javax.swing.JComboBox<>();
        textNamaPelangganAdd = new javax.swing.JLabel();
        buttonTambahDataTrx = new javax.swing.JButton();
        tf_tanggal = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        panelTotalTambah = new javax.swing.JLabel();
        tf_totalBayar = new javax.swing.JTextField();
        textTanggalTambah = new javax.swing.JLabel();
        panelNoTrx = new javax.swing.JPanel();
        textIDTrx = new javax.swing.JLabel();
        tf_uangTunai = new javax.swing.JTextField();
        textNoTransaksiAdd = new javax.swing.JLabel();
        buttonReset = new javax.swing.JButton();
        buttonAddItem = new javax.swing.JButton();
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
        panelTambahPelanggan = new javax.swing.JPanel();
        textInputNama = new javax.swing.JLabel();
        tf_newNamaPelanggan = new javax.swing.JTextField();
        textInputAlamat = new javax.swing.JLabel();
        tf_newAlamatP = new javax.swing.JTextField();
        textInputTelp = new javax.swing.JLabel();
        tf_newNoTelp = new javax.swing.JTextField();
        buttonTambahkanPelanggan = new javax.swing.JButton();
        scrolPanellListPelanggan = new javax.swing.JScrollPane();
        panelListPelanggan = new javax.swing.JPanel();
        listBukuText1 = new javax.swing.JLabel();
        panelHeaderPelanggan = new javax.swing.JPanel();
        headNomor = new javax.swing.JTextField();
        headNama = new javax.swing.JTextField();
        headAlamat = new javax.swing.JTextField();
        headNoTelp = new javax.swing.JTextField();
        headEdit = new javax.swing.JTextField();
        headHapus = new javax.swing.JTextField();
        buttonTambahPelanggan = new javax.swing.JButton();
        tabPegawai = new javax.swing.JPanel();
        listPegawaiText = new javax.swing.JLabel();
        panelTambahPegawai = new javax.swing.JPanel();
        tf_newAlamatPegawai = new javax.swing.JTextField();
        tf_newNamaPegawai = new javax.swing.JTextField();
        tf_newNotelpPegawai = new javax.swing.JTextField();
        tf_newUnamePegawai = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        panelNewShif = new javax.swing.JPanel();
        rb_newPagi = new javax.swing.JRadioButton();
        rb_newSore = new javax.swing.JRadioButton();
        jTextField4 = new javax.swing.JTextField();
        panelNewAkses = new javax.swing.JPanel();
        rb_newAdmin = new javax.swing.JRadioButton();
        rb_newKasir = new javax.swing.JRadioButton();
        labelInformasiAkun = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        labelPengaturanAkun = new javax.swing.JLabel();
        jpf_newUpass = new javax.swing.JPasswordField();
        jpf_newPass = new javax.swing.JPasswordField();
        btnShowUpwd = new javax.swing.JButton();
        btnShowPwd = new javax.swing.JButton();
        btnResetNewPegawai = new javax.swing.JButton();
        panelTools = new javax.swing.JPanel();
        tf_generatePassword = new javax.swing.JTextField();
        btnGeneratePass = new javax.swing.JButton();
        btnCopyGeneratePass = new javax.swing.JButton();
        labelToolAkun = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        btnTambahkanPegawai = new javax.swing.JButton();
        labelWarningUN = new javax.swing.JLabel();
        labelWarningPW = new javax.swing.JLabel();
        labelWarningUPW = new javax.swing.JLabel();
        newNomorPegawai = new javax.swing.JLabel();
        labelWarningNama = new javax.swing.JLabel();
        labelWarningUpwd = new javax.swing.JLabel();
        labelWarningUname = new javax.swing.JLabel();
        labelWarningPwd = new javax.swing.JLabel();
        labelWarningAkses = new javax.swing.JLabel();
        labelWarningShif = new javax.swing.JLabel();
        labelWarningNoTelp = new javax.swing.JLabel();
        labelWarningAlamat = new javax.swing.JLabel();
        panelEditPegawai = new javax.swing.JPanel();
        scrollPaneListlPegawai = new javax.swing.JScrollPane();
        panelListPegawai = new javax.swing.JPanel();
        panelDTPegawai = new javax.swing.JPanel();
        listBukuText3 = new javax.swing.JLabel();
        tf_alamatPegawai = new javax.swing.JTextField();
        tf_notelpPegawai = new javax.swing.JTextField();
        jTextField1 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        rb_pagi = new javax.swing.JRadioButton();
        rb_sore = new javax.swing.JRadioButton();
        tf_namaPegawai = new javax.swing.JTextField();
        tf_unamePegawai = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        rb_admin = new javax.swing.JRadioButton();
        rb_kasir = new javax.swing.JRadioButton();
        btnHapusPegawai = new javax.swing.JButton();
        btnEditPassword = new javax.swing.JButton();
        btnEditPegawai = new javax.swing.JButton();
        labelIDPegawai = new javax.swing.JLabel();
        btnTambahPegawai = new javax.swing.JButton();
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
        setMaximumSize(new java.awt.Dimension(1920, 1080));
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

        tabbedPanel.addTab("  Dashboard  ", tabDashboard);

        tabDaftarBuku.setBackground(new java.awt.Color(75, 75, 75));
        tabDaftarBuku.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelTambahBuku.setBackground(new java.awt.Color(51, 51, 51));
        panelTambahBuku.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tf_newJudulBuku.setBackground(new java.awt.Color(51, 51, 51));
        tf_newJudulBuku.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        tf_newJudulBuku.setForeground(new java.awt.Color(255, 255, 255));
        tf_newJudulBuku.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Judul Buku", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Bahnschrift", 0, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        tf_newJudulBuku.setCaretColor(new java.awt.Color(255, 255, 102));
        tf_newJudulBuku.setDisabledTextColor(new java.awt.Color(200, 200, 200));
        tf_newJudulBuku.setMargin(new java.awt.Insets(2, 6, 2, 6));
        tf_newJudulBuku.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tf_newJudulBukuKeyReleased(evt);
            }
        });
        panelTambahBuku.add(tf_newJudulBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 600, 60));

        labelWarningIsbn.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelWarningIsbn.setForeground(new java.awt.Color(255, 153, 153));
        labelWarningIsbn.setText("*) Belum diisi!");
        labelWarningIsbn.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        panelTambahBuku.add(labelWarningIsbn, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 280, 280, 30));
        labelWarningNama.setVisible(false);

        ftf_newIsbn.setBackground(new java.awt.Color(51, 51, 51));
        ftf_newIsbn.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "ISBN", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Bahnschrift", 0, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        ftf_newIsbn.setForeground(new java.awt.Color(255, 255, 255));
        try {
            ftf_newIsbn.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###-###-####-##-#")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        ftf_newIsbn.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ftf_newIsbn.setCaretColor(new java.awt.Color(255, 255, 102));
        ftf_newIsbn.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        ftf_newIsbn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ftf_newIsbnKeyReleased(evt);
            }
        });
        panelTambahBuku.add(ftf_newIsbn, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 280, 60));

        labelWarningJudulBuku.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelWarningJudulBuku.setForeground(new java.awt.Color(255, 153, 153));
        labelWarningJudulBuku.setText("*) Belum diisi!");
        labelWarningJudulBuku.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        panelTambahBuku.add(labelWarningJudulBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 170, 280, 30));
        labelWarningNama.setVisible(false);

        tf_newPenulisBuku.setBackground(new java.awt.Color(51, 51, 51));
        tf_newPenulisBuku.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        tf_newPenulisBuku.setForeground(new java.awt.Color(255, 255, 255));
        tf_newPenulisBuku.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Penulis", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Bahnschrift", 0, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        tf_newPenulisBuku.setCaretColor(new java.awt.Color(255, 255, 102));
        tf_newPenulisBuku.setDisabledTextColor(new java.awt.Color(200, 200, 200));
        tf_newPenulisBuku.setMargin(new java.awt.Insets(2, 6, 2, 6));
        tf_newPenulisBuku.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tf_newPenulisBukuKeyReleased(evt);
            }
        });
        panelTambahBuku.add(tf_newPenulisBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 320, 280, 60));

        labelWarningPenulis.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelWarningPenulis.setForeground(new java.awt.Color(255, 153, 153));
        labelWarningPenulis.setText("*) Belum diisi!");
        labelWarningPenulis.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        panelTambahBuku.add(labelWarningPenulis, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 390, 280, 30));
        labelWarningNama.setVisible(false);

        tf_newPenerbitBuku.setBackground(new java.awt.Color(51, 51, 51));
        tf_newPenerbitBuku.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        tf_newPenerbitBuku.setForeground(new java.awt.Color(255, 255, 255));
        tf_newPenerbitBuku.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Penerbit", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Bahnschrift", 0, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        tf_newPenerbitBuku.setCaretColor(new java.awt.Color(255, 255, 102));
        tf_newPenerbitBuku.setDisabledTextColor(new java.awt.Color(200, 200, 200));
        tf_newPenerbitBuku.setMargin(new java.awt.Insets(2, 6, 2, 6));
        tf_newPenerbitBuku.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tf_newPenerbitBukuKeyReleased(evt);
            }
        });
        panelTambahBuku.add(tf_newPenerbitBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 430, 280, 60));

        labelWarningPenerbitBuku.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelWarningPenerbitBuku.setForeground(new java.awt.Color(255, 153, 153));
        labelWarningPenerbitBuku.setText("*) Belum diisi!");
        labelWarningPenerbitBuku.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        panelTambahBuku.add(labelWarningPenerbitBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 500, 280, 30));
        labelWarningNama.setVisible(false);

        labelInformasiBuku.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        labelInformasiBuku.setForeground(new java.awt.Color(255, 255, 255));
        labelInformasiBuku.setText("Informasi Buku");
        panelTambahBuku.add(labelInformasiBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        cb_newKategoriBuku.setBackground(new java.awt.Color(51, 51, 51));
        cb_newKategoriBuku.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        cb_newKategoriBuku.setForeground(new java.awt.Color(255, 255, 255));
        cb_newKategoriBuku.setEditor(null);
        cb_newKategoriBuku.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_newKategoriBukuActionPerformed(evt);
            }
        });
        panelTambahBuku.add(cb_newKategoriBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 217, 220, 50));

        labelKategoriBuku.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        labelKategoriBuku.setForeground(new java.awt.Color(255, 255, 255));
        labelKategoriBuku.setText("Kategori :");
        panelTambahBuku.add(labelKategoriBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 190, 70, 20));

        btnMoreKategori.setBackground(new java.awt.Color(102, 102, 102));
        btnMoreKategori.setForeground(new java.awt.Color(255, 255, 255));
        btnMoreKategori.setText("...");
        btnMoreKategori.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnMoreKategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoreKategoriActionPerformed(evt);
            }
        });
        panelTambahBuku.add(btnMoreKategori, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 217, 50, 50));

        ftf_newTahunBuku.setBackground(new java.awt.Color(51, 51, 51));
        ftf_newTahunBuku.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        ftf_newTahunBuku.setForeground(new java.awt.Color(255, 255, 255));
        ftf_newTahunBuku.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tahun", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Bahnschrift", 0, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        ftf_newTahunBuku.setCaretColor(new java.awt.Color(255, 255, 102));
        ftf_newTahunBuku.setDisabledTextColor(new java.awt.Color(200, 200, 200));
        ftf_newTahunBuku.setMargin(new java.awt.Insets(2, 6, 2, 6));
        ftf_newTahunBuku.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ftf_newTahunBukuKeyReleased(evt);
            }
        });
        panelTambahBuku.add(ftf_newTahunBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 320, 280, 60));

        labelWarningTahun.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelWarningTahun.setForeground(new java.awt.Color(255, 153, 153));
        labelWarningTahun.setText("*) Belum diisi!");
        labelWarningTahun.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        panelTambahBuku.add(labelWarningTahun, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 390, 280, 30));
        labelWarningNama.setVisible(false);

        panelCoverBuku.setBackground(new java.awt.Color(60, 60, 60));
        panelCoverBuku.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelCoverBuku.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        newCoverBuku.setBackground(new java.awt.Color(51, 51, 51));
        newCoverBuku.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ImageIcon ii = new ImageIcon(getClass().getResource("/tokobuku/images/book.png"));
        Image image = ii.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH);
        newCoverBuku.setIcon(new javax.swing.ImageIcon(image));
        newCoverBuku.setPreferredSize(new java.awt.Dimension(140, 140));
        panelCoverBuku.add(newCoverBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        btnDeleteCoverBuku.setBackground(new java.awt.Color(150, 150, 150));
        btnDeleteCoverBuku.setText("Hapus Gambar");
        btnDeleteCoverBuku.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnDeleteCoverBuku.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteCoverBukuActionPerformed(evt);
            }
        });
        panelCoverBuku.add(btnDeleteCoverBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 110, 140, 50));

        btnSelectCoverBuku.setBackground(new java.awt.Color(150, 150, 150));
        btnSelectCoverBuku.setText("Pilih Gambar Cover...");
        btnSelectCoverBuku.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnSelectCoverBuku.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectCoverBukuActionPerformed(evt);
            }
        });
        panelCoverBuku.add(btnSelectCoverBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 20, 140, 50));

        panelTambahBuku.add(panelCoverBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 100, 340, 180));

        ftf_newHargaPokok.setBackground(new java.awt.Color(51, 51, 51));
        ftf_newHargaPokok.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Harga Pokok", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Bahnschrift", 0, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        ftf_newHargaPokok.setForeground(new java.awt.Color(255, 255, 255));
        ftf_newHargaPokok.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0"))));
        ftf_newHargaPokok.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ftf_newHargaPokok.setCaretColor(new java.awt.Color(255, 255, 102));
        ftf_newHargaPokok.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        ftf_newHargaPokok.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ftf_newHargaPokokKeyReleased(evt);
            }
        });
        panelTambahBuku.add(ftf_newHargaPokok, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 320, 160, 60));

        labelWarningHargaPokok.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelWarningHargaPokok.setForeground(new java.awt.Color(255, 153, 153));
        labelWarningHargaPokok.setText("*) Belum diisi!");
        labelWarningHargaPokok.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        panelTambahBuku.add(labelWarningHargaPokok, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 390, 160, 30));
        labelWarningNama.setVisible(false);

        ftf_newHargaJual.setBackground(new java.awt.Color(51, 51, 51));
        ftf_newHargaJual.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Harga Jual", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Bahnschrift", 0, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        ftf_newHargaJual.setForeground(new java.awt.Color(255, 255, 255));
        ftf_newHargaJual.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0"))));
        ftf_newHargaJual.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ftf_newHargaJual.setCaretColor(new java.awt.Color(255, 255, 102));
        ftf_newHargaJual.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        ftf_newHargaJual.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ftf_newHargaJualKeyReleased(evt);
            }
        });
        panelTambahBuku.add(ftf_newHargaJual, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 320, 160, 60));

        labelWarningHargaJual.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelWarningHargaJual.setForeground(new java.awt.Color(255, 153, 153));
        labelWarningHargaJual.setText("*) Belum diisi!");
        labelWarningHargaJual.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        panelTambahBuku.add(labelWarningHargaJual, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 390, 160, 30));
        labelWarningNama.setVisible(false);
        panelTambahBuku.add(sep5, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 440, 340, 10));

        btnTambahkanBuku.setBackground(new java.awt.Color(51, 255, 51));
        btnTambahkanBuku.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        btnTambahkanBuku.setText("TAMBAHKAN");
        btnTambahkanBuku.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahkanBukuActionPerformed(evt);
            }
        });
        panelTambahBuku.add(btnTambahkanBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 470, 160, 50));

        btnResetNewBuku.setBackground(new java.awt.Color(55, 55, 55));
        btnResetNewBuku.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        btnResetNewBuku.setForeground(new java.awt.Color(255, 153, 153));
        btnResetNewBuku.setText("ATUR ULANG");
        btnResetNewBuku.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetNewBukuActionPerformed(evt);
            }
        });
        panelTambahBuku.add(btnResetNewBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 470, 160, 50));
        panelTambahBuku.add(sep4, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 50, 820, 10));

        labelWarningKategori.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelWarningKategori.setForeground(new java.awt.Color(255, 153, 153));
        labelWarningKategori.setText("*) Belum diipilih!");
        labelWarningKategori.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        panelTambahBuku.add(labelWarningKategori, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 280, 280, 30));
        labelWarningNama.setVisible(false);

        tabDaftarBuku.add(panelTambahBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 115, 1040, 560));

        headerDaftarBuku.setBackground(new java.awt.Color(75, 75, 75));
        headerDaftarBuku.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        sep3.setBackground(new java.awt.Color(255, 194, 149));
        sep3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        headerDaftarBuku.add(sep3, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 20, 10, 80));

        listBukuText.setFont(new CustomFont().getFont("bahnschrift", 36));
        listBukuText.setForeground(new java.awt.Color(255, 255, 255));
        listBukuText.setText("Daftar Buku");
        headerDaftarBuku.add(listBukuText, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, -1, -1));

        pencarianText.setFont(new CustomFont().getFont("tahoma",18));
        pencarianText.setForeground(new java.awt.Color(255, 255, 255));
        pencarianText.setText("Pencarian:");
        headerDaftarBuku.add(pencarianText, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 20, -1, -1));

        fieldPencarian.setBackground(new java.awt.Color(35, 35, 35));
        fieldPencarian.setFont(new CustomFont().getFont("tahoma",14));
        fieldPencarian.setForeground(new java.awt.Color(255, 255, 255));
        fieldPencarian.setText("ISBN / Judul Buku / Penulis / Penerbit");
        fieldPencarian.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        fieldPencarian.setCaretColor(new java.awt.Color(255, 255, 0));
        fieldPencarian.setSelectedTextColor(new java.awt.Color(51, 51, 51));
        fieldPencarian.setSelectionColor(new java.awt.Color(255, 205, 158));
        fieldPencarian.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fieldPencarianKeyPressed(evt);
            }
        });
        headerDaftarBuku.add(fieldPencarian, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 60, 190, 40));

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
        headerDaftarBuku.add(buttonPencarian, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 60, 60, 40));

        sortingText.setFont(new CustomFont().getFont(18));
        sortingText.setForeground(new java.awt.Color(255, 255, 255));
        sortingText.setText("Urutkan berdasarkan:");
        headerDaftarBuku.add(sortingText, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 20, -1, -1));

        sep2.setBackground(new java.awt.Color(255, 194, 149));
        sep2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        headerDaftarBuku.add(sep2, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 20, 10, 80));

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
        headerDaftarBuku.add(sortingComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 60, 170, 40));

        btnTambahBuku.setBackground(new java.awt.Color(0, 255, 0));
        btnTambahBuku.setText("Tambah Buku");
        btnTambahBuku.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnTambahBuku.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahBukuActionPerformed(evt);
            }
        });
        headerDaftarBuku.add(btnTambahBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 60, 110, 40));

        tabDaftarBuku.add(headerDaftarBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1045, 120));

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

        panelLoadingBuku.setBackground(new java.awt.Color(51, 51, 51));
        panelLoadingBuku.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        textLoadingDataBuku.setFont(new CustomFont().getFont("bahnschrift", 1, 36));
        textLoadingDataBuku.setForeground(new java.awt.Color(255, 255, 255));
        textLoadingDataBuku.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textLoadingDataBuku.setText("Mengambil Data Buku...");
        panelLoadingBuku.add(textLoadingDataBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 1010, 70));

        tabDaftarBuku.add(panelLoadingBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 115, 1040, 560));

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
        scrollPanelDTtambah.setPreferredSize(new java.awt.Dimension(610, 340));

        panelDTtambah.setBackground(new java.awt.Color(40, 40, 40));
        panelDTtambah.setPreferredSize(new java.awt.Dimension(610, 310));
        panelDTtambah.setLayout(new java.awt.GridLayout(1, 0));
        scrollPanelDTtambah.setViewportView(panelDTtambah);

        panelTambahTransaksi.add(scrollPanelDTtambah, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, -1, -1));

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

        textUangTunaiAdd.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        textUangTunaiAdd.setForeground(new java.awt.Color(255, 255, 150));
        textUangTunaiAdd.setText("Uang Tunai:");
        panelTambahTransaksi.add(textUangTunaiAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 110, -1, 40));

        cb_namaPelanggan.setBackground(new java.awt.Color(97, 97, 97));
        cb_namaPelanggan.setEditable(true);
        cb_namaPelanggan.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        cb_namaPelanggan.setForeground(new java.awt.Color(255, 255, 255));
        cb_namaPelanggan.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        cb_namaPelanggan.setNextFocusableComponent(tf_uangTunai);
        cb_namaPelanggan.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_namaPelangganItemStateChanged(evt);
            }
        });
        panelTambahTransaksi.add(cb_namaPelanggan, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 50, 290, 40));

        textNamaPelangganAdd.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        textNamaPelangganAdd.setForeground(new java.awt.Color(255, 255, 150));
        textNamaPelangganAdd.setText("Nama Pelanggan:");
        panelTambahTransaksi.add(textNamaPelangganAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 10, -1, 40));

        buttonTambahDataTrx.setBackground(new java.awt.Color(84, 198, 47));
        buttonTambahDataTrx.setFont(new java.awt.Font("Bebas", 1, 18)); // NOI18N
        buttonTambahDataTrx.setForeground(new java.awt.Color(30, 30, 30));
        buttonTambahDataTrx.setText("<html><center>TAMBAH<br>TRANSAKSI</center></html>");
        buttonTambahDataTrx.setMargin(new java.awt.Insets(2, 2, 2, 2));
        buttonTambahDataTrx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonTambahDataTrxActionPerformed(evt);
            }
        });
        panelTambahTransaksi.add(buttonTambahDataTrx, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 580, 150, 80));

        tf_tanggal.setEditable(false);
        tf_tanggal.setBackground(new java.awt.Color(80, 80, 80));
        tf_tanggal.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        tf_tanggal.setForeground(new java.awt.Color(200, 200, 200));
        tf_tanggal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tf_tanggal.setText("24/05/2000");
        tf_tanggal.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tf_tanggal.setDisabledTextColor(new java.awt.Color(200, 200, 200));
        tf_tanggal.setMargin(new java.awt.Insets(2, 14, 2, 14));
        panelTambahTransaksi.add(tf_tanggal, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 610, 170, 50));

        jSeparator3.setBackground(new java.awt.Color(80, 80, 80));
        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        panelTambahTransaksi.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 580, 10, 80));

        panelTotalTambah.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        panelTotalTambah.setForeground(new java.awt.Color(255, 255, 255));
        panelTotalTambah.setText("Total:");
        panelTambahTransaksi.add(panelTotalTambah, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 570, 250, 40));

        tf_totalBayar.setEditable(false);
        tf_totalBayar.setBackground(new java.awt.Color(80, 80, 80));
        tf_totalBayar.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        tf_totalBayar.setForeground(new java.awt.Color(200, 200, 200));
        tf_totalBayar.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tf_totalBayar.setText("Rp. 0,-");
        tf_totalBayar.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tf_totalBayar.setDisabledTextColor(new java.awt.Color(200, 200, 200));
        panelTambahTransaksi.add(tf_totalBayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 610, 250, 50));

        textTanggalTambah.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        textTanggalTambah.setForeground(new java.awt.Color(255, 255, 255));
        textTanggalTambah.setText("Tanggal:");
        panelTambahTransaksi.add(textTanggalTambah, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 570, 170, 40));

        panelNoTrx.setBackground(new java.awt.Color(77, 77, 77));
        panelNoTrx.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelNoTrx.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        textIDTrx.setFont(new java.awt.Font("Bebas", 0, 32)); // NOI18N
        textIDTrx.setForeground(new java.awt.Color(255, 255, 255));
        textIDTrx.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textIDTrx.setText("NO");
        panelNoTrx.add(textIDTrx, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 120, 110));

        panelTambahTransaksi.add(panelNoTrx, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 120, 110));

        tf_uangTunai.setBackground(new java.awt.Color(80, 80, 80));
        tf_uangTunai.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        tf_uangTunai.setForeground(new java.awt.Color(255, 255, 255));
        tf_uangTunai.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tf_uangTunai.setText("0");
        tf_uangTunai.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        tf_uangTunai.setCaretColor(new java.awt.Color(255, 255, 150));
        tf_uangTunai.setDisabledTextColor(new java.awt.Color(200, 200, 200));
        tf_uangTunai.setInputVerifier(ivUang);
        tf_uangTunai.setNextFocusableComponent(buttonAddItem);
        panelTambahTransaksi.add(tf_uangTunai, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 110, 190, 40));

        textNoTransaksiAdd.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        textNoTransaksiAdd.setForeground(new java.awt.Color(255, 255, 255));
        textNoTransaksiAdd.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textNoTransaksiAdd.setText("No Transaksi");
        panelTambahTransaksi.add(textNoTransaksiAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 120, 30));

        buttonReset.setBackground(new java.awt.Color(104, 55, 55));
        buttonReset.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        buttonReset.setForeground(new java.awt.Color(255, 255, 255));
        buttonReset.setText("Hapus Semua");
        buttonReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonResetActionPerformed(evt);
            }
        });
        panelTambahTransaksi.add(buttonReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 50, 150, 40));

        buttonAddItem.setBackground(new java.awt.Color(80, 80, 80));
        buttonAddItem.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        buttonAddItem.setForeground(new java.awt.Color(255, 255, 255));
        buttonAddItem.setText("Tambah Item");
        buttonAddItem.setNextFocusableComponent(buttonTambahDataTrx);
        buttonAddItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                buttonAddItemMouseExited(evt);
            }
        });
        buttonAddItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAddItemActionPerformed(evt);
            }
        });
        panelTambahTransaksi.add(buttonAddItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 110, 150, 40));

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

        tabPelanggan.setBackground(new java.awt.Color(75, 75, 75));
        tabPelanggan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelTambahPelanggan.setBackground(new java.awt.Color(73, 73, 73));
        panelTambahPelanggan.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelTambahPelanggan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        textInputNama.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        textInputNama.setForeground(new java.awt.Color(200, 200, 200));
        textInputNama.setText("Nama:");
        panelTambahPelanggan.add(textInputNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 280, 30));

        tf_newNamaPelanggan.setBackground(new java.awt.Color(70, 70, 70));
        tf_newNamaPelanggan.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        tf_newNamaPelanggan.setForeground(new java.awt.Color(255, 255, 255));
        tf_newNamaPelanggan.setText("Masukkan Nama Pelanggan");
        tf_newNamaPelanggan.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        tf_newNamaPelanggan.setInputVerifier(ivBlank);
        tf_newNamaPelanggan.setName("tf_nama"); // NOI18N
        tf_newNamaPelanggan.setNextFocusableComponent(tf_newAlamatP);
        tf_newNamaPelanggan.setPreferredSize(new java.awt.Dimension(280, 40));
        panelTambahPelanggan.add(tf_newNamaPelanggan, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));

        textInputAlamat.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        textInputAlamat.setForeground(new java.awt.Color(200, 200, 200));
        textInputAlamat.setText("Alamat:");
        panelTambahPelanggan.add(textInputAlamat, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 280, 30));

        tf_newAlamatP.setBackground(new java.awt.Color(70, 70, 70));
        tf_newAlamatP.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        tf_newAlamatP.setForeground(new java.awt.Color(255, 255, 255));
        tf_newAlamatP.setText("Masukkan Alamat");
        tf_newAlamatP.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        tf_newAlamatP.setInputVerifier(ivBlank);
        tf_newAlamatP.setName("tf_alamat"); // NOI18N
        tf_newAlamatP.setNextFocusableComponent(tf_newNoTelp);
        tf_newAlamatP.setPreferredSize(new java.awt.Dimension(280, 40));
        panelTambahPelanggan.add(tf_newAlamatP, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, -1, -1));

        textInputTelp.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        textInputTelp.setForeground(new java.awt.Color(200, 200, 200));
        textInputTelp.setText("Nomor Telepon:");
        panelTambahPelanggan.add(textInputTelp, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 280, 30));

        tf_newNoTelp.setBackground(new java.awt.Color(70, 70, 70));
        tf_newNoTelp.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        tf_newNoTelp.setForeground(new java.awt.Color(255, 255, 255));
        tf_newNoTelp.setText("Masukkan Nomor Telepon");
        tf_newNoTelp.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        tf_newNoTelp.setInputVerifier(ivPhoneNumber);
        tf_newNoTelp.setName("tf_telp"); // NOI18N
        tf_newNoTelp.setNextFocusableComponent(buttonTambahkanPelanggan);
        tf_newNoTelp.setPreferredSize(new java.awt.Dimension(280, 40));
        panelTambahPelanggan.add(tf_newNoTelp, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, -1, -1));

        buttonTambahkanPelanggan.setBackground(new java.awt.Color(51, 180, 51));
        buttonTambahkanPelanggan.setFont(new java.awt.Font("Bahnschrift", 1, 14)); // NOI18N
        buttonTambahkanPelanggan.setForeground(new java.awt.Color(255, 255, 255));
        buttonTambahkanPelanggan.setText("TAMBAHKAN");
        buttonTambahkanPelanggan.setMultiClickThreshhold(1000L);
        buttonTambahkanPelanggan.setNextFocusableComponent(buttonTambahPelanggan);
        buttonTambahkanPelanggan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonTambahkanPelangganActionPerformed(evt);
            }
        });
        panelTambahPelanggan.add(buttonTambahkanPelanggan, new org.netbeans.lib.awtextra.AbsoluteConstraints(63, 323, 190, 50));

        tabPelanggan.add(panelTambahPelanggan, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 195, 325, 410));

        scrolPanellListPelanggan.setBackground(new java.awt.Color(60, 60, 60));
        scrolPanellListPelanggan.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        scrolPanellListPelanggan.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrolPanellListPelanggan.setPreferredSize(new java.awt.Dimension(700, 610));

        panelListPelanggan.setBackground(new java.awt.Color(70, 70, 70));
        panelListPelanggan.setPreferredSize(new java.awt.Dimension(565, 605));
        panelListPelanggan.setLayout(new java.awt.GridLayout(1, 0));
        scrolPanellListPelanggan.setViewportView(panelListPelanggan);

        tabPelanggan.add(scrolPanellListPelanggan, new org.netbeans.lib.awtextra.AbsoluteConstraints(335, 55, -1, -1));

        listBukuText1.setFont(new CustomFont().getFont("bahnschrift", 36));
        listBukuText1.setForeground(new java.awt.Color(255, 255, 255));
        listBukuText1.setText("Data Pelanggan");
        tabPelanggan.add(listBukuText1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, -1, -1));

        panelHeaderPelanggan.setBackground(new java.awt.Color(65, 65, 65));
        panelHeaderPelanggan.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelHeaderPelanggan.setPreferredSize(new java.awt.Dimension(565, 45));
        panelHeaderPelanggan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        headNomor.setEditable(false);
        headNomor.setBackground(new java.awt.Color(51, 51, 51));
        headNomor.setFont(new CustomFont().getFont("bahnschrift", 14));
        headNomor.setForeground(new java.awt.Color(255, 255, 255));
        headNomor.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        headNomor.setText("No.");
        headNomor.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        headNomor.setPreferredSize(new java.awt.Dimension(60, 35));
        panelHeaderPelanggan.add(headNomor, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 5, 40, -1));

        headNama.setEditable(false);
        headNama.setBackground(new java.awt.Color(51, 51, 51));
        headNama.setFont(new CustomFont().getFont("bahnschrift", 14));
        headNama.setForeground(new java.awt.Color(255, 255, 255));
        headNama.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        headNama.setText("Nama");
        headNama.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        headNama.setPreferredSize(new java.awt.Dimension(60, 35));
        panelHeaderPelanggan.add(headNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 5, 210, -1));

        headAlamat.setEditable(false);
        headAlamat.setBackground(new java.awt.Color(51, 51, 51));
        headAlamat.setFont(new CustomFont().getFont("bahnschrift", 14));
        headAlamat.setForeground(new java.awt.Color(255, 255, 255));
        headAlamat.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        headAlamat.setText("Alamat");
        headAlamat.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        headAlamat.setPreferredSize(new java.awt.Dimension(60, 35));
        panelHeaderPelanggan.add(headAlamat, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 5, 150, -1));

        headNoTelp.setEditable(false);
        headNoTelp.setBackground(new java.awt.Color(51, 51, 51));
        headNoTelp.setFont(new CustomFont().getFont("bahnschrift", 14));
        headNoTelp.setForeground(new java.awt.Color(255, 255, 255));
        headNoTelp.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        headNoTelp.setText("No. Telepon");
        headNoTelp.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        headNoTelp.setPreferredSize(new java.awt.Dimension(60, 35));
        panelHeaderPelanggan.add(headNoTelp, new org.netbeans.lib.awtextra.AbsoluteConstraints(425, 5, 130, -1));

        headEdit.setEditable(false);
        headEdit.setBackground(new java.awt.Color(51, 180, 51));
        headEdit.setFont(new CustomFont().getFont("bahnschrift", Font.BOLD ,14));
        headEdit.setForeground(new java.awt.Color(255, 255, 255));
        headEdit.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        headEdit.setText("Edit");
        headEdit.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        headEdit.setPreferredSize(new java.awt.Dimension(60, 35));
        panelHeaderPelanggan.add(headEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 5, 60, -1));

        headHapus.setEditable(false);
        headHapus.setBackground(new java.awt.Color(255, 51, 51));
        headHapus.setFont(new CustomFont().getFont("bahnschrift", Font.BOLD ,14));
        headHapus.setForeground(new java.awt.Color(255, 255, 255));
        headHapus.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        headHapus.setText("Hapus");
        headHapus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        headHapus.setPreferredSize(new java.awt.Dimension(60, 35));
        panelHeaderPelanggan.add(headHapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(625, 5, 65, -1));

        tabPelanggan.add(panelHeaderPelanggan, new org.netbeans.lib.awtextra.AbsoluteConstraints(335, 5, 700, -1));

        buttonTambahPelanggan.setBackground(new java.awt.Color(90, 90, 90));
        buttonTambahPelanggan.setFont(new java.awt.Font("Bahnschrift", 1, 14)); // NOI18N
        buttonTambahPelanggan.setForeground(new java.awt.Color(255, 255, 255));
        buttonTambahPelanggan.setText("TAMBAH DATA");
        buttonTambahPelanggan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonTambahPelangganActionPerformed(evt);
            }
        });
        tabPelanggan.add(buttonTambahPelanggan, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 130, 325, 60));

        tabbedPanel.addTab("  Pelanggan  ", tabPelanggan);

        tabPegawai.setBackground(new java.awt.Color(75, 75, 75));
        tabPegawai.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        listPegawaiText.setFont(new CustomFont().getFont("bahnschrift", 36));
        listPegawaiText.setForeground(new java.awt.Color(255, 255, 255));
        listPegawaiText.setText("Data Pegawai Kasir");
        tabPegawai.add(listPegawaiText, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, -1, -1));

        panelTambahPegawai.setBackground(new java.awt.Color(65, 65, 65));
        panelTambahPegawai.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tf_newAlamatPegawai.setBackground(new java.awt.Color(65, 65, 65));
        tf_newAlamatPegawai.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        tf_newAlamatPegawai.setForeground(new java.awt.Color(255, 255, 255));
        tf_newAlamatPegawai.setToolTipText("");
        tf_newAlamatPegawai.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Alamat", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Bahnschrift", 0, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        tf_newAlamatPegawai.setCaretColor(new java.awt.Color(255, 255, 102));
        tf_newAlamatPegawai.setDisabledTextColor(new java.awt.Color(200, 200, 200));
        tf_newAlamatPegawai.setMargin(new java.awt.Insets(2, 6, 2, 6));
        panelTambahPegawai.add(tf_newAlamatPegawai, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 220, 280, 60));

        tf_newNamaPegawai.setBackground(new java.awt.Color(65, 65, 65));
        tf_newNamaPegawai.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        tf_newNamaPegawai.setForeground(new java.awt.Color(255, 255, 255));
        tf_newNamaPegawai.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Nama", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Bahnschrift", 0, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        tf_newNamaPegawai.setCaretColor(new java.awt.Color(255, 255, 102));
        tf_newNamaPegawai.setDisabledTextColor(new java.awt.Color(200, 200, 200));
        tf_newNamaPegawai.setMargin(new java.awt.Insets(2, 6, 2, 6));
        panelTambahPegawai.add(tf_newNamaPegawai, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, 280, 60));

        tf_newNotelpPegawai.setBackground(new java.awt.Color(65, 65, 65));
        tf_newNotelpPegawai.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        tf_newNotelpPegawai.setForeground(new java.awt.Color(255, 255, 255));
        tf_newNotelpPegawai.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "No. Telepon", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Bahnschrift", 0, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        tf_newNotelpPegawai.setCaretColor(new java.awt.Color(255, 255, 102));
        tf_newNotelpPegawai.setDisabledTextColor(new java.awt.Color(200, 200, 200));
        tf_newNotelpPegawai.setMargin(new java.awt.Insets(2, 6, 2, 6));
        panelTambahPegawai.add(tf_newNotelpPegawai, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 340, 280, 60));

        tf_newUnamePegawai.setBackground(new java.awt.Color(65, 65, 65));
        tf_newUnamePegawai.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        tf_newUnamePegawai.setForeground(new java.awt.Color(255, 255, 255));
        tf_newUnamePegawai.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Username", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Bahnschrift", 0, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        tf_newUnamePegawai.setCaretColor(new java.awt.Color(255, 255, 102));
        tf_newUnamePegawai.setDisabledTextColor(new java.awt.Color(200, 200, 200));
        tf_newUnamePegawai.setMargin(new java.awt.Insets(2, 6, 2, 6));
        tf_newUnamePegawai.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tf_newUnamePegawaiKeyReleased(evt);
            }
        });
        panelTambahPegawai.add(tf_newUnamePegawai, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 100, 280, 60));

        jTextField3.setBackground(new java.awt.Color(65, 65, 65));
        jTextField3.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        jTextField3.setForeground(new java.awt.Color(255, 255, 255));
        jTextField3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField3.setText("Shif");
        jTextField3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        panelTambahPegawai.add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 460, 35, 20));

        panelNewShif.setBackground(new java.awt.Color(65, 65, 65));
        panelNewShif.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        panelNewShif.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        rb_newPagi.setBackground(new java.awt.Color(65, 65, 65));
        btnGroupNewShif.add(rb_newPagi);
        rb_newPagi.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        rb_newPagi.setForeground(new java.awt.Color(255, 255, 255));
        rb_newPagi.setText("Pagi");
        panelNewShif.add(rb_newPagi, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 130, 40));

        rb_newSore.setBackground(new java.awt.Color(65, 65, 65));
        btnGroupNewShif.add(rb_newSore);
        rb_newSore.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        rb_newSore.setForeground(new java.awt.Color(255, 255, 255));
        rb_newSore.setText("Sore");
        panelNewShif.add(rb_newSore, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 10, 130, 40));

        panelTambahPegawai.add(panelNewShif, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 470, 280, 55));

        jTextField4.setBackground(new java.awt.Color(65, 65, 65));
        jTextField4.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        jTextField4.setForeground(new java.awt.Color(255, 119, 119));
        jTextField4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField4.setText("Akses");
        jTextField4.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        panelTambahPegawai.add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 460, 50, 20));

        panelNewAkses.setBackground(new java.awt.Color(65, 65, 65));
        panelNewAkses.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 51, 51), 1, true));
        panelNewAkses.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        rb_newAdmin.setBackground(new java.awt.Color(65, 65, 65));
        btnGroupNewAkses.add(rb_newAdmin);
        rb_newAdmin.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        rb_newAdmin.setForeground(new java.awt.Color(255, 255, 255));
        rb_newAdmin.setText("Admin");
        panelNewAkses.add(rb_newAdmin, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 130, 40));

        rb_newKasir.setBackground(new java.awt.Color(65, 65, 65));
        btnGroupNewAkses.add(rb_newKasir);
        rb_newKasir.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        rb_newKasir.setForeground(new java.awt.Color(255, 255, 255));
        rb_newKasir.setText("Kasir");
        panelNewAkses.add(rb_newKasir, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 10, 130, 40));

        panelTambahPegawai.add(panelNewAkses, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 470, 280, 55));

        labelInformasiAkun.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        labelInformasiAkun.setForeground(new java.awt.Color(255, 255, 255));
        labelInformasiAkun.setText("Informasi Akun");
        panelTambahPegawai.add(labelInformasiAkun, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, -1, -1));
        panelTambahPegawai.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 50, 120, 10));
        panelTambahPegawai.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 50, 110, 10));

        labelPengaturanAkun.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        labelPengaturanAkun.setForeground(new java.awt.Color(255, 255, 255));
        labelPengaturanAkun.setText("Pengaturan Akun");
        panelTambahPegawai.add(labelPengaturanAkun, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 40, -1, -1));

        jpf_newUpass.setBackground(new java.awt.Color(65, 65, 65));
        jpf_newUpass.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jpf_newUpass.setForeground(new java.awt.Color(255, 255, 255));
        jpf_newUpass.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Ulangi Password", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Bahnschrift", 0, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jpf_newUpass.setCaretColor(new java.awt.Color(255, 255, 102));
        jpf_newUpass.setDisabledTextColor(new java.awt.Color(200, 200, 200));
        jpf_newUpass.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jpf_newUpassKeyReleased(evt);
            }
        });
        panelTambahPegawai.add(jpf_newUpass, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 340, 230, 60));

        jpf_newPass.setBackground(new java.awt.Color(65, 65, 65));
        jpf_newPass.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jpf_newPass.setForeground(new java.awt.Color(255, 255, 255));
        jpf_newPass.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Password", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Bahnschrift", 0, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jpf_newPass.setCaretColor(new java.awt.Color(255, 255, 102));
        jpf_newPass.setDisabledTextColor(new java.awt.Color(200, 200, 200));
        jpf_newPass.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jpf_newPassKeyReleased(evt);
            }
        });
        panelTambahPegawai.add(jpf_newPass, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 220, 230, 60));

        btnShowUpwd.setBackground(new java.awt.Color(70, 70, 70));
        btnShowUpwd.setForeground(new java.awt.Color(255, 255, 255));
        btnShowUpwd.setText("SHOW");
        btnShowUpwd.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnShowUpwd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowUpwdActionPerformed(evt);
            }
        });
        panelTambahPegawai.add(btnShowUpwd, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 347, 50, 50));

        btnShowPwd.setBackground(new java.awt.Color(70, 70, 70));
        btnShowPwd.setForeground(new java.awt.Color(255, 255, 255));
        btnShowPwd.setText("SHOW");
        btnShowPwd.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnShowPwd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowPwdActionPerformed(evt);
            }
        });
        panelTambahPegawai.add(btnShowPwd, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 227, 50, 50));

        btnResetNewPegawai.setBackground(new java.awt.Color(55, 55, 55));
        btnResetNewPegawai.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        btnResetNewPegawai.setForeground(new java.awt.Color(255, 153, 153));
        btnResetNewPegawai.setText("ATUR ULANG");
        btnResetNewPegawai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetNewPegawaiActionPerformed(evt);
            }
        });
        panelTambahPegawai.add(btnResetNewPegawai, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 390, 180, 50));

        panelTools.setBackground(new java.awt.Color(60, 60, 60));
        panelTools.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tf_generatePassword.setBackground(new java.awt.Color(60, 60, 60));
        tf_generatePassword.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tf_generatePassword.setForeground(new java.awt.Color(255, 255, 255));
        tf_generatePassword.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tf_generatePassword.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tf_generatePassword.setDisabledTextColor(new java.awt.Color(222, 222, 222));
        tf_generatePassword.setEnabled(false);
        panelTools.add(tf_generatePassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 180, 50));

        btnGeneratePass.setBackground(new java.awt.Color(51, 51, 255));
        btnGeneratePass.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        btnGeneratePass.setForeground(new java.awt.Color(255, 255, 255));
        btnGeneratePass.setText("<html>Generate<br>Password</html>");
        btnGeneratePass.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnGeneratePass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGeneratePassActionPerformed(evt);
            }
        });
        panelTools.add(btnGeneratePass, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 80, 100, 50));

        btnCopyGeneratePass.setBackground(new java.awt.Color(75, 75, 75));
        btnCopyGeneratePass.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        btnCopyGeneratePass.setForeground(new java.awt.Color(255, 255, 255));
        btnCopyGeneratePass.setText("COPY");
        btnCopyGeneratePass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCopyGeneratePassActionPerformed(evt);
            }
        });
        panelTools.add(btnCopyGeneratePass, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 70, 50));

        panelTambahPegawai.add(panelTools, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 100, 220, 150));

        labelToolAkun.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        labelToolAkun.setForeground(new java.awt.Color(255, 255, 255));
        labelToolAkun.setText("Tools");
        panelTambahPegawai.add(labelToolAkun, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 40, -1, -1));
        panelTambahPegawai.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 50, 150, 10));

        btnTambahkanPegawai.setBackground(new java.awt.Color(51, 255, 51));
        btnTambahkanPegawai.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        btnTambahkanPegawai.setText("TAMBAHKAN");
        btnTambahkanPegawai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahkanPegawaiActionPerformed(evt);
            }
        });
        panelTambahPegawai.add(btnTambahkanPegawai, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 470, 180, 50));

        labelWarningUN.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelWarningUN.setForeground(new java.awt.Color(255, 153, 153));
        labelWarningUN.setText("*) Belum diisi!");
        labelWarningUN.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        panelTambahPegawai.add(labelWarningUN, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 170, 280, 40));
        labelWarningUN.setVisible(false);

        labelWarningPW.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelWarningPW.setForeground(new java.awt.Color(255, 153, 153));
        labelWarningPW.setText("*) Belum diisi!");
        labelWarningPW.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        panelTambahPegawai.add(labelWarningPW, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 290, 280, 40));
        labelWarningPW.setVisible(false);

        labelWarningUPW.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelWarningUPW.setForeground(new java.awt.Color(255, 153, 153));
        labelWarningUPW.setText("*) Belum diisi!");
        labelWarningUPW.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        panelTambahPegawai.add(labelWarningUPW, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 410, 280, 40));
        labelWarningUPW.setVisible(false);

        newNomorPegawai.setBackground(new java.awt.Color(65, 65, 65));
        newNomorPegawai.setForeground(new java.awt.Color(65, 65, 65));
        newNomorPegawai.setText("jLabel1");
        panelTambahPegawai.add(newNomorPegawai, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 310, -1, -1));

        labelWarningNama.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelWarningNama.setForeground(new java.awt.Color(255, 153, 153));
        labelWarningNama.setText("*) Belum diisi!");
        labelWarningNama.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        panelTambahPegawai.add(labelWarningNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 170, 280, 40));
        labelWarningNama.setVisible(false);

        labelWarningUpwd.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelWarningUpwd.setForeground(new java.awt.Color(255, 153, 153));
        labelWarningUpwd.setText("*) Password tidak sama!");
        labelWarningUpwd.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        panelTambahPegawai.add(labelWarningUpwd, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 410, 280, 40));

        labelWarningUname.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelWarningUname.setForeground(new java.awt.Color(255, 153, 153));
        labelWarningUname.setText("<html>*) Username sudah terpakai,<br>&nbsp;&nbsp;&nbsp;&nbsp;gunakan username lain</html>");
        labelWarningUname.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        panelTambahPegawai.add(labelWarningUname, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 165, 280, 40));

        labelWarningPwd.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelWarningPwd.setForeground(new java.awt.Color(255, 153, 153));
        labelWarningPwd.setText("<html>*) harap gunakan kombinasi huruf, angka, dan simbol. Minimal 8 digit karakter.</html>");
        labelWarningPwd.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        panelTambahPegawai.add(labelWarningPwd, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 290, 280, 40));

        labelWarningAkses.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelWarningAkses.setForeground(new java.awt.Color(255, 153, 153));
        labelWarningAkses.setText("*) Belum dipilih!");
        labelWarningAkses.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        panelTambahPegawai.add(labelWarningAkses, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 540, 280, 40));
        labelWarningAkses.setVisible(false);

        labelWarningShif.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelWarningShif.setForeground(new java.awt.Color(255, 153, 153));
        labelWarningShif.setText("*) Belum dipilih!");
        labelWarningShif.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        panelTambahPegawai.add(labelWarningShif, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 540, 280, 40));
        labelWarningShif.setVisible(false);

        labelWarningNoTelp.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelWarningNoTelp.setForeground(new java.awt.Color(255, 153, 153));
        labelWarningNoTelp.setText("*) Belum diisi!");
        labelWarningNoTelp.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        panelTambahPegawai.add(labelWarningNoTelp, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 410, 280, 40));
        labelWarningNoTelp.setVisible(false);

        labelWarningAlamat.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelWarningAlamat.setForeground(new java.awt.Color(255, 153, 153));
        labelWarningAlamat.setText("*) Belum diisi!");
        labelWarningAlamat.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        panelTambahPegawai.add(labelWarningAlamat, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 290, 280, 40));
        labelWarningAlamat.setVisible(false);

        tabPegawai.add(panelTambahPegawai, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 1040, 585));

        panelEditPegawai.setBackground(new java.awt.Color(65, 65, 65));
        panelEditPegawai.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        scrollPaneListlPegawai.setBackground(new java.awt.Color(65, 65, 65));
        scrollPaneListlPegawai.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        scrollPaneListlPegawai.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollPaneListlPegawai.setPreferredSize(new java.awt.Dimension(1020, 215));

        panelListPegawai.setBackground(new java.awt.Color(65, 65, 65));
        panelListPegawai.setLayout(new java.awt.GridLayout(1, 0));
        scrollPaneListlPegawai.setViewportView(panelListPegawai);

        panelEditPegawai.add(scrollPaneListlPegawai, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        panelDTPegawai.setBackground(new java.awt.Color(65, 65, 65));
        panelDTPegawai.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        panelDTPegawai.setMaximumSize(new java.awt.Dimension(1020, 340));
        panelDTPegawai.setMinimumSize(new java.awt.Dimension(1020, 340));
        panelDTPegawai.setPreferredSize(new java.awt.Dimension(1020, 345));
        panelDTPegawai.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        listBukuText3.setFont(new CustomFont().getFont("bahnschrift", 24));
        listBukuText3.setForeground(new java.awt.Color(255, 255, 255));
        listBukuText3.setText("Informasi Pegawai");
        panelDTPegawai.add(listBukuText3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        tf_alamatPegawai.setBackground(new java.awt.Color(65, 65, 65));
        tf_alamatPegawai.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        tf_alamatPegawai.setForeground(new java.awt.Color(255, 255, 255));
        tf_alamatPegawai.setText("Alamat Pegawai");
        tf_alamatPegawai.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Alamat", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Bahnschrift", 0, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        tf_alamatPegawai.setDisabledTextColor(new java.awt.Color(200, 200, 200));
        tf_alamatPegawai.setEnabled(false);
        tf_alamatPegawai.setMargin(new java.awt.Insets(2, 6, 2, 6));
        panelDTPegawai.add(tf_alamatPegawai, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 180, 280, 60));

        tf_notelpPegawai.setBackground(new java.awt.Color(65, 65, 65));
        tf_notelpPegawai.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        tf_notelpPegawai.setForeground(new java.awt.Color(255, 255, 255));
        tf_notelpPegawai.setText("Nomor Telepon");
        tf_notelpPegawai.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "No. Telepon", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Bahnschrift", 0, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        tf_notelpPegawai.setDisabledTextColor(new java.awt.Color(200, 200, 200));
        tf_notelpPegawai.setEnabled(false);
        tf_notelpPegawai.setMargin(new java.awt.Insets(2, 6, 2, 6));
        panelDTPegawai.add(tf_notelpPegawai, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 90, 280, 60));

        jTextField1.setBackground(new java.awt.Color(65, 65, 65));
        jTextField1.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        jTextField1.setForeground(new java.awt.Color(255, 255, 255));
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField1.setText("Shif");
        jTextField1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        panelDTPegawai.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 175, 35, 20));

        jPanel2.setBackground(new java.awt.Color(65, 65, 65));
        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        rb_pagi.setBackground(new java.awt.Color(65, 65, 65));
        btnGroupShif.add(rb_pagi);
        rb_pagi.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        rb_pagi.setForeground(new java.awt.Color(255, 255, 255));
        rb_pagi.setText("Pagi");
        rb_pagi.setEnabled(false);
        jPanel2.add(rb_pagi, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 130, 40));

        rb_sore.setBackground(new java.awt.Color(65, 65, 65));
        btnGroupShif.add(rb_sore);
        rb_sore.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        rb_sore.setForeground(new java.awt.Color(255, 255, 255));
        rb_sore.setText("Sore");
        rb_sore.setEnabled(false);
        jPanel2.add(rb_sore, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 10, 130, 40));

        panelDTPegawai.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 185, 280, 55));

        tf_namaPegawai.setBackground(new java.awt.Color(65, 65, 65));
        tf_namaPegawai.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        tf_namaPegawai.setForeground(new java.awt.Color(255, 255, 255));
        tf_namaPegawai.setText("Nama Pegawai");
        tf_namaPegawai.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Nama", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Bahnschrift", 0, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        tf_namaPegawai.setDisabledTextColor(new java.awt.Color(200, 200, 200));
        tf_namaPegawai.setEnabled(false);
        tf_namaPegawai.setMargin(new java.awt.Insets(2, 6, 2, 6));
        panelDTPegawai.add(tf_namaPegawai, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, 280, 60));

        tf_unamePegawai.setBackground(new java.awt.Color(65, 65, 65));
        tf_unamePegawai.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        tf_unamePegawai.setForeground(new java.awt.Color(255, 255, 255));
        tf_unamePegawai.setText("Username");
        tf_unamePegawai.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Username", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Bahnschrift", 0, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        tf_unamePegawai.setDisabledTextColor(new java.awt.Color(200, 200, 200));
        tf_unamePegawai.setEnabled(false);
        tf_unamePegawai.setMargin(new java.awt.Insets(2, 6, 2, 6));
        panelDTPegawai.add(tf_unamePegawai, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 90, 280, 60));

        jTextField2.setBackground(new java.awt.Color(65, 65, 65));
        jTextField2.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        jTextField2.setForeground(new java.awt.Color(255, 102, 102));
        jTextField2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField2.setText("Akses");
        jTextField2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        panelDTPegawai.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 175, 50, 20));

        jPanel3.setBackground(new java.awt.Color(65, 65, 65));
        jPanel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 51, 51), 1, true));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        rb_admin.setBackground(new java.awt.Color(65, 65, 65));
        btnGroupAkses.add(rb_admin);
        rb_admin.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        rb_admin.setForeground(new java.awt.Color(255, 255, 255));
        rb_admin.setText("Admin");
        rb_admin.setEnabled(false);
        jPanel3.add(rb_admin, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 130, 40));

        rb_kasir.setBackground(new java.awt.Color(65, 65, 65));
        btnGroupAkses.add(rb_kasir);
        rb_kasir.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        rb_kasir.setForeground(new java.awt.Color(255, 255, 255));
        rb_kasir.setText("Kasir");
        rb_kasir.setEnabled(false);
        jPanel3.add(rb_kasir, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 10, 130, 40));

        panelDTPegawai.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 185, 280, 55));

        btnHapusPegawai.setBackground(new java.awt.Color(255, 51, 51));
        btnHapusPegawai.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        btnHapusPegawai.setForeground(new java.awt.Color(255, 255, 255));
        btnHapusPegawai.setText("HAPUS");
        btnHapusPegawai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusPegawaiActionPerformed(evt);
            }
        });
        panelDTPegawai.add(btnHapusPegawai, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 280, 110, 40));

        btnEditPassword.setBackground(new java.awt.Color(102, 102, 102));
        btnEditPassword.setFont(new java.awt.Font("Bahnschrift", 0, 12)); // NOI18N
        btnEditPassword.setForeground(new java.awt.Color(255, 255, 255));
        btnEditPassword.setText("<html><center>UBAH<br>PASSWORD</center></html>");
        btnEditPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditPasswordActionPerformed(evt);
            }
        });
        panelDTPegawai.add(btnEditPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 280, 120, 40));
        btnEditPassword.setVisible(false);

        btnEditPegawai.setBackground(new java.awt.Color(51, 180, 51));
        btnEditPegawai.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        btnEditPegawai.setForeground(new java.awt.Color(255, 255, 255));
        btnEditPegawai.setText("EDIT");
        btnEditPegawai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditPegawaiActionPerformed(evt);
            }
        });
        panelDTPegawai.add(btnEditPegawai, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 280, 110, 40));

        labelIDPegawai.setForeground(new java.awt.Color(65, 65, 65));
        labelIDPegawai.setText("idpegawai");
        panelDTPegawai.add(labelIDPegawai, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 20, -1, -1));

        panelEditPegawai.add(panelDTPegawai, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 235, 1020, 340));

        tabPegawai.add(panelEditPegawai, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 1040, 585));

        btnTambahPegawai.setBackground(new java.awt.Color(90, 90, 90));
        btnTambahPegawai.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        btnTambahPegawai.setForeground(new java.awt.Color(255, 255, 255));
        btnTambahPegawai.setText("TAMBAH");
        btnTambahPegawai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahPegawaiActionPerformed(evt);
            }
        });
        tabPegawai.add(btnTambahPegawai, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 25, 110, 40));

        tabbedPanel.addTab("  Pegawai Kasir  ", tabPegawai);

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
        ubahProfilButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ubahProfilButtonActionPerformed(evt);
            }
        });
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
            PreferencedHelper.clear();
            destroyInstance();
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
            loadDataBuku(item);
        }
    }//GEN-LAST:event_sortingComboBoxActionPerformed

    /**
     * Fungsi untuk menangkap inputan tombol enter ketika mengedit kolom
     * pencarian
     *
     * @param evt
     */
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
            case 3:
                title = "K-SIR Book: Data Pelanggan";
                break;
            case 4:
                title = "K-SIR Book: Data Pegawai Kasir";
                break;
            default:
                title = "K-SIR Book";
        }
        Thread t = new Thread() {
            @Override
            public void run() {
                setTitle(title);
                if (listPelanggans.size() > 0) {
                    listPelanggans.forEach((l) -> {
                        if (!arrayPelanggans.contains(l.getNama_pelanggan())) {
                            setCBpelanggan();
                        }
                    });
                }
                textTitleBar.setText(title);
            }
        };
        t.start();
    }//GEN-LAST:event_tabbedPanelStateChanged

    private void buttonAddTransaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAddTransaksiActionPerformed
        if (panelInfo.isVisible() || panelDetailTransaksi.isVisible()) {
            buttonAddTransaksi.setText("BATAL");
            setPanelTransaksi(false, false, true);
            setInfoTambahTrx();
        } else if (panelTambahTransaksi.isVisible()) {
            buttonAddTransaksi.setText("TAMBAH TRANSAKSI");
            setPanelTransaksi(true, false, false);
        }
    }//GEN-LAST:event_buttonAddTransaksiActionPerformed

    private void buttonTambahPelangganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonTambahPelangganActionPerformed
        if (panelTambahPelanggan.isVisible()) {
            buttonTambahPelanggan.setText("TAMBAH PELANGGAN");
            panelTambahPelanggan.setVisible(false);
        } else {
            buttonTambahPelanggan.setText("BATAL");
            panelTambahPelanggan.setVisible(true);
            clearTFpelanggan();
        }
    }//GEN-LAST:event_buttonTambahPelangganActionPerformed

    private void buttonTambahkanPelangganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonTambahkanPelangganActionPerformed
        if (inputNamaP && inputAlamatP && inputTelpP) {
            Thread t = new Thread("thread-tambahDataPelanggan") {
                @Override
                public void run() {
                    try {
                        Pelanggan newPelanggan = new Pelanggan();
                        newPelanggan.setId_pelanggan(listPelanggans.size() + 1);
                        newPelanggan.setNama_pelanggan(tf_newNamaPelanggan.getText());
                        newPelanggan.setAlamat(tf_newAlamatP.getText());
                        newPelanggan.setNoTelp(tf_newNoTelp.getText());
                        pelanggan.insert(newPelanggan);
                        loadDataPelanggan();
                    } catch (SQLException ex) {
                        Logger.getLogger(DashboardPegawaiView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
            t.start();
            buttonTambahPelangganActionPerformed(evt);
        }
    }//GEN-LAST:event_buttonTambahkanPelangganActionPerformed

    private int indexDetailTrx = 0;

    private void buttonAddItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAddItemActionPerformed
        if (panelTambahTransaksi.isVisible()) {
            panelDTtambah.removeAll();
            indexDetailTrx += 1;
            DetailTransaksi detailTransaksi = new DetailTransaksi();
            detailTransaksi.setId_detail_trx(indexDetailTrx);
            detailTransaksi.setId_trx(listTransaksi.size() + 1);
            detailTrx.listDetailTransaksis.add(detailTransaksi);
            loadDataDetailTransaksi();
        }
    }//GEN-LAST:event_buttonAddItemActionPerformed

    private void buttonResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonResetActionPerformed
        detailTrx.listDetailTransaksis.clear();
        indexDetailTrx = 0;
        loadDataDetailTransaksi();
        JTextField textField = (JTextField) cb_namaPelanggan.getEditor().getEditorComponent();
        textField.setText("");
        tf_uangTunai.setText("0");
    }//GEN-LAST:event_buttonResetActionPerformed

    private void buttonTambahDataTrxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonTambahDataTrxActionPerformed
        CustomJPanelView jOpt = new CustomJPanelView();
        jOpt.setPanel("black");
        if (indexDetailTrx > 0) { // cek apakah daftar barang yang ingin dibeli sudah diisi apa belum
            if (inputUangT) { // cek apakah uang sudah diisi apa masih kosong
                int uangTunai = Integer.parseInt(tf_uangTunai.getText());
                String[] t = tf_totalBayar.getText().split(" ");
                t = t[1].split(",");
                String t1 = t[0].replace(".", "");
                int total = Integer.parseInt(t1);
                if (uangTunai >= total) { // cek uang tunai yang dibayarkan sudah cukup belum
                    JTextField textField = (JTextField) cb_namaPelanggan.getEditor().getEditorComponent();
                    inputNamaT = textField.getText().length() > 0;
                    if (inputNamaT) { // cek nama pelanggan sudah diisi belum
                        Transaksi trx = new Transaksi();
                        trx.setId(Integer.parseInt(textIDTrx.getText()));
                        listPelanggans.forEach((Pelanggan p) -> {
                            if (p.getNama_pelanggan().equalsIgnoreCase(textField.getText())) {
                                trx.setIdPelanggan(p.getId_pelanggan());
                                trx.setNamaPelanggan(textField.getText());
                            }
                        });
                        // cek nama pelanggan terdapat di daftar pelanggan
                        if (trx.getIdPelanggan() > 0) {
                            trx.setIdPetugas(PreferencedHelper.getId());
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            Date now = new Date();
                            trx.setTanggal(formatter.format(now));
                            trx.setNominalBayar(Double.valueOf(uangTunai));
                            try { // Memasukkan data transaksi ke list dan database
                                listDetailTransaksis = detailTrx.listDetailTransaksis;
                                Boolean isKosong = false;
                                for (int i = 0; i < listDetailTransaksis.size(); i++) {
                                    if (listDetailTransaksis.get(i).getHarga() == 0 || listDetailTransaksis.get(i).getIsbn().equals("")) {
                                        isKosong = true;
                                        break;
                                    }
                                }
                                if (!isKosong) {
                                    transaksi.insert(trx);
                                    int maxLoop = (listDetailTransaksis.size() != indexDetailTrx) ? listDetailTransaksis.size() : indexDetailTrx;
                                    for (int i = 0; i < maxLoop; i++) {
                                        DetailTransaksi dtrx = new DetailTransaksi();
                                        dtrx.setId_trx(trx.getId());
                                        dtrx.setIsbn(listDetailTransaksis.get(i).getIsbn());
                                        dtrx.setBanyak(listDetailTransaksis.get(i).getBanyak());
                                        try { // Memasukkan data detail transaksi ke list dan database
                                            detailTrx.insert(dtrx);
                                        } catch (SQLException ex) {
                                            System.out.println(i + ". Error: " + ex.getMessage());
                                        }
                                    }
                                    indexDetailTrx = 0;
                                    jOpt.displayInfo(panelTambahTransaksi, "Berhasil menambahkan transaksi!", "Sukses!");
                                    trx.setNamaPetugas(PreferencedHelper.getName());
                                    trx.setTotalBayar(total);
                                    buttonResetActionPerformed(evt);
                                    setInfoTambahTrx();
                                    loadDataTransaksi();
                                } else {
                                    jOpt.displayError(panelTambahTransaksi, "Terdapat daftar item yang masih kosong,\nSilahkan isi atau hapus item dari daftar!", "Item belum lengkap!");
                                }
                            } catch (SQLException ex) {
                                System.out.println("Error: " + ex.getMessage());
                            }
                        } else {
                            int aksi = jOpt.displayConfirmDialog(panelTambahTransaksi, "Nama Pelanggan belum terdaftar pada daftar,\ntambahkan sekarang?", "Nama Pelanggan belum terdaftar!");
                            switch (aksi) {
                                case 0: {
                                    tabbedPanel.setSelectedIndex(3);
                                    buttonTambahPelangganActionPerformed(evt);
                                    tf_newNamaPelanggan.requestFocusInWindow();
                                    tf_newNamaPelanggan.setText(((JTextField) cb_namaPelanggan.getEditor().getEditorComponent()).getText());
                                    break;
                                }
                            }
                        }
                    } else {
                        jOpt.displayError(panelTambahTransaksi, "Nama Pelanggan belum dipilih,\nsilahkan pilih nama pelanggan terlebih dahulu!", "Pelanggan belum diisi!");
                        setCBpelanggan();
                        cb_namaPelanggan.requestFocusInWindow();
                    }
                } else {
                    jOpt.displayError(panelTambahTransaksi, "Nominal uang yang dibayarkan kurang,\nsilahkan cek kembali!", "Nominal uang kurang!");
                }
            } else {
                jOpt.displayError(panelTambahTransaksi, "Nominal uang masih kosong,\nsilahkan isikan terlebih dahulu", "Nominal uang kosong!");
                tf_uangTunai.requestFocusInWindow();
            }
        } else {
            jOpt.displayError(panelTambahTransaksi, "Anda belum menambahkan daftar buku yang ingin dibeli\nSilahkan tambahkan terlebih dahulu!", "Daftar Buku kosong!");
            buttonAddItem.requestFocusInWindow();
        }
    }//GEN-LAST:event_buttonTambahDataTrxActionPerformed

    private void cb_namaPelangganItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_namaPelangganItemStateChanged
        inputNamaT = true;
    }//GEN-LAST:event_cb_namaPelangganItemStateChanged

    private void buttonAddItemMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonAddItemMouseExited
        SwingUtilities.updateComponentTreeUI(panelTambahTransaksi);
    }//GEN-LAST:event_buttonAddItemMouseExited

    private void btnHapusPegawaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusPegawaiActionPerformed
        CustomJPanelView jOpt = new CustomJPanelView();
        jOpt.setPanel("black");
        int aksi = jOpt.displayConfirmDialog(panelDTPegawai, "Apakah anda ingin menghapus " + tf_namaPegawai.getText() + " dari daftar pelanggan?", "Konfirmasi");
        switch (aksi) {
            case 0: {
                Thread t = new Thread() {
                    @Override
                    public void run() {
                        try {
                            pegawai.delete(listPegawais.get(Integer.parseInt(labelIDPegawai.getText())));
                            panelListPegawai.remove(Integer.parseInt(labelIDPegawai.getText()));
                            loadDataPegawai();
                            panelDTPegawai.setVisible(false);
                            SwingUtilities.updateComponentTreeUI(tabPegawai);
                        } catch (SQLException ex) {
                            jOpt.setPanel("black");
                            jOpt.displayWarning(tabPegawai, "Gagal menghapus pegawai!", "Gagal");
                            System.out.println(ex.getErrorCode() + " : " + ex.getMessage());
                        }
                    }
                };
                t.start();
                break;
            }
        }
    }//GEN-LAST:event_btnHapusPegawaiActionPerformed

    private void btnEditPegawaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditPegawaiActionPerformed
        if (btnEditPegawai.getText().equalsIgnoreCase("edit")) {
            // Change TextField Enable
            btnEditPegawai.setText("SIMPAN");
            tf_namaPegawai.setEnabled(true);
            tf_alamatPegawai.setEnabled(true);
            tf_notelpPegawai.setEnabled(true);
            tf_unamePegawai.setEnabled(true);
            rb_pagi.setEnabled(true);
            rb_sore.setEnabled(true);
            rb_admin.setEnabled(true);
            rb_kasir.setEnabled(true);
            btnEditPassword.setVisible(true);
            ChangePasswordPanel.setImpl(pegawai);
        } else if (btnEditPegawai.getText().equalsIgnoreCase("simpan")) {
            Pegawai oldP = pegawai.listPegawais.get(Integer.parseInt(labelIDPegawai.getText()));
            Pegawai newP = new Pegawai();
            newP.setIdKasir(oldP.getIdKasir());
            newP.setNama(tf_namaPegawai.getText());
            newP.setAlamat(tf_alamatPegawai.getText());
            newP.setTelepon(tf_notelpPegawai.getText());
            String shif = rb_pagi.isSelected() ? "Pagi" : "Sore";
            newP.setShif(shif);
            newP.setUsername(tf_unamePegawai.getText());
            newP.setPassword(oldP.getPassword());
            newP.setSalt(oldP.getSalt());
            String akses = rb_admin.isSelected() ? "admin" : "kasir";
            newP.setAkses(akses);

            boolean same = oldP.getIdKasir() == newP.getIdKasir()
                    && oldP.getNama().equals(newP.getNama())
                    && oldP.getAlamat().equals(newP.getAlamat())
                    && oldP.getTelepon().equals(newP.getTelepon())
                    && oldP.getUsername().equals(newP.getUsername())
                    && oldP.getAkses().equals(newP.getAkses())
                    && oldP.getPassword().equals(newP.getPassword())
                    && oldP.getShif().equals(newP.getShif())
                    && oldP.getSalt().equals(newP.getSalt());

            if (!same) {
                try {
                    pegawai.update(newP);
                    loadDataPegawai();
                    SwingUtilities.updateComponentTreeUI(tabPegawai);
                } catch (SQLException ex) {
                    Logger.getLogger(DashboardPegawaiView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            // Change TextField Enable
            btnEditPegawai.setText("EDIT");
            tf_namaPegawai.setEnabled(false);
            tf_alamatPegawai.setEnabled(false);
            tf_notelpPegawai.setEnabled(false);
            tf_unamePegawai.setEnabled(false);
            rb_pagi.setEnabled(false);
            rb_sore.setEnabled(false);
            rb_admin.setEnabled(false);
            rb_kasir.setEnabled(false);
            btnEditPassword.setVisible(false);
        }
    }//GEN-LAST:event_btnEditPegawaiActionPerformed

    private void btnEditPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditPasswordActionPerformed
        ChangePasswordPanel.display(listPegawais.get(Integer.parseInt(labelIDPegawai.getText())));
    }//GEN-LAST:event_btnEditPasswordActionPerformed

    /**
     * Variabel untuk menyimpan salt milik pegawai baru
     */
    private String newSalt = "";

    private void btnTambahPegawaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahPegawaiActionPerformed
        if (btnTambahPegawai.getText().equalsIgnoreCase("tambah")) {
            btnTambahPegawai.setText("BATAL");
            newSalt = PasswordUtils.getSalt(32);
            labelIDPegawai.setText(String.valueOf(listPegawais.get(listPegawais.size() - 1).getIdKasir() + 1));
            panelEditPegawai.setVisible(false);
            panelTambahPegawai.setVisible(true);
        } else {
            btnTambahPegawai.setText("TAMBAH");
            panelTambahPegawai.setVisible(false);
            panelEditPegawai.setVisible(true);
        }
    }//GEN-LAST:event_btnTambahPegawaiActionPerformed

    /**
     * Algorithm to generate random password
     *
     * @param evt
     */
    private void btnGeneratePassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGeneratePassActionPerformed
        Random random = new SecureRandom();
        String angka = "0123456789";
        String hurufB = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String hurufK = "abcdefghijklmnopqrstuvwxyz";
        String simbol = "#?!@$ %^&*-";
        int iterasi = 15;
        StringBuilder pass = new StringBuilder(10);
        for (int i = 0; i < iterasi; i++) {
            pass.append(angka.charAt(random.nextInt(angka.length())));
            pass.append(hurufB.charAt(random.nextInt(angka.length())));
            pass.append(hurufK.charAt(random.nextInt(angka.length())));
            pass.append(simbol.charAt(random.nextInt(angka.length())));
        }
        String list = String.valueOf(pass);
        pass = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            pass.append(list.charAt(random.nextInt(list.length())));
        }
        tf_generatePassword.setText(String.valueOf(pass));
    }//GEN-LAST:event_btnGeneratePassActionPerformed

    /**
     * Feature to copy text from JTextField to clipboard
     *
     * @param evt
     */
    private void btnCopyGeneratePassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCopyGeneratePassActionPerformed
        if (!tf_generatePassword.getText().isEmpty()) {
            StringSelection textToCopy = new StringSelection(tf_generatePassword.getText());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(textToCopy, null);
        } else {
            CustomJPanelView jOpt = new CustomJPanelView();
            jOpt.setPanel("black");
            jOpt.displayError(panelTools, "Harap klik tombol\ngenerate password\nterlebih dahulu!", "Gagal!");
        }
    }//GEN-LAST:event_btnCopyGeneratePassActionPerformed

    /**
     * Feature to show/hide password in JPasswordField <code>jpf_newPass</code>
     *
     * @param evt
     */
    private void btnShowPwdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowPwdActionPerformed
        if (btnShowPwd.getText().equalsIgnoreCase("show")) {
            btnShowPwd.setText("HIDE");
            jpf_newPass.setEchoChar((char) 0);
        } else {
            btnShowPwd.setText("SHOW");
            jpf_newPass.setEchoChar('\u25cf');
        }
    }//GEN-LAST:event_btnShowPwdActionPerformed

    /**
     * Feature to show/hide password in JPasswordField <code>jpf_newUpass</code>
     *
     * @param evt
     */
    private void btnShowUpwdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowUpwdActionPerformed
        if (btnShowUpwd.getText().equalsIgnoreCase("show")) {
            btnShowUpwd.setText("HIDE");
            jpf_newUpass.setEchoChar((char) 0);
        } else {
            btnShowUpwd.setText("SHOW");
            jpf_newUpass.setEchoChar('\u25cf');
        }
    }//GEN-LAST:event_btnShowUpwdActionPerformed

    /**
     * Feature for reset form in panelTambahPegawai
     *
     * @param evt
     */
    private void btnResetNewPegawaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetNewPegawaiActionPerformed
        CustomJPanelView jOpt = new CustomJPanelView();
        jOpt.setPanel("black");
        int aksi = jOpt.displayConfirmDialog(panelTambahPegawai, "Apakah anda yakin\ningin mengatur ulang form\nyang sudah anda isikan?", "Konfirmasi");
        switch (aksi) {
            case 0: //yes option
            {
                tf_newNamaPegawai.setText("");
                tf_newAlamatPegawai.setText("");
                tf_newNotelpPegawai.setText("");
                tf_newUnamePegawai.setText("");
                jpf_newPass.setText("");
                jpf_newUpass.setText("");
                tf_generatePassword.setText("");
                btnGroupNewShif.clearSelection();
                btnGroupNewAkses.clearSelection();
                break;
            }
            default:
            //nothing
        }
    }//GEN-LAST:event_btnResetNewPegawaiActionPerformed

    private boolean cekUname;
    private boolean cekPass;
    private boolean cekUpass = false;

    /**
     * Aksi ketika tombol tambahkan pegawai diklik
     *
     * @param evt
     */
    private void btnTambahkanPegawaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahkanPegawaiActionPerformed
        boolean npIsEmpty = tf_newNamaPegawai.getText().isEmpty();
        labelWarningNama.setVisible(npIsEmpty);
        boolean apIsEmpty = tf_newAlamatPegawai.getText().isEmpty();
        labelWarningAlamat.setVisible(apIsEmpty);
        boolean tpIsEmpty = tf_newNotelpPegawai.getText().isEmpty();
        labelWarningNoTelp.setVisible(tpIsEmpty);
        boolean unIsEmpty = tf_newUnamePegawai.getText().isEmpty();
        labelWarningUN.setVisible(unIsEmpty);
        boolean pwIsEmpty = jpf_newPass.getPassword().length == 0;
        labelWarningPW.setVisible(pwIsEmpty);
        boolean upIsEmpty = jpf_newUpass.getPassword().length == 0;
        labelWarningUPW.setVisible(upIsEmpty);
        boolean akIsEmpty = !(rb_newAdmin.isSelected() || rb_newKasir.isSelected());
        labelWarningAkses.setVisible(akIsEmpty);
        boolean shIsEmpty = !(rb_newPagi.isSelected() || rb_newSore.isSelected());
        labelWarningShif.setVisible(shIsEmpty);

        boolean checkAllisEmpty = npIsEmpty || apIsEmpty || tpIsEmpty || unIsEmpty || pwIsEmpty || upIsEmpty || akIsEmpty || shIsEmpty;

        if (!checkAllisEmpty) {
            CustomJPanelView jOpt = new CustomJPanelView();
            jOpt.setPanel("black");
            if (!cekUname && !cekPass && cekUpass) {
                Pegawai newPegawai = new Pegawai();
                newPegawai.setIdKasir(Integer.valueOf(labelIDPegawai.getText()));
                newPegawai.setNama(tf_newNamaPegawai.getText());
                newPegawai.setAlamat(tf_newAlamatPegawai.getText());
                newPegawai.setTelepon(tf_newNotelpPegawai.getText());
                String shif = rb_newPagi.isSelected() ? "Pagi" : "Sore";
                newPegawai.setShif(shif);
                String akses = rb_newAdmin.isSelected() ? "admin" : "kasir";
                newPegawai.setAkses(akses);
                newPegawai.setUsername(tf_newUnamePegawai.getText());
                String securePassword = PasswordUtils.generateSecurePassword(String.valueOf(jpf_newPass.getPassword()), newSalt);
                newPegawai.setPassword(securePassword);
                newPegawai.setSalt(newSalt);

                try {
                    pegawai.insert(newPegawai);
                    jOpt.displayInfo(panelTambahPegawai, "Sukses menambah pegawai", "Sukses");

                    tf_newNamaPegawai.setText("");
                    tf_newAlamatPegawai.setText("");
                    tf_newNotelpPegawai.setText("");
                    tf_newUnamePegawai.setText("");
                    jpf_newPass.setText("");
                    jpf_newUpass.setText("");
                    tf_generatePassword.setText("");
                    btnGroupNewShif.clearSelection();
                    btnGroupNewAkses.clearSelection();

                    btnTambahPegawaiActionPerformed(evt);
                    loadDataPegawai();

                } catch (SQLException ex) {
                    jOpt.displayError(panelTambahPegawai, "Gagal Menambah Pegawai!", "Error");
                    System.out.println("Error : " + ex.getMessage());
                }
            }
        }
    }//GEN-LAST:event_btnTambahkanPegawaiActionPerformed

    private void jpf_newUpassKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jpf_newUpassKeyReleased
        if (!Arrays.equals(jpf_newPass.getPassword(), jpf_newUpass.getPassword())) {
            cekUpass = false;
            labelWarningUpwd.setVisible(true);
        } else {
            cekUpass = true;
            labelWarningUpwd.setVisible(false);
        }
    }//GEN-LAST:event_jpf_newUpassKeyReleased

    private void jpf_newPassKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jpf_newPassKeyReleased
        Pattern pattern = Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,}$");
        Matcher matcher = pattern.matcher(String.valueOf(jpf_newPass.getPassword()));
        cekPass = !matcher.matches();
        labelWarningPW.setVisible(false);
        labelWarningPwd.setVisible(cekPass);
    }//GEN-LAST:event_jpf_newPassKeyReleased

    private void tf_newUnamePegawaiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tf_newUnamePegawaiKeyReleased
        for (Pegawai listPegawai : listPegawais) {
            if (listPegawai.getUsername().equals(tf_newUnamePegawai.getText())) {
                cekUname = true;
                break;
            }
            cekUname = false;
        }
        labelWarningUN.setVisible(false);
        labelWarningUname.setVisible(cekUname);
    }//GEN-LAST:event_tf_newUnamePegawaiKeyReleased

    private void ubahProfilButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ubahProfilButtonActionPerformed
        UpdateProfilPanel updateProfil = new UpdateProfilPanel(listPegawais.get(PreferencedHelper.getId() - 1), pegawai);
        updateProfil.display();
        loadDataPegawai();
        setDataPegawai();
        SwingUtilities.updateComponentTreeUI(panelDTPegawai);
    }//GEN-LAST:event_ubahProfilButtonActionPerformed

    private void btnTambahBukuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahBukuActionPerformed
        if (btnTambahBuku.getText().equalsIgnoreCase("tambah buku")) {
            btnTambahBuku.setText("BATAL");
            panelTambahBuku.setVisible(true);
            scrollPanelListBuku.setVisible(false);
        } else {
            btnTambahBuku.setText("TAMBAH BUKU");
            panelTambahBuku.setVisible(false);
            scrollPanelListBuku.setVisible(true);
        }
    }//GEN-LAST:event_btnTambahBukuActionPerformed

    private void btnResetNewBukuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetNewBukuActionPerformed
        CustomJPanelView jOpt = new CustomJPanelView();
        jOpt.setPanel("black");
        int aksi = jOpt.displayConfirmDialog(panelTambahBuku, "Apakah anda yakin\ningin mengatur ulang form\nyang sudah anda isikan?", "Konfirmasi");
        switch (aksi) {
            case 0: //yes option
            {
                tf_newJudulBuku.setText("");
                ftf_newIsbn.setText("");
                tf_newPenulisBuku.setText("");
                tf_newPenerbitBuku.setText("");
                cb_newKategoriBuku.setSelectedIndex(-1);
                ftf_newTahunBuku.setText("");
                ftf_newHargaJual.setText("");
                ftf_newHargaPokok.setText("");
                break;
            }
            default:
            //nothing
        }
    }//GEN-LAST:event_btnResetNewBukuActionPerformed

    /**
     * bernilai true bila tidak kosong
     */
    private boolean cekJudul,
            cekIsbn,
            cekPenulis,
            cekPenerbit,
            cekKategori,
            cekTahun,
            cekHargaPokok,
            cekHargaJual;

    private Buku newBuku = new Buku();

    private void btnSelectCoverBukuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectCoverBukuActionPerformed
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image File", "jpg", "jpeg");
        JFileChooser fc = new JFileChooser(FileSystemView.getFileSystemView());
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.removeChoosableFileFilter(fc.getFileFilter());
        fc.setFileFilter(filter);
        int aksi = fc.showOpenDialog(panelTambahBuku);
        if (aksi == JFileChooser.APPROVE_OPTION) {
            try {
                File img = fc.getSelectedFile();
                newBuku.setImage(Formatter.extractBytes(img));
                ImageIcon ii = new ImageIcon(Formatter.buffBytes(newBuku.getImage()));
                int f = 140 / ii.getIconHeight();
                Image image = ii.getImage().getScaledInstance(ii.getIconWidth() * f, ii.getIconHeight() * f, Image.SCALE_SMOOTH);
                newCoverBuku.setIcon(new ImageIcon(image));
            } catch (FileNotFoundException ex) {
                System.out.println("Error (FileNotFound): " + ex.getMessage());
            } catch (IOException ex) {
                System.out.println("Error (IO): " + ex.getMessage());
            }
        }
    }//GEN-LAST:event_btnSelectCoverBukuActionPerformed

    private void btnDeleteCoverBukuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteCoverBukuActionPerformed
        newBuku.setImage(null);
        ImageIcon ii = new ImageIcon(getClass().getResource("/tokobuku/images/book.png"));
        Image image = ii.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH);
        newCoverBuku.setIcon(new ImageIcon(image));
    }//GEN-LAST:event_btnDeleteCoverBukuActionPerformed

    private void tf_newJudulBukuKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tf_newJudulBukuKeyReleased
        cekJudul = !tf_newJudulBuku.getText().isEmpty();
        labelWarningJudulBuku.setVisible(!cekJudul);
    }//GEN-LAST:event_tf_newJudulBukuKeyReleased

    private void ftf_newIsbnKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ftf_newIsbnKeyReleased
        cekIsbn = !ftf_newIsbn.getText().isEmpty();
        labelWarningIsbn.setVisible(!cekIsbn);
    }//GEN-LAST:event_ftf_newIsbnKeyReleased

    private void tf_newPenulisBukuKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tf_newPenulisBukuKeyReleased
        cekPenulis = !tf_newPenulisBuku.getText().isEmpty();
        labelWarningPenulis.setVisible(!cekPenulis);
    }//GEN-LAST:event_tf_newPenulisBukuKeyReleased

    private void tf_newPenerbitBukuKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tf_newPenerbitBukuKeyReleased
        cekPenerbit = !tf_newPenerbitBuku.getText().isEmpty();
        labelWarningPenerbitBuku.setVisible(!cekPenerbit);
    }//GEN-LAST:event_tf_newPenerbitBukuKeyReleased

    private void ftf_newTahunBukuKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ftf_newTahunBukuKeyReleased
        cekTahun = !ftf_newTahunBuku.getText().isEmpty();
        labelWarningTahun.setVisible(!cekTahun);
    }//GEN-LAST:event_ftf_newTahunBukuKeyReleased

    private void ftf_newHargaPokokKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ftf_newHargaPokokKeyReleased
        cekHargaPokok = !ftf_newHargaPokok.getText().isEmpty();
        labelWarningHargaPokok.setVisible(!cekHargaPokok);
    }//GEN-LAST:event_ftf_newHargaPokokKeyReleased

    private void ftf_newHargaJualKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ftf_newHargaJualKeyReleased
        cekHargaJual = !ftf_newHargaJual.getText().isEmpty();
        labelWarningHargaJual.setVisible(!cekHargaJual);
    }//GEN-LAST:event_ftf_newHargaJualKeyReleased

    private void btnTambahkanBukuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahkanBukuActionPerformed
        /**
         * bernilai true bila semua kolom tidak kosong
         */
        boolean cekAll = cekJudul && cekIsbn && cekPenulis && cekPenerbit && cekKategori && cekTahun && cekHargaPokok && cekHargaJual;
        if (cekAll) {
            try {
                newBuku.setJudul_buku(tf_newJudulBuku.getText());
                newBuku.setIsbn(Formatter.destructIsbn(ftf_newIsbn.getText()));
                newBuku.setPenulis(tf_newPenulisBuku.getText());
                newBuku.setPenerbit(tf_newPenerbitBuku.getText());
                newBuku.setKategori(cb_newKategoriBuku.getSelectedItem().toString());
                newBuku.setTahun(ftf_newTahunBuku.getText());
                String[] newHJ = ftf_newHargaJual.getText().split(",");
                String hj = "";
                for (String newHJ1 : newHJ) {
                    hj += newHJ1;
                }
                newBuku.setHarga_jual(Integer.parseInt(hj));

                String[] newHP = ftf_newHargaPokok.getText().split(",");
                String hp = "";
                for (String newHP1 : newHP) {
                    hp += newHP1;
                }
                newBuku.setHarga_pokok(Integer.parseInt(hp));
                buku.insert(newBuku);
                panelTambahBuku.setVisible(false);

                loadDataBuku(0);
                newBuku = new Buku();

                btnDeleteCoverBukuActionPerformed(evt);
                tf_newJudulBuku.setText("");
                ftf_newIsbn.setText("");
                tf_newPenulisBuku.setText("");
                tf_newPenerbitBuku.setText("");
                cb_newKategoriBuku.setSelectedIndex(-1);
                ftf_newTahunBuku.setText("");
                ftf_newHargaJual.setText("");
                ftf_newHargaPokok.setText("");
            } catch (SQLException ex) {

            }
        }
    }//GEN-LAST:event_btnTambahkanBukuActionPerformed

    private void cb_newKategoriBukuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_newKategoriBukuActionPerformed
        try {
            cekKategori = cb_newKategoriBuku.getSelectedItem().toString().length() > 0;
            labelWarningKategori.setVisible(!cekKategori);
        } catch (NullPointerException ex) {
            
        }
    }//GEN-LAST:event_cb_newKategoriBukuActionPerformed

    private void btnMoreKategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoreKategoriActionPerformed
        JDialog dialog = new JDialog();
        JOptionPane optionPane = new JOptionPane(new KategoriPanelView(), JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
        dialog.setTitle("Daftar Kategori");
        dialog.setBackground(new java.awt.Color(55, 55, 55));
        dialog.setModal(true);
        dialog.setLocationByPlatform(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setContentPane(optionPane);
        dialog.pack();
        dialog.setVisible(true);
        loadDataKategori();
    }//GEN-LAST:event_btnMoreKategoriActionPerformed

    private void loadDataDetailTransaksi() {
        listDetailTransaksis = detailTrx.listDetailTransaksis;
        setListDetailTransaksiPanel();
    }

    private void setListDetailTransaksiPanel() {
        scrollPanelDTtambah.setVisible(false);
        panelDTtambah.removeAll();
        int rows = listDetailTransaksis.size();
        int height = (rows * 50);
        panelDTtambah.setLayout(new GridLayout(rows, 1, 0, 0));
        if (rows < 7) {
            if (rows == 0) {
                scrollPanelDTtambah.setPreferredSize(new Dimension(610, 340));
            } else {
                scrollPanelDTtambah.setPreferredSize(new Dimension(610, height + 5));
            }
        } else {
            scrollPanelDTtambah.setPreferredSize(new Dimension(610, 340));
        }
        panelDTtambah.setPreferredSize(new Dimension(610, height));
        int i = 0;
        for (DetailTransaksi list : listDetailTransaksis) {
            i++;
            AddDetailTransaksiView newPanel = new AddDetailTransaksiView(listBuku, detailTrx, i);
            newPanel.apply(panelDTtambah, tf_totalBayar);
        }
        SwingUtilities.updateComponentTreeUI(panelTambahTransaksi);
        scrollPanelDTtambah.setVisible(true);
    }

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

    /**
     * Update Waktu secara realtime
     */
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

    /**
     * Mengubah tanggal dan juga nomor transaksi pada panel tambah transaksi
     */
    private synchronized void setInfoTambahTrx() {
        textIDTrx.setText(String.valueOf(listTransaksi.get(listTransaksi.size() - 1).getId() + 1));
        tf_tanggal.setText(String.valueOf(tanggal.tanggal(new Date())));
    }

    /**
     * Mengubah visibilitas dari panel-panel yang terdapat pada panel transaksi.
     *
     * @param info
     * @param detail
     * @param tambah
     */
    private void setPanelTransaksi(Boolean info, Boolean detail, Boolean tambah) {
        panelInfo.setVisible(info);
        panelDetailTransaksi.setVisible(detail);
        panelTambahTransaksi.setVisible(tambah);
    }

    /**
     * Set Default TextField pada Panel Tambah Pelanggan
     */
    private void clearTFpelanggan() {
        tf_newNamaPelanggan.setText("Masukkan Nama Pelanggan");
        tf_newNamaPelanggan.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        tf_newAlamatP.setText("Masukkan Alamat");
        tf_newAlamatP.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        tf_newNoTelp.setText("Masukkan Nomor Telepon");
        tf_newNoTelp.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
    }

    private final List<String> arrayPelanggans = new ArrayList<>();

    /**
     * Mengatur item pilihan pada combo box pelanggan
     */
    private void setCBpelanggan() {
        inputNamaT = false;
        arrayPelanggans.clear();
        cb_namaPelanggan.removeAllItems();
        listPelanggans.forEach((p) -> {
            try {
                arrayPelanggans.add(p.getNama_pelanggan());
                cb_namaPelanggan.addItem(p.getNama_pelanggan());
            } catch (NullPointerException ex) {
                System.out.println("NoData");
            }
        });
        cb_namaPelanggan.setSelectedIndex(-1);

        JTextField textField = (JTextField) cb_namaPelanggan.getEditor().getEditorComponent();
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                SwingUtilities.invokeLater(() -> {
                    filterNamaPelanggan(textField.getText());
                });
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && cb_namaPelanggan.getItemCount() > 0) {
                    inputNamaT = true;
                    textField.setText(cb_namaPelanggan.getItemAt(0));
                    cb_namaPelanggan.setPopupVisible(false);
                    textField.transferFocus();
                }
            }
        });
    }

    private synchronized void filterNamaPelanggan(String keyword) {
        if (!cb_namaPelanggan.isPopupVisible()) {
            cb_namaPelanggan.showPopup();
        }

        List<String> filterArray = new ArrayList<>();

        for (int i = 0; i < arrayPelanggans.size(); i++) {
            if (arrayPelanggans.get(i).toLowerCase().contains(keyword.toLowerCase())) {
                filterArray.add(arrayPelanggans.get(i));
            }
        }

        if (filterArray.size() > 0) {
            DefaultComboBoxModel model = (DefaultComboBoxModel) cb_namaPelanggan.getModel();
            model.removeAllElements();
            filterArray.forEach((s) -> {
                model.addElement(s);
            });
            JTextField textField = (JTextField) cb_namaPelanggan.getEditor().getEditorComponent();
            textField.setText(keyword);
        }
    }

    private void setDataPegawai() {
        namaKasirText.setText(PreferencedHelper.getName());
        noTeleponText.setText(PreferencedHelper.getTel());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel backgroundPanel;
    private javax.swing.JPanel basePanel;
    private javax.swing.JButton btnCopyGeneratePass;
    private javax.swing.JButton btnDeleteCoverBuku;
    private javax.swing.JButton btnEditPassword;
    private javax.swing.JButton btnEditPegawai;
    private javax.swing.JButton btnGeneratePass;
    private javax.swing.ButtonGroup btnGroupAkses;
    private javax.swing.ButtonGroup btnGroupNewAkses;
    private javax.swing.ButtonGroup btnGroupNewShif;
    private javax.swing.ButtonGroup btnGroupShif;
    private javax.swing.JButton btnHapusPegawai;
    private javax.swing.JButton btnMoreKategori;
    private javax.swing.JButton btnResetNewBuku;
    private javax.swing.JButton btnResetNewPegawai;
    private javax.swing.JButton btnSelectCoverBuku;
    private javax.swing.JButton btnShowPwd;
    private javax.swing.JButton btnShowUpwd;
    private javax.swing.JButton btnTambahBuku;
    private javax.swing.JButton btnTambahPegawai;
    private javax.swing.JButton btnTambahkanBuku;
    private javax.swing.JButton btnTambahkanPegawai;
    private javax.swing.JButton buttonAddItem;
    private javax.swing.JButton buttonAddTransaksi;
    private javax.swing.JButton buttonPencarian;
    private javax.swing.JButton buttonReset;
    private javax.swing.JButton buttonTambahDataTrx;
    private javax.swing.JButton buttonTambahPelanggan;
    private javax.swing.JButton buttonTambahkanPelanggan;
    private javax.swing.JComboBox<String> cb_namaPelanggan;
    private javax.swing.JComboBox<String> cb_newKategoriBuku;
    private javax.swing.JLabel closeButton;
    private javax.swing.JLabel curTime;
    private javax.swing.JPanel dragPanel;
    private javax.swing.JTextField fieldPencarian;
    private javax.swing.JFormattedTextField ftf_newHargaJual;
    private javax.swing.JFormattedTextField ftf_newHargaPokok;
    private javax.swing.JFormattedTextField ftf_newIsbn;
    private javax.swing.JTextField ftf_newTahunBuku;
    private javax.swing.JTextField headAlamat;
    private javax.swing.JTextField headEdit;
    private javax.swing.JTextField headHapus;
    private javax.swing.JTextField headNama;
    private javax.swing.JTextField headNoTelp;
    private javax.swing.JTextField headNomor;
    private javax.swing.JPanel headerDaftarBuku;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JPasswordField jpf_newPass;
    private javax.swing.JPasswordField jpf_newUpass;
    private javax.swing.JLabel labelIDPegawai;
    private javax.swing.JLabel labelInformasiAkun;
    private javax.swing.JLabel labelInformasiBuku;
    private javax.swing.JLabel labelKategoriBuku;
    private javax.swing.JLabel labelPengaturanAkun;
    private javax.swing.JLabel labelToolAkun;
    private javax.swing.JLabel labelWarningAkses;
    private javax.swing.JLabel labelWarningAlamat;
    private javax.swing.JLabel labelWarningHargaJual;
    private javax.swing.JLabel labelWarningHargaPokok;
    private javax.swing.JLabel labelWarningIsbn;
    private javax.swing.JLabel labelWarningJudulBuku;
    private javax.swing.JLabel labelWarningKategori;
    private javax.swing.JLabel labelWarningNama;
    private javax.swing.JLabel labelWarningNoTelp;
    private javax.swing.JLabel labelWarningPW;
    private javax.swing.JLabel labelWarningPenerbitBuku;
    private javax.swing.JLabel labelWarningPenulis;
    private javax.swing.JLabel labelWarningPwd;
    private javax.swing.JLabel labelWarningShif;
    private javax.swing.JLabel labelWarningTahun;
    private javax.swing.JLabel labelWarningUN;
    private javax.swing.JLabel labelWarningUPW;
    private javax.swing.JLabel labelWarningUname;
    private javax.swing.JLabel labelWarningUpwd;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JLabel listBukuText;
    private javax.swing.JLabel listBukuText1;
    private javax.swing.JLabel listBukuText3;
    private javax.swing.JLabel listPegawaiText;
    private javax.swing.JButton logoutButton;
    private javax.swing.JLabel minimizedIcon;
    private javax.swing.JLabel namaKasirText;
    private javax.swing.JLabel newCoverBuku;
    private javax.swing.JLabel newNomorPegawai;
    private javax.swing.JLabel noTeleponText;
    private javax.swing.JPanel panelCoverBuku;
    private javax.swing.JPanel panelDT;
    private javax.swing.JPanel panelDTPegawai;
    private javax.swing.JPanel panelDTtambah;
    private javax.swing.JPanel panelDetailTransaksi;
    private javax.swing.JPanel panelEditPegawai;
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
    private javax.swing.JPanel panelHeaderPelanggan;
    private javax.swing.JPanel panelInfo;
    private javax.swing.JPanel panelListBuku;
    private javax.swing.JPanel panelListPegawai;
    private javax.swing.JPanel panelListPelanggan;
    private javax.swing.JPanel panelListTransaksi;
    private javax.swing.JPanel panelLoadingBuku;
    private javax.swing.JPanel panelNewAkses;
    private javax.swing.JPanel panelNewShif;
    private javax.swing.JPanel panelNoTrx;
    private javax.swing.JPanel panelTableHeader;
    private javax.swing.JPanel panelTableHeaderTambah;
    private javax.swing.JPanel panelTambahBuku;
    private javax.swing.JPanel panelTambahPegawai;
    private javax.swing.JPanel panelTambahPelanggan;
    private javax.swing.JPanel panelTambahTransaksi;
    private javax.swing.JPanel panelTools;
    private javax.swing.JLabel panelTotalTambah;
    private javax.swing.JPanel panelTransaksi;
    private javax.swing.JLabel pencarianText;
    private javax.swing.JRadioButton rb_admin;
    private javax.swing.JRadioButton rb_kasir;
    private javax.swing.JRadioButton rb_newAdmin;
    private javax.swing.JRadioButton rb_newKasir;
    private javax.swing.JRadioButton rb_newPagi;
    private javax.swing.JRadioButton rb_newSore;
    private javax.swing.JRadioButton rb_pagi;
    private javax.swing.JRadioButton rb_sore;
    private javax.swing.JScrollPane scrolPanellListPelanggan;
    private javax.swing.JScrollPane scrollPaneListlPegawai;
    private javax.swing.JScrollPane scrollPanelDT;
    private javax.swing.JScrollPane scrollPanelDTtambah;
    private javax.swing.JScrollPane scrollPanelListBuku;
    private javax.swing.JScrollPane scrollPanelTransaksi;
    private javax.swing.JLabel selamatDatangText;
    private javax.swing.JSeparator sep1;
    private javax.swing.JSeparator sep2;
    private javax.swing.JSeparator sep3;
    private javax.swing.JSeparator sep4;
    private javax.swing.JSeparator sep5;
    private javax.swing.JComboBox<String> sortingComboBox;
    private javax.swing.JLabel sortingText;
    private javax.swing.JPanel tabDaftarBuku;
    private javax.swing.JPanel tabDashboard;
    private javax.swing.JPanel tabPegawai;
    private javax.swing.JPanel tabPelanggan;
    private javax.swing.JPanel tabTransaksi;
    private javax.swing.JTabbedPane tabbedPanel;
    private javax.swing.JLabel textBayarDT;
    private javax.swing.JLabel textHarga;
    private javax.swing.JLabel textHarga1;
    private javax.swing.JLabel textIDTrx;
    private javax.swing.JLabel textInputAlamat;
    private javax.swing.JLabel textInputNama;
    private javax.swing.JLabel textInputTelp;
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
    private javax.swing.JTextField tf_alamatPegawai;
    private javax.swing.JTextField tf_generatePassword;
    private javax.swing.JTextField tf_namaPegawai;
    private javax.swing.JTextField tf_newAlamatP;
    private javax.swing.JTextField tf_newAlamatPegawai;
    private javax.swing.JTextField tf_newJudulBuku;
    private javax.swing.JTextField tf_newNamaPegawai;
    private javax.swing.JTextField tf_newNamaPelanggan;
    private javax.swing.JTextField tf_newNoTelp;
    private javax.swing.JTextField tf_newNotelpPegawai;
    private javax.swing.JTextField tf_newPenerbitBuku;
    private javax.swing.JTextField tf_newPenulisBuku;
    private javax.swing.JTextField tf_newUnamePegawai;
    private javax.swing.JTextField tf_notelpPegawai;
    private javax.swing.JTextField tf_tanggal;
    private javax.swing.JTextField tf_totalBayar;
    private javax.swing.JTextField tf_uangTunai;
    private javax.swing.JTextField tf_unamePegawai;
    private javax.swing.JLabel titleText;
    private javax.swing.JButton ubahProfilButton;
    private javax.swing.JLabel versionText;
    // End of variables declaration//GEN-END:variables

}
