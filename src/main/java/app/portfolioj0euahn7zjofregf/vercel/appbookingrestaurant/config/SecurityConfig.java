package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.config;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.Security.CustomUserDetailsService;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.Security.JwtAuthenticationEntryPoint;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.Security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtAuthenticationFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET, "/api/restaurants").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/user/*/restaurant").hasRole("RESTO")
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest()
                .authenticated();
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
//    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

//    @Override
//    @Bean
//    public AuthenticationManager authenticationManagerBean() throws Exception{
//        return super.authenticationManagerBean();
//    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authConfig) throws Exception{
        return authConfig.getAuthenticationManager();
    }


    // Link https://github.com/Jofregf/Spring/blob/main/Spring-Security-JWT-08/src/main/java/com/example/SpringSecurityJWT08/security/config/SecurityConfig.java
    /*
    USER

    eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyMUBjb3JyZS5jb20iLCJpYXQiOjE2ODAyNzQ1MjQsImV4cCI6MTY4MDM2MDkyNH0.cX1HLm9AAuJDu-XKOvuJKOOzhK-N4WiwvsrabSB7f_kaPqRW79Q8sSllw94ICM9r_IJYEobIFHpnR8q_eDlA2A

            RESTO

    eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJiZWxpQGNvcnJlLmNvbSIsImlhdCI6MTY4MDI3NDQ4NiwiZXhwIjoxNjgwMzYwODg2fQ.MF_gtvMrAQGZO2T5U--a74eincK6hPGh7H4PI7UkvWc6JljBWQdYuz8ZdEewENG-b6mZD6i1tDRbIEq2UCWYyQ

            ADMIN

    eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY4MDM1NzcwMywiZXhwIjoxNjgwNDQ0MTAzLCJ1c2VySWQiOiI1MzFkMzQ4NC1mM2QxLTRmYzQtYTVjYS0yODgxZDRjNjdhNGQifQ.82g0NEhKtxms_dmuPTdcX7HPS7CJJJvInJKKLuogJ9uA-D-ZX7r6edazw9Ew8MsBP-hvDjFpRKdGc5Y7XXGU_Q
*/
}
