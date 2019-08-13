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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@SpringBootTest
// todo: this enables MockMvc to be instantiated. In our case it is not required, since we are instantiating it
//@AutoConfigureMockMvc
@AutoConfigureStubRunner(ids="com.vgrazi.presentations:spring-cloud-contracts-provider:+:stubs:9080", stubsMode = StubRunnerProperties.StubsMode.LOCAL)
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

    private ClientConsumerController controller;

    @Test
    public void shouldIncreaseCreditLineWhenAvailableCredit() throws Exception {
        controller = new ClientConsumerController(new RestTemplate(), portfolioRepository,
                clientRepository, pricingRepository, "localhost",
                9080, "/request-credit-increase");

        Client client = new Client(1, "Jonn Jonz", "12345", 1_000_000, 1000);
        when(portfolioRepository.getClient(anyInt())).thenReturn(client);
        when(portfolioRepository.getAvailableFunds(any(Client.class))).thenCallRealMethod();
        when(pricingRepository.getPrice(any(Stock.class))).thenReturn(120.0);
// todo: Presentation: at this point, we traditionally might stub the rest call.
//  However that is not a scalable solution, since the endpoint might change!
//  For this, we need contract-generated stubs.
// when(restTemplate.postForObject(any(URI.class), any(CreditIncreaseRequest.class), any(Class.class))).thenReturn(150_000.0);

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
