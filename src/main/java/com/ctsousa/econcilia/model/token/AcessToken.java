package com.ctsousa.econcilia.model.token;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AcessToken {

    @JsonProperty("accessToken")
    private String accessToken;

    @JsonProperty("type")
    private String type;

    @JsonProperty("expiresIn")
    private int expiresIn;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
