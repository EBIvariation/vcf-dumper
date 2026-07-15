/*
 * Copyright 2017 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.ebi.eva.vcfdump.evawsclient;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;

import uk.ac.ebi.eva.vcfdump.MockServerClientHelper;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;

public class EvaWsClientTest {

    private static ClientAndServer mockServer;
    private MockServerClient mockServerClient;

    @BeforeAll
    public static void startServer() {
        mockServer = startClientAndServer(0);
    }

    @AfterAll
    public static void stopServer() {
        mockServer.stop();
    }

    @BeforeEach
    public void setUp() {
        mockServerClient = new MockServerClient("localhost", mockServer.getLocalPort());
        mockServerClient.reset();
        MockServerClientHelper.hSapiensGrch37(mockServerClient, "hsapiens_grch37");
    }

    @Test
    public void getChromosomes() throws Exception {
        EvaWsClient evaWsClient = new EvaWsClient("eva_hsapiens_grch37".replace("eva_", ""),
                String.format("http://localhost:%s/eva/webservices/rest/", mockServer.getLocalPort()),
                "v1");

        assertEquals(
                new HashSet<>(Arrays.asList("1", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                        "19", "2", "20", "21", "22", "3", "4", "5", "6", "7", "8", "9", "MT", "X", "Y")),
                evaWsClient.getChromosomes()
        );
    }

}