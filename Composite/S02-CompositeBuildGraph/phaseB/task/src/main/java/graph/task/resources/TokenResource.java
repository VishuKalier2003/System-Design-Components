package graph.task.resources;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.springframework.stereotype.Component;

import graph.task.model.Resource;

@Component
public class TokenResource implements Resource<String> {

    private final SecureRandom RAND = new SecureRandom();
    private final String HEX = "0123456789abcdef";

    public String hex(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++)
            sb.append(HEX.charAt(RAND.nextInt(HEX.length())));
        return sb.toString();
    }

    public String sha256(int length) {
        try {
            byte[] seed = new byte[32];
            RAND.nextBytes(seed);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(seed);
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.substring(0, length);
        } catch (NoSuchAlgorithmException e) { throw new RuntimeException(e); }
    }

    @Override public String provide() {
        return sha256(12);
    }
}
