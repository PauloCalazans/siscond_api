package br.com.siscond.controller.form;

import br.com.siscond.modelo.Contabilidade;
import br.com.siscond.modelo.Endereco;
import br.com.siscond.repository.EnderecoRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CNPJ;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Optional;

@Getter @Setter @AllArgsConstructor
public class ContabilidadeForm {


    private String celular;

    @CNPJ
    private String cnpj;

    @NotNull
    @NotEmpty
    private String contador;

    @NotNull
    @NotEmpty
    private String email;

    @NotNull
    @NotEmpty
    private String fantasia;

    @NotNull
    @NotEmpty
    private String razao;

    private String site;

    private String telefone;

    @NotNull
    private Long idEndereco;

    public Contabilidade converter(EnderecoRepository enderecoRepository) {
        Optional<Endereco> endereco = enderecoRepository.findById(this.idEndereco);
        Contabilidade vo = new Contabilidade();
        vo.setCelular(this.celular);
        vo.setCnpj(this.cnpj);
        vo.setContador(this.contador);
        vo.setEmail(this.email);
        vo.setFantasia(this.fantasia);
        vo.setRazao(this.razao);
        vo.setSite(this.site);
        vo.setTelefone(this.telefone);
        vo.setEndereco(endereco.get());

        return vo;
    }
}
