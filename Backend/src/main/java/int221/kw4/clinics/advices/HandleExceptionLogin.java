package int221.kw4.clinics.advices;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HandleExceptionLogin {
    private String timestamp;
    private Integer status;
    private String message;
    private String error;
    private String path;
    private Map<String,String> filedErrors;
}
