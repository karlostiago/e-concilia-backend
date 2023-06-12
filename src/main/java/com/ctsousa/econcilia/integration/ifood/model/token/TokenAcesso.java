package com.ctsousa.econcilia.integration.ifood.model.token;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenAcesso {

    @JsonProperty("accessToken")
    private String tokenAcesso;

    @JsonProperty("type")
    private String tipo;

    @JsonProperty("expiresIn")
    private int expiraEm;

}
