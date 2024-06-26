package ru.practicum.shareit.item;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

public class Mapper {
    public static ItemDto convertToDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.isAvailable());
        if (item.getLastBooking() != null) {
            itemDto.setLastBooking(Mapper.convertToBookingDto(item.getLastBooking()));
        }
        if (item.getNextBooking() != null) {
            itemDto.setNextBooking(Mapper.convertToBookingDto(item.getNextBooking()));
        }
        if (item.getRequestId() != null) {
            itemDto.setRequestId(item.getRequestId());
        }

        List<CommentDto> commentDtoList = item.getComments().stream()
                .map(comment -> Mapper.convertCommentToDto(comment))
                .collect(Collectors.toList());
        itemDto.setComments(commentDtoList);

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
        if (itemDto.getRequestId() != null) {
            item.setRequestId(itemDto.getRequestId());
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

    public static CommentDto convertCommentToDto(Comment comment) {
        CommentDto commentDto = new CommentDto();

        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setAuthorName(comment.getUser().getName());
        commentDto.setCreated(comment.getCreated());
        return commentDto;
    }

}
