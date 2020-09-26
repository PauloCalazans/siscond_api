package br.com.siscond.config.security;

import br.com.siscond.modelo.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class GenerateToken {

    @Value("${siscond.jwt.secret}")
    private String secret;

    public String gerarToken(Authentication authenticate) {
        Usuario user = (Usuario) authenticate.getPrincipal();
        Calendar ex = Calendar.getInstance();
        ex.add(Calendar.HOUR_OF_DAY, 2);

        return Jwts.builder().setIssuer("API SISCOND")
                .setSubject(user.getId().toString())
                .setIssuedAt(Calendar.getInstance().getTime()).setExpiration(ex.getTime())
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public boolean isValid(String token) {
        try {
            Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Long getIdUser(String token) {
        Claims body = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
        return Long.parseLong(body.getSubject());
    }
}
