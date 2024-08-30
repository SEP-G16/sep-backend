package com.devx.booking_service.record;


public record RoomCountByRoomType(Long roomTypeId, String type, int roomCount) {
    public RoomCountByRoomType(Long roomTypeId, String type, int roomCount)
    {
        this.roomTypeId = roomTypeId;
        this.type = type;
        this.roomCount = roomCount;
    }

    @Override
    public String toString() {
        return "RoomCountByRoomType{" +
                "roomTypeId=" + roomTypeId +
                ", type='" + type + '\'' +
                ", roomCount=" + roomCount +
                '}';
    }
}
