package com.ctsousa.econcilia.assembler;

import java.util.List;

public interface ColecaoAssembler<T, D> {

    List<D> paraLista (List<T> entidade);
}
