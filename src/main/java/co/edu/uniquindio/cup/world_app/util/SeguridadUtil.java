package co.edu.uniquindio.cup.world_app.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utilidades de seguridad: hash de contraseñas con SHA-256.
 */
public final class SeguridadUtil {

    private SeguridadUtil() {}

    /**
     * Genera el hash SHA-256 de una cadena de texto.
     *
     * @param texto texto plano a hashear
     * @return representación hexadecimal del hash
     */
    public static String hashSHA256(String texto) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(texto.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 no disponible", e);
        }
    }

    /**
     * Verifica si un texto plano coincide con un hash SHA-256.
     */
    public static boolean verificar(String textPlano, String hash) {
        return hashSHA256(textPlano).equals(hash);
    }
}
