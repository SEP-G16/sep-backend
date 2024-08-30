package com.devx.booking_service.record;

public record RoomListByRoomType(Long roomId, int roomNo) {
    public RoomListByRoomType(Long roomId, int roomNo)
    {
        this.roomId = roomId;
        this.roomNo = roomNo;
    }
}
