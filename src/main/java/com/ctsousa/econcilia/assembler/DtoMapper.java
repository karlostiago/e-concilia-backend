package com.ctsousa.econcilia.assembler;

public interface DtoMapper<T, D>{

    D paraDTO (T entidade);
}
