package com.ctsousa.econcilia.scheduler;

import com.ctsousa.econcilia.enumaration.TipoParametro;

public interface Scheduler {

    void processar();

    TipoParametro tipoParametro();
}
