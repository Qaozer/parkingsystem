package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class ParkingSpotTest {
    private static ParkingSpot parkingSpot;

    @BeforeEach
    private void init(){
        parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
    }

    @Test
    public void getIdTest(){
        assertEquals(1, parkingSpot.getId());
    }

    @Test
    public void setIdTest(){
        parkingSpot.setId(2);
        assertEquals(2, parkingSpot.getId());
    }

    @Test
    public void getParkingTypeTest(){
        assertEquals(ParkingType.CAR, parkingSpot.getParkingType());
    }

    @Test
    public void setParkingTypeTest(){
        parkingSpot.setParkingType(ParkingType.BIKE);
        assertEquals(ParkingType.BIKE, parkingSpot.getParkingType());
    }

    @Test
    public void isAvailableTest(){
        assertTrue(parkingSpot.isAvailable());
    }

    @Test
    public void setAvailableTest(){
        parkingSpot.setAvailable(false);
        assertFalse(parkingSpot.isAvailable());
    }

    @Test
    public void equalsTest(){
        ParkingSpot parkingSpotB = new ParkingSpot(1, ParkingType.CAR, true);
        assertTrue(parkingSpot.equals(parkingSpotB));
        parkingSpotB.setId(2);
        assertFalse(parkingSpot.equals(parkingSpotB));
    }
}
