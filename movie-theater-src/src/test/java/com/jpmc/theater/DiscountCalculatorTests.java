package com.jpmc.theater;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

class DiscountCalculatorTests {

	@Test
	void calculateDiscount() {
		var customer = new Customer("John Doe", "unused-id");
        var showing = new Showing(
                new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 1),
                5,
                LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0))
        );
        assertTrue(DiscountCalculator.calculateDiscount(showing)==2.5);
	}
	@Test
    void totalDiscountedFeeSpecialAndFirstShow() {
        var customer = new Customer("John Doe", "unused-id");
        var showing = new Showing(
                new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 1),
                1,
                LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0))
        );
        assertTrue(DiscountCalculator.calculateDiscount(showing) == 3);
    }
    
    @Test
    void totalDiscountedFeeSpecialAndSecondShow() {
        var customer = new Customer("John Doe", "unused-id");
        var showing = new Showing(
                new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 1),
                2,
                LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0))
        );
        assertTrue(DiscountCalculator.calculateDiscount(showing)  == 2.5);
    }
    
    @Test
    void totalDiscountedFeeSpecialFirstShow11to4() {
        var customer = new Customer("John Doe", "unused-id");
        var showing = new Showing(
                new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 1),
                1,
                LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 0))
        );
        assertTrue(DiscountCalculator.calculateDiscount(showing) == 3.125);
    }
}
