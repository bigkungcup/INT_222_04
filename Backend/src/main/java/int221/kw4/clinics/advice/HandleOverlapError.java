package int221.kw4.clinics.advice;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString

public class HandleOverlapError extends Exception{
    public HandleOverlapError(String message){
        super(message);
    }
}
