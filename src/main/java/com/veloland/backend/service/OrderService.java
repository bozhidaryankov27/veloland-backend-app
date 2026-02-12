package com.veloland.backend.service;

import com.veloland.backend.dto.OrderDto;
import com.veloland.backend.dto.OrderItemDto;
import com.veloland.backend.model.Order;
import com.veloland.backend.model.OrderItem;
import com.veloland.backend.model.OrderStatus;
import com.veloland.backend.model.Product;
import com.veloland.backend.repository.OrderRepository;
import com.veloland.backend.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderService(ProductRepository productRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public void validateData(OrderDto request) {
        if (request.getCustomerName() == null || request.getCustomerName().isBlank()) {
            throw new IllegalArgumentException("Customer name is required");
        }
        if (request.getAddress() == null || request.getAddress().isBlank()) {
            throw new IllegalArgumentException("Address is required");
        }
        if (request.getPaymentMethod() == null || request.getPaymentMethod().isBlank()) {
            throw new IllegalArgumentException("Payment method is required");
        }
        if (request.getContact() == null || request.getContact().isBlank()) {
            throw new IllegalArgumentException("Contact is required");
        }
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order items are required");
        }


        for (OrderItemDto item : request.getItems()) {
            if(item.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than 0!");
            }
        }

        for(OrderItemDto item : request.getItems()) {
            if (!productRepository.existsById(item.getProductId())) {
                throw new IllegalArgumentException(
                        "Product with id " + item.getProductId() + " does not exist"
                );
            }
        }
    }

    @Transactional
    public Order createOrder(OrderDto request) {
        validateData(request);
        Order order = new Order();

        order.setCustomerName(request.getCustomerName());
        order.setAddress(request.getAddress());
        order.setContact(request.getContact());
        order.setPaymentMethod(request.getPaymentMethod());

        order.setStatus(OrderStatus.NEW);
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalPrice(BigDecimal.valueOf(0));

        List <OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (OrderItemDto item : request.getItems()) {
            Long productId = item.getProductId();
            int quantity = item.getQuantity();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Product not found!"));


            if(product.getStock() < quantity) {
                throw new IllegalArgumentException("Product is not in enough stock!");
            } else {
                int remainingStock = product.getStock() - quantity;
                product.setStock(remainingStock);
            }

            OrderItem orderItem = new OrderItem();

            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            orderItem.setUnitPrice(product.getPrice());
            orderItem.setOrder(order);

            orderItems.add(orderItem);
            BigDecimal totalPriceItem = product.getPrice().multiply(BigDecimal.valueOf(quantity));
            totalPrice = totalPrice.add(totalPriceItem);

        }
        order.setItems(orderItems);
        order.setTotalPrice(totalPrice);
        return orderRepository.save(order);
    }
}
