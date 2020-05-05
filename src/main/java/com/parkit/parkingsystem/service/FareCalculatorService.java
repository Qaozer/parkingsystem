package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    private final double ONEHOUR = 3600000;
    private TicketDAO ticketDAO = new TicketDAO();

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        double reduction = ticketDAO.isReduced(ticket.getVehicleRegNumber()) ? 0.95 : 1;

        long inHour = ticket.getInTime().getTime();
        long outHour = ticket.getOutTime().getTime();

        double factor = (double) (outHour - inHour) / ONEHOUR;
        factor = (factor < 0.5) ? 0 : factor; //if duration was less than 30min, parking is free

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(factor * Fare.CAR_RATE_PER_HOUR * reduction);
                break;
            }
            case BIKE: {
                ticket.setPrice(factor * Fare.BIKE_RATE_PER_HOUR * reduction);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}