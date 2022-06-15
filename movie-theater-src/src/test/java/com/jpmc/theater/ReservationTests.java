package com.jpmc.theater;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReservationTests {

    @Test
    void totalFee() {
        var customer = new Customer("John Doe", "unused-id");
        var showing = new Showing(
                new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 1),
                1,
                LocalDateTime.now()
        );
//this assert will some time pass and fail depending up its showStartTime (in 11am-4pm duration or not) as it is taking current LocalDtaeTime
        assertTrue(new Reservation(customer, showing, 3).totalFee() == 28.5);
    }
    
    @Test
    void totalDiscountedFee() {
        var customer = new Customer("John Doe", "unused-id");
        var showing = new Showing(
                new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 1),
                1,
                LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0))
        );
        System.out.println("3$ Discounted movie ticket as it's showing 1st of the day");
        assertTrue(new Reservation(customer, showing, 3).totalFee() == 28.5);
    }
    
    @Test
    void totalDiscountedFeeShowing7th() {
        var customer = new Customer("John Doe", "unused-id");
        var showing = new Showing(
                new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 0),
                7,
                LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0))
        );
        System.out.println("1$ Discounted movie ticket as its showing 7th of the day");
        assertTrue(new Reservation(customer, showing, 3).totalFee() == 34.5);
    }
    
    @Test
    void totalDiscountedFeeShowing11to4() {
        var customer = new Customer("John Doe", "unused-id");
        var showing = new Showing(
                new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 0),
                7,
                LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 0))
        );
        System.out.println("$25 Discounted movie ticket as its showing between 11AM-4PM i.e 9.375*3=28.125");
        assertTrue(new Reservation(customer, showing, 3).totalFee() == 28.125);
    }
    
}
