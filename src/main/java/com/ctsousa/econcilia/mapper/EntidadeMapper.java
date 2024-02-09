package com.ctsousa.econcilia.mapper;

public interface EntidadeMapper<T, D> extends Mapper {

    T paraEntidade(D dto);
}
