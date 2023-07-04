package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.request.exception.BadRequestItemRequestException;
import ru.practicum.shareit.request.exception.NotFoundItemRequestException;
import ru.practicum.shareit.request.exception.PaginationException;
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
        return RequestMapper.ListItemRequestToItemRequestDto(itemRequests, items);
    }

    @Override
    public List<ItemRequestDto> getOtherRequest(Long userId, Integer from, Integer size) {
        checkPagination(from,size);
        Sort rule = Sort.by(Sort.Direction.DESC, "created");
        Pageable page = PageRequest.of(from / size, size, rule);
        List<ItemRequest> itemRequests = requestRepository.findAllByIdNot(userId, page);
        List<Long> itemRequestId = getItemRequestId(itemRequests);
        List<Item> items = itemRepository.findAllByRequestIdIn(itemRequestId);
        return RequestMapper.ListItemRequestToItemRequestDto(itemRequests, items);

    }

    @Override
    public ItemRequestDto getRequest(Long userId, Long requestId) {
        userService.getUserById(userId);

        ItemRequest itemRequest = requestRepository.findById(requestId).orElseThrow(() ->
                new NotFoundItemRequestException("Запрос с requestId " + requestId + " не найден"));
        List<Item> items = itemRepository.findAllByRequestIdIn(List.of(itemRequest.getId()));
        return RequestMapper.itemRequestToItemRequestDto(itemRequest, items);
    }

    public void checkPagination(Integer from, Integer size) {
        if (from < 0) {
            throw new PaginationException("Индекс первого элемента не может быть отрицательным");
        }
        if (size < 0) {
            throw new PaginationException("Количество элементов для отображения " +
                    "не может быть отрицательным");
        }
        if (from == 0 && size == 0) {
            throw new PaginationException("Индекс первого элемента и количество элементов для отображения " +
                    "не могут быть равны 0");
        }
    }

    private List<Long> getItemRequestId(List<ItemRequest> itemRequests) {
        List<Long> itemRequestId = new ArrayList<>();
        for (ItemRequest one : itemRequests) {
            itemRequestId.add(one.getId());
        }
        return itemRequestId;
    }


}
