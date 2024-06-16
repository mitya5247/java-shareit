package ru.practicum.shareit.item;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class Mapper {
    public static ItemDto convertToDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.isAvailable());
        if (item.getNextBooking() != null || item.getLastBooking() != null) {
            itemDto.setLastBooking(Mapper.convertToBookingDto(item.getLastBooking()));
            itemDto.setNextBooking(Mapper.convertToBookingDto(item.getNextBooking()));
        }
        return itemDto;
    }

    public static Item convertToItem(Long userId, ItemDto itemDto) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setOwner(userId);
        item.setAvailable(itemDto.getAvailable());
        if (itemDto.getNextBooking() != null || itemDto.getLastBooking() != null) {
            item.setLastBooking(Mapper.convertToBooking(itemDto.getLastBooking()));
            item.setNextBooking(Mapper.convertToBooking(itemDto.getNextBooking()));
        }
        return item;
    }

    public static Booking convertToBooking(BookingDto bookingDto) {
        Booking booking = new Booking();

        booking.setStatus(bookingDto.getStatus());
        booking.setEnd(bookingDto.getEnd());
        booking.setStart(bookingDto.getStart());

        return booking;
    }

    public static BookingDto convertToBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();

        bookingDto.setId(booking.getId());
        bookingDto.setStatus(booking.getStatus());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setStart(booking.getStart());
        bookingDto.setBookerId(booking.getBooker().getId());
        bookingDto.setItemId(booking.getItem().getId());
        return bookingDto;
    }

    public static BookingDtoResponse convertToBookingDtoResponse(Booking booking) {
        BookingDtoResponse bookingDtoResponse = new BookingDtoResponse();

        bookingDtoResponse.setId(booking.getId());
        bookingDtoResponse.setStatus(booking.getStatus());
        bookingDtoResponse.setEnd(booking.getEnd());
        bookingDtoResponse.setStart(booking.getStart());
        bookingDtoResponse.setBooker(booking.getBooker());
        bookingDtoResponse.setItem(Mapper.convertToDto(booking.getItem()));
        return bookingDtoResponse;
    }

}
