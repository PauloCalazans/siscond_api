package br.com.siscond.controller.dto;

import br.com.siscond.modelo.Contabilidade;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter @Setter @AllArgsConstructor
public class ContabilidadeDto {

    private String celular;
    private String cnpj;
    private String contador;
    private String email;
    private String fantasia;
    private String razao;
    private String site;
    private String telefone;
    private Long idEndereco;

    public ContabilidadeDto(Contabilidade vo) {
        this.celular = vo.getCelular();
        this.cnpj = vo.getCnpj();
        this.contador = vo.getContador();
        this.email = vo.getEmail();
        this.fantasia = vo.getFantasia();
        this.razao = vo.getRazao();
        this.site = vo.getSite();
        this.telefone = vo.getTelefone();
        this.idEndereco = vo.getEndereco().getId();
    }

    public static Page<ContabilidadeDto> converter(Page<Contabilidade> pages) {
        return pages.map(ContabilidadeDto::new);
    }
}
