package com.ctsousa.econcilia.exceptions;

import com.ctsousa.econcilia.mapper.impl.EmpresaMapper;
import com.ctsousa.econcilia.resource.EmpresaResource;
import com.ctsousa.econcilia.service.EmpresaService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.is;

@WithMockUser
@WebMvcTest(EmpresaResource.class)
class NotificacaoExceptionTest {

    @Autowired
    private MockMvc mock;

    @MockBean
    private EmpresaService empresaService;

    @MockBean
    private EmpresaMapper mapper;

    @Test
    void deveLancarNotificacaoExceptionHandler() throws Exception {

        Mockito.when(empresaService.pesquisarPorId(1L))
                        .thenThrow(new NotificacaoException("Empresa com id 1 não encontrado"));

        mock.perform(MockMvcRequestBuilders.get("/empresas/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status", is("BAD_REQUEST")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].mensagem", is("Empresa com id 1 não encontrado")));

        Mockito.verify(empresaService, Mockito.times(1))
                .pesquisarPorId(1L);
    }
}
