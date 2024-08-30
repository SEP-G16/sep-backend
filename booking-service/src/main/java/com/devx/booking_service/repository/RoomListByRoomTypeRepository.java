package com.devx.booking_service.repository;

import com.devx.booking_service.exception.KeyNotFoundException;
import com.devx.booking_service.exception.StoredProcedureCallException;
import com.devx.booking_service.record.RoomListByRoomType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public class RoomListByRoomTypeRepository {
    public static final Logger LOG = LoggerFactory.getLogger(RoomListByRoomTypeRepository.class);

    private final SimpleJdbcCall roomListByRoomTypeProcCall;

    // Constructor to initialize the SimpleJdbcCall with the procedure name
    @Autowired
    public RoomListByRoomTypeRepository(DataSource dataSource) {
        this.roomListByRoomTypeProcCall = new SimpleJdbcCall(dataSource)
                .withProcedureName("Get_Available_Rooms_List");
    }

    // Method to call the stored procedure and get available room counts
    public List<RoomListByRoomType> getAvailableRoomList(LocalDate checkinDate, LocalDate checkoutDate, Long roomTypeId) throws StoredProcedureCallException, KeyNotFoundException {
        try {
            // Set input parameters for the stored procedure
            SqlParameterSource in = new MapSqlParameterSource()
                    .addValue("checkin_date", checkinDate)
                    .addValue("checkout_date", checkoutDate)
                    .addValue("room_type_id", roomTypeId);

            // Execute the stored procedure and return the result
            Map<String, Object> result = roomListByRoomTypeProcCall.execute(in);

            List<Map<String, Object>> mapList = ((List<Map<String, Object>>) result.get("#result-set-1"));

            for (Map<String, Object> map : mapList) {
                boolean idExists = map.containsKey("id");
                boolean roomNoExists = map.containsKey("room_no");
                if (!(idExists && roomNoExists)) {
                    throw new KeyNotFoundException("Key not found");
                }
            }

            return ((List<Map<String, Object>>) result.get("#result-set-1")).stream().map(map -> new RoomListByRoomType(
                    (Long) map.get("id"),
                    (int) map.get("room_no")
                    )
            ).toList();
        } catch (KeyNotFoundException e) {
            throw new KeyNotFoundException(e.getMessage());
        } catch (ClassCastException e) {
            throw new ClassCastException(e.getMessage());
        } catch (Exception e) {
            throw new StoredProcedureCallException(e.getMessage());
        }
    }
}
