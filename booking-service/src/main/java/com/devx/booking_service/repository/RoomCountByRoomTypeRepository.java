package com.devx.booking_service.repository;

import com.devx.booking_service.exception.KeyNotFoundException;
import com.devx.booking_service.exception.StoredProcedureCallException;
import com.devx.booking_service.record.RoomCountByRoomType;
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
public class RoomCountByRoomTypeRepository {

    public static final Logger LOG = LoggerFactory.getLogger(RoomCountByRoomTypeRepository.class);

    private final SimpleJdbcCall roomCountByCategoryProcCall;

    // Constructor to initialize the SimpleJdbcCall with the procedure name
    @Autowired
    public RoomCountByRoomTypeRepository(DataSource dataSource) {
        this.roomCountByCategoryProcCall = new SimpleJdbcCall(dataSource)
                .withProcedureName("Get_Available_Room_Count");
    }

    // Method to call the stored procedure and get available room counts
    public List<RoomCountByRoomType> getAvailableRoomCount(LocalDate checkinDate, LocalDate checkoutDate) throws StoredProcedureCallException, KeyNotFoundException {
        try {
            // Set input parameters for the stored procedure
            SqlParameterSource in = new MapSqlParameterSource()
                    .addValue("checkin_date", checkinDate)
                    .addValue("checkout_date", checkoutDate);

            // Execute the stored procedure and return the result
            Map<String, Object> result = roomCountByCategoryProcCall.execute(in);

            List<Map<String, Object>> mapList = ((List<Map<String, Object>>) result.get("#result-set-1"));

            for(Map<String, Object> map : mapList)
            {
                boolean roomTypeIdExists = map.containsKey("room_type_id");
                boolean typeExists = map.containsKey("type");
                boolean roomCountExists = map.containsKey("room_count");

                if(!(roomTypeIdExists && typeExists && roomCountExists))
                {
                    throw new KeyNotFoundException("Key not found");
                }
            }

            return ((List<Map<String, Object>>) result.get("#result-set-1")).stream().map(map -> new RoomCountByRoomType(
                        (Long) map.get("room_type_id"),
                        (String) map.get("type"),
                        (int) map.get("room_count")
                    )
            ).toList();
        }
        catch (KeyNotFoundException e) {
            throw new KeyNotFoundException(e.getMessage());
        }

        catch (ClassCastException e)
        {
            throw new ClassCastException(e.getMessage());
        }

        catch (Exception e)
        {
            throw new StoredProcedureCallException(e.getMessage());
        }
    }
}
