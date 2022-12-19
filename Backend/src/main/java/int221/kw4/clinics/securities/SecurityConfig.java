package int221.kw4.clinics.securities;

import int221.kw4.clinics.filter.CustomAuthenticationFilter;
import int221.kw4.clinics.filter.CustomAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import static org.springframework.http.HttpMethod.*;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");

        http.csrf().disable().cors();

        http.authorizeRequests().antMatchers("/api/users/register/**").permitAll()
                .antMatchers("/api/token/refresh", "/api/token/remove").permitAll()
                .antMatchers("/api/login").permitAll()
                .antMatchers("/api/events/guest").permitAll()
                .antMatchers("/Files/Uploads/**").permitAll()
                .antMatchers("/api/users/loginWithMS").permitAll();

        http.authorizeRequests().antMatchers(POST,"/api/users/registerCategory/{userId}/{eventCategory}").hasAnyAuthority( "admin")
                .antMatchers("/api/users/{userId}/eventCategory/{eventCategoryId}").hasAnyAuthority( "admin")
                .antMatchers(GET,"/api/users/{userId}").hasAnyAuthority("admin", "lecturer", "student", "guest")
                .antMatchers(GET, "api/user/checkEvent/{userId}").hasAnyAuthority("admin", "lecturer", "student", "guest")
                .antMatchers(DELETE,"/api/users/{userId}").hasAnyAuthority("admin")
                .antMatchers(PUT,"/api/users/{userId}").hasAnyAuthority("admin");

        http.authorizeRequests().antMatchers("/api/events/lecturer/{userId}").hasAnyAuthority("lecturer", "admin")
                .antMatchers(GET, "/api/events/**").hasAnyAuthority("lecturer", "admin", "student");

        http.authorizeRequests().antMatchers("/api/users/**").hasAnyAuthority("admin");

        http.authorizeRequests().antMatchers(POST, "/api/events/**").hasAnyAuthority("student", "admin", "guest")
                .antMatchers(PUT, "/api/events/**").hasAnyAuthority("student", "admin")
                .antMatchers(DELETE, "/api/events/**").hasAnyAuthority("student", "admin")
                .antMatchers(DELETE, "/api/files/**").hasAnyAuthority("student", "admin")
                .antMatchers(GET,"/api/files/**").permitAll();

        http.authorizeRequests().antMatchers("/api/eventCategories/**").permitAll()
                .anyRequest().authenticated();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler()).and()
                .addFilter(customAuthenticationFilter)
                .addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
