package es.speakly.store_backend.annotations;

import es.speakly.store_backend.domain.dto.LoginUserDto;
import es.speakly.store_backend.domain.model.UserRole;
import es.speakly.store_backend.exceptions.ForbiddenException;
import es.speakly.store_backend.exceptions.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {

        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        boolean requiresAuth =
                handlerMethod.hasMethodAnnotation(Authenticated.class)
                        || handlerMethod.getBeanType().isAnnotationPresent(Authenticated.class);

        boolean requiresAdmin =
                handlerMethod.hasMethodAnnotation(Admin.class)
                        || handlerMethod.getBeanType().isAnnotationPresent(Admin.class);

        if (!requiresAuth && !requiresAdmin) {
            return true;
        }

        LoginUserDto user = (LoginUserDto) request.getAttribute("user");

        if (user == null) {
            throw new UnauthorizedException("Token de autenticación requerido o inválido");
        }

        if (user.id() == null) {
            throw new UnauthorizedException("Token de autenticación inválido o corrupto");
        }

        if (requiresAdmin && user.role() == null) {
            throw new ForbiddenException("Se requiere rol de administrador para acceder a este recurso");
        }

        if (requiresAdmin && user.role() != UserRole.ADMIN) {
            throw new ForbiddenException("Acceso denegado: Se requiere rol de administrador");
        }

        return true;
    }
}
