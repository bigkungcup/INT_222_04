package int221.kw4.clinics.advices;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class HandleExceptionBadRequest extends  Exception{
    public HandleExceptionBadRequest(String message) {
        super(message);
    }
}
