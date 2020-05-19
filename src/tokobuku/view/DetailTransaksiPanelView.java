package tokobuku.view;

import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.JTextField;
import tokobuku.model.DetailTransaksi;
import tokobuku.util.CustomFont;
import tokobuku.util.Formatter;

/**
 * Tampilan Daftar Item pada Panel Detail Transaksi
 *
 * @author Rosyid Iz
 */
public class DetailTransaksiPanelView {
    
    private final static Font DEFAULT_FONT = new CustomFont().getFont("Bahnschrift", 14);
    private final int index;
    private final Formatter<Integer> uang;
    
    private final JPanel basePanel;
    private final JTextField tf_noDetail;
    private final JTextField tf_isbn;
    private final JTextField tf_qty;
    private final JTextField tf_harga;
    private final JTextField tf_total;
    
    private DetailTransaksi detailTransaksi;
    
    /**
     * Inisialisasi komponen-komponen dan nomer urutan/index
     * @param index
     */
    public DetailTransaksiPanelView(int index) {
        this.index = index;
        this.uang = new Formatter<>();
        
        basePanel = new javax.swing.JPanel();
        basePanel.setBackground(new java.awt.Color(45, 45, 45));
        basePanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        basePanel.setMinimumSize(new java.awt.Dimension(610, 50));
        basePanel.setPreferredSize(new java.awt.Dimension(605, 50));
        basePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tf_noDetail = new javax.swing.JTextField();
        tf_noDetail.setEditable(false);
        tf_noDetail.setBackground(new java.awt.Color(35, 35, 35));
        tf_noDetail.setFont(DEFAULT_FONT);
        tf_noDetail.setForeground(new java.awt.Color(255, 255, 255));
        tf_noDetail.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tf_noDetail.setText("NO");
        tf_noDetail.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        tf_noDetail.setCaretColor(new java.awt.Color(255, 255, 255));
        tf_noDetail.setFocusable(false);
        tf_noDetail.setPreferredSize(new java.awt.Dimension(40, 40));

        tf_isbn = new javax.swing.JTextField();
        tf_isbn.setEditable(false);
        tf_isbn.setBackground(new java.awt.Color(35, 35, 35));
        tf_isbn.setFont(DEFAULT_FONT);
        tf_isbn.setForeground(new java.awt.Color(255, 255, 255));
        tf_isbn.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tf_isbn.setText("ISBN");
        tf_isbn.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        tf_isbn.setCaretColor(new java.awt.Color(255, 255, 255));
        tf_isbn.setFocusable(false);
        tf_isbn.setPreferredSize(new java.awt.Dimension(180, 40));

        tf_qty = new javax.swing.JTextField();
        tf_qty.setEditable(false);
        tf_qty.setBackground(new java.awt.Color(35, 35, 35));
        tf_qty.setFont(DEFAULT_FONT);
        tf_qty.setForeground(new java.awt.Color(255, 255, 255));
        tf_qty.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tf_qty.setText("QTY");
        tf_qty.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        tf_qty.setCaretColor(new java.awt.Color(255, 255, 255));
        tf_qty.setFocusable(false);
        tf_qty.setPreferredSize(new java.awt.Dimension(80, 40));

        tf_harga = new javax.swing.JTextField();
        tf_harga.setEditable(false);
        tf_harga.setBackground(new java.awt.Color(35, 35, 35));
        tf_harga.setFont(DEFAULT_FONT);
        tf_harga.setForeground(new java.awt.Color(255, 255, 255));
        tf_harga.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tf_harga.setText("HARGA");
        tf_harga.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        tf_harga.setCaretColor(new java.awt.Color(255, 255, 255));
        tf_harga.setFocusable(false);
        tf_harga.setPreferredSize(new java.awt.Dimension(130, 40));

        tf_total = new javax.swing.JTextField();
        tf_total.setEditable(false);
        tf_total.setBackground(new java.awt.Color(35, 35, 35));
        tf_total.setFont(DEFAULT_FONT);
        tf_total.setForeground(new java.awt.Color(255, 255, 255));
        tf_total.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tf_total.setText("TOTAL");
        tf_total.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        tf_total.setCaretColor(new java.awt.Color(255, 255, 255));
        tf_total.setFocusable(false);
        tf_total.setPreferredSize(new java.awt.Dimension(140, 40));
    }
    
    /**
     * Fungsi untuk menerapkan penempatan komponen
     * komponen ke dalam panel
     * @param jPanel
     */
    public void apply(JPanel jPanel) {
        basePanel.add(tf_noDetail, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 5, -1, -1));
        basePanel.add(tf_isbn, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 5, -1, -1));
        basePanel.add(tf_qty, new org.netbeans.lib.awtextra.AbsoluteConstraints(235, 5, -1, -1));
        basePanel.add(tf_harga, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 5, -1, -1));
        basePanel.add(tf_total, new org.netbeans.lib.awtextra.AbsoluteConstraints(455, 5, -1, -1));
        jPanel.add(basePanel);
    }
    
    /**
     * Setter untuk Model DetailTransaksi
     * @param detailTransaksi 
     */
    public void setDetailTransaksi(DetailTransaksi detailTransaksi) {
        this.detailTransaksi = detailTransaksi;
        setTextToComponent();
    }
    
    /**
     * Mengubah text pada Component
     */
    private void setTextToComponent() {
        int total = (detailTransaksi.getBanyak() * detailTransaksi.getHarga());
        tf_noDetail.setText(String.valueOf(index));
        tf_isbn.setText(detailTransaksi.getIsbn());
        tf_qty.setText(String.valueOf(detailTransaksi.getBanyak()));
        tf_harga.setText(uang.rupiah(detailTransaksi.getHarga())+",-");
        tf_total.setText(uang.rupiah(total)+",-");
    }
}