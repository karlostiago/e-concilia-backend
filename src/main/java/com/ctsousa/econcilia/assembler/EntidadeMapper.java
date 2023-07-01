package com.ctsousa.econcilia.assembler;

public interface EntidadeMapper<T, D>{

    T paraEntidade(D dto);
}
