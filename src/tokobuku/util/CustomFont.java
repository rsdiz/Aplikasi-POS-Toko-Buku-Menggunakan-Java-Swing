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
    private final String fileAsap = "/tokobuku/fonts/Asap-Regular.ttf";
    private final String fileBahnschrift = "/tokobuku/fonts/bahnschrift.ttf";
    private final String fileBebas = "/tokobuku/fonts/bebas.ttf";
    private final String fileTahoma = "/tokobuku/fonts/tahoma.ttf";
    
    private Font fontAsap;
    private Font fontBahnschrift;
    private Font fontBebas;
    private Font fontTahoma;

    public CustomFont() {
        try {
            InputStream is = CustomFont.class.getResourceAsStream(fileAsap);
            fontAsap = Font.createFont(Font.TRUETYPE_FONT, is);
            is = CustomFont.class.getResourceAsStream(fileBahnschrift);
            fontBahnschrift = Font.createFont(Font.TRUETYPE_FONT, is);
            is = CustomFont.class.getResourceAsStream(fileBebas);
            fontBebas = Font.createFont(Font.TRUETYPE_FONT, is);
            is = CustomFont.class.getResourceAsStream(fileTahoma);
            fontTahoma = Font.createFont(Font.TRUETYPE_FONT, is);
            is.close();
        } catch (FontFormatException | IOException ex) {
            fontAsap = new Font("Asap",0,12);
            fontBahnschrift = new Font("Bahnschrift",0,12);
            fontBebas = new Font("Bebas",0,12);
            fontTahoma = new Font("Tahoma",0,12);            
        }
    }
    
    public Font getFont(String font, float size) {
        if ("asap".equalsIgnoreCase(font)) {
            return fontAsap = fontAsap.deriveFont(size);
        } else if ("bahnschrift".equalsIgnoreCase(font)) {
            return fontBahnschrift = fontBahnschrift.deriveFont(size);
        } else if ("bebas".equalsIgnoreCase(font)) {
            return fontBebas = fontBebas.deriveFont(size);
        } else if ("tahoma".equalsIgnoreCase(font)) {
            return fontTahoma = fontTahoma.deriveFont(size);
        } else {
            Font f;
            return f = Float.isNaN(size) ? getFont() : getFont(size);
        }
    }

    public Font getFont(String font, int type, float size) {
        if ("asap".equalsIgnoreCase(font)) {
            return fontAsap = fontAsap.deriveFont(type, size);
        } else if ("bahnschrift".equalsIgnoreCase(font)) {
            return fontBahnschrift = fontBahnschrift.deriveFont(type, size);
        } else if ("bebas".equalsIgnoreCase(font)) {
            return fontBebas = fontBebas.deriveFont(type, size);
        } else if ("tahoma".equalsIgnoreCase(font)) {
            return fontTahoma = fontTahoma.deriveFont(type, size);
        } else {
            Font f;
            return f = Float.isNaN(size) ? getFont() : getFont(type, size);
        }
    }

    public Font getFont() {
        return fontTahoma = fontTahoma.deriveFont(12);
    }
    
    public Font getFont(float size) {
        return fontTahoma = fontTahoma.deriveFont(size);
    }
    
    public Font getFont(int type, float size) {
        return fontTahoma = fontTahoma.deriveFont(type, size);
    }
}
