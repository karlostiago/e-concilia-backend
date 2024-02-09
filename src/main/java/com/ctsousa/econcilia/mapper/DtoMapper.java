package com.ctsousa.econcilia.mapper;

public interface DtoMapper<T, D> extends Mapper {

    D paraDTO(T entidade);
}
