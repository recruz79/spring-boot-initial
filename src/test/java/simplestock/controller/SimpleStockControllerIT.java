package simplestock.controller;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.net.URL;
import java.sql.Timestamp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import simplestock.model.Trade;
import simplestock.model.TradeType;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SimpleStockControllerIT {

    @LocalServerPort
    private int port;

    private URL baseURL;

    @Autowired
    private TestRestTemplate template;

    @Before
    public void setUp() throws Exception {
        this.baseURL = new URL("http://localhost:" + port + "/");
    }

    @Test
    public void postTrade() throws Exception {
        Trade trade = new Trade("TEA", new Timestamp(System.currentTimeMillis()), 12, TradeType.BUY, 5L);
        URL url = new URL(baseURL, "trade");
        RequestEntity<Trade> request = RequestEntity.post(url.toURI())
                .contentType(MediaType.APPLICATION_JSON).body(trade);

        ResponseEntity<Void> response = template.exchange(request, Void.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

}