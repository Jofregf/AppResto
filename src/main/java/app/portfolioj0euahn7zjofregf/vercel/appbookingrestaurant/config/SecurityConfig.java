package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.config;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.security.CustomUserDetailsService;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.security.JwtAuthenticationEntryPoint;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

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

        http.cors()
                .and()
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET, "/api/restaurants", "/api/restaurants/**", "/api/menus/**",
                        "/api/search/**").permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/users/password/**", "/api/users/forgotpassword").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/restaurants/**").hasRole("RESTO")
                .requestMatchers(HttpMethod.PUT, "/api/restaurants/**").hasRole("RESTO")
                .requestMatchers(HttpMethod.DELETE, "/api/restaurants/**").hasRole("RESTO")
                .requestMatchers(HttpMethod.POST, "/api/menus/**").hasRole("RESTO")
                .requestMatchers(HttpMethod.PUT, "/api/menus/**").hasRole("RESTO")
                .requestMatchers(HttpMethod.DELETE, "/api/menus/**").hasRole("RESTO")
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest()
                .authenticated();
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public FilterRegistrationBean<CORSFilter> corsFilterFilterRegistrationBean(){
        FilterRegistrationBean<CORSFilter> registrationBean = new FilterRegistrationBean<>();
        CORSFilter corsFilter = new CORSFilter();
        registrationBean.setFilter(corsFilter);
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
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
}
