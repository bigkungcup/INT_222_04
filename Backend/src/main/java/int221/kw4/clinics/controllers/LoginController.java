package int221.kw4.clinics.controllers;

import int221.kw4.clinics.advices.HandleValidationError;
import int221.kw4.clinics.dtos.LoginDTO;
import int221.kw4.clinics.securities.JwtResponse;
import int221.kw4.clinics.securities.JwtTokenUtil;
import int221.kw4.clinics.services.JwtUserDetailsService;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtil jwtTokenUtil;

    private final JwtUserDetailsService userDetailsService;

    HandleValidationError errors = new HandleValidationError();
    public LoginController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, JwtUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginDTO authenticationRequest) throws Exception {
        Map<String, String> errorMap = new HashMap<>();

        errors = new HandleValidationError(Instant.now(),HttpStatus.NOT_FOUND.value(),
                "Not_Found", "Validation", "/api/login", errorMap);
//        if(authenticationRequest == null){
            if(authenticationRequest.getPassword().length() < 8 || authenticationRequest.getPassword().length() > 14){
                errorMap.put("Error: ", "Password should be between 8 and 14 characters in length.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
            }
//            if (authenticationRequest.getEmail().length() > 50 || authenticationRequest.getEmail().length() == 0) {
//                errorMap.put("Error :", "Email should be between 0 and 50 characters in length.");
//            }
//            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
//        }


        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        UserDetails userdetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        String token =  jwtTokenUtil.generateToken(userdetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @GetMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(HttpServletRequest request) throws Exception {
        // From the HttpRequest get the claims
        DefaultClaims claims = (io.jsonwebtoken.impl.DefaultClaims) request.getAttribute("claims");

        Map<String, Object> expectedMap = getMapFromIoJsonwebtokenClaims(claims);
        String token = jwtTokenUtil.doGenerateRefreshToken(expectedMap, expectedMap.get("sub").toString());
        return ResponseEntity.ok(new JwtResponse(token));
    }

    public Map<String, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
        Map<String, Object> expectedMap = new HashMap<String, Object>();
        for (Entry<String, Object> entry : claims.entrySet()) {
            expectedMap.put(entry.getKey(), entry.getValue());
        }
        return expectedMap;
    }

}
