package com.jpmc.theater;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;
import java.util.Set;

public final class DiscountCalculator {
	private static Logger logger = LogManager.getLogger(DiscountCalculator.class);
	private static Properties sequenceDiscountCache = new Properties();
	private static Properties showTimeDiscountCache = new Properties();
	private static Properties specialMovieDiscountCache = new Properties();
	
	static {
		loadDiscount();
	}
	
	public static double calculateDiscount(Showing showing) {
		double specialDiscount = 0;
		double sequenceDiscount = 0;
		double showTimeDiscount = 0;

		showTimeDiscount = calculateShowTimeDiscount(showing);
		specialDiscount = calculateSpecialDiscount(showing.getMovie());
		sequenceDiscount = calculateSequenceDiscount(showing);
//        else {
//            throw new IllegalArgumentException("failed exception");
//        }
		// biggest discount wins
		return (showTimeDiscount > specialDiscount && showTimeDiscount > sequenceDiscount) ? showTimeDiscount
				: (specialDiscount > showTimeDiscount && specialDiscount > sequenceDiscount) ? specialDiscount
						: sequenceDiscount;
	}

	/**
	 * @param movie
	 * @param movieCodeSpecial
	 * @param specialDiscount
	 * @return
	 */
	private static double calculateSpecialDiscount(Movie movie) {
		double specialDiscount = 0;
		int movieCodeSpecial = movie.getSpecialCode();
		if(!specialMovieDiscountCache.isEmpty()) {
			Set<Entry<Object, Object>> discounts = specialMovieDiscountCache.entrySet();
		    for (Entry<Object, Object> discount : discounts) {
		      if(movieCodeSpecial==Integer.valueOf(discount.getKey().toString())) {
		    	  specialDiscount = movie.getTicketPrice() * Integer.valueOf(discount.getValue().toString())/100; // 20% discount for special movie
		      }
		    }
		}
		return specialDiscount;
	}

	/**
	 * @param showing
	 * @param sequenceDiscount
	 * @return
	 */
	private static double calculateSequenceDiscount(Showing showing) {
		double sequenceDiscount = 0;
		if(!sequenceDiscountCache.isEmpty()) {
			Set<Entry<Object, Object>> discounts = sequenceDiscountCache.entrySet();
		    for (Entry<Object, Object> discount : discounts) {
		      if(showing.getSequenceOfTheDay()==Integer.valueOf(discount.getKey().toString())) {
		    	  sequenceDiscount = Double.valueOf(discount.getValue().toString());
		      }
		    }
		}
		return sequenceDiscount;
	}

	/**
	 * 25% discount on Showtime between 11AM-4PM
	 * 
	 * @param showing
	 * @param movie
	 * @param discountedStartTime
	 * @param discountedEndTime
	 * @param showTimeDiscount
	 * @return
	 */
	private static double calculateShowTimeDiscount(Showing showing) {
		double showTimeDiscount = 0.0;
		if(!showTimeDiscountCache.isEmpty()) {
			Movie movie = showing.getMovie();
			int afterHour = Integer.valueOf(showTimeDiscountCache.getProperty("after"));
			int beforeHour = Integer.valueOf(showTimeDiscountCache.getProperty("before"));
			double discountPercent = Double.valueOf(showTimeDiscountCache.getProperty("discount"));
			
		    
			LocalTime discountedStartTime = LocalTime.of(afterHour, 0);
			LocalTime discountedEndTime = LocalTime.of(beforeHour, 0);
	
			if (showing.getStartTime().toLocalTime().isAfter(discountedStartTime)
					&& showing.getStartTime().toLocalTime().isBefore(discountedEndTime)) {
				showTimeDiscount = movie.getTicketPrice() * (discountPercent/100);
			}
		}
		return showTimeDiscount;
	}
	
	private static void loadDiscount() {
		try (InputStream showingDiscountStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("showingDiscount.properties");
				InputStream specialMovieDiscount = Thread.currentThread().getContextClassLoader().getResourceAsStream("specialMovieDiscount.properties");
				InputStream showTimeDiscount = Thread.currentThread().getContextClassLoader().getResourceAsStream("showTimeDiscount.properties")) {
			
			System.out.println("Reading all properties from the file");
			sequenceDiscountCache.load(showingDiscountStream);
			showTimeDiscountCache.load(showTimeDiscount);
			specialMovieDiscountCache.load(specialMovieDiscount);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
