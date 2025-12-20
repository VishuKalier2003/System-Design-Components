package iam.aws.errors;

public class AccessDenied extends RuntimeException {
    public AccessDenied(String reason) {
        super("The Access is failed due to reason : "+reason);
    }
}
