package tokobuku.util;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;
import javax.imageio.ImageIO;

/**
 * Class Generic ini berfungsi untuk membantu pemformatan mata uang maupun
 * tanggal dalam format Negara Indonesia
 *
 * @author Rosyid Iz
 * @param <T>
 */
public class Formatter<T> {

    private T t;

    public Formatter() {
    }

    public static String structIsbn(String isbn) {
        return String.format("%s-%s-%s-%s-%s",
                isbn.substring(0, 0 + 3),
                isbn.substring(3, 3 + 3),
                isbn.substring(6, 6 + 4),
                isbn.substring(10, 10 + 2),
                isbn.substring(12, 12 + 1));
    }

    public static String destructIsbn(String isbn) {
        String returnValue = "";
        String[] arrayIsbn = isbn.split("-");
        for (String i : arrayIsbn) {
            returnValue += i;
        }
        return returnValue;
    }

    public static byte[] extractBytes(File fileName) throws IOException {
        BufferedImage buf = ImageIO.read(fileName);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(buf, "jpg", baos);

        return baos.toByteArray();
    }
    
    public static BufferedImage buffBytes(byte[] bytes) throws IOException{
        BufferedImage  buff = ImageIO.read(new ByteArrayInputStream(bytes));
        return buff;
    }

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
