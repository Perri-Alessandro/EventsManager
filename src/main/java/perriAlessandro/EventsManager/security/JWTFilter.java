package perriAlessandro.EventsManager.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import perriAlessandro.EventsManager.entities.User;
import perriAlessandro.EventsManager.exceptions.UnauthorizedException;
import perriAlessandro.EventsManager.services.UserService;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {
    @Autowired
    private JWTTools jwtTools;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization"); // Authorization Header --> Bearer eyJhbGciOiJIUzM4NCJ9.eyJpYXQiOjE3MTMxNzY3NDUsImV4cCI6MTcxMzc4MTU0NSwic3ViIjoiZDFlZTlmN2MtZWQwZS00ZTQ3LThmN2EtYTQ0Yzk5MTNkMzE0In0.HFk14O-60faQY4TEnvsNgqjQdOVy7aD-1L-jCvayGz2VTRIQQqGDRzx1qSx5WWxy

        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new UnauthorizedException("Per favore inserisci il token nell'Authorization Header");

        String accessToken = authHeader.substring(7);

        jwtTools.verifyToken(accessToken);

        String id = jwtTools.extractIdFromToken(accessToken);
        User currentUser = this.userService.findById(Long.valueOf(id));


        Authentication authentication = new UsernamePasswordAuthenticationToken(currentUser, null, currentUser.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);


        filterChain.doFilter(request, response); // Vado al prossimo elemento della catena, passandogli gli oggetti request e response

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}
