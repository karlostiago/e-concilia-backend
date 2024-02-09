package com.ctsousa.econcilia.mapper;

import java.util.List;

public interface ColecaoMapper<T, D> extends Mapper {

    List<D> paraLista(List<T> entidade);
}
