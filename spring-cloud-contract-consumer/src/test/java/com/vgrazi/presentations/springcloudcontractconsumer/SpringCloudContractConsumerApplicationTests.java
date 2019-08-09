package com.vgrazi.presentations.springcloudcontractconsumer;

import com.vgrazi.presentations.springcloudcontractconsumer.controller.ClientConsumerController;
import com.vgrazi.presentations.springcloudcontractconsumer.domain.Client;
import com.vgrazi.presentations.springcloudcontractconsumer.repository.ClientRepository;
import com.vgrazi.presentations.springcloudcontractconsumer.repository.PortfolioRepository;
import com.vgrazi.presentations.springcloudcontractconsumer.repository.PricingRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SpringCloudContractConsumerApplicationTests {

    private MockMvc mockMvc;
    @Mock
    PortfolioRepository portfolioRepository;
    @Mock
    ClientRepository clientRepository;
    @Mock
    PricingRepository pricingRepository;
    @Mock
    RestTemplate restTemplate;


    @InjectMocks
    private ClientConsumerController controller;

    @Test
    public void shouldIncreaseCreditLineWhenAvailableCredit() throws Exception {
        when(portfolioRepository.getClient(anyInt())).thenReturn(new Client(1, "Jonn Jonz", "12345", 1_000_000, 1000));
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mockMvc.perform(
                MockMvcRequestBuilders.post("/buy-sell")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"clientId\": \"1\",\n" +
                                "  \"stock\": {\n" +
                                "    \"symbol\": \"MSFT\",\n" +
                                "    \"exchange\": \"NASD\"\n" +
                                "  },\n" +
                                "  \"shares\": \"1000\"\n" +
                                "}"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json("{\n" +
                                "    \"client\": {\n" +
                                "        \"clientId\": 1,\n" +
                                "        \"taxId\": \"12345\",\n" +
                                "        \"creditLimit\": 1000000.0,\n" +
                                "        \"cashOnDeposit\": 1000.0,\n" +
                                "        \"positions\": []\n" +
                                "    },\n" +
                                "    \"stock\": {\n" +
                                "        \"symbol\": \"MSFT\",\n" +
                                "        \"exchange\": \"NASD\"\n" +
                                "    },\n" +
                                "    \"shares\": 1000\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}
