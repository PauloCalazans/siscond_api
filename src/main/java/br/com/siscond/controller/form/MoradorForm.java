package br.com.siscond.controller.form;

import br.com.siscond.modelo.Condominio;
import br.com.siscond.modelo.Endereco;
import br.com.siscond.modelo.Morador;
import br.com.siscond.repository.CondominioRepository;
import br.com.siscond.repository.EnderecoRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Getter @Setter @AllArgsConstructor
public class MoradorForm {

    @CPF
    private String cpf;

    @NotNull
    @NotEmpty
    private String nome;

    @NotNull
    private Long idEndereco;

    @NotNull
    private Long idCondominio;

    private String celular;

    private String telefone;

    private String email;

    private String site;

    public Morador converter(EnderecoRepository enderecoRepository, CondominioRepository condominioRepository) {
        Optional<Endereco> endereco = enderecoRepository.findById(this.idEndereco);
        Condominio condominio = condominioRepository.getOne(this.idCondominio);
        Morador vo = new Morador();
        vo.setCpf(this.cpf);;
        vo.setNome(this.nome);
        vo.setEndereco(endereco.get());
        vo.setCondominio(condominio);
        vo.setCelular(this.celular);
        vo.setTelefone(this.telefone);
        vo.setEmail(this.email);
        vo.setSite(this.site);

        return vo;
    }
}
