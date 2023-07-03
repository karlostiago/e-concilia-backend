package com.ctsousa.econcilia.assembler;

import java.util.List;

public interface ColecaoMapper<T, D> {

    List<D> paraLista (List<T> entidade);
}
