package com.example.plateReader.config.security;

import com.example.plateReader.Utils.JwtBlacklist;
import com.example.plateReader.service.AppUserService;
import com.example.plateReader.service.authentication.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    private final JwtService jwtService;
    private final AppUserService appUserService;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtBlacklist jwtBlacklist;

    @Autowired
    public AuthenticationFilter(
            JwtService jwtService,
            @Lazy AppUserService appUserService,
            @Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver,
            JwtBlacklist jwtBlacklist
    ) {
        this.jwtService = jwtService;
        this.appUserService = appUserService;
        this.handlerExceptionResolver = handlerExceptionResolver;
        this.jwtBlacklist = jwtBlacklist;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // --- CAMINHO 1: SEM TOKEN ---
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return; // Encerra a execução deste filtro aqui
        }

        final String token = authHeader.substring(7);
        final String username;

        // --- CAMINHO 2: TOKEN NA BLACKLIST ---
        if (jwtBlacklist.isTokenInvalidated(token)) {
            logger.warn("Tentativa de uso de token na blacklist: {}", token);
            filterChain.doFilter(request, response);
            return; // Encerra a execução deste filtro aqui
        }

        // --- CAMINHO 3: TENTATIVA DE AUTENTICAÇÃO COM O TOKEN ---
        try {
            username = jwtService.getUsernameFromToken(token);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = appUserService.loadUserByUsername(username);

                if (jwtService.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            // Se a autenticação foi bem-sucedida (ou o usuário já estava autenticado),
            // passamos a requisição para o próximo filtro.
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            // Se HOUVER QUALQUER ERRO na validação do token, delegamos para o handler
            // e NÃO CONTINUAMOS a cadeia de filtros.
            logger.error("Falha na autenticação JWT: {}", e.getMessage());
            handlerExceptionResolver.resolveException(request, response, null, e);
            // O 'return' aqui é implícito pois a exceção encerra o fluxo normal.
        }
    }
}