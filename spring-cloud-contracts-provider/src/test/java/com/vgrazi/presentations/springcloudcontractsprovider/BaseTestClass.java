package com.vgrazi.presentations.springcloudcontractsprovider;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;

public class BaseTestClass {
    @Before
    public void before() {
        ClientProviderController clientProviderController = new ClientProviderController(10_000, 1_000_000);
        RestAssuredMockMvc.standaloneSetup(clientProviderController);
    }
}
