package com.harbor.server.features.tickets.service;

import com.harbor.server.common.dto.PageResponse;
import com.harbor.server.common.security.CurrentUserProvider;
import com.harbor.server.common.security.CustomUserDetails;
import com.harbor.server.features.tickets.dto.request.GetRequesterTicketsQuery;
import com.harbor.server.features.tickets.dto.response.RequesterTicketItemResponse;
import com.harbor.server.features.tickets.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetRequesterTickets {
  private final CurrentUserProvider currentUserProvider;
  private final TicketRepository ticketRepository;

  public PageResponse<RequesterTicketItemResponse> execute(GetRequesterTicketsQuery request) {
    CustomUserDetails userDetails = currentUserProvider.getCurrentUser();

    Pageable pageable =
        PageRequest.of(
            request.page() - 1, request.limit(), Sort.by(Sort.Direction.DESC, "createdAt"));

    return PageResponse.from(
        ticketRepository.findRequesterTickets(
            userDetails.getId(),
            userDetails.getOrganizationId(),
            pageable,
            request.search(),
            request.status()));
  }
}
