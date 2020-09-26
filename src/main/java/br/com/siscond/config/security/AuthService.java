package br.com.siscond.config.security;

import br.com.siscond.modelo.Usuario;
import br.com.siscond.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UsuarioRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Usuario> user = userRepository.findByEmail(email);
        if(user.isPresent()) {
            return user.get();
        }

        throw new UsernameNotFoundException("Usuário e/ou senha incorretos");
    }
}
