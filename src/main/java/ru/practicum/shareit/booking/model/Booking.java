package ru.practicum.shareit.booking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.practicum.shareit.booking.enumerated.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "bookings", schema = "public")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Booking {

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    Status status;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    @Column(name = "start_date")
    private LocalDateTime start;
    @Column(name = "end_date")
    private LocalDateTime end;
    @Column(name = "item_id")
    private Long itemId;
    @ManyToOne(targetEntity = Item.class)
    @JoinColumn(name = "item_id", insertable = false, updatable = false)
    @JsonIgnore
    private Item item;
    @Column(name = "booker_id")
    private Long bookerId;
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "booker_id", insertable = false, updatable = false)
    @JsonIgnore
    private User user;

}
