package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.enumerated.Status;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerId(Long booker, Pageable page);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(Long booker, LocalDateTime start, LocalDateTime end, Pageable page);

    List<Booking> findAllByBookerIdAndEndBefore(Long booker, LocalDateTime end, Pageable page);

    List<Booking> findAllByBookerIdAndStartAfter(Long booker, LocalDateTime start, Pageable page);

    List<Booking> findAllByBookerIdAndStatus(Long booker, Status status, Pageable page);

    @Query(" select b from Booking b " +
            " where b.item.owner = ?1 " +
            "   and (?2 = 'ALL' or " +
            "        (?2 = 'CURRENT' and ?3 between b.start and b.end) or " +
            "        (?2 = 'PAST' and ?3 > b.end) or " +
            "        (?2 = 'FUTURE' and ?3 < b.start) or " +
            "        (?2 = 'WAITING' and b.status = ?2) or " +
            "        (?2 = 'REJECTED' and b.status = ?2) " +
            "       )" +
            " order by b.start desc")
    List<Booking> findAllByOwnerId(Long booker, String state, LocalDateTime localDateTime, Pageable page);


    @Query("select b from Booking b " +
            "where b.itemId = ?1 " +
            "and b.start < ?2 " +
            "and b.status <> 'REJECTED' " +
            "ORDER BY b.start DESC")
    List<Booking> findLastBooking(Long itemId, LocalDateTime ldt);

    @Query("select b from Booking b " +
            "where b.itemId = ?1 " +
            "and b.start > ?2 " +
            "and b.status <> 'REJECTED' " +
            "ORDER BY b.start ASC")
    List<Booking> findNextBooking(Long itemId, LocalDateTime ldt);

    List<Booking> findAllByBookerIdAndItemIdAndStatusAndEndBefore(Long userId, Long itemId,
                                                                  Status status, LocalDateTime ldt);

    List<Booking> findAllByItemIdInAndStatusNotOrderByStartAsc(List<Long> itemIdList,
                                                               Status status1, Pageable page);


}
