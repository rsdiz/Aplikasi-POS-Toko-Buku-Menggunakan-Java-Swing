package tokobuku.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Class Generic ini berfungsi untuk
 * membantu pemformatan mata uang maupun tanggal dalam
 * format Negara Indonesia
 *
 * @author Rosyid Iz
 * @param <T>
 */
public class Formatter<T> {

    private T t;

    public T rupiah(T t) {
        DecimalFormat formatter = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRupiah = new DecimalFormatSymbols();
        formatRupiah.setCurrencySymbol("Rp. ");
        formatRupiah.setMonetaryDecimalSeparator(',');
        formatRupiah.setGroupingSeparator('.');
        formatter.setDecimalFormatSymbols(formatRupiah);
        formatter.setMaximumFractionDigits(0);
        this.t = (T) formatter.format(t);
        return this.t;
    }
    
    public T tanggal(T t) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy", new Locale("id"));
        this.t = (T) formatter.format(t);
        return this.t;
    }
}
