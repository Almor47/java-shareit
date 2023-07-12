package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.enumerated.Status;
import ru.practicum.shareit.booking.exception.BadRequestBookingException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.exception.BadRequestCommentException;
import ru.practicum.shareit.item.exception.BadRequestItemException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.UpdateWithoutXSharerException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.pagination.Pagination;
import ru.practicum.shareit.request.service.RequestServiceImpl;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final RequestServiceImpl requestService;
    private final Pagination pagination;

    @Transactional
    @Override
    public Item addItem(Item item, Long userId) {
        if (userId == null) {
            throw new UserNotFoundException("У предмета отсутствует пользователь");
        } else if (item.getAvailable() == null) {
            throw new BadRequestItemException("У предмета отсутствует доступность");
        } else if (item.getName() == null || item.getName().isEmpty()) {
            throw new BadRequestItemException("У предмета отсутствует имя");
        } else if (item.getDescription() == null || item.getDescription().isEmpty()) {
            throw new BadRequestItemException("У предмета отсутствует описание");
        } else if (userService.getUserById(userId) == null) {
            throw new UserNotFoundException("Пользователя с таким id " + userId + " не существует");
        }
        item.setOwner(userId);
        return itemRepository.save(item);
    }

    @Transactional
    @Override
    public Item updateItem(Item item, Long userId, Long itemId) {
        if (userId == null) {
            throw new UpdateWithoutXSharerException("Запрос без X-Sharer-User-Id");
        }
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден");
        }
        long owner = getItemByIdRepository(itemId).getOwner();
        if (owner != userId) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не является владельцем вещи");
        }

        Item itemDb = getItemByIdRepository(itemId);
        if (item.getName() != null) {
            itemDb.setName(item.getName());
        }
        if (item.getDescription() != null) {
            itemDb.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            itemDb.setAvailable(item.getAvailable());
        }

        return itemRepository.save(itemDb);
    }

    @Override
    public ItemDto getItemById(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new ItemNotFoundException("Предмет с id " + itemId + "не найден"));
        List<CommentDto> commentsDto = listCommentToListCommentDto(item);
        if (item.getOwner() != userId) {
            return ItemMapper.mapToItemDto(item, null, null, commentsDto);
        }
        Booking last = bookingRepository.findLastBooking(itemId, LocalDateTime.now()).stream().findFirst().orElse(null);
        Booking next = bookingRepository.findNextBooking(itemId, LocalDateTime.now()).stream().findFirst().orElse(null);
        return ItemMapper.mapToItemDto(item, last, next, commentsDto);

    }

    @Override
    public List<ItemDto> getUserItem(Long userId, Integer from, Integer size) {
        pagination.checkPagination(from,size);
        Pageable page = PageRequest.of(from / size, size);
        List<Item> itemList = itemRepository.findAllByOwner(userId,page);
        List<Long> itemIdList = new ArrayList<>();
        for (Item one : itemList) {
            itemIdList.add(one.getId());
        }
        List<Booking> bookings = bookingRepository.findAllByItemIdInAndStatusNotOrderByStartAsc(itemIdList,
                Status.REJECTED,page);
        Map<Long, Booking> lastBookings = new HashMap<>();
        LocalDateTime ldt = LocalDateTime.now();
        for (Booking one : bookings) {
            if (lastBookings.get(one.getItemId()) == null) {
                if (one.getStart().isBefore(ldt)) {
                    lastBookings.put(one.getItemId(), one);
                } else {
                    continue;
                }
            }
            if (lastBookings.get(one.getItemId()).getStart().isBefore(one.getStart())
                    && one.getStart().isBefore(ldt)) {
                lastBookings.put(one.getItemId(), one);
            }
        }
        Map<Long, Booking> nextBookings = new HashMap<>();
        for (Booking one : bookings) {
            if (nextBookings.get(one.getItemId()) == null) {
                if (one.getStart().isAfter(ldt)) {
                    nextBookings.put(one.getItemId(), one);
                } else {
                    continue;
                }
            }
            if (nextBookings.get(one.getItemId()).getStart().isAfter(one.getStart())
                    && one.getStart().isAfter(ldt)) {
                nextBookings.put(one.getItemId(), one);
            }
        }
        List<ItemDto> ans = new ArrayList<>();
        for (Item one : itemList) {
            Booking last = lastBookings.get(one.getId());
            Booking next = nextBookings.get(one.getId());
            List<CommentDto> commentsDto = listCommentToListCommentDto(one);
            ans.add(ItemMapper.mapToItemDto(one, last, next, commentsDto));
        }
        return ans;

    }

    @Override
    public List<Item> searchItem(String text, Integer from, Integer size) {
        pagination.checkPagination(from,size);
        Pageable page = PageRequest.of(from / size, size);
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }
        return itemRepository.search(text,page);
    }

    @Override
    public Item getItemByIdRepository(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(
                () -> new ItemNotFoundException("Предмет с id " + itemId + "не найден"));
    }

    @Transactional
    public CommentDto addComment(Comment comment, Long userId, Long itemId) {
        if (comment.getText() == null || comment.getText().isEmpty()) {
            throw new BadRequestCommentException("У комментария отсутсвует текст");
        }
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = bookingRepository.findAllByBookerIdAndItemIdAndStatusAndEndBefore(userId, itemId,
                Status.APPROVED, now);
        Booking booking = bookings.stream().findFirst().orElseThrow(() ->
                new BadRequestBookingException("У пользователя нет законченных бронирований"));
        User user = userService.getUserById(userId);
        comment.setItemId(itemId);
        comment.setAuthorId(userId);
        comment.setCreated(now);
        commentRepository.save(comment);
        return CommentMapper.commentToCommentDto(comment, user);
    }


    private List<CommentDto> listCommentToListCommentDto(Item item) {
        List<Comment> comments = item.getComment();
        List<CommentDto> commentsDto = new ArrayList<>();
        for (Comment one : comments) {
            commentsDto.add(CommentMapper.commentToCommentDto(one, one.getAuthor()));
        }
        return commentsDto;
    }

}
