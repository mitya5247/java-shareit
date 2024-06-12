package ru.practicum.shareit.item;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

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

    public static Booking convertToBooking(Long userId, BookingDto bookingDto) {
        Booking booking = new Booking();
        User user = new User();
        Item item = new Item();

    //    user.setId(userId);
    //    item.setId(bookingDto.getItemId());

        booking.setStatus(bookingDto.getStatus());
        booking.setEnd(bookingDto.getEnd());
        booking.setStart(bookingDto.getStart());
        booking.setBooker(user);
        booking.setItem(item);
        return booking;
    }

    public static BookingDto convertToBookingDto(Long userId, Booking booking) {
        BookingDto bookingDto = new BookingDto();
        User user = new User();
        Item item = new Item();

        //    user.setId(userId);
        //    item.setId(bookingDto.getItemId());

        bookingDto.setStatus(booking.getStatus());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setStart(booking.getStart());
   //     bookingDto.setBooker(user.getId());
        bookingDto.setItemId(item.getId());
        return bookingDto;
    }
}
