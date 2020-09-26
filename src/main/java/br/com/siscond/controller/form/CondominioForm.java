package br.com.siscond.controller.form;

import br.com.siscond.modelo.Condominio;
import br.com.siscond.modelo.Contabilidade;
import br.com.siscond.modelo.Endereco;
import br.com.siscond.repository.ContabilidadeRepository;
import br.com.siscond.repository.EnderecoRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CNPJ;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Getter @Setter @AllArgsConstructor
public class CondominioForm {

    @NotNull
    @NotEmpty
    private String razao;

    @NotNull
    @NotEmpty
    private String fantasia;

    @NotNull
    private Long idContabilidade;

    @NotNull
    private Long idEndereco;

    @CNPJ
    private String cnpj;

    private String celular;

    private String telefone;

    private String email;

    private String site;

    private byte[] foto;

    public Condominio converter(EnderecoRepository enderecoRepository, ContabilidadeRepository contabilidadeRepository) {
        Optional<Endereco> endereco = enderecoRepository.findById(this.idEndereco);
        Optional<Contabilidade> contabilidade = contabilidadeRepository.findById(this.idContabilidade);
        Condominio vo = new Condominio();
        vo.setRazao(this.razao);;
        vo.setFantasia(this.fantasia);
        vo.setEndereco(endereco.get());
        vo.setContabilidade(contabilidade.get());
        vo.setCelular(this.celular);
        vo.setTelefone(this.telefone);
        vo.setEmail(this.email);
        vo.setSite(this.site);
        vo.setCnpj(this.cnpj);
        vo.setFotoBase64(this.foto);

        return vo;
    }
}
