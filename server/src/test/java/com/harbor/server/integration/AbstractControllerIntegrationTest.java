package com.harbor.server.integration;

import com.harbor.server.integration.helper.TestAuthHelper;
import com.harbor.server.integration.helper.TestDataFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@Import({TestAuthHelper.class, TestDataFactory.class})
public abstract class AbstractControllerIntegrationTest extends AbstractIntegrationTest {

  @Autowired protected MockMvc mockMvc;
  @Autowired protected TestAuthHelper testAuthHelper;
  @Autowired protected TestDataFactory testDataFactory;
}
