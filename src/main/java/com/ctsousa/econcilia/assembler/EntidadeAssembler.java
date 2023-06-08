package com.ctsousa.econcilia.assembler;

public interface EntidadeAssembler<T, D>{

    T paraEntidade(D dto);
}
