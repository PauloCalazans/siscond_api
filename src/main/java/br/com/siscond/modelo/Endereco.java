package br.com.siscond.modelo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Endereco {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@NotEmpty
	@Column(length = 8, nullable = false)
	private String cep;

	@NotNull
	@NotEmpty
	@Column(nullable = false)
	private String logradouro;

	@NotNull
	@NotEmpty
	@Column(nullable = false)
	private String bairro;

	@NotNull
	@NotEmpty
	@Column(nullable = false)
	private String cidade;

	@NotNull
	@NotEmpty
	@Column(nullable = false)
	private String uf;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_cadastro")
	private Date dtCadastro = new Date();

	private String complemento;

	private String apartamento;

	private String bloco;

	private String garagem;

}
