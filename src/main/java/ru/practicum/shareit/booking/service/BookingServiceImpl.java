package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
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
import ru.practicum.shareit.booking.exception.WrongState;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;


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
        if (booking.getBookerId() != userId) {
            if (item.getUser().getId() != userId) {
                throw new UserNotFoundException("Пользователя с id " + userId + "не найден");
            }
        }
        return BookingMapper.mapToFullBooking(booking, user, item);
    }

    @Override
    public List<FullBookingDto> getAllUserBooking(String state, Long userId) {
        User user = userService.getUserById(userId);
        Sort rule = Sort.by(Sort.Direction.DESC, "start");
        if (state.equals(State.ALL.name())) {
            return bookingRepository.findAllByBookerId(userId, rule)
                    .stream()
                    .map(booking -> BookingMapper.mapToFullBooking(booking, user,
                            itemService.getItemByIdRepository(booking.getItemId())))
                    .collect(Collectors.toList());
        } else if (state.equals(State.CURRENT.name())) {
            return bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(userId, LocalDateTime.now(),
                            LocalDateTime.now(),
                            Sort.by(Sort.Direction.ASC, "start"))
                    .stream()
                    .map(booking -> BookingMapper.mapToFullBooking(booking, user,
                            itemService.getItemByIdRepository(booking.getItemId())))
                    .collect(Collectors.toList());
        } else if (state.equals(State.PAST.name())) {
            return bookingRepository.findAllByBookerIdAndEndBefore(userId,
                            LocalDateTime.now(), rule)
                    .stream()
                    .map(booking -> BookingMapper.mapToFullBooking(booking, user,
                            itemService.getItemByIdRepository(booking.getItemId())))
                    .collect(Collectors.toList());
        } else if (state.equals(State.FUTURE.name())) {
            return bookingRepository.findAllByBookerIdAndStartAfter(userId,
                            LocalDateTime.now(), rule)
                    .stream()
                    .map(booking -> BookingMapper.mapToFullBooking(booking, user,
                            itemService.getItemByIdRepository(booking.getItemId())))
                    .collect(Collectors.toList());
        } else if (state.equals(State.WAITING.name())) {
            return bookingRepository.findAllByBookerIdAndStatus(userId, Status.WAITING, rule)
                    .stream()
                    .map(booking -> BookingMapper.mapToFullBooking(booking, user,
                            itemService.getItemByIdRepository(booking.getItemId())))
                    .collect(Collectors.toList());
        } else if (state.equals(State.REJECTED.name())) {
            return bookingRepository.findAllByBookerIdAndStatus(userId, Status.REJECTED, rule)
                    .stream()
                    .map(booking -> BookingMapper.mapToFullBooking(booking, user,
                            itemService.getItemByIdRepository(booking.getItemId())))
                    .collect(Collectors.toList());
        } else {
            throw new WrongState(String.format("Unknown state: %s", state));
        }

    }

    @Override
    public List<FullBookingDto> getAllItemUserBooking(String states, Long userId) {
        State state;
        try {
            state = State.valueOf(states);
        } catch (IllegalArgumentException e) {
            throw new WrongState(String.format("Unknown state: %s", states));
        }
        User user = userService.getUserById(userId);
        return bookingRepository.findAllByOwnerId(userId, String.valueOf(state),
                        LocalDateTime.now())
                .stream()
                .map(booking -> BookingMapper.mapToFullBooking(booking,
                        userService.getUserById(booking.getBookerId()),
                        itemService.getItemByIdRepository(booking.getItemId())))
                .collect(Collectors.toList());
    }

    private Booking getBooking(Long bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(
                () -> new BookingNotFoundException("Бронирование с bookingId " + bookingId + " не найдено"));
    }


}
