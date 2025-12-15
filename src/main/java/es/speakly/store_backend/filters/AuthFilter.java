package es.speakly.store_backend.filters;

import es.speakly.store_backend.domain.dto.LoginUserDto;
import es.speakly.store_backend.domain.dto.UserDto;
import es.speakly.store_backend.domain.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AuthFilter extends OncePerRequestFilter {

    private final AuthService authService;

    public AuthFilter(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String token = obtenerToken(request);

        if (token != null) {
            LoginUserDto user = authService.getUserFromToken(token);

            if (user == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token invalido");
                return;
            }

            request.setAttribute("user", user);
        }
        filterChain.doFilter(request, response);
    }

    private String obtenerToken(HttpServletRequest request) {
        // 1️⃣ Header Authorization
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        // 2️⃣ Query param ?token=
        return request.getParameter("token");
    }
}

