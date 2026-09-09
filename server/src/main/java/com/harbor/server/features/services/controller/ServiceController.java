package com.harbor.server.features.services.controller;

import com.harbor.server.features.services.dto.request.CreateServiceRequest;
import com.harbor.server.features.services.dto.response.ServiceResponse;
import com.harbor.server.features.services.service.CreateService;
import com.harbor.server.features.services.service.GetServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/services")
@RequiredArgsConstructor
public class ServiceController {

  private final CreateService createService;
  private final GetServices getServices;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ServiceResponse createService(@Valid @RequestBody CreateServiceRequest body) {
    return createService.execute(body);
  }

  @GetMapping
  public List<ServiceResponse> getServices() {
    return getServices.execute();
  }
}
