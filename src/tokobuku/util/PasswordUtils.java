package tokobuku.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Encrypt user password and verify user password,
 * Hasil copy paste broh:v
 * @author Rosyid Iz
 */
public class PasswordUtils {
    private static final Random RANDOM = new SecureRandom();
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int ITERATION = 99999;
    private static final int KEY_LENGTH = 256;
    
    /**
     * Generate Salt
     * @param length
     * @return a <code>String</code>
     */
    public static String getSalt(int length) {
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            stringBuilder.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return new String(stringBuilder);
    }
    
    /**
     * Hashing a password
     * @param password
     * @param salt
     * @return hash password, a <code>Byte[]</code>
     */
    public static byte[] hash(char[] password, byte[] salt) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATION, KEY_LENGTH);
        Arrays.fill(password, Character.MIN_VALUE);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing a password: "+e.getMessage());
        } finally {
            spec.clearPassword();
        }
    }
    
    /**
     * Generate secure password with salt
     * @param password
     * @param salt
     * @return a <code>String</code>
     */
    public static String generateSecurePassword(String password, String salt) {
        byte[] securePassword = hash(password.toCharArray(), salt.getBytes());
        return Base64.getEncoder().encodeToString(securePassword);
    }
    
    /**
     * Check password
     * @param checkPassword
     * @param securePassword
     * @param salt
     * @return a <code>Boolean</code>
     */
    public static boolean veriifyPassword(String checkPassword, String securePassword, String salt) {
        String newSecurePassword = generateSecurePassword(checkPassword, salt);
        return newSecurePassword.equalsIgnoreCase(securePassword);
    }
}
