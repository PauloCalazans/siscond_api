package br.com.siscond.config.security;

import br.com.siscond.modelo.Usuario;
import br.com.siscond.repository.UsuarioRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

public class FilterTokenAuth extends OncePerRequestFilter {

    private final GenerateToken generateToken;
    private final UsuarioRepository usuarioRepository;

    public FilterTokenAuth(GenerateToken generateToken, UsuarioRepository usuarioRepository) {
        this.generateToken = generateToken;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getToken(request);
        if(generateToken.isValid(token)) {
            authClient(token);
        }

        filterChain.doFilter(request, response);
    }

    private void authClient(String token) {
        Long idUser = generateToken.getIdUser(token);
        Usuario user = usuarioRepository.findById(idUser).get();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String initHeader = "Bearer ";
        if(authHeader == null || authHeader.isEmpty() || !authHeader.startsWith(initHeader)) {
            return null;
        }

        return authHeader.replace(initHeader, "");
    }
}
