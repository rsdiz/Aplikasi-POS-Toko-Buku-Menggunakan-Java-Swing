package tokobuku.view;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import tokobuku.util.CustomFont;

/**
 *
 * @author Rosyid Iz
 */
public class TransaksiPanelView {
    
    private final JPanel basePanel;
    private final JPanel baseNumberPanel;
    private final JLabel number;
    private final JLabel tanggal;
    private final JLabel namaPembeli;
    
    public TransaksiPanelView(){
        basePanel = new JPanel();
        baseNumberPanel = new JPanel();
        number = new JLabel();
        tanggal = new JLabel();
        namaPembeli = new JLabel();
        basePanel.setBackground(new Color(203,134,49));
        basePanel.setPreferredSize(new Dimension(407, 100));
        basePanel.setLayout(new AbsoluteLayout());
        baseNumberPanel.setBackground(new Color(223, 144, 49));
        basePanel.add(baseNumberPanel, new AbsoluteConstraints(10, 10, 80, 80));
    };
    
    public void setProperty(
            int tNumber, 
            String tTanggal, 
            String tNamaPembeli
    ) {
        // set Number
        number.setFont(new CustomFont().getFont("bebas", 36));
        number.setHorizontalAlignment(SwingConstants.CENTER);
        number.setText(String.valueOf(tNumber));
        // set Tanggal
        tanggal.setFont(new CustomFont().getFont("Bahnschrift", 1, 18));
        tanggal.setText(tTanggal);
        // set Nama Pembeli
        namaPembeli.setFont(new CustomFont().getFont("Bahnschrift", 14)); 
        namaPembeli.setText(tNamaPembeli);
        namaPembeli.setVerticalAlignment(SwingConstants.TOP);
        // add
        basePanel.add(tanggal, new AbsoluteConstraints(110, 10, 230, 40));
        basePanel.add(namaPembeli, new AbsoluteConstraints(110, 60, 230, 30));
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
    }
    
    public void apply(JPanel panel) {
        panel.add(basePanel);
    }
}
