package int221.kw4.clinics.advice;

import lombok.*;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OverLapError {
    private Date timestamp = new Date();
    private Integer status;
    private String message;
    private String error;
    private String path;
}
