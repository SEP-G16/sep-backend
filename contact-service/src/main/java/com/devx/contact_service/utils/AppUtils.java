package com.devx.contact_service.utils;

import com.devx.contact_service.dto.SupportTicketDto;
import com.devx.contact_service.dto.TicketResponseDto;
import com.devx.contact_service.dto.response.AddSupportTicketResponseBody;
import com.devx.contact_service.dto.response.AddTicketResponseResponseBody;
import com.devx.contact_service.dto.response.GetAllSupportTicketsResponseDto;
import com.devx.contact_service.dto.response.GetAllSupportTicketsSingleTicketDto;
import com.devx.contact_service.model.SupportTicket;
import com.devx.contact_service.model.TicketResponse;

public class AppUtils {

    private AppUtils() {}

    public static class SupportTicketUtils{
        public static SupportTicketDto convertEntityToDto(SupportTicket supportTicket)
        {
            SupportTicketDto supportTicketDto = new SupportTicketDto();
            if(supportTicket != null)
            {
                supportTicketDto.setId(supportTicket.getId());
                supportTicketDto.setName(supportTicket.getName());
                supportTicketDto.setEmail(supportTicket.getEmail());
                supportTicketDto.setDescription(supportTicket.getDescription());
                supportTicketDto.setResponses(supportTicket.getResponses().stream().map(TicketResponseUtils::convertEntityToDto).toList());
                supportTicketDto.setCreatedAt(supportTicket.getCreatedAt());
                supportTicketDto.setUpdatedAt(supportTicket.getUpdatedAt());
            }
            return supportTicketDto;
        }

        public static SupportTicket convertDtoToEntity(SupportTicketDto supportTicketDto)
        {
            SupportTicket supportTicket = new SupportTicket();
            supportTicket.setId(supportTicketDto.getId());
            supportTicket.setName(supportTicketDto.getName());
            supportTicket.setEmail(supportTicketDto.getEmail());
            supportTicket.setDescription(supportTicketDto.getDescription());
            supportTicket.setResponses(supportTicketDto.getResponses().stream().map(TicketResponseUtils::convertDtoToEntity).toList());
            supportTicket.setCreatedAt(supportTicketDto.getCreatedAt());
            supportTicket.setUpdatedAt(supportTicketDto.getUpdatedAt());
            return supportTicket;
        }
    }

    public static class TicketResponseUtils{
        public static TicketResponseDto convertEntityToDto(TicketResponse ticketResponse)
        {

            TicketResponseDto ticketResponseDto = new TicketResponseDto();
            ticketResponseDto.setId(ticketResponse.getId());
            ticketResponseDto.setResponse(ticketResponse.getResponse());
            ticketResponseDto.setSupportTicket(SupportTicketUtils.convertEntityToDto(ticketResponse.getSupportTicket()));
            ticketResponseDto.setCreatedAt(ticketResponse.getCreatedAt());
            ticketResponseDto.setUpdatedAt(ticketResponse.getUpdatedAt());
            return ticketResponseDto;
        }

        public static TicketResponse convertDtoToEntity(TicketResponseDto ticketResponseDto)
        {
            TicketResponse ticketResponse = new TicketResponse();
            ticketResponse.setId(ticketResponseDto.getId());
            ticketResponse.setResponse(ticketResponseDto.getResponse());
            ticketResponse.setSupportTicket(SupportTicketUtils.convertDtoToEntity(ticketResponseDto.getSupportTicket()));
            ticketResponse.setCreatedAt(ticketResponseDto.getCreatedAt());
            ticketResponse.setUpdatedAt(ticketResponseDto.getUpdatedAt());
            return ticketResponse;
        }
    }

    public static class ResponseDtoMapper {
        public static GetAllSupportTicketsSingleTicketDto convertDtoToResponse(SupportTicketDto supportTicketDto)
        {
            GetAllSupportTicketsSingleTicketDto response = new GetAllSupportTicketsSingleTicketDto();
            response.setId(supportTicketDto.getId());
            response.setName(supportTicketDto.getName());
            response.setEmail(supportTicketDto.getEmail());
            response.setDescription(supportTicketDto.getDescription());
            response.setResponses(supportTicketDto.getResponses().stream().map(ResponseDtoMapper::convertTicketResponseDtoToResponseBody).toList());
            response.setCreatedAt(supportTicketDto.getCreatedAt());
            response.setUpdatedAt(supportTicketDto.getUpdatedAt());
            return response;
        }

        public static GetAllSupportTicketsResponseDto convertTicketResponseDtoToResponseBody(TicketResponseDto ticketResponseDto)
        {
            GetAllSupportTicketsResponseDto response = new GetAllSupportTicketsResponseDto();
            response.setId(ticketResponseDto.getId());
            response.setResponse(ticketResponseDto.getResponse());
            response.setCreatedAt(ticketResponseDto.getCreatedAt());
            response.setUpdatedAt(ticketResponseDto.getUpdatedAt());
            return  response;
        }


        public static AddSupportTicketResponseBody convertDtoToAddTicketResponseBody(SupportTicketDto supportTicketDto)
        {
            AddSupportTicketResponseBody responseBody = new AddSupportTicketResponseBody();
            responseBody.setId(supportTicketDto.getId());
            responseBody.setName(supportTicketDto.getName());
            responseBody.setEmail(supportTicketDto.getEmail());
            responseBody.setDescription(supportTicketDto.getDescription());
            responseBody.setCreatedAt(supportTicketDto.getCreatedAt());
            responseBody.setUpdatedAt(supportTicketDto.getUpdatedAt());
            return responseBody;
        }

        public static AddTicketResponseResponseBody convertTicketResponseDtoToAddTicketResponseBody(TicketResponseDto ticketResponseDto)
        {
            AddTicketResponseResponseBody responseBody = new AddTicketResponseResponseBody();
            responseBody.setId(ticketResponseDto.getId());
            responseBody.setResponse(ticketResponseDto.getResponse());
            responseBody.setCreatedAt(ticketResponseDto.getCreatedAt());
            responseBody.setUpdatedAt(ticketResponseDto.getUpdatedAt());
            return responseBody;
        }
    }
}
