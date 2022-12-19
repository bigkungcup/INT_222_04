package int221.kw4.clinics.filter;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import int221.kw4.clinics.advices.HandleExceptionLogin;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    private final String secret = "secret";

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (request.getServletPath().equals("/api/login") || request.getServletPath().equals("/api/token/refresh") ||
                request.getServletPath().equals("/api/users/register") || request.getServletPath().equals("/api/events/guest") ||
                request.getServletPath().equals("/api/eventCategories") || request.getServletPath().equals("/api/token/remove") ||
                request.getServletPath().equals("/Files/Uploads/**") || request.getServletPath().equals("/api/users/loginWithMS")) {
            filterChain.doFilter(request, response);
        } else {
            Cookie access_token = WebUtils.getCookie(request, "access_token");
            String authorizationHeader = request.getHeader(AUTHORIZATION);

            if(authorizationHeader != null){
                try{
                    String token = authorizationHeader.substring("Bearer ".length());
                    String[] chunks = token.split("\\.");

                    JSONObject header = new JSONObject(decode(chunks[0]));
                    JSONObject payload = new JSONObject(decode(chunks[1]));
                    String signature = decode(chunks[2]);

                    System.out.println("Header: " + header);
                    System.out.println("Payload: " + payload);

                    if(payload.getString("iss").equals("https://login.microsoftonline.com/6f4432dc-20d2-441d-b1db-ac3380ba633d/v2.0")){
                        DecodedJWT decodedJWT = JWT.decode(token);
                        JwkProvider provider = new UrlJwkProvider(new URL("https://login.microsoftonline.com/6f4432dc-20d2-441d-b1db-ac3380ba633d/discovery/v2.0/keys"));
                        Jwk jwk = provider.get(decodedJWT.getKeyId());
                        Algorithm algorithm = Algorithm.RSA256((java.security.interfaces.RSAPublicKey) jwk.getPublicKey(), null);
                        algorithm.verify(decodedJWT); // if the token signature is invalid, the method will throw SignatureVerificationException
                    }

                    String username = payload.getString("preferred_username");
                    String role;
                    try{
                        role = payload.getJSONArray("roles").getString(0);
                    }catch (Exception e){
                        role = "guest";
                    }
                    System.out.println("Role: " + role);
                    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority(role));
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                }catch (Exception exception){
                    System.out.println("Error login in:" + exception.getMessage());
                    response.setHeader("Error", exception.getMessage());
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    Map<String, String> error = new HashMap<>();
                    error.put("error_message", exception.getMessage());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                }
            }else if (access_token != null) {
                try {
                    Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = verifier.verify(access_token.getValue());
                    String username = decodedJWT.getSubject();
                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    stream(roles).forEach(role -> {
                        authorities.add(new SimpleGrantedAuthority(role));
                    });
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                } catch (Exception exception) {
                    System.out.println("Error login in:" + exception.getMessage());
                    response.setHeader("Error", exception.getMessage());
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    Map<String, String> error = new HashMap<>();
                    error.put("error_message", exception.getMessage());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                }
            }  else {
                HandleExceptionLogin errors;
                Map<String, String> errorMap = new HashMap<>();
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                errorMap.put("Unauthorized", "Not Authentication");
                errors = new HandleExceptionLogin(sdf3.format(timestamp), HttpStatus.UNAUTHORIZED.value(),
                        "Unauthorized", "Validation", request.getRequestURI(), errorMap);
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), errors);
                filterChain.doFilter(request, response);
            }
        }
    }

    public String decode(String chunks){
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String decode = new String(decoder.decode(chunks));
        return decode;
    }
}