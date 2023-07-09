package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.pagination.Pagination;
import ru.practicum.shareit.request.exception.BadRequestItemRequestException;
import ru.practicum.shareit.request.exception.NotFoundItemRequestException;
import ru.practicum.shareit.request.exception.PaginationException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.request.service.RequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private Pagination pagination;

    @InjectMocks
    private RequestServiceImpl requestService;

    @Test
    void addItemRequest_whenDescriptionEmpty_thenReturnBadRequestItemRequestException() {
        long userId = 0L;
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(null);
        User user = new User();
        user.setEmail("valid@yandex.ru");

        when(userService.getUserById(anyLong()))
                .thenReturn(user);

        assertThrows(BadRequestItemRequestException.class, () ->
                requestService.addRequest(itemRequest, userId));

        verify(requestRepository, never()).save(itemRequest);
    }

    @Test
    void addItemRequest_whenValid_thenSaveItemRequest() {
        long userId = 0L;
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription("Not null");
        User user = new User();
        user.setEmail("valid@yandex.ru");

        when(userService.getUserById(anyLong()))
                .thenReturn(user);

        when(requestRepository.save(any(ItemRequest.class)))
                .thenReturn(itemRequest);

        ItemRequest actualItemRequest = requestService.addRequest(itemRequest, userId);
        assertEquals(itemRequest, actualItemRequest);
    }

    @Test
    void getRequest_whenNotFound_thenThrowNotFountItemRequestException() {
        long userId = 0L;
        long requestId = 0L;

        when(requestRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundItemRequestException.class, () ->
                requestService.getRequest(userId, requestId));
    }

    @Test
    void getOtherRequest_whenBellowFromZero_thenThrowPaginationException() {
        long userId = 0L;
        int from = -2;
        int size = 32;

        doThrow(PaginationException.class).when(pagination).checkPagination(from,size);

        assertThrows(PaginationException.class, () ->
                requestService.getOtherRequest(userId, from, size));


    }

    @Test
    void getOtherRequest_whenBellowSizeZero_thenThrowPaginationException() {
        long userId = 0L;
        int from = 0;
        int size = -32;

        doThrow(PaginationException.class).when(pagination).checkPagination(from,size);

        assertThrows(PaginationException.class, () ->
                requestService.getOtherRequest(userId, from, size));


    }

    @Test
    void getOtherRequest_whenSizeAndFromZero_thenThrowPaginationException() {
        long userId = 0L;
        int from = 0;
        int size = 0;

        doThrow(PaginationException.class).when(pagination).checkPagination(from,size);

        assertThrows(PaginationException.class, () ->
                requestService.getOtherRequest(userId, from, size));


    }


}
