package br.com.siscond.controller.dto;

import br.com.siscond.modelo.Arquivo;
import br.com.siscond.modelo.Contabilidade;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Getter @Setter @AllArgsConstructor
public class ArquivoDto {

    private Long id;
    private byte[] boleto;
    private LocalDateTime dhLancamento;
    private Long idMorador;
    private String observacao;

    public ArquivoDto(Arquivo vo) {
        this.id = vo.getId();
        this.boleto = vo.getBoleto();
        this.dhLancamento = vo.getDhLancamento();
        this.idMorador = vo.getMorador().getId();
        this.observacao = vo.getObservacao();
    }

    public static Page<ArquivoDto> converter(Page<Arquivo> pages) {
        return pages.map(ArquivoDto::new);
    }
}
