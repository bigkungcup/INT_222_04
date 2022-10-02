package int221.kw4.clinics.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import int221.kw4.clinics.entities.User;
import int221.kw4.clinics.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping("/api")
@Slf4j
public class TokenController {
    private final AuthenticationManager authenticationManager;
    private final UserService service;
    private final String secret = "secret";
    private final Integer jwtExpirationInMs = 30 * 60 * 1000;
    private final Integer refreshExpirationDateInMs = 24 * 60 * 60 * 1000;


    public TokenController(AuthenticationManager authenticationManager, UserService service) {
        this.authenticationManager = authenticationManager;
        this.service = service;
    }

    @GetMapping("/token/refresh")
    public void refreshtoken(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        String authorizationHeader = request.getHeader(AUTHORIZATION);
        Cookie refresh_token = WebUtils.getCookie(request, "refresh_token");
        if (refresh_token != null) {
            try {
//                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256(secret.getBytes(StandardCharsets.UTF_8));
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token.getValue());
                String email = decodedJWT.getSubject();
                User user = service.getUserByEmail(email);

                ArrayList<String> roles = new ArrayList<>();
                roles.add(user.getRole().toString());

                String access_token = JWT.create()
                        .withSubject(user.getEmail())
                        .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                        .withIssuer(request.getRequestURI().toString())
                        .withClaim("roles", roles)
                        .sign(algorithm);

//                Map<String, String> tokens = new HashMap<>();
//                tokens.put("access_token", access_token);
//                tokens.put("refresh_token", refresh_token);
//                response.setContentType(APPLICATION_JSON_VALUE);
//                new ObjectMapper().writeValue(response.getOutputStream(), tokens);

//                Cookie access_cookie = new Cookie("access_token", access_token);
//                Cookie refresh_cookie = new Cookie("refresh_token", refresh_token.getValue());
//
//                access_cookie.setHttpOnly(true);
//                refresh_cookie.setHttpOnly(true);
//
//                access_cookie.setPath("/");
//                refresh_cookie.setPath("/");
//
//                response.addCookie(access_cookie);
//                response.addCookie(refresh_cookie);

                response.addCookie(createCookie("access_token", access_token, jwtExpirationInMs));
                response.addCookie(createCookie("refresh_token", refresh_token.getValue(), refreshExpirationDateInMs));


                Map<String, String> userDetails = new HashMap<>();
                userDetails.put("id", user.getId().toString());
                userDetails.put("name",user.getName());
                userDetails.put("email", user.getEmail());
                userDetails.put("role", user.getRole().toString());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), userDetails);

            } catch (Exception exception) {
                log.error("Error logging in: {}", exception.getMessage());
                response.setHeader("error", exception.getMessage());
                response.setStatus(UNAUTHORIZED.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }

    @PostMapping("/token/remove")
    private void eraseCookie(HttpServletRequest req, HttpServletResponse resp) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null)
            for (Cookie cookie : cookies) {
                cookie.setValue("");
                cookie.setPath("/");
//                cookie.setMaxAge(-1);
                resp.addCookie(cookie);
            }
    }

    public Cookie createCookie(String name, String value, Integer maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        return cookie;
    }

}


