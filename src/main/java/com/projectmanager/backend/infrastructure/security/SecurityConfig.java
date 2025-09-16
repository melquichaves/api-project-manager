package com.projectmanager.backend.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher; // Importar esta classe

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Desabilita a proteção CSRF, crucial para APIs REST que não usam sessões
                // baseadas em cookies
                .csrf(AbstractHttpConfigurer::disable)

                // Configura as regras de autorização para as requisições HTTP
                .authorizeHttpRequests(authorize -> authorize
                        // Permite acesso a todos os métodos (GET, POST, PUT, DELETE) para qualquer
                        // caminho que comece com /api/usuarios
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/api/usuarios/**")).permitAll()

                        // Qualquer outra requisição que não foi explicitamente permitida acima, exige
                        // autenticação.
                        // Isso é importante para o futuro quando implementarmos JWT.
                        .anyRequest().authenticated());

        // Como estamos criando uma API REST, não queremos autenticação baseada em
        // formulário ou HTTP Basic por enquanto.
        // Essas linhas são comentadas porque vamos usar JWT.
        // .formLogin(withDefaults())
        // .httpBasic(withDefaults());

        return http.build();
    }
}