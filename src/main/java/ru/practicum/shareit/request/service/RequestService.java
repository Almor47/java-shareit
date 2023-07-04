package ru.practicum.shareit.request.service;

import org.apache.coyote.Request;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface RequestService {

    ItemRequest addRequest(ItemRequest request, Long userId);
    List<ItemRequestDto> getOwnRequest(Long userId);
    List<ItemRequestDto> getOtherRequest(Long userId, Integer from, Integer size);

    ItemRequestDto getRequest(Long userId, Long requestId);

}
