package tokobuku.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import tokobuku.model.Transaksi;
import tokobuku.util.Formatter;

/**
 *
 * @author Rosyid Iz
 */
public class NewTransaksiPanelView {

    private final int index;
    private final Transaksi transaksi;
    private final JSeparator sep1;
    private final JSeparator sep2;
    private final JSeparator sep3;
    private final JPanel basePanel;
    private final JLabel labelNoTrx;
    private final JLabel labelTglTrx;
    private final JLabel labelNamaPembeli;
    private final JLabel labelTotalTrx;
    private JPanel rootPanel;

    public NewTransaksiPanelView(Transaksi transaksi, int index) {
        this.index = index;
        this.transaksi = transaksi;
        basePanel = new javax.swing.JPanel();
        basePanel.setBackground(new java.awt.Color(65, 65, 65));
        basePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelNoTrx = new javax.swing.JLabel();
        labelNoTrx.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        labelNoTrx.setForeground(new java.awt.Color(255, 255, 255));
        labelNoTrx.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelNoTrx.setText("NO.");
        basePanel.add(labelNoTrx, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 60, 50));

        sep1 = new javax.swing.JSeparator();
        sep1.setBackground(new java.awt.Color(200, 200, 200));
        sep1.setForeground(new java.awt.Color(200, 200, 200));
        sep1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        basePanel.add(sep1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 15, 10, 20));

        labelTglTrx = new javax.swing.JLabel();
        labelTglTrx.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        labelTglTrx.setForeground(new java.awt.Color(255, 255, 255));
        labelTglTrx.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelTglTrx.setText("TANGGAL TRANSAKSI");
        basePanel.add(labelTglTrx, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 0, 300, 50));

        sep2 = new javax.swing.JSeparator();
        sep2.setBackground(new java.awt.Color(200, 200, 200));
        sep2.setForeground(new java.awt.Color(200, 200, 200));
        sep2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        basePanel.add(sep2, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 15, 10, 20));

        labelNamaPembeli = new javax.swing.JLabel();
        labelNamaPembeli.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        labelNamaPembeli.setForeground(new java.awt.Color(255, 255, 255));
        labelNamaPembeli.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelNamaPembeli.setText("NAMA PEMBELI");
        basePanel.add(labelNamaPembeli, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 0, 300, 50));

        sep3 = new javax.swing.JSeparator();
        sep3.setBackground(new java.awt.Color(200, 200, 200));
        sep3.setForeground(new java.awt.Color(200, 200, 200));
        sep3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        basePanel.add(sep3, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 15, 10, 20));

        labelTotalTrx = new javax.swing.JLabel();
        labelTotalTrx.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        labelTotalTrx.setForeground(new java.awt.Color(255, 255, 255));
        labelTotalTrx.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelTotalTrx.setText("TOTAL TRANSAKSI");
        basePanel.add(labelTotalTrx, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 0, 250, 50));
    }

    public void apply(JPanel rootPanel) {
        labelNoTrx.setText(String.valueOf(index));
        labelNamaPembeli.setText(transaksi.getNamaPelanggan());
        Date date1;
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd").parse(transaksi.getTanggal());
            labelTglTrx.setText(String.valueOf(new Formatter<>().tanggal(date1)));
        } catch (ParseException ex) {
        }
        try {
            labelTotalTrx.setText(String.valueOf(new Formatter<>().rupiah(transaksi.getNominalBayar())));
        } catch (NullPointerException ex) {
            labelTotalTrx.setText(String.valueOf(transaksi.getNominalBayar()));
        }
        this.rootPanel = rootPanel;
        rootPanel.add(basePanel);
    }

}
