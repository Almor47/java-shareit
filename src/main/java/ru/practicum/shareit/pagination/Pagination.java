package ru.practicum.shareit.pagination;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.exception.PaginationException;

@Service
public class Pagination {

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

}
