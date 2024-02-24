package com.ctsousa.econcilia.resource;

import com.ctsousa.econcilia.mapper.impl.CancelamentoMapper;
import com.ctsousa.econcilia.mapper.impl.CnpjMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WithMockUser
@WebMvcTest(CnpjResource.class)
class CnpjResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CancelamentoMapper.CnpjService cnpjService;

    @MockBean
    private CnpjMapper mapper;

    @Test
    void deveRetornarStatus200OkQuandoBuscarPorUmCnpjValido() throws Exception {
        String cnpj = "00000000000191";

        mockMvc.perform(MockMvcRequestBuilders.get("/cnpj/{cnpj}", cnpj))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
