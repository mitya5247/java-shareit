package ru.practicum.shareit.item;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class Mapper {
    public static ItemDto convertToDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.isAvailable());
        return itemDto;
    }

    public static Item convertToItem(Long userId, ItemDto itemDto) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setOwner(userId);
        item.setAvailable(itemDto.getAvailable());
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

        bookingDto.setStatus(booking.getStatus());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setStart(booking.getStart());
        bookingDto.setBooker(booking.getBooker().getId());
        bookingDto.setItemId(booking.getItem().getId());
        return bookingDto;
    }

}
