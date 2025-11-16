package reactivepipe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Response {
    private String transactionID;
    private String statusUrl;
    private String resultUrl;
}
