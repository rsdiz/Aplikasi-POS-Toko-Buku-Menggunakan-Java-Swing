package tokobuku.util;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Rosyid Iz
 */
public class CustomFont {
    private static final String FILE_ASAP = "/tokobuku/fonts/Asap-Regular.ttf";
    private static final String FILE_BAHNSCHRIFT = "/tokobuku/fonts/bahnschrift.ttf";
    private static final String FILE_BEBAS = "/tokobuku/fonts/bebas.ttf";
    private static final String FILE_TAHOMA = "/tokobuku/fonts/tahoma.ttf";
    
    private static Font fontAsap;
    private static Font fontBahnschrift;
    private static Font fontBebas;
    private static Font fontTahoma;

    public CustomFont() {
        try {
            InputStream is = CustomFont.class.getResourceAsStream(FILE_ASAP);
            fontAsap = Font.createFont(Font.TRUETYPE_FONT, is);
            is = CustomFont.class.getResourceAsStream(FILE_BAHNSCHRIFT);
            fontBahnschrift = Font.createFont(Font.TRUETYPE_FONT, is);
            is = CustomFont.class.getResourceAsStream(FILE_BEBAS);
            fontBebas = Font.createFont(Font.TRUETYPE_FONT, is);
            is = CustomFont.class.getResourceAsStream(FILE_TAHOMA);
            fontTahoma = Font.createFont(Font.TRUETYPE_FONT, is);
            is.close();
        } catch (FontFormatException | IOException ex) {
            fontAsap = new Font("Asap",0,12);
            fontBahnschrift = new Font("Bahnschrift",0,12);
            fontBebas = new Font("Bebas",0,12);
            fontTahoma = new Font("Tahoma",0,12);            
        }
    }
    
    public static Font getFont(String font, float size) {
        if ("asap".equalsIgnoreCase(font)) {
            return fontAsap = fontAsap.deriveFont(size);
        } else if ("bahnschrift".equalsIgnoreCase(font)) {
            return fontBahnschrift = fontBahnschrift.deriveFont(size);
        } else if ("bebas".equalsIgnoreCase(font)) {
            return fontBebas = fontBebas.deriveFont(size);
        } else if ("tahoma".equalsIgnoreCase(font)) {
            return fontTahoma = fontTahoma.deriveFont(size);
        } else {
            Font returnFont = Float.isNaN(size) ? getFont() : getFont(size);
            return returnFont;
        }
    }

    public static Font getFont(String font, int type, float size) {
        if ("asap".equalsIgnoreCase(font)) {
            return fontAsap = fontAsap.deriveFont(type, size);
        } else if ("bahnschrift".equalsIgnoreCase(font)) {
            return fontBahnschrift = fontBahnschrift.deriveFont(type, size);
        } else if ("bebas".equalsIgnoreCase(font)) {
            return fontBebas = fontBebas.deriveFont(type, size);
        } else if ("tahoma".equalsIgnoreCase(font)) {
            return fontTahoma = fontTahoma.deriveFont(type, size);
        } else {
            Font returnFont = Float.isNaN(size) ? getFont() : getFont(type, size);
            return returnFont;
        }
    }

    public static Font getFont() {
        return fontTahoma = fontTahoma.deriveFont(12);
    }
    
    public static Font getFont(float size) {
        return fontTahoma = fontTahoma.deriveFont(size);
    }
    
    public static Font getFont(int type, float size) {
        return fontTahoma = fontTahoma.deriveFont(type, size);
    }
}
