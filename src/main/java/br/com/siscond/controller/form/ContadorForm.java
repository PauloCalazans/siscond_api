package br.com.siscond.controller.form;

import br.com.siscond.modelo.Endereco;
import br.com.siscond.repository.EnderecoRepository;
import br.com.siscond.modelo.Contador;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Getter @Setter @AllArgsConstructor
public class ContadorForm {

    @CPF
    private String cpf;

    @NotNull @NotEmpty
    private String nome;

    @NotNull @NotEmpty
    private String crc;

    @NotNull
    private Long idEndereco;

    private String celular;

    private String telefone;

    public Contador converter(EnderecoRepository enderecoRepository) {
        Optional<Endereco> endereco = enderecoRepository.findById(this.idEndereco);
        Contador vo = new Contador();
        vo.setCpf(this.cpf);;
        vo.setNome(this.nome);
        vo.setEndereco(endereco.get());
        vo.setCrc(this.crc);
        vo.setCelular(this.celular);
        vo.setTelefone(this.telefone);

        return vo;
    }
}
