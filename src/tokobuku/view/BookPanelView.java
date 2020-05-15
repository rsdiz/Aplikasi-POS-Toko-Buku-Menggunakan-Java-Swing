package tokobuku.view;

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.ColorUIResource;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import tokobuku.util.CustomFont;

/**
 *
 * @author Rosyid Iz
 */
public class BookPanelView {

    private JLabel imageBuku = new JLabel();
    private final JLabel judulBukuText;
    private final JLabel penulisText;
    private final JLabel penerbitText;
    private final JLabel stokText;
    private final JLabel hargaJualText;
    private final JLabel kategoriText;
    private final JLabel isbnText;
    private final JPanel bookPanel;

    public BookPanelView() {
        this.judulBukuText = new JLabel();
        this.isbnText = new JLabel();
        this.penulisText = new JLabel();
        this.penerbitText = new JLabel();
        this.stokText = new JLabel();
        this.hargaJualText = new JLabel();
        this.kategoriText = new JLabel();
        this.bookPanel = new JPanel(true);
        bookPanel.setLayout(new AbsoluteLayout());
        bookPanel.setBounds(0, 0, 200, 200);
        bookPanel.setBackground(new ColorUIResource(0x212121));
        bookPanel.setForeground(Color.WHITE);
    }

    public void setProperty(
            byte[] image,
            String judul_buku,
            String isbn,
            String kategori,
            String penulis,
            String penerbit,
            String tahun,
            int stok,
            float harga_jual
    ) throws Exception { 
        // Set image from db
        if (image != null) {
            imageBuku = new JLabel(new ImageIcon(image));
        } else {
            imageBuku.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tokobuku/images/book.png")));
        }
        bookPanel.add(imageBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));
        
        //set judul buku
//        new java.awt.Font("Bahnschrift", 0, 18)
        judulBukuText.setFont(new CustomFont().getFont("bahnschrift", 18));
        judulBukuText.setText(judul_buku);
        judulBukuText.setToolTipText(judul_buku);
        bookPanel.add(judulBukuText, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, 360, -1));
//        new java.awt.Font("Tahoma", 0, 12)
        isbnText.setFont(new CustomFont().getFont("asap",12)); // NOI18N
        isbnText.setText("ISBN: "+isbn);
        bookPanel.add(isbnText, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 40, 230, -1));
        
        //set kategori
//        new java.awt.Font("Tahoma", 0, 14)
        kategoriText.setFont(new CustomFont().getFont(14));
        kategoriText.setText(kategori);
        bookPanel.add(kategoriText, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 60, 210, 20));
        
        //set penulis
//        new java.awt.Font("Tahoma", 0, 14)
        penulisText.setFont(new CustomFont().getFont(14));
        penulisText.setText(penulis);
        bookPanel.add(penulisText, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 80, 200, 30));
        
        //set penerbit
//        new java.awt.Font("Tahoma", 0, 14)
        penerbitText.setFont(new CustomFont().getFont(14));
        penerbitText.setText(penerbit+", "+tahun);
        bookPanel.add(penerbitText, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 110, 210, -1));
        
        //set stok
//        new java.awt.Font("Tahoma", 0, 18)
        stokText.setFont(new CustomFont().getFont(18));
        stokText.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        stokText.setText("Stok: "+String.valueOf(stok));
        bookPanel.add(stokText, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 50, 130, 20));
        
        //set harga_jual
//        new java.awt.Font("Tahoma", 1, 24)
        hargaJualText.setFont(new CustomFont().getFont().deriveFont(1, 20));
        hargaJualText.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        DecimalFormat format = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRupiah = new DecimalFormatSymbols();
        formatRupiah.setCurrencySymbol("Rp.");
        formatRupiah.setMonetaryDecimalSeparator(',');
        formatRupiah.setGroupingSeparator('.');
        format.setDecimalFormatSymbols(formatRupiah);
        format.setMaximumFractionDigits(0);
        hargaJualText.setText(format.format(harga_jual));
        hargaJualText.setToolTipText("Harga: "+format.format(harga_jual));
        bookPanel.add(hargaJualText, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 80, 140, 40));
    }
    
    public void apply(JPanel panel)
    {
        panel.add(bookPanel);
    }

}