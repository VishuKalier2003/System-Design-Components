package fabric.sharding.utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Service;

@Service
public class ShaHasher {

    public long hashToLong(String transactionID) {
        try {
            byte inputChannel[] = transactionID.getBytes(StandardCharsets.UTF_8);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte hashBytes[] = digest.digest(inputChannel);
            BigInteger bigInt = new BigInteger(1, hashBytes);
            long value = bigInt.longValue();
            return value & Long.MAX_VALUE;
        } catch(NoSuchAlgorithmException e) {
            throw new RuntimeException("Some error in hashing !!!");
        }
    }
}
