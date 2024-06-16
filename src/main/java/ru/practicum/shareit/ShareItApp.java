package ru.practicum.shareit;

import org.hibernate.engine.internal.StatefulPersistenceContext;
import org.hibernate.engine.spi.PersistenceContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.practicum.shareit.booking.Booking;

@SpringBootApplication
public class ShareItApp {

	public static void main(String[] args) {
		SpringApplication.run(ShareItApp.class, args);
	}

}
