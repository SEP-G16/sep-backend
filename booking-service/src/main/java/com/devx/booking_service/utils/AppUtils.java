package com.devx.booking_service.utils;

import com.devx.booking_service.dto.BookingDto;
import com.devx.booking_service.dto.RoomDto;
import com.devx.booking_service.dto.RoomTypeDto;
import com.devx.booking_service.dto.TempBookingDto;
import com.devx.booking_service.dto.request.AddRoomTypeRequestBody;
import com.devx.booking_service.exception.NoRoomsSelectedException;
import com.devx.booking_service.exception.NullFieldException;
import com.devx.booking_service.exception.RoomCountMismatchException;
import com.devx.booking_service.model.Booking;
import com.devx.booking_service.model.Room;
import com.devx.booking_service.model.RoomType;
import com.devx.booking_service.model.TempBooking;

public class AppUtils {
    private AppUtils(){}

    public static class TempBookingUtils{
        public static TempBookingDto convertEntityToDto(TempBooking tempBooking)
        {
            TempBookingDto tempBookingDto = new TempBookingDto();
            tempBookingDto.setId(tempBooking.getId());
            tempBookingDto.setCreatedAt(tempBooking.getCreatedAt());
            tempBookingDto.setCustomerName(tempBooking.getCustomerName());
            tempBookingDto.setEmail(tempBooking.getEmail());
            tempBookingDto.setPhoneNo(tempBooking.getPhoneNo());
            tempBookingDto.setRoomType(RoomTypeUtils.convertEntityToDto(tempBooking.getRoomType()));
            tempBookingDto.setAdultCount(tempBooking.getAdultCount());
            tempBookingDto.setChildrenCount(tempBooking.getChildrenCount());
            tempBookingDto.setRoomCount(tempBooking.getRoomCount());
            tempBookingDto.setCheckinDate(tempBooking.getCheckinDate());
            tempBookingDto.setCheckoutDate(tempBooking.getCheckoutDate());
            return tempBookingDto;
        }

        public static TempBooking convertDtoToEntity(TempBookingDto tempBookingDto)
        {
            if(tempBookingDto.hasNullFields())
            {
                throw new NullFieldException("Required field cannot be null");
            }
            TempBooking tempBooking = new TempBooking();
            tempBooking.setId(tempBookingDto.getId());
            tempBooking.setCreatedAt(tempBookingDto.getCreatedAt());
            tempBooking.setCustomerName(tempBookingDto.getCustomerName());
            tempBooking.setEmail(tempBookingDto.getEmail());
            tempBooking.setPhoneNo(tempBookingDto.getPhoneNo());
            tempBooking.setRoomType(RoomTypeUtils.convertDtoToEntity(tempBookingDto.getRoomType()));
            tempBooking.setAdultCount(tempBookingDto.getAdultCount());
            tempBooking.setChildrenCount(tempBookingDto.getChildrenCount());
            tempBooking.setRoomCount(tempBookingDto.getRoomCount());
            tempBooking.setCheckinDate(tempBookingDto.getCheckinDate());
            tempBooking.setCheckoutDate(tempBookingDto.getCheckoutDate());
            return tempBooking;
        }
    }

    public static class RoomTypeUtils{
        public static RoomTypeDto convertEntityToDto(RoomType roomType)
        {
            RoomTypeDto roomTypeDto = new RoomTypeDto();
            roomTypeDto.setId(roomType.getId());
            roomTypeDto.setType(roomType.getType());
            roomTypeDto.setPrice(roomType.getPrice());
            return roomTypeDto;
        }

        public static RoomType convertDtoToEntity(RoomTypeDto roomTypeDto)
        {
            if(roomTypeDto.isEveryFieldNull()){
                throw new NullFieldException("Both id and name cannot be null");
            }
            RoomType roomType = new RoomType();
            roomType.setId(roomTypeDto.getId());
            roomType.setType(roomTypeDto.getType());
            return roomType;
        }

        public static RoomType convertAddRoomTypeReqBodyToEntity(AddRoomTypeRequestBody requestBody)
        {
            RoomType roomType = new RoomType();
            roomType.setId(requestBody.getId());
            roomType.setType(requestBody.getType());
            roomType.setPrice(requestBody.getPrice());
            roomType.setDescription(requestBody.getDescription());
            roomType.setKeywords(requestBody.getKeywords());
            roomType.setBedCount(requestBody.getBedCount());
            roomType.setFloorArea(requestBody.getFloorArea());
            roomType.setMaximumOccupancy(requestBody.getMaximumOccupancy());
            return roomType;
        }
    }

    public static class BookingUtils{
        public static BookingDto convertEntityToDto(Booking booking)
        {
            BookingDto bookingDto = new BookingDto();
            bookingDto.setId(booking.getId());
            bookingDto.setCreatedAt(booking.getCreatedAt());
            bookingDto.setCustomerName(booking.getCustomerName());
            bookingDto.setEmail(booking.getEmail());
            bookingDto.setPhoneNo(booking.getPhoneNo());
            bookingDto.setRoomType(RoomTypeUtils.convertEntityToDto(booking.getRoomType()));
            bookingDto.setAdultCount(booking.getAdultCount());
            bookingDto.setChildrenCount(booking.getChildrenCount());
            bookingDto.setRoomCount(booking.getRoomCount());
            bookingDto.setRoomList(booking.getRoomList().stream().map(RoomUtils::convertEntityToDto).toList());
            bookingDto.setCheckinDate(booking.getCheckinDate());
            bookingDto.setCheckoutDate(booking.getCheckoutDate());
            return bookingDto;
        }

        public static Booking convertDtoToEntity(BookingDto bookingDto)
        {
            if(bookingDto.hasNullFields())
            {
                throw new NullFieldException("Required field cannot be null");
            }
            if(bookingDto.hasNullOrEmptyRoomList())
            {
                throw new NoRoomsSelectedException("Room list cannot be null or empty");
            }

            if(bookingDto.isRoomCountAndListLengthMismatch())
            {
                throw new RoomCountMismatchException("Room count and room list length mismatch");
            }

            Booking booking = new Booking();
            booking.setId(bookingDto.getId());
            booking.setCreatedAt(bookingDto.getCreatedAt());
            booking.setCustomerName(bookingDto.getCustomerName());
            booking.setEmail(bookingDto.getEmail());
            booking.setPhoneNo(bookingDto.getPhoneNo());
            booking.setRoomType(RoomTypeUtils.convertDtoToEntity(bookingDto.getRoomType()));
            booking.setAdultCount(bookingDto.getAdultCount());
            booking.setChildrenCount(bookingDto.getChildrenCount());
            booking.setRoomCount(bookingDto.getRoomCount());
            booking.setRoomList(bookingDto.getRoomList().stream().map(RoomUtils::convertDtoToEntity).toList());
            booking.setCheckinDate(bookingDto.getCheckinDate());
            booking.setCheckoutDate(bookingDto.getCheckoutDate());
            return booking;
        }
    }

    public static class RoomUtils{
        public static RoomDto convertEntityToDto(Room room)
        {
            RoomDto roomDto = new RoomDto();
            roomDto.setId(room.getId());
            roomDto.setRoomNo(room.getRoomNo());
            roomDto.setRoomType(RoomTypeUtils.convertEntityToDto(room.getRoomType()));
            return roomDto;
        }

        public static Room convertDtoToEntity(RoomDto roomDto)
        {
            if(roomDto.isEveryFieldNull()){
                throw new NullFieldException("Both id and room number cannot be null");
            }
            Room room = new Room();
            room.setId(roomDto.getId());
            room.setRoomNo(roomDto.getRoomNo());
            room.setRoomType(RoomTypeUtils.convertDtoToEntity(roomDto.getRoomType()));
            return room;
        }
    }
}
