package com.unir.operator.service;

import com.unir.operator.controller.model.OperatorRequest;
import com.unir.operator.data.OrderJpaRepository;
import com.unir.operator.data.model.Order;
import com.unir.operator.facade.BookFacade;
import com.unir.operator.facade.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OperatorServiceImpl implements OperatorService {
    @Autowired
    private BookFacade bookFacade;
    @Autowired
    private OrderJpaRepository repository;
    @Override
    public Order createOrder(OperatorRequest request) {
        List<com.unir.operator.data.model.Book> books = request.getBooks().stream()
                .map(bookRequest -> {
                    Book bookFromFacade = bookFacade.getBook(bookRequest.getId());
                    if (bookFromFacade == null || !Boolean.TRUE.equals(bookFromFacade.getVisible())) {
                        throw new RuntimeException("El libro con ID " + bookRequest.getId() + " no está disponible o no es visible");
                    }
                    return com.unir.operator.data.model.Book.builder()
                            .id(bookFromFacade.getId())
                            .unit(bookRequest.getQuantity())
                            .build();
                })
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toList());

        Order order = Order.builder()
                .books(books)
                .clientID(request.getClientID())
                .totalAmount(request.getTotalAmount())
                .build();

        return repository.save(order);
    }
    @Override
    public Order getOrder(String id) {
        return repository.findById(Long.valueOf(id)).orElse(null);
    }

    @Override
    public List<Order> getOrders() {
        List<Order> orders = repository.findAll();
        return orders.isEmpty() ? null : orders;
    }
}
