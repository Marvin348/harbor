package com.harbor.server.features.tickets.sla.controller;

import com.harbor.server.features.tickets.sla.dto.request.CreateSlaPolicyRequest;
import com.harbor.server.features.tickets.sla.dto.response.SlaPolicyResponse;
import com.harbor.server.features.tickets.sla.service.CreateSlaPolicy;
import com.harbor.server.features.tickets.sla.service.GetSlaPolicies;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sla-policies")
@RequiredArgsConstructor
public class SlaPolicyController {

  private final CreateSlaPolicy createSlaPolicy;
  private final GetSlaPolicies getSlaPolicies;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void createSlaPolicy(@Valid @RequestBody CreateSlaPolicyRequest body) {
    createSlaPolicy.execute(body);
  }

  @GetMapping
  public List<SlaPolicyResponse> getSlaPolicies() {
    return getSlaPolicies.execute();
  }
}
