package com.vgrazi.presentations.springcloudcontractsprovider;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.NestedServletException;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SpringCloudContractsProviderApplicationTests {
    @Autowired
    MockMvc mockMvc;

    @Test
    public void shouldIncreaseCreditLineTo10_000() throws Exception {
        shouldIncreaseCreditLineWhenAvailableCredit(0, 1_000, 10_000);
    }
    @Test
    public void shouldIncreaseCreditLineTo100_000() throws Exception {
        shouldIncreaseCreditLineWhenAvailableCredit(90_000, 98_000, 100_000);
    }

    @Test
    public void shouldIncreaseCreditLineTo1000_000() throws Exception {
        shouldIncreaseCreditLineWhenAvailableCredit(999_000, 98_000, 1_000);
    }

    @Test
    public void shouldDenyIncrease() throws Exception {
        try {
            shouldIncreaseCreditLineWhenAvailableCredit(1_000_000, 1, 1_000);
            fail("Should throw an exception");
        } catch (NestedServletException e) {
            if (!(e.getCause() instanceof IllegalArgumentException)) {
                fail(e);
            }
        }
    }
    private void shouldIncreaseCreditLineWhenAvailableCredit(double currentCreditLine, double increaseAmount, double expectedIncrease) throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/request-credit-increase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                "{" +
                                        "\"currentCreditLine\":" + currentCreditLine + ",\n" +
                                        "\"increaseAmount\":" + increaseAmount + ",\n" +
                                        "\"clientId\":1" +
                                        "}")
        )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json("{" +
                                "\"increaseAmount\":" +
                                expectedIncrease +
                                ",\n" +
                                "\"clientId\":1" +
                                "}")
                )
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        ;
    }

}
