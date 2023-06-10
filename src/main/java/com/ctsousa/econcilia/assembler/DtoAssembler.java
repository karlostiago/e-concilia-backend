package com.ctsousa.econcilia.assembler;

public interface DtoAssembler<T, D>{

    D paraDTO (T entidade);
}
