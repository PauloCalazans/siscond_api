package br.com.siscond.controller.dto;

import br.com.siscond.modelo.Usuario;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter @Setter @NoArgsConstructor
public class UsuarioDto {

    private String email;
    private Long idMorador;
    private Long idGrupo;
    private Long idContabilidade;

    public UsuarioDto(Usuario usuario) {
        this.email = usuario.getEmail();
        this.idGrupo = usuario.getGrupo().getId();

        if(usuario.getMorador() != null) {
            this.idMorador = usuario.getMorador().getId();
        }

        if(usuario.getContabilidade() != null) {
            this.idContabilidade = usuario.getContabilidade().getId();
        }
    }


    public static Page<UsuarioDto> converter(Page<Usuario> usuario) {
       return usuario.map(UsuarioDto::new);
    }
}
