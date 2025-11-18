package distributed.saga.data;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder        // builder pattern implemented
public class Data {
    private String transactionID;
    private State state;
    // Data used by Identity Verification Service
    private String user;
    private int age;
    private String tokenID;
    // Data used by Kyc Verification Service
    private String aadhar;
    private String kycID;
    // Data used by Risk Evaluation Service
    private boolean evaluateRisk;
    private double riskScore;
    private List<String> logs;
}
