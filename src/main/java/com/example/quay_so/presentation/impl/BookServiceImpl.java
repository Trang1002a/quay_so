package com.example.quay_so.presentation.impl;

import com.example.quay_so.model.entity.*;
import com.example.quay_so.model.repository.*;
import com.example.quay_so.model.request.books.*;
import com.example.quay_so.exception.ErrorCode;
import com.example.quay_so.exception.ProjectException;
import com.example.quay_so.model.dto.UserInfoDto;
import com.example.quay_so.model.entity.*;
import com.example.quay_so.model.repository.*;
import com.example.quay_so.model.request.books.*;
import com.example.quay_so.model.request.rate.CommentPublicResponse;
import com.example.quay_so.model.request.rate.CommentResponse;
import com.example.quay_so.model.response.TotalResponse;
import com.example.quay_so.presentation.service.BookService;
import com.example.quay_so.utils.OrdersStatus;
import com.example.quay_so.utils.StatusType;
import com.example.quay_so.utils.UserInfoService;
import com.example.quay_so.utils.mapper.BookMapper;
import com.example.quay_so.utils.mapper.CommentMapper;
import com.example.quay_so.utils.mapper.VoteMapper;
import com.example.quay_so.utils.validate.CustomerType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    @Resource
    BookRepository bookRepository;

    @Resource
    CategoryBookRepository categoryBookRepository;

    @Resource
    CategoryRepository categoryRepository;

    @Resource
    VoteRepository voteRepository;

    @Resource
    CommentRepository commentRepository;

    @Resource
    AccountRepository accountRepository;

    @Resource
    OrderDetailRepository orderDetailRepository;

    @Override
    public GetAllBookResponse getAll(GetAllBookRequest request) {
        UserInfoDto userInfoDto = UserInfoService.getUserInfo(CustomerType.ADMIN.name());
        GetAllBookResponse response = new GetAllBookResponse();
        List<String> bookIds = null;
        if (!CollectionUtils.isEmpty(request.getCategoriesId())) {
            bookIds = categoryRepository.findListBookIds(StatusType.ACTIVE.name(), request.getCategoriesId());
            if (CollectionUtils.isEmpty(bookIds)) {
                return response;
            }
        }
        Sort sort = Sort.by(Sort.Direction.ASC, "updatedAt");
        Pageable pageable;
        try {
            pageable = PageRequest.of(request.getPageRequest().getPage(), request.getPageRequest().getSize(), sort);
        } catch (Exception e) {
            pageable = PageRequest.of(1, 10, sort);
        }
        Page<BooksEntity> list = bookRepository.findBooksWithRequest(bookIds, request.getName(), request.getAuthor(), request.getStatus(), pageable);
        if (list.isEmpty()) {
            return response;
        }
        TotalResponse totalResponse = new TotalResponse();
        totalResponse.setTotalPages(String.valueOf(list.getTotalPages()));
        totalResponse.setTotalElements(String.valueOf(list.getTotalElements()));
        response.setTotalResponse(totalResponse);
        response.setBookResponseList(list.stream().map(a -> BookMapper.mapBookEntityToBookResponse(a, getListCategoriesIdByBookId(a.getId()), getVoteBook(a.getId()))).collect(Collectors.toList()));
        return response;
    }

    @Override
    public BookResponse findById(String id) {
        UserInfoDto userInfoDto = UserInfoService.getUserInfo(CustomerType.ADMIN.name());
        Optional<BooksEntity> booksEntity = bookRepository.findById(id);
        return getBookResponse(booksEntity);
    }

    @Override
    public GetAllBookResponse getAllBookActive(GetAllBookRequest request) {
        GetAllBookResponse response = new GetAllBookResponse();
        List<String> bookIds = null;
        if (!CollectionUtils.isEmpty(request.getCategoriesId())) {
            bookIds = categoryRepository.findListBookIds(StatusType.ACTIVE.name(), request.getCategoriesId());
            if (CollectionUtils.isEmpty(bookIds)) {
                return response;
            }
        }
        Sort sort = Sort.by(Sort.Direction.ASC, "updatedAt");
        Pageable pageable;
        try {
            pageable = PageRequest.of(request.getPageRequest().getPage(), request.getPageRequest().getSize(), sort);
        } catch (Exception e) {
            pageable = PageRequest.of(1, 10, sort);
        }
        Page<BooksEntity> list = bookRepository.findBooksActiveWithRequest(bookIds, request.getName(), request.getAuthor(), StatusType.ACTIVE.name(), pageable);
        if (list.isEmpty()) {
            return response;
        }
        TotalResponse totalResponse = new TotalResponse();
        totalResponse.setTotalPages(String.valueOf(list.getTotalPages()));
        totalResponse.setTotalElements(String.valueOf(list.getTotalElements()));
        response.setTotalResponse(totalResponse);
        response.setBookResponseList(list.stream().map(a -> BookMapper.mapBookEntityToBookResponse(a, getListCategoriesIdByBookId(a.getId()), getVoteBook(a.getId()))).collect(Collectors.toList()));
        return response;
    }

    @Override
    public BookResponse findByIdPublic(String id) {
        Optional<BooksEntity> booksEntity = bookRepository.findByIdAndStatus(id, StatusType.ACTIVE.name());
        return getBookResponse(booksEntity);
    }

    private BookResponse getBookResponse(Optional<BooksEntity> booksEntity) {
        if (!booksEntity.isPresent()) {
            throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        List<CategoryBookEntity> list = categoryBookRepository.findAllByBookIdAndStatus(booksEntity.get().getId(), StatusType.ACTIVE.name());
        return BookMapper.mapBookEntityToBookResponse(booksEntity.get(), list.stream().map(CategoryBookEntity::getBookId).collect(Collectors.toList()), getVoteBook(booksEntity.get().getId()));
    }

    @Override
    public VotesByIdResponse votesByBookId(String bookId) {
        VotesByIdResponse response = new VotesByIdResponse();
        Optional<BooksEntity> booksEntity = bookRepository.findByIdAndStatus(bookId, StatusType.ACTIVE.name());
        if (!booksEntity.isPresent()) {
            throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        List<VotesEntity> list = voteRepository.findAllByBookIdAndStatusOrderByCreatedAtAsc(bookId, StatusType.ACTIVE.name());
        if (list.isEmpty()) {
            return response;
        }
        Map<String, String> mapAccounts = accountRepository.findAllById(list.stream().map(VotesEntity::getAccountId).collect(Collectors.toList()))
                .stream().collect(Collectors.toMap(AccountsEntity::getId, AccountsEntity::getUserName));
        response.setVoteResponses(list.stream().map(e -> VoteMapper.mapVoteEntityToVoteResponses(e, mapAccounts.get(e.getAccountId()))).collect(Collectors.toList()));
        return response;
    }

    @Override
    public CommentsByIdResponse commentsByBookId(String bookId) {
        CommentsByIdResponse response = new CommentsByIdResponse();
        Optional<BooksEntity> booksEntity = bookRepository.findByIdAndStatus(bookId, StatusType.ACTIVE.name());
        if (!booksEntity.isPresent()) {
            throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        List<CommentsEntity> list = commentRepository.findAllByBookIdAndStatusOrderByCreatedAtAsc(bookId, StatusType.ACTIVE.name());
        if (list.isEmpty()) {
            return response;
        }
        Map<String, String> mapAccounts = accountRepository.findAllById(list.stream().map(CommentsEntity::getAccountId).collect(Collectors.toList()))
                .stream().collect(Collectors.toMap(AccountsEntity::getId, AccountsEntity::getUserName));
        Map<String, List<CommentResponse>> map = list.stream()
                .filter(commentsEntity -> StringUtils.isNotBlank(commentsEntity.getParentId()))
                .collect(Collectors.groupingBy(CommentsEntity::getParentId,
                        Collectors.mapping(e -> CommentMapper.mapCommentEntityToCommentResponse(e, mapAccounts.get(e.getAccountId())), Collectors.toList())));

        List<CommentPublicResponse> commentPublicResponses = list.stream()
                .filter(commentsEntity -> StringUtils.isBlank(commentsEntity.getParentId()))
                .map(commentsEntity -> {
                    CommentPublicResponse commentPublicResponse = new CommentPublicResponse();
                    commentPublicResponse.setId(commentsEntity.getId());
                    commentPublicResponse.setStatus(commentsEntity.getStatus());
                    commentPublicResponse.setAccountName(mapAccounts.get(commentsEntity.getAccountId()));
                    commentPublicResponse.setCreatedAt(commentsEntity.getCreatedAt().toString());
                    commentPublicResponse.setValue(commentsEntity.getValue());
                    commentPublicResponse.setChildren(map.get(commentsEntity.getId()));
                    return commentPublicResponse;
                }).collect(Collectors.toList());
        response.setCommentPublicResponses(commentPublicResponses);
        return response;
    }

    @Override
    public List<BookResponse> getAllPopular() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        calendar.add(Calendar.MONTH, -1);
        Date oneMonthAgo = calendar.getTime();
        List<OrderDetailsEntity> entities = orderDetailRepository.findAllByStatusInAndCreatedAtAfter(OrdersStatus.getAllStatus(), oneMonthAgo);
        return getBookResponses(entities);
    }

    @Override
    public List<BookResponse> getAllBestSelling() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        calendar.add(Calendar.MONTH, -1);
        Date oneMonthAgo = calendar.getTime();
        List<OrderDetailsEntity> entities = orderDetailRepository.findAllByStatusInAndCreatedAtAfter(Collections.singletonList(OrdersStatus.DELIVERED.name()), oneMonthAgo);
        return getBookResponses(entities);
    }

    @Override
    public TotalBookResponse getTotalBook() {
        long bookAll = bookRepository.countAllBook();
        return new TotalBookResponse(bookAll);
    }

    private List<BookResponse> getBookResponses(List<OrderDetailsEntity> entities) {
        List<String> bookIds = entities.stream()
                .collect(Collectors.groupingBy(OrderDetailsEntity::getBookId, Collectors.summingInt(OrderDetailsEntity::getQuantity)))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(4)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        List<BooksEntity> booksEntities = bookRepository.findAllByIdInAndStatus(bookIds, StatusType.ACTIVE.name());
        return booksEntities.stream().map(a -> BookMapper.mapBookEntityToBookResponse(a, getListCategoriesIdByBookId(a.getId()), getVoteBook(a.getId()))).collect(Collectors.toList());
    }


    private List<String> getListCategoriesIdByBookId(String bookId) {
        return categoryBookRepository.findAllByBookIdAndStatus(bookId, StatusType.ACTIVE.name())
                .stream().map(CategoryBookEntity::getCategoryId).collect(Collectors.toList());
    }

    public double getVoteBook(String bookId) {
        List<VotesEntity> list = voteRepository.findAllByBookIdAndStatus(bookId, StatusType.ACTIVE.name());
        if (CollectionUtils.isEmpty(list)) {
            return 0;
        }
        return list.stream().mapToDouble(VotesEntity::getValue)
                .average()
                .orElse(0);
    }

}
