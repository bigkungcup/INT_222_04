package int221.kw4.clinics.advices;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class HandleExceptionUnique extends Exception{

    public HandleExceptionUnique(String message){
        super(message);
    }
}
