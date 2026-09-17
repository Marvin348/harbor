package com.harbor.server.features.serviceTeam.controller;

import com.harbor.server.features.serviceTeam.dto.request.CreateServiceTeamRequest;
import com.harbor.server.features.serviceTeam.dto.request.GetServiceTeamsQuery;
import com.harbor.server.features.serviceTeam.dto.response.ServiceTeamListItemResponse;
import com.harbor.server.features.serviceTeam.dto.response.ServiceTeamOptionResponse;
import com.harbor.server.features.serviceTeam.dto.response.ServiceTeamResponse;
import com.harbor.server.features.serviceTeam.service.CreateServiceTeam;
import com.harbor.server.features.serviceTeam.service.GetServiceTeamsOptions;
import com.harbor.server.features.serviceTeam.service.GetServiceTeams;
import com.harbor.server.features.serviceTeam.service.GetServicesByServiceTeam;
import com.harbor.server.features.services.dto.response.ServiceResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/service-teams")
@RequiredArgsConstructor
public class ServiceTeamController {

  private final CreateServiceTeam createServiceTeam;
  private final GetServiceTeams getServiceTeams;
  private final GetServicesByServiceTeam getServicesByServiceTeam;
  private final GetServiceTeamsOptions getServiceTeamsOptions;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ServiceTeamResponse createServiceTeam(@Valid @RequestBody CreateServiceTeamRequest body) {
    return createServiceTeam.createServiceTeam(body);
  }

  @GetMapping
  public Page<ServiceTeamListItemResponse> getServiceTeams(
      @Valid GetServiceTeamsQuery queryParams) {
    return getServiceTeams.execute(queryParams);
  }

  @GetMapping("/options")
  public List<ServiceTeamOptionResponse> getServiceTeamOptions() {
    return getServiceTeamsOptions.execute();
  }

  @GetMapping("/{serviceTeamId}")
  public List<ServiceResponse> getServiceTeamServices(@PathVariable @Positive Long serviceTeamId) {
    return getServicesByServiceTeam.execute(serviceTeamId);
  }
}
