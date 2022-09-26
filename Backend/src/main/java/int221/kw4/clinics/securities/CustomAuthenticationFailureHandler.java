//package int221.kw4.clinics.securities;
//
//import java.io.IOException;
//import java.io.OutputStream;
//import java.sql.Timestamp;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import int221.kw4.clinics.advices.HandleExceptionLogin;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//import org.springframework.stereotype.Component;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
//
//@Component
//public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
//
//    @Override
//    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
//            throws IOException, ServletException {
//
////        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        HandleExceptionLogin errors;
//        Map<String, String> errorMap = new HashMap<>();
//        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        errorMap.put("Not Found", "Not found user: ");
//        errors = new HandleExceptionLogin(sdf3.format(timestamp), HttpStatus.NOT_FOUND.value(),
//                "Not Found", "Validation", request.getRequestURI(), errorMap);
//        response.setStatus(HttpStatus.NOT_FOUND.value());
//        response.setContentType(APPLICATION_JSON_VALUE);
//        new ObjectMapper().writeValue(response.getOutputStream(), errors);
//    }
//}