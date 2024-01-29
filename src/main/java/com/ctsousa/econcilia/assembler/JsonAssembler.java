package com.ctsousa.econcilia.assembler;

public interface JsonAssembler<T, D> {

    T paraDTO(D json);
}
