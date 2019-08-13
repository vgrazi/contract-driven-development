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
        postRequest(0, 1_000, 10_000, null);
    }

    @Test
    public void shouldIncreaseCreditLineTo100_000() throws Exception {
        postRequest(90_000, 98_000, 100_000, null);
    }

    @Test
    public void shouldIncreaseCreditLineTo1000_000() throws Exception {
        postRequest(900_000, 98_000, 100_000, null);
    }

    @Test
    public void shouldDenyIncrease() throws Exception {
        postRequest(1_000_000, 1, 0, "Credit line has reached its max. Available: 0.0");
    }

    private void postRequest(double currentCreditLine, double increaseAmount, double expectedIncrease, String denialReason) throws Exception {
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
                                (denialReason != null ? ",\"denialReason\":" + "\"" + denialReason + "\"" : "") +
                                "}")
                )
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        ;
    }

}
