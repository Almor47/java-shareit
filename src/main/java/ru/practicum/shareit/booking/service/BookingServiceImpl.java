package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.FullBookingDto;
import ru.practicum.shareit.booking.enumerated.State;
import ru.practicum.shareit.booking.enumerated.Status;
import ru.practicum.shareit.booking.exception.BadRequestBookingException;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.exception.WrongStateException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.service.RequestServiceImpl;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;
    private final RequestServiceImpl requestService;


    @Transactional
    @Override
    public FullBookingDto addBooking(BookingDto bookingDto, Long userId) {
        LocalDateTime ldt = LocalDateTime.now();
        if (userId == null) {
            throw new UserNotFoundException("У предмета отсутствует пользователь");
        } else if (bookingDto.getStart() == null) {
            throw new BadRequestBookingException("Время старта равно null");
        } else if (bookingDto.getEnd() == null) {
            throw new BadRequestBookingException("Время окончания равно null");
        } else if (bookingDto.getStart().equals(bookingDto.getEnd())) {
            throw new BadRequestBookingException("Время старта равно времени окончания");
        } else if (bookingDto.getStart().isAfter(bookingDto.getEnd())) {
            throw new BadRequestBookingException("Время окончания в прошлом");
        } else if (bookingDto.getStart().isBefore(ldt)) {
            throw new BadRequestBookingException("Время старта в прошлом");
        }
        Item item = itemService.getItemByIdRepository(bookingDto.getItemId());
        User user = userService.getUserById(userId);
        if (!item.getAvailable()) {
            throw new BadRequestBookingException("Данная вещь недоступна к бронированию");
        }
        if (item.getOwner() == userId) {
            throw new BookingNotFoundException("Пользователь не может взять в аренду вещь сам у себя");
        }
        Booking booking = BookingMapper.mapToBooking(bookingDto, user);
        bookingRepository.save(booking);
        return BookingMapper.mapToFullBooking(booking, user, item);
    }

    @Transactional
    @Override
    public FullBookingDto updateBooking(Long bookingId, boolean approved, Long userId) {
        Booking booking = getBooking(bookingId);
        Item item = itemService.getItemByIdRepository(booking.getItemId());
        User user = userService.getUserById(booking.getBookerId());
        if (item.getUser().getId() != userId) {
            throw new BookingNotFoundException("Пользователь с id " + userId + " не является владельцем вещи");
        }
        if (booking.getStatus() == Status.APPROVED) {
            throw new BadRequestBookingException("Бронирование уже подтверждено");
        }
        Status status;
        if (approved) {
            status = Status.APPROVED;
            booking.setStatus(status);
        } else {
            status = Status.REJECTED;
            booking.setStatus(status);
        }
        bookingRepository.save(booking);
        return BookingMapper.mapToFullBooking(booking, user, item);
    }


    @Override
    public FullBookingDto getBookingById(Long bookingId, Long userId) {
        Booking booking = getBooking(bookingId);
        Item item = itemService.getItemByIdRepository(booking.getItemId());
        User user = userService.getUserById(booking.getBookerId());
        if (!Objects.equals(booking.getBookerId(), userId)) {
            if (item.getUser().getId() != userId) {
                throw new UserNotFoundException("Пользователя с id " + userId + "не найден");
            }
        }
        return BookingMapper.mapToFullBooking(booking, user, item);
    }

    @Override
    public List<FullBookingDto> getAllUserBooking(String state, Long userId, Integer from, Integer size) {
        User user = userService.getUserById(userId);
        Sort rule = Sort.by(Sort.Direction.DESC, "start");
        requestService.checkPagination(from,size);
        if (State.ALL.name().equals(state)) {
            Pageable page = PageRequest.of(from / size, size, Sort.Direction.DESC, "start");
            return bookingRepository.findAllByBookerId(userId, page)
                    .stream()
                    .map(booking -> BookingMapper.mapToFullBooking(booking, user, booking.getItem()))
                    .collect(Collectors.toList());
        } else if (State.CURRENT.name().equals(state)) {
            Pageable page = PageRequest.of(from / size, size,Sort.Direction.ASC, "item_id");
            return bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(userId, LocalDateTime.now(),
                            LocalDateTime.now(), page)
                    .stream()
                    .map(booking -> BookingMapper.mapToFullBooking(booking, user, booking.getItem()))
                    .collect(Collectors.toList());
        } else if (State.PAST.name().equals(state)) {
            Pageable page = PageRequest.of(from / size, size, Sort.Direction.DESC, "start");
            return bookingRepository.findAllByBookerIdAndEndBefore(userId,
                            LocalDateTime.now(),page)
                    .stream()
                    .map(booking -> BookingMapper.mapToFullBooking(booking, user, booking.getItem()))
                    .collect(Collectors.toList());
        } else if (State.FUTURE.name().equals(state)) {
            Pageable page = PageRequest.of(from / size, size, Sort.Direction.DESC, "start");
            return bookingRepository.findAllByBookerIdAndStartAfter(userId,
                            LocalDateTime.now(), page)
                    .stream()
                    .map(booking -> BookingMapper.mapToFullBooking(booking, user, booking.getItem()))
                    .collect(Collectors.toList());
        } else if (State.WAITING.name().equals(state)) {
            Pageable page = PageRequest.of(from / size, size, Sort.Direction.DESC, "start");
            return bookingRepository.findAllByBookerIdAndStatus(userId, Status.WAITING, page)
                    .stream()
                    .map(booking -> BookingMapper.mapToFullBooking(booking, user, booking.getItem()))
                    .collect(Collectors.toList());
        } else if (State.REJECTED.name().equals(state)) {
            Pageable page = PageRequest.of(from / size, size, Sort.Direction.DESC, "start");
            return bookingRepository.findAllByBookerIdAndStatus(userId, Status.REJECTED, page)
                    .stream()
                    .map(booking -> BookingMapper.mapToFullBooking(booking, user, booking.getItem()))
                    .collect(Collectors.toList());
        } else {
            throw new WrongStateException(String.format("Unknown state: %s", state));
        }

    }

    @Override
    public List<FullBookingDto> getAllItemUserBooking(String states, Long userId, Integer from, Integer size) {
        State state;
        try {
            state = State.valueOf(states);
        } catch (IllegalArgumentException e) {
            throw new WrongStateException(String.format("Unknown state: %s", states));
        }
        requestService.checkPagination(from,size);
        Pageable page = PageRequest.of(from / size, size);
        userService.getUserById(userId);
        return bookingRepository.findAllByOwnerId(userId, String.valueOf(state),
                        LocalDateTime.now(), page)
                .stream()
                .map(booking -> BookingMapper.mapToFullBooking(booking,
                        booking.getUser(), booking.getItem()))
                .collect(Collectors.toList());
    }

    private Booking getBooking(Long bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(
                () -> new BookingNotFoundException("Бронирование с bookingId " + bookingId + " не найдено"));
    }


}
