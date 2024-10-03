package com.devx.table_reservation_service.utils;

import com.devx.table_reservation_service.dto.ReservationDto;
import com.devx.table_reservation_service.dto.RestaurantTableDto;
import com.devx.table_reservation_service.exception.NullFieldException;
import com.devx.table_reservation_service.model.Reservation;
import com.devx.table_reservation_service.model.RestaurantTable;

public class AppUtils {
    private AppUtils() {
    }

    public static class ReservationUtils{
        public static ReservationDto convertReservationEntityToReservationDto(Reservation reservation) {
            ReservationDto reservationDto = new ReservationDto();
            reservationDto.setId(reservation.getId());
            reservationDto.setCustomerName(reservation.getCustomerName());
            reservationDto.setReservedDate(reservation.getReservedDate());
            reservationDto.setPeopleCount(reservation.getPeopleCount());
            reservationDto.setPhoneNo(reservation.getPhoneNo());
            reservationDto.setTimeSlotStart(reservation.getTimeSlotStart());
            reservationDto.setTimeSlotEnd(reservation.getTimeSlotEnd());
            reservationDto.setRestaurantTableList(reservation.getRestaurantTableList().stream().map(RestaurantTableUtils::convertRestaurantTableEntityToRestaurantTableDto).toList());
            return reservationDto;
        }

        public static Reservation convertReservationDtoToReservationEntity(ReservationDto reservationDto) {
            if(reservationDto.hasNullFields())
            {
                throw new NullFieldException("ReservationDto has null fields");
            }
            Reservation reservation = new Reservation();
            reservationDto.setId(reservationDto.getId());
            reservation.setCustomerName(reservationDto.getCustomerName());
            reservation.setReservedDate(reservationDto.getReservedDate());
            reservation.setPeopleCount(reservationDto.getPeopleCount());
            reservation.setPhoneNo(reservationDto.getPhoneNo());
            reservation.setTimeSlotStart(reservationDto.getTimeSlotStart());
            reservation.setTimeSlotEnd(reservationDto.getTimeSlotEnd());
            reservation.setRestaurantTableList(reservationDto.getRestaurantTableList().stream().map(tableDto -> {
                if(tableDto.hasNullFields())
                {
                    throw new NullFieldException("RestaurantTableDto has null fields");
                }
                return RestaurantTableUtils.convertRestaurantTableDtoToRestaurantTableEntity(tableDto);
            }).toList());
            return reservation;
        }
    }


    public static class RestaurantTableUtils{
        public static RestaurantTableDto convertRestaurantTableEntityToRestaurantTableDto(RestaurantTable restaurantTable) {
            RestaurantTableDto restaurantTableDto = new RestaurantTableDto();
            restaurantTableDto.setId(restaurantTable.getId());
            restaurantTableDto.setTableNo(restaurantTable.getTableNo());
            restaurantTableDto.setChairCount(restaurantTable.getChairCount());
            return restaurantTableDto;
        }

        public static RestaurantTable convertRestaurantTableDtoToRestaurantTableEntity(RestaurantTableDto restaurantTableDto) {
            if(restaurantTableDto.hasNullFields())
            {
                throw new NullFieldException("RestaurantTableDto has null fields");
            }
            RestaurantTable restaurantTable = new RestaurantTable();
            restaurantTable.setId(restaurantTableDto.getId());
            restaurantTable.setTableNo(restaurantTableDto.getTableNo());
            restaurantTable.setChairCount(restaurantTableDto.getChairCount());
            return restaurantTable;
        }
    }



}
