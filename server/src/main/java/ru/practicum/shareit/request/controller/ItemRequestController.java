package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.RequestService;

import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private static final String HEADER = "X-Sharer-User-Id";
    private final RequestService requestService;

    @PostMapping
    public ItemRequest addRequest(@RequestBody ItemRequest request, @RequestHeader(HEADER) Long userId) {
        return requestService.addRequest(request,userId);
    }

    @GetMapping
    public List<ItemRequestDto> getOwnRequest(@RequestHeader(HEADER) Long userId) {
        return requestService.getOwnRequest(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getOtherRequest(@RequestHeader(HEADER) Long userId,
                                             @RequestParam(defaultValue = "0") Integer from,
                                             @RequestParam(defaultValue = "32") Integer size) {
        return requestService.getOtherRequest(userId,from,size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequest(@RequestHeader(HEADER) Long userId, @PathVariable Long requestId) {
        return requestService.getRequest(userId,requestId);
    }
}
