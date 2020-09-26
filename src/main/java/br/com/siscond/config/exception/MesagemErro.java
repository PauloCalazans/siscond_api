package br.com.siscond.config.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class MesagemErro {

    String mensagem;
    String erro;
}
