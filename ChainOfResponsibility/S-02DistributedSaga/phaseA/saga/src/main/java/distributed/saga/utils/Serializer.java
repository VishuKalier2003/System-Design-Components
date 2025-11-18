package distributed.saga.utils;

import org.springframework.stereotype.Component;

@Component
public class Serializer {
    private int index = 0;
    public String generateTokenID(String word, int age) {
        return "tkn"+word+"#8DY"+age;
    }

    public String generateKycID(String aadhar, String token) {
        return "kyc" + aadhar + token;
    }

    public double evaluateRisk(String kyc, String tkn) {
        return Integer.parseInt(kyc) / (Integer.parseInt(tkn) + 0.0d);
    }

    public String generateTransactionID() {
        index++;
        return "task"+index;
    }
}
