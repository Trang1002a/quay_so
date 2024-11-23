package com.example.quay_so.presentation.impl;

import com.example.quay_so.exception.ErrorCode;
import com.example.quay_so.exception.ProjectException;
import com.example.quay_so.model.dto.UserInfoDto;
import com.example.quay_so.model.entity.BooksEntity;
import com.example.quay_so.model.entity.OrderDetailsEntity;
import com.example.quay_so.model.entity.OrdersEntity;
import com.example.quay_so.model.repository.BookRepository;
import com.example.quay_so.model.repository.OrderDetailRepository;
import com.example.quay_so.model.repository.OrderRepository;
import com.example.quay_so.model.request.orders.*;
import com.example.quay_so.model.response.ResStatus;
import com.example.quay_so.model.response.TotalResponse;
import com.example.quay_so.model.response.order.CreateOrderResponse;
import com.example.quay_so.presentation.service.OrderService;
import com.example.quay_so.utils.Contants;
import com.example.quay_so.utils.OrdersStatus;
import com.example.quay_so.utils.UserInfoService;
import com.example.quay_so.utils.mapper.OrderMapper;
import com.example.quay_so.utils.validate.CustomerType;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    OrderRepository orderRepository;

    @Resource
    OrderDetailRepository orderDetailRepository;

    @Resource
    BookRepository bookRepository;

    @Override
    public GetAllOrderResponse getAll(GetAllOrderRequest request) {
        UserInfoDto userInfoDto = UserInfoService.getUserInfo(CustomerType.ADMIN.name());
        GetAllOrderResponse response = new GetAllOrderResponse();
        Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");
        Pageable pageable;
        try {
            pageable = PageRequest.of(request.getPageRequest().getPage(), request.getPageRequest().getSize(), sort);
        } catch (Exception e) {
            pageable = PageRequest.of(1, 10, sort);
        }
        Page<OrdersEntity> list = orderRepository.findOrdersWithRequest(request.getAccountName(), request.getStatus(), pageable);
        if (list.isEmpty()) {
            return response;
        }
        TotalResponse totalResponse = new TotalResponse();
        totalResponse.setTotalPages(String.valueOf(list.getTotalPages()));
        totalResponse.setTotalElements(String.valueOf(list.getTotalElements()));
        response.setTotalResponse(totalResponse);
        response.setOrderResponses(list.stream().map(OrderMapper::mapOrderEntityToOrderResponse).collect(Collectors.toList()));
        return response;
    }

    @Override
    public OrderDetailsResponse findById(String id) {
        OrderDetailsResponse response = new OrderDetailsResponse();
        UserInfoDto userInfoDto = UserInfoService.getUserInfo(CustomerType.ADMIN.name());
        if (StringUtils.isBlank(id)) {
            throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR, "Mã giao dịch không chính xác");
        }
        Optional<OrdersEntity> ordersEntity = orderRepository.findById(id);
        if (!ordersEntity.isPresent()) {
            throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR, "Mã giao dịch không chính xác");
        }
        List<OrderDetailsEntity> orderDetailsEntityList = orderDetailRepository.findAllByOrderId(id);
        List<BooksEntity> booksEntityList = bookRepository.findAll();
        Map<String,String> bookImage =  booksEntityList.stream().collect(Collectors.toMap(BooksEntity::getId, BooksEntity::getImage));
        response.setId(id);
        response.setAccountName(ordersEntity.get().getAccountName());
        response.setTotalPrice(ordersEntity.get().getTotalPrice().toString());
        response.setQuantity(ordersEntity.get().getQuantity().toString());
        response.setStatus(ordersEntity.get().getStatus());
        response.setPhoneNumber(ordersEntity.get().getPhoneNumber());
        response.setAddress(ordersEntity.get().getAddress());
        response.setPaymentMethods(ordersEntity.get().getPaymentMethods());
        response.setNote(ordersEntity.get().getNote());
        response.setDiscount(ObjectUtils.isEmpty(ordersEntity.get().getDiscount()) ? null : ordersEntity.get().getDiscount().toString());
        response.setCreatedAt(ordersEntity.get().getCreatedAt().toString());
        response.setUpdatedAt(ObjectUtils.isEmpty(ordersEntity.get().getUpdatedAt()) ? null : ordersEntity.get().getUpdatedAt().toString());
        response.setDetailResponseList(orderDetailsEntityList.stream().map(entity -> OrderMapper.mapToOrderDetailResponse(entity,bookImage)).collect(Collectors.toList()));
        return response;
    }

    @Override
    public ResStatus updateStatus(UpdateStatusOrderRequest request) {
        UserInfoDto userInfoDto = UserInfoService.getUserInfo(CustomerType.ADMIN.name());
        if (StringUtils.isBlank(request.getId()) || !OrdersStatus.contains(request.getStatus())) {
            throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR, "Request không hợp lệ");
        }
        Optional<OrdersEntity> entity = orderRepository.findById(request.getId());
        if (!entity.isPresent()) {
            throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR, "Request không hợp lệ");
        }
        OrdersEntity ordersEntity = entity.get();
        List<OrderDetailsEntity> orderDetailsEntityList = new ArrayList<>();
        List<BooksEntity> booksEntitiesNew = new ArrayList<>();
        OrdersStatus status = OrdersStatus.valueOf(request.getStatus());
        switch (status) {
            case CANCEL:
                orderDetailsEntityList = getDetailsEntityList(request, userInfoDto);
                List<BooksEntity> booksEntities = bookRepository.findAllById(orderDetailsEntityList.stream().map(OrderDetailsEntity::getBookId).collect(Collectors.toList()));
                Map<String, Integer> mapBookDetails = orderDetailsEntityList.stream().collect(Collectors.toMap(OrderDetailsEntity::getBookId, OrderDetailsEntity::getQuantity));
                booksEntitiesNew = booksEntities.stream().peek(book -> {
                            if (!ObjectUtils.isEmpty(mapBookDetails.get(book.getId()))) {
                                book.setInventory(book.getInventory() + mapBookDetails.get(book.getId()));
                            }
                        }
                ).collect(Collectors.toList());
                break;
            case DELIVERING:
                orderDetailsEntityList = getDetailsEntityList(request, userInfoDto);
                break;
            case DELIVERED:
                orderDetailsEntityList = getDetailsEntityList(request, userInfoDto);
                break;
            default:
                throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR, "Request không hợp lệ");
        }
        ordersEntity.setStatus(request.getStatus());
        bookRepository.saveAll(booksEntitiesNew);
        orderRepository.save(ordersEntity);
        orderDetailRepository.saveAll(orderDetailsEntityList);
        return new ResStatus(Contants.SUCCESS);
    }

    @Override
    public GetAllOrderResponse getAllCustomerOrder() {
        UserInfoDto userInfoDto = UserInfoService.getUserInfo(CustomerType.CUSTOMER.name());
        GetAllOrderResponse response = new GetAllOrderResponse();
        List<OrdersEntity> list = orderRepository.findAllByAccountIdOrderByCreatedAtDesc(userInfoDto.getUserId());
        if (list.isEmpty()) {
            return response;
        }
        response.setOrderResponses(list.stream().map(OrderMapper::mapOrderEntityToOrderResponse).collect(Collectors.toList()));
        return response;
    }

    @Override
    public OrderDetailsResponse getCustomerOrderDetails(String id) {
        OrderDetailsResponse response = new OrderDetailsResponse();
        UserInfoDto userInfoDto = UserInfoService.getUserInfo(CustomerType.CUSTOMER.name());
        if (StringUtils.isBlank(id)) {
            throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR, "Mã giao dịch không chính xác");
        }
        Optional<OrdersEntity> ordersEntity = orderRepository.findById(id);
        if (!ordersEntity.isPresent()) {
            throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR, "Mã giao dịch không chính xác");
        }
        List<OrderDetailsEntity> orderDetailsEntityList = orderDetailRepository.findAllByOrderId(id);
        List<BooksEntity> booksEntityList = bookRepository.findAll();
        Map<String,String> bookImage =  booksEntityList.stream().collect(Collectors.toMap(BooksEntity::getId, BooksEntity::getImage));

        response.setId(id);
        response.setAccountName(ordersEntity.get().getAccountName());
        response.setTotalPrice(ordersEntity.get().getTotalPrice().toString());
        response.setQuantity(ordersEntity.get().getQuantity().toString());
        response.setStatus(ordersEntity.get().getStatus());
        response.setPhoneNumber(ordersEntity.get().getPhoneNumber());
        response.setAddress(ordersEntity.get().getAddress());
        response.setPaymentMethods(ordersEntity.get().getPaymentMethods());
        response.setNote(ordersEntity.get().getNote());
        response.setDiscount(ObjectUtils.isNotEmpty(ordersEntity.get().getDiscount()) ? ordersEntity.get().getDiscount().toString() : "");
        response.setCreatedAt(ordersEntity.get().getCreatedAt().toString());
        response.setUpdatedAt(ObjectUtils.isNotEmpty(ordersEntity.get().getUpdatedAt()) ? ordersEntity.get().getUpdatedAt().toString() : "");
        response.setDetailResponseList(orderDetailsEntityList.stream().map(entity -> OrderMapper.mapToOrderDetailResponse(entity,bookImage)).collect(Collectors.toList()));
        return response;
    }

    @Override
    public CreateOrderResponse createOrder(CreateOrderRequest createOrderRequest) {
        CreateOrderResponse createOrderResponse = new CreateOrderResponse();
        try {
            UserInfoDto userInfoDto = UserInfoService.getUserInfo(CustomerType.CUSTOMER.name());
            String orderId = UUID.randomUUID().toString();
            OrdersEntity ordersEntity = new OrdersEntity();
            ordersEntity.setId(orderId);
            ordersEntity.setStatus(OrdersStatus.WAITING_CONFIRM.name());
            ordersEntity.setAccountName(userInfoDto.getUserName());
            ordersEntity.setAccountId(userInfoDto.getUserId());
            ordersEntity.setPhoneNumber(createOrderRequest.getPhoneNumber());
            ordersEntity.setAddress(createOrderRequest.getAddress());
            ordersEntity.setPaymentMethods(createOrderRequest.getPaymentMethods());
            ordersEntity.setNote(createOrderRequest.getNote());
            ordersEntity.setDiscountCode(createOrderRequest.getDiscount());
            ordersEntity.setCreatedAt(Calendar.getInstance().getTime());
            ordersEntity.setCreatedBy(userInfoDto.getUserName());
            ordersEntity.setQuantity(0);
            ordersEntity.setTotalPrice(Float.valueOf(0));
            List<BooksEntity> booksEntityList = bookRepository.findAll();
            Map<String,BooksEntity> booksEntityMap = new HashMap<>();
            booksEntityList.stream().forEach(booksEntity -> booksEntityMap.put(booksEntity.getId(),booksEntity));
            createOrderRequest.getBookOrders().stream().forEach(bookOrder -> createOrderDetails(booksEntityMap,bookOrder,ordersEntity,orderId,userInfoDto.getUserName()));

            OrdersEntity save = orderRepository.save(ordersEntity);

            createOrderResponse.setOrderStatus("SUCCESS");
            createOrderResponse.setTotalPaid(save.getTotalPrice().toString());
            return createOrderResponse;
        }catch (Exception e){
            createOrderResponse.setOrderStatus("FAILED");
            return createOrderResponse;
        }
    }

    @Override
    public OrderTotalResponse total() {
        OrderTotalResponse response = new OrderTotalResponse();
        response.setTotalBalance((double) 0);
        response.setTotalOrder(0);
        response.setTotalBook(0);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        Date oneMonthAgo = calendar.getTime();
        List<OrdersEntity> list = orderRepository.findOrdersOneMonthAgoWithSuccessStatus(OrdersStatus.DELIVERED.name(), oneMonthAgo);
        if (list.size() == 0) {
            return response;
        }
        double total = list.stream().mapToDouble(OrdersEntity::getTotalPrice).sum();
        int totalBook = list.stream().mapToInt(OrdersEntity::getQuantity).sum();
        response.setTotalBalance(total);
        response.setTotalOrder(list.size());
        response.setTotalBook(totalBook);
        return response;
    }

    private List<OrderDetailsEntity> getDetailsEntityList(UpdateStatusOrderRequest request, UserInfoDto userInfoDto) {
        return orderDetailRepository.findAllByOrderId(request.getId())
                .stream().peek(e -> {
                    e.setStatus(request.getStatus());
                    e.setUpdatedAt(new Date(System.currentTimeMillis()));
                    e.setUpdatedBy(userInfoDto.getUserName());
                }).collect(Collectors.toList());
    }
    private void createOrderDetails(Map<String,BooksEntity> booksEntityMap,BookOrder bookOrder,OrdersEntity ordersEntity,String orderId,String userName){
        Float price = booksEntityMap.get(bookOrder.getBookId()).getPrice();
        Float totalPriceB = price*bookOrder.getQuantity();

        Integer totalQuantity=0;
        Float totalPrice = Float.valueOf(0);
        totalQuantity += bookOrder.getQuantity();
        totalPrice+=totalPriceB;

        OrderDetailsEntity orderDetailsEntity = new OrderDetailsEntity();
        orderDetailsEntity.setId(UUID.randomUUID().toString());
        orderDetailsEntity.setStatus(OrdersStatus.WAITING_CONFIRM.name());
        orderDetailsEntity.setOrderId(orderId);
        orderDetailsEntity.setBookId(bookOrder.getBookId());
        orderDetailsEntity.setBookName(booksEntityMap.get(bookOrder.getBookId()).getName());
        orderDetailsEntity.setPrice(totalPrice);
        orderDetailsEntity.setQuantity(bookOrder.getQuantity());
        orderDetailsEntity.setCreatedAt(Calendar.getInstance().getTime());
        orderDetailsEntity.setCreatedBy(userName);

        orderDetailRepository.save(orderDetailsEntity);

        ordersEntity.setQuantity(ordersEntity.getQuantity()+totalQuantity);
        ordersEntity.setTotalPrice(ordersEntity.getTotalPrice()+totalPrice);
    }
}
