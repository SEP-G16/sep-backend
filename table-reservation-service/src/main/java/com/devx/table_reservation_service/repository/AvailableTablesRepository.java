package com.devx.table_reservation_service.repository;

import com.devx.table_reservation_service.exception.KeyNotFoundException;
import com.devx.table_reservation_service.exception.StoredProcedureCallException;
import com.devx.table_reservation_service.model.RestaurantTable;
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
public class AvailableTablesRepository {
    public static final Logger LOG = LoggerFactory.getLogger(AvailableTablesRepository.class);

    private final SimpleJdbcCall availableTablesProcCall;

    // Constructor to initialize the SimpleJdbcCall with the procedure name
    @Autowired
    public AvailableTablesRepository(DataSource dataSource) {
        this.availableTablesProcCall = new SimpleJdbcCall(dataSource)
                .withProcedureName("Get_Available_Room_Count");
    }

    // Method to call the stored procedure and get available room counts
    public List<RestaurantTable> getAvailableRoomCount(LocalDate selectedDate, int timeSlotStart, int timeSlotEnd) throws StoredProcedureCallException, KeyNotFoundException {
        try {
            // Set input parameters for the stored procedure
            SqlParameterSource in = new MapSqlParameterSource()
                    .addValue("selected_date", selectedDate)
                    .addValue("time_slot_start", timeSlotStart)
                    .addValue("time_slot_end", timeSlotEnd);

            // Execute the stored procedure and return the result
            Map<String, Object> result = availableTablesProcCall.execute(in);

            List<Map<String, Object>> mapList = ((List<Map<String, Object>>) result.get("#result-set-1"));

            for(Map<String, Object> map : mapList)
            {
                boolean idExists = map.containsKey("id");
                boolean tableNoExists = map.containsKey("table_no");
                boolean chairCountExists = map.containsKey("chair_count");

                if(!(idExists && tableNoExists && chairCountExists))
                {
                    throw new KeyNotFoundException("Key not found");
                }
            }

            return ((List<Map<String, Object>>) result.get("#result-set-1")).stream().map(map -> new RestaurantTable(
                            (Long) map.get("id"),
                            (int) map.get("table_no"),
                            (int) map.get("chair_count")
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
