package com.ctsousa.econcilia.mapper;

import java.util.List;

public interface ColecaoMapper<T, D> {

    List<D> paraLista (List<T> entidade);
}
