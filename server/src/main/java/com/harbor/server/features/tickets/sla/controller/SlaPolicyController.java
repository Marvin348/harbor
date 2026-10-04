package com.harbor.server.features.tickets.sla.controller;

import com.harbor.server.features.tickets.sla.dto.request.UpdateSlaPolicyRequest;
import com.harbor.server.features.tickets.sla.dto.response.SlaPolicyResponse;
import com.harbor.server.features.tickets.sla.service.GetSlaPolicies;
import com.harbor.server.features.tickets.sla.service.UpdateSlaPolicy;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sla-policies")
@RequiredArgsConstructor
public class SlaPolicyController {

  private final UpdateSlaPolicy updateSlaPolicy;
  private final GetSlaPolicies getSlaPolicies;

  @PatchMapping("/{id}")
  public void updateSlaPolicy(
      @PathVariable @Positive Long id, @Valid @RequestBody UpdateSlaPolicyRequest body) {
    updateSlaPolicy.execute(id, body);
  }

  @GetMapping
  public List<SlaPolicyResponse> getSlaPolicies() {
    return getSlaPolicies.execute();
  }
}
