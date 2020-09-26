package br.com.siscond.modelo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Grupo implements GrantedAuthority {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@NotEmpty
	@Length(max = 40)
	@Column(nullable = false, length = 40)
	private String nome;

	@NotNull
	@NotEmpty
	@Length(max = 80)
	@Column(nullable = false, length = 80)
	private String descricao;

	@Override
	public String getAuthority() {
		return this.nome;
	}
}
