package com.devx.staff_service.unit;

import com.devx.staff_service.dto.StaffDto;
import com.devx.staff_service.model.Staff;
import com.devx.staff_service.service.StaffService;
import com.devx.staff_service.service.StaffServiceIntegration;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ContextConfiguration
@ActiveProfiles(value = "test")
public class StaffServiceUnitTests {

    @InjectMocks
    private StaffService staffService;

    @Mock
    private StaffServiceIntegration staffServiceIntegration;

    private StaffDto staffDto;
    private List<StaffDto> staffList;

    @BeforeEach
    public void setup(){
        staffDto = new StaffDto();

        staffList = new ArrayList<>();
        staffList.add(staffDto);

        when(staffServiceIntegration.addStaff(any(Staff.class))).thenReturn(Mono.just(staffDto));
        when(staffServiceIntegration.getAllStaff()).thenReturn(Mono.just(staffList).flatMapMany(Flux::fromIterable));
        //TODO:write another mock behaviours
    }

    //TODO:write unit tests
}
