package iam.aws.data.output;

import iam.aws.data.input.Info;
import iam.aws.data.input.TokenData;
import iam.aws.enums.Access;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder=true)        // allowing builder pattern to its best
public class Output {
    private String transactionID;
    private TokenData tknData;
    private Info info;
    private Access access;
}
