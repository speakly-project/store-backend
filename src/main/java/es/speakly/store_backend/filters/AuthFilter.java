package es.speakly.store_backend.filters;

import es.speakly.store_backend.domain.dto.UserDto;
import es.speakly.store_backend.domain.model.User;
import es.speakly.store_backend.persistence.dao.AuthDao;
import es.speakly.store_backend.persistence.dao.impl.entity.UserJpaEntity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

public class AuthFilter extends OncePerRequestFilter {
    private final AuthDao authDao;

    public AuthFilter(AuthDao authDao) {
        this.authDao = authDao;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
        throws ServletException, IOException {
        String token = request.getHeader("Authorization");

        if (token != null) {
            Optional<UserJpaEntity> user = authDao.findByToken(token);

//            if (user != null){
//                UsernamePasswordAuthenticationToken authentication =
//                    new UsernamePasswordAuthenticationToken(
//                            user,
//                            null,
//                            null
//                    );
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
        }
        filterChain.doFilter(request, response);
    }
}
