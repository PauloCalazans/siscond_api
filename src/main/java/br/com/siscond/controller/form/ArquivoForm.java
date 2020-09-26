package br.com.siscond.controller.form;

import br.com.siscond.modelo.Arquivo;
import br.com.siscond.modelo.Morador;
import br.com.siscond.repository.MoradorRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Getter @Setter @AllArgsConstructor
public class ArquivoForm {

    @NotNull
    @ManyToOne
    private Long idMorador;

    private String observacao;

    public Arquivo converter(MoradorRepository moradorRepository) {
        Morador morador = moradorRepository.getOne(this.idMorador);
        Arquivo vo = new Arquivo();
        vo.setMorador(morador);
        vo.setObservacao(this.observacao);

        return vo;
    }
}
