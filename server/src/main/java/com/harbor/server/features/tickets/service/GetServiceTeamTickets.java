package com.harbor.server.features.tickets.service;

import com.harbor.server.common.dto.PageResponse;
import com.harbor.server.common.exception.ForbiddenException;
import com.harbor.server.common.exception.NotFoundException;
import com.harbor.server.common.security.CurrentUserProvider;
import com.harbor.server.common.security.CustomUserDetails;
import com.harbor.server.features.serviceTeam.repository.ServiceTeamMemberRepository;
import com.harbor.server.features.serviceTeam.repository.ServiceTeamRepository;
import com.harbor.server.features.tickets.dto.request.GetServiceTeamTicketsQuery;
import com.harbor.server.features.tickets.dto.response.ServiceTeamTicketListItemResponse;
import com.harbor.server.features.tickets.repository.TicketRepository;
import com.harbor.server.features.user.model.OrganizationRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetServiceTeamTickets {
  private final CurrentUserProvider currentUserProvider;
  private final TicketRepository ticketRepository;
  private final ServiceTeamRepository serviceTeamRepository;
  private final ServiceTeamMemberRepository serviceTeamMemberRepository;

  public PageResponse<ServiceTeamTicketListItemResponse> execute(
      Long serviceTeamId, GetServiceTeamTicketsQuery query) {
    Long organizationId = currentUserProvider.getCurrentOrganizationId();
    CustomUserDetails currentUser = currentUserProvider.getCurrentUser();

    Long currentUserId = currentUser.getId();
    OrganizationRole role = currentUser.getRole();

    if (!serviceTeamRepository.existsByIdAndOrganizationId(serviceTeamId, organizationId)) {
      throw new NotFoundException("Service team not found");
    }

    if (role == OrganizationRole.AGENT
        && !serviceTeamMemberRepository.existsByServiceTeamIdAndUserId(
            serviceTeamId, currentUserId)) {
      throw new ForbiddenException("You do not have access to this service team");
    }

    Pageable pageable =
        PageRequest.of(query.page() - 1, query.limit(), Sort.by(Sort.Direction.DESC, "createdAt"));

    return PageResponse.from(
        ticketRepository.findServiceTeamTickets(
            organizationId,
            serviceTeamId,
            pageable,
            query.search(),
            query.status(),
            query.priority()));
  }
}
