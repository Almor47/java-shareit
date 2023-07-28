package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequest;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;
    private static final String HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> addRequest(@RequestBody @Valid ItemRequest request, @RequestHeader(HEADER) long userId) {
        log.info("Creating request with userId={}", userId);
        return itemRequestClient.addRequest(request, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getOwnRequest(@RequestHeader(HEADER) long userId) {
        log.info("Get own request with userId={}", userId);
        return itemRequestClient.getOwnRequest(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getOtherRequest(@RequestHeader(HEADER) long userId,
                                                  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                  @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get other request with userId={}, from={}, size={}", userId, from, size);
        return itemRequestClient.getOtherRequest(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequest(@RequestHeader(HEADER) long userId,
                                             @PathVariable long requestId) {
        log.info("Get request by requestId={}, userId={}", requestId, userId);
        return itemRequestClient.getRequest(userId, requestId);
    }

}