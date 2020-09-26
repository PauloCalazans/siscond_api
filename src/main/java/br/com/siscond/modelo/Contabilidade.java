package br.com.siscond.modelo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.br.CNPJ;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Contabilidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @NotNull
    @NotEmpty
    @Column(nullable = false)
    private String razao;

    @NotNull
    @NotEmpty
    @Column(nullable = false)
    private String fantasia;

    @NotNull
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "endereco_id", nullable = false)
    private Endereco endereco;

    @NotNull
    @NotEmpty
    private String contador;

    @CNPJ
    private String cnpj;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Type(type = "org.hibernate.type.ImageType")
    private byte[] logo;

    private String celular;

    private String telefone;

    private String email;

    private String site;

}
