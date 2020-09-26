package br.com.siscond.controller.form;

import br.com.siscond.modelo.Contabilidade;
import br.com.siscond.modelo.Grupo;
import br.com.siscond.modelo.Morador;
import br.com.siscond.modelo.Usuario;
import br.com.siscond.repository.ContabilidadeRepository;
import br.com.siscond.repository.GrupoRepository;
import br.com.siscond.repository.MoradorRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Getter @Setter @AllArgsConstructor
public class UsuarioForm {

    @NotNull
    @NotEmpty
    @Email
    private String email;

    @NotNull
    @NotEmpty
    private String senha;

    private Long idMorador;

    private Long idContabilidade;

    @NotNull
    private Long idGrupo;

    public Usuario converter(MoradorRepository moradorRepository, ContabilidadeRepository contabilidadeRepository, GrupoRepository grupoRepository) {
        Optional<Contabilidade> contabilidade = contabilidadeRepository.findById(this.idContabilidade);
        Optional<Morador> morador = moradorRepository.findById(this.idMorador);
        Optional<Grupo> grupo = grupoRepository.findById(this.idGrupo);
        Usuario vo = new Usuario();
        vo.setEmail(this.email);
        vo.setSenha(this.senha);

        morador.ifPresent(vo::setMorador);
        contabilidade.ifPresent(vo::setContabilidade);
        grupo.ifPresent(vo::setGrupo);

        return vo;
    }
}
