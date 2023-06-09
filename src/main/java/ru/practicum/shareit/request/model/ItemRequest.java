package ru.practicum.shareit.request.model;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests", schema = "public")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    Long id;
    @Column(name = "description")
    String description;
    @Column(name = "requestor_id")
    Long requestor;
    @Column(name = "created")
    LocalDateTime created = LocalDateTime.now();

}
