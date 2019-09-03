package com.vgrazi.presentations.springcloudcontractconsumer;

import com.vgrazi.presentations.springcloudcontractconsumer.controller.ClientConsumerController;
import com.vgrazi.presentations.springcloudcontractconsumer.domain.Client;
import com.vgrazi.presentations.springcloudcontractconsumer.domain.Stock;
import com.vgrazi.presentations.springcloudcontractconsumer.repository.ClientRepository;
import com.vgrazi.presentations.springcloudcontractconsumer.repository.PortfolioRepository;
import com.vgrazi.presentations.springcloudcontractconsumer.repository.PricingRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureStubRunner(ids="com.vgrazi.presentations:spring-cloud-contracts-provider:+:stubs:9080", stubsMode = StubRunnerProperties.StubsMode.LOCAL)
public class SpringCloudContractConsumerApplicationTests {

    private MockMvc mockMvc;
    @Mock
    PortfolioRepository portfolioRepository;
    @Mock
    ClientRepository clientRepository;
    @Mock
    PricingRepository pricingRepository;

    RestTemplate restTemplate = new RestTemplate();

    private ClientConsumerController controller;

    @Test
    public void shouldIncreaseCreditLineWhenAvailableCredit() throws Exception {
        controller = new ClientConsumerController(restTemplate, portfolioRepository,
                clientRepository, pricingRepository, "localhost",
                9080, "/request-credit-increase");

        Client client = new Client(1, "Mary Smith", "123-45-6789", 100_000, 1000);
        when(portfolioRepository.getClient(anyInt())).thenReturn(client);
        when(portfolioRepository.getAvailableFunds(any(Client.class))).thenCallRealMethod();
        when(pricingRepository.getPrice(any(Stock.class))).thenReturn(120.0);

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
                                "  \"date\":" + LocalDateTime.now().toEpochSecond(ZoneOffset.MIN) + "\n," +
                                "  \"shares\": \"1000\"\n" +
                                "}"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json("{\n" +
                                "    \"client\": {\n" +
                                "        \"clientId\": 1,\n" +
                                "        \"taxId\": \"123-45-6789\",\n" +
                                "        \"creditLimit\": 120000,\n" +
                                "        \"cashOnDeposit\": 1000,\n" +
                                "        \"positions\": []\n" +
                                "    },\n" +
                                "    \"stock\": {\n" +
                                "        \"symbol\": \"MSFT\",\n" +
                                "        \"exchange\": \"NASD\"\n" +
                                "    },\n" +
                                "    \"date\": \"" + LocalDate.now().format(DateTimeFormatter.ISO_DATE) + "\"\n," +
                                "    \"shares\": 1000\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void shouldDenyWhenNoAvailableCredit() throws Exception {
        controller = new ClientConsumerController(restTemplate, portfolioRepository,
                clientRepository, pricingRepository, "localhost",
                9080, "/request-credit-increase");

        Client client = new Client(2, "John Jones", "246-80-1357", 100_000, 1000);
        when(portfolioRepository.getClient(anyInt())).thenReturn(client);
        when(portfolioRepository.getAvailableFunds(any(Client.class))).thenCallRealMethod();
        when(pricingRepository.getPrice(any(Stock.class))).thenReturn(120.0);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mockMvc.perform(
                MockMvcRequestBuilders.post("/buy-sell")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"clientId\": \"2\",\n" +
                                "  \"stock\": {\n" +
                                "    \"symbol\": \"MSFT\",\n" +
                                "    \"exchange\": \"NASD\"\n" +
                                "  },\n" +
                                "  \"date\":" + LocalDateTime.now().toEpochSecond(ZoneOffset.MIN) + "\n," +
                                "  \"shares\": \"10000\"\n" +
                                "}"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json("{\n" +
                                "    \"client\": {\n" +
                                "        \"clientId\": 2,\n" +
                                "        \"taxId\": \"246-80-1357\",\n" +
                                "        \"creditLimit\": 100000,\n" +
                                "        \"cashOnDeposit\": 1000,\n" +
                                "        \"positions\": []\n" +
                                "    },\n" +
                                "    \"stock\": {\n" +
                                "        \"symbol\": \"MSFT\",\n" +
                                "        \"exchange\": \"NASD\"\n" +
                                "    },\n" +
                                "    \"shares\": 0\n," +
                                "    \"date\": \"" + LocalDate.now().format(DateTimeFormatter.ISO_DATE) + "\"\n," +
                                "    \"denialReason\":\"Credit line has reached its max. Available: 900000.0\"" +
                                "}"))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}
