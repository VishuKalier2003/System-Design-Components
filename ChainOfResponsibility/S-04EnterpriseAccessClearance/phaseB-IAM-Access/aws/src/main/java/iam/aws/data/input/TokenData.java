package iam.aws.data.input;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TokenData {
    // The tokenID that helps to access token in token store
    private String tokenID;
    // The scopeID used to get the scope from the database
    private String scopeID;
}
