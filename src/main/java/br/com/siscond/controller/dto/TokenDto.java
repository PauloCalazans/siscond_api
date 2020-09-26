package br.com.siscond.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TokenDto {

    private String token;
    private String authHeader;

    public TokenDto(String token, String authHeader) {
        this.token = token;
        this.authHeader = authHeader;
    }
}
