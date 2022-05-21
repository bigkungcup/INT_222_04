package int221.kw4.clinics.advice;

import lombok.*;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HandleValidationError {
    private Date timestamp = new Date();
    private Integer status = 400;
    private String message = "Bad Request";
    private String error = "Validation Error";
    private Map<String,String> filedErrors;
}
