package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface RequestService {

    ItemRequest addRequest(ItemRequest request, Long userId);

    List<ItemRequestDto> getOwnRequest(Long userId);

    List<ItemRequestDto> getOtherRequest(Long userId, Integer from, Integer size);

    ItemRequestDto getRequest(Long userId, Long requestId);

}
