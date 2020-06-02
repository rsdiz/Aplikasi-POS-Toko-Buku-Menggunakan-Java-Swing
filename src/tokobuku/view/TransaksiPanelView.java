package tokobuku.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import tokobuku.impl.DetailTransaksiImpl;
import tokobuku.model.DetailTransaksi;
import tokobuku.model.Transaksi;
import tokobuku.util.CustomFont;
import tokobuku.util.Formatter;

/**
 * Tampilan Panel pada Transaksi
 *
 * @author Rosyid Iz
 */
public class TransaksiPanelView {

    private final Formatter<Date> dateF;
    private final Formatter<Double> doubleF;
    private final Formatter<Integer> intF;

    // List Transaksi
    private final JPanel panelListTransaksi;
    private final JPanel baseNumberPanel;
    private final JLabel number;
    private final JLabel tanggal;
    private final JLabel namaPembeli;
    private Transaksi transaksi;
    private final DetailTransaksiImpl detailTrx;
    private List<DetailTransaksi> listDetailTrx;
    private final int index;
    //Detail Transaksi
    private JPanel panelDetailTransaksi;
    private JPanel panelDT;
    private JScrollPane scrollPanelDT;
    private JPanel panelInfo;
    private JPanel panelTambahTransaksi;
    private JButton buttonAddTransaksi;
    private JLabel txtNoTrx;
    private JLabel txtNama_pelanggan;
    private JLabel txtNama_kasir;
    private JLabel txtUang_tunai;
    private JLabel txtTotal;
    private JLabel txtTanggal;

    public TransaksiPanelView(int index) {
        this.index = index;
        this.intF = new Formatter<>();
        this.doubleF = new Formatter<>();
        this.dateF = new Formatter<>();
        this.detailTrx = new DetailTransaksiImpl();
        this.listDetailTrx = detailTrx.listDetailTransaksis;
        panelListTransaksi = new JPanel();
        panelListTransaksi.setBackground(new Color(60, 60, 60));
        panelListTransaksi.setPreferredSize(new Dimension(407, 100));
        panelListTransaksi.setLayout(new AbsoluteLayout());

        baseNumberPanel = new JPanel();
        baseNumberPanel.setBackground(new Color(80, 80, 80));

        number = new JLabel();
        number.setForeground(Color.WHITE);
        number.setFont(CustomFont.getFont("bebas", 36));
        number.setHorizontalAlignment(SwingConstants.CENTER);

        tanggal = new JLabel();
        tanggal.setForeground(Color.WHITE);
        tanggal.setFont(CustomFont.getFont("Bahnschrift", 1, 18));

        namaPembeli = new JLabel();
        namaPembeli.setForeground(Color.WHITE);
        namaPembeli.setFont(CustomFont.getFont("Bahnschrift", 14));
        namaPembeli.setVerticalAlignment(SwingConstants.TOP);

    }

    public void apply(JPanel panel) {
        GroupLayout baseNumberLayout = new GroupLayout(baseNumberPanel);

        baseNumberPanel.setLayout(baseNumberLayout);

        baseNumberLayout.setHorizontalGroup(
                baseNumberLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(number, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
        );

        baseNumberLayout.setVerticalGroup(
                baseNumberLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(number, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
        );
        panelListTransaksi.add(baseNumberPanel, new AbsoluteConstraints(10, 10, 80, 80));
        panelListTransaksi.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Runnable runnable = () -> {
                    panelDT.removeAll();
                    listDetailTrx = detailTrx.listDetailTransaksis;
                    if (buttonAddTransaksi.getText().equalsIgnoreCase("batal")) {
                        buttonAddTransaksi.setText("Tambah Transaksi");
                    }
                    if (panelDetailTransaksi.isVisible()) {
                        if (txtNoTrx.getText().equals("No Transaksi: " + transaksi.getId())) {
                            panelTambahTransaksi.setVisible(false);
                            panelDetailTransaksi.setVisible(false);
                            panelInfo.setVisible(true);
                        } else {
                            setDetail();
                            setListDetail();
                        }
                    } else {
                        setDetail();
                        setListDetail();
                        panelTambahTransaksi.setVisible(false);
                        panelInfo.setVisible(false);
                        panelDetailTransaksi.setVisible(true);
                    }
                };
                runnable.run();
            }
        });
        panelListTransaksi.add(tanggal, new AbsoluteConstraints(110, 10, 230, 40));
        panelListTransaksi.add(namaPembeli, new AbsoluteConstraints(110, 60, 230, 30));

        panel.add(panelListTransaksi);
    }

    public void setTransaksi(Transaksi transaksi) {
        this.transaksi = transaksi;
        setProperty();
        syncDataDetailTransaksi();
    }

    private void setProperty() {
        // set Number
        number.setText(String.valueOf(index));
        // set Tanggal
        try {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(transaksi.getTanggal());
            tanggal.setText(String.valueOf(new Formatter<>().tanggal(date1)));
        } catch (ParseException ex) {
        }
        // set Nama Pembeli
        namaPembeli.setText(transaksi.getNamaPelanggan());
    }
    
    private void syncDataDetailTransaksi() {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    listDetailTrx = detailTrx.load(transaksi.getId());
                } catch (SQLException ex) {
                    Logger.getLogger(TransaksiPanelView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        t.start();
    }

    public void initJLabel(
            JLabel txtNotrx,
            JLabel txtNama_pelanggan,
            JLabel txtNama_kasir,
            JLabel txtUang_tunai,
            JLabel txtTotal,
            JLabel txtTanggal
    ) {
        this.txtNoTrx = txtNotrx;
        this.txtNama_pelanggan = txtNama_pelanggan;
        this.txtNama_kasir = txtNama_kasir;
        this.txtUang_tunai = txtUang_tunai;
        this.txtTotal = txtTotal;
        this.txtTanggal = txtTanggal;
    }

    public void initBase(
            JPanel baseDetail,
            JScrollPane baseScrollPanel,
            JPanel baseTableDetail,
            JPanel baseInfo,
            JPanel baseTambah,
            JButton btnTambah
    ) {
        this.panelDetailTransaksi = baseDetail;
        this.panelDT = baseTableDetail;
        this.scrollPanelDT = baseScrollPanel;
        this.panelInfo = baseInfo;
        this.panelTambahTransaksi = baseTambah;
        this.buttonAddTransaksi = btnTambah;
    }

    private synchronized void setDetail() {
        txtNoTrx.setText("No Transaksi: " + transaksi.getId());
        txtNama_pelanggan.setText("Pelanggan: " + transaksi.getNamaPelanggan());
        txtNama_kasir.setText("Kasir: " + transaksi.getNamaPetugas());
        txtUang_tunai.setText("Uang Tunai: " + doubleF.rupiah(transaksi.getNominalBayar()) + ",-");
        txtTanggal.setText("Tanggal: " + transaksi.getTanggal());
        txtTotal.setText(intF.rupiah(transaksi.getTotalBayar()) + ",-");
    }

    private synchronized void setListDetail() {
        int rows = listDetailTrx.size();
        int height = 50 * rows;
        panelDT.setLayout(new GridLayout(rows, 1, 0, 0));
        if (rows < 8) {
            scrollPanelDT.setPreferredSize(new Dimension(610, height+3));
        } else {
            scrollPanelDT.setPreferredSize(new Dimension(610, 410));
        }
        panelDT.setPreferredSize(new Dimension(610, height));
        for (int i = 0, j = i + 1; i < rows; i++, j++) {
            DetailTransaksiPanelView dtrxview = new DetailTransaksiPanelView(j);
            dtrxview.setDetailTransaksi(listDetailTrx.get(i));
            dtrxview.apply(panelDT);
        }
        SwingUtilities.updateComponentTreeUI(panelDT);
    }
}