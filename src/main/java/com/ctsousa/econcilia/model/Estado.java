package com.ctsousa.econcilia.model;

import com.ctsousa.econcilia.annotation.ExcludedCoverage;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.io.Serializable;

import static com.ctsousa.econcilia.util.StringUtil.maiuscula;

@Getter
@Setter
@Embeddable
@ExcludedCoverage
public class Estado implements Serializable {

    @Column(nullable = false, length = 2)
    private String uf;

    @PrePersist
    @PreUpdate
    public void init() {
        setUf(maiuscula(getUf()));
    }
}