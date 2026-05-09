package backend.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtils {
    
    /**
     * Hashes a plaintext password using SHA-256.
     * @param password Plaintext password
     * @return Hexadecimal representation of the hash
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }
    
    /**
     * Checks if a plaintext password matches a hashed password.
     * @param plaintext The password to check
     * @param hashed The stored hash
     * @return true if matches, false otherwise
     */
    public static boolean checkPassword(String plaintext, String hashed) {
        return hashPassword(plaintext).equals(hashed);
    }
}
