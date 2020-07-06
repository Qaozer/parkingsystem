package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.Date;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown(){

    }


    @Test
    public void testParkingACar() throws Exception{
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        assertEquals(1, parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR));
        parkingService.processIncomingVehicle();
        assertNotNull(ticketDAO.getTicket("ABCDEF"));
        assertNotNull(ticketDAO.getTicket("ABCDEF").getOutTime());
        assertEquals(2, parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR));
    }

    @Test
    public void testParkingLotExit() throws Exception{
        testParkingACar();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        assertEquals(new Timestamp(new Date(0).getTime()), ticketDAO.getTicket("ABCDEF").getOutTime());
        Thread.sleep(2000);
        parkingService.processExitingVehicle();
        assertEquals(new Date().getTime()/1000, ticketDAO.getTicket("ABCDEF").getOutTime().getTime()/1000);
        assertEquals(1, parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR));
    }

    @Test
    public void testReducedFare() throws Exception{
        testParkingLotExit();
        assertEquals(false, ticketDAO.isReduced("ABCDEF"));
        testParkingACar();
        assertEquals(true, ticketDAO.isReduced("ABCDEF"));
    }

    @Test
    public void testTwoEntry()throws Exception{
        testParkingLotExit();
        testParkingLotExit();
    }

    @Test
    public void saveAndGetTicketTest(){
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        Ticket ticket = new Ticket();
        Ticket returnedTicket;
        Date inTime = new Date();
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("AAAA");
        ticket.setPrice(0);
        ticket.setInTime(inTime);
        ticket.setOutTime(new Date(0));

        returnedTicket = ticketDAO.getTicket("AAAA");
        assertNull(returnedTicket);
        ticketDAO.saveTicket(ticket);
        returnedTicket = ticketDAO.getTicket("AAAA");
        assertEquals(returnedTicket.getParkingSpot(), parkingSpot);
        assertEquals(returnedTicket.getVehicleRegNumber(), "AAAA");
        assertEquals(returnedTicket.getPrice(),0.0);
        assertEquals(returnedTicket.getInTime().getTime()/(1000*60), ticket.getInTime().getTime()/(1000*60));
    }
}
