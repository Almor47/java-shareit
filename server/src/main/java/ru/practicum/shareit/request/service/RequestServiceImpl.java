package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.pagination.Pagination;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.request.exception.BadRequestItemRequestException;
import ru.practicum.shareit.request.exception.NotFoundItemRequestException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserService userService;
    private final ItemRepository itemRepository;
    private final Pagination pagination;

    @Transactional
    @Override
    public ItemRequest addRequest(ItemRequest request, Long userId) {
        userService.getUserById(userId);
        if (request.getDescription() == null) {
            throw new BadRequestItemRequestException("Запрос не может иметь пустое описание");
        }
        request.setRequestor(userId);
        return requestRepository.save(request);
    }

    @Override
    public List<ItemRequestDto> getOwnRequest(Long userId) {
        userService.getUserById(userId);
        List<ItemRequest> itemRequests = requestRepository.getAllByRequestorOrderByIdDesc(userId);
        List<Long> itemRequestId = getItemRequestId(itemRequests);
        List<Item> items = itemRepository.findAllByRequestIdIn(itemRequestId);
        return RequestMapper.listItemRequestToItemRequestDto(itemRequests, items);
    }

    @Override
    public List<ItemRequestDto> getOtherRequest(Long userId, Integer from, Integer size) {
        pagination.checkPagination(from, size);
        Sort rule = Sort.by(Sort.Direction.DESC, "created");
        Pageable page = PageRequest.of(from / size, size, rule);
        List<ItemRequest> itemRequests = requestRepository.findAllByRequestorNot(userId, page);
        List<Long> itemRequestId = getItemRequestId(itemRequests);
        List<Item> items = itemRepository.findAllByRequestIdIn(itemRequestId);
        return RequestMapper.listItemRequestToItemRequestDto(itemRequests, items);

    }

    @Override
    public ItemRequestDto getRequest(Long userId, Long requestId) {
        userService.getUserById(userId);

        ItemRequest itemRequest = requestRepository.findById(requestId).orElseThrow(() ->
                new NotFoundItemRequestException("Запрос с requestId " + requestId + " не найден"));
        List<Item> items = itemRepository.findAllByRequestIdIn(List.of(itemRequest.getId()));
        return RequestMapper.itemRequestToItemRequestDto(itemRequest, items);
    }


    private List<Long> getItemRequestId(List<ItemRequest> itemRequests) {
        List<Long> itemRequestId = new ArrayList<>();
        for (ItemRequest one : itemRequests) {
            itemRequestId.add(one.getId());
        }
        return itemRequestId;
    }


}
