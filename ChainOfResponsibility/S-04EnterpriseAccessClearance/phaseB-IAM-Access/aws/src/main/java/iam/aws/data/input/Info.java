package iam.aws.data.input;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Info {
    // These values will be employee IDs
    private final String requester;
    private final String receiver;
}
