package br.com.siscond.controller.dto;

import br.com.siscond.modelo.Morador;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter @Setter @NoArgsConstructor
public class MoradorDto {

    private String cpf;
    private String nome;
    private Long idEndereco;
    private Long idCondominio;
    private String celular;
    private String telefone;
    private String email;
    private String site;

    public MoradorDto(Morador morador) {
        this.cpf = morador.getCpf();
        this.nome = morador.getNome();
        this.idEndereco = morador.getEndereco().getId();
        this.idCondominio = morador.getCondominio().getId();
        this.celular = morador.getCelular();
        this.telefone = morador.getTelefone();
        this.email = morador.getEmail();
        this.site = morador.getSite();
    }


    public static Page<MoradorDto> converter(Page<Morador> morador) {
       return morador.map(MoradorDto::new);
    }
}
