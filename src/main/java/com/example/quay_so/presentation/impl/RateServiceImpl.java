package com.example.quay_so.presentation.impl;

import com.example.quay_so.model.repository.CommentRepository;
import com.example.quay_so.exception.ErrorCode;
import com.example.quay_so.exception.ProjectException;
import com.example.quay_so.model.dto.UserInfoDto;
import com.example.quay_so.model.entity.AccountsEntity;
import com.example.quay_so.model.entity.BooksEntity;
import com.example.quay_so.model.entity.CommentsEntity;
import com.example.quay_so.model.entity.VotesEntity;
import com.example.quay_so.model.repository.AccountRepository;
import com.example.quay_so.model.repository.BookRepository;
import com.example.quay_so.model.repository.VoteRepository;
import com.example.quay_so.model.request.rate.*;
import com.example.quay_so.model.response.ResStatus;
import com.example.quay_so.model.response.TotalResponse;
import com.example.quay_so.presentation.service.RateService;
import com.example.quay_so.utils.*;
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
public class RateServiceImpl implements RateService {

    @Resource
    BookRepository bookRepository;

    @Resource
    AccountRepository accountRepository;

    @Resource
    VoteRepository voteRepository;

    @Resource
    CommentRepository commentRepository;

    @Override
    public GetAllVotesResponse getAllVotes(GetAllVotesRequest request) {
        UserInfoDto userInfoDto = UserInfoService.getUserInfo(CustomerType.ADMIN.name());
        GetAllVotesResponse response = new GetAllVotesResponse();
        List<BooksEntity> booksEntities = new ArrayList<>();
        List<AccountsEntity> accountsEntities = new ArrayList<>();
        if (StringUtils.isNotBlank(request.getBookName())) {
            booksEntities = bookRepository.findAllByNameContaining(request.getBookName());
            if (CollectionUtils.isEmpty(booksEntities)) {
                return response;
            }
        }

        if (StringUtils.isNotBlank(request.getUserName())) {
            accountsEntities = accountRepository.findAllByUserNameContaining(request.getUserName());
            if (CollectionUtils.isEmpty(accountsEntities)) {
                return response;
            }
        }
        Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");
        Pageable pageable;
        try {
            pageable = PageRequest.of(request.getPageRequest().getPage(), request.getPageRequest().getSize(), sort);
        } catch (Exception e) {
            pageable = PageRequest.of(1, 10, sort);
        }
        List<String> bookIds = booksEntities.stream().map(BooksEntity::getId).collect(Collectors.toList());
        List<String> accountIds = accountsEntities.stream().map(AccountsEntity::getId).collect(Collectors.toList());
        Page<VotesEntity> list = voteRepository.findVotesWithRequest(bookIds, accountIds, request.getStatus(), pageable);
        if (list.isEmpty()) {
            return response;
        }
        TotalResponse totalResponse = new TotalResponse();
        totalResponse.setTotalPages(String.valueOf(list.getTotalPages()));
        totalResponse.setTotalElements(String.valueOf(list.getTotalElements()));
        Map<String, String> mapBooks = bookRepository.findAllById(list.stream().map(VotesEntity::getBookId).collect(Collectors.toList()))
                .stream().collect(Collectors.toMap(BooksEntity::getId, BooksEntity::getName));
        Map<String, String> mapAccounts = accountRepository.findAllById(list.stream().map(VotesEntity::getAccountId).collect(Collectors.toList()))
                .stream().collect(Collectors.toMap(AccountsEntity::getId, AccountsEntity::getUserName));
        response.setTotalResponse(totalResponse);
        response.setVoteResponses(list.stream().map(entity -> VoteMapper.mapVoteEntityToVoteResponses(entity, mapBooks.get(entity.getBookId()), mapAccounts.get(entity.getAccountId()))).collect(Collectors.toList()));
        return response;
    }

    @Override
    public GetAllCommentsResponse getAllComments(GetAllCommentsRequest request) {
        UserInfoDto userInfoDto = UserInfoService.getUserInfo(CustomerType.ADMIN.name());
        GetAllCommentsResponse response = new GetAllCommentsResponse();
        List<BooksEntity> booksEntities = new ArrayList<>();
        List<AccountsEntity> accountsEntities = new ArrayList<>();
        if (StringUtils.isNotBlank(request.getBookName())) {
            booksEntities = bookRepository.findAllByNameContaining(request.getBookName());
            if (CollectionUtils.isEmpty(booksEntities)) {
                return response;
            }
        }

        if (StringUtils.isNotBlank(request.getUserName())) {
            accountsEntities = accountRepository.findAllByUserNameContaining(request.getUserName());
            if (CollectionUtils.isEmpty(accountsEntities)) {
                return response;
            }
        }
        Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");
        Pageable pageable;
        try {
            pageable = PageRequest.of(request.getPageRequest().getPage(), request.getPageRequest().getSize(), sort);
        } catch (Exception e) {
            pageable = PageRequest.of(1, 10, sort);
        }
        List<String> bookIds = booksEntities.stream().map(BooksEntity::getId).collect(Collectors.toList());
        List<String> accountIds = accountsEntities.stream().map(AccountsEntity::getId).collect(Collectors.toList());
        Page<CommentsEntity> list = commentRepository.findCommentsWithRequest(bookIds, accountIds, request.getStatus(), pageable);
        if (list.isEmpty()) {
            return response;
        }
        TotalResponse totalResponse = new TotalResponse();
        totalResponse.setTotalPages(String.valueOf(list.getTotalPages()));
        totalResponse.setTotalElements(String.valueOf(list.getTotalElements()));
        Map<String, String> mapBooks = bookRepository.findAllById(list.stream().map(CommentsEntity::getBookId).collect(Collectors.toList()))
                .stream().collect(Collectors.toMap(BooksEntity::getId, BooksEntity::getName));
        Map<String, String> mapAccounts = accountRepository.findAllById(list.stream().map(CommentsEntity::getAccountId).collect(Collectors.toList()))
                .stream().collect(Collectors.toMap(AccountsEntity::getId, AccountsEntity::getUserName));
        response.setTotalResponse(totalResponse);
        response.setCommentResponses(list.stream().map(entity -> CommentMapper.mapCommentEntityToCommentResponses(entity, mapBooks.get(entity.getBookId()), mapAccounts.get(entity.getAccountId()))).collect(Collectors.toList()));
        return response;
    }

    @Override
    public VoteResponse findVoteById(String id) {
        UserInfoDto userInfoDto = UserInfoService.getUserInfo(CustomerType.ADMIN.name());
        Optional<VotesEntity> votesEntity = voteRepository.findById(id);
        if (!votesEntity.isPresent()) {
            throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR, "Thông tin request không hợp lệ");
        }
        Optional<BooksEntity> booksEntity = bookRepository.findById(votesEntity.get().getBookId());
        if (!booksEntity.isPresent()) {
            throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR, "Thông tin request không hợp lệ");
        }
        Optional<AccountsEntity> accountsEntity = accountRepository.findById(votesEntity.get().getAccountId());
        if (!accountsEntity.isPresent()) {
            throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR, "Thông tin request không hợp lệ");
        }
        return VoteMapper.mapVoteEntityToVoteResponses(votesEntity.get(), booksEntity.get().getName(), accountsEntity.get().getUserName());
    }

    @Override
    public CommentResponse findCommentById(String id) {
        UserInfoDto userInfoDto = UserInfoService.getUserInfo(CustomerType.ADMIN.name());
        Optional<CommentsEntity> commentsEntity = commentRepository.findById(id);
        if (!commentsEntity.isPresent()) {
            throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR, "Thông tin request không hợp lệ");
        }
        Optional<BooksEntity> booksEntity = bookRepository.findById(commentsEntity.get().getBookId());
        if (!booksEntity.isPresent()) {
            throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR, "Thông tin request không hợp lệ");
        }
        Optional<AccountsEntity> accountsEntity = accountRepository.findById(commentsEntity.get().getAccountId());
        if (!accountsEntity.isPresent()) {
            throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR, "Thông tin request không hợp lệ");
        }
        return CommentMapper.mapCommentEntityToCommentResponses(commentsEntity.get(), booksEntity.get().getName(), accountsEntity.get().getUserName());
    }

    @Override
    public ResStatus voteUpdate(VoteUpdateRequest request) {
        UserInfoDto userInfoDto = UserInfoService.getUserInfo(CustomerType.ADMIN.name());
        Optional<VotesEntity> votesEntity = voteRepository.findById(request.getId());
        if (!votesEntity.isPresent()) {
            throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR, "Thông tin request không hợp lệ");
        }
        votesEntity.get().setStatus(StringUtils.equalsIgnoreCase(request.getStatus(), StatusType.ACTIVE.name()) ? StatusType.ACTIVE.name() : StatusType.DEACTIVE.name());
        votesEntity.get().setUpdatedAt(new Date(System.currentTimeMillis()));
        voteRepository.save(votesEntity.get());
        return new ResStatus(Contants.SUCCESS);
    }

    @Override
    public ResStatus commentUpdate(CommentUpdateRequest request) {
        UserInfoDto userInfoDto = UserInfoService.getUserInfo(CustomerType.ADMIN.name());
        Optional<CommentsEntity> commentsEntity = commentRepository.findById(request.getId());
        if (!commentsEntity.isPresent()) {
            throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR, "Thông tin request không hợp lệ");
        }
        commentsEntity.get().setStatus(StringUtils.equalsIgnoreCase(request.getStatus(), StatusType.ACTIVE.name()) ? StatusType.ACTIVE.name() : StatusType.DEACTIVE.name());
        commentsEntity.get().setUpdatedAt(new Date(System.currentTimeMillis()));
        commentRepository.save(commentsEntity.get());
        return new ResStatus(Contants.SUCCESS);
    }

    @Override
    public ResStatus postComments(PostCommentRequest request) {
        UserInfoDto userInfoDto = UserInfoService.getUserInfo(CustomerType.CUSTOMER.name());
        if (StringUtils.isBlank(request.getValue())
                || StringUtils.isBlank(request.getBookId())) {
            throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR, "Thông tin request không hợp lệ");
        }
        Optional<BooksEntity> booksEntity = bookRepository.findById(request.getBookId());
        if (!booksEntity.isPresent()) {
            throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR, "Thông tin request không hợp lệ");
        }
        if (StringUtils.isNotBlank(request.getParentId())) {
            Optional<CommentsEntity> comEntity = commentRepository.findByIdAndStatusAndParentIdIsNull(request.getParentId(), StatusType.ACTIVE.name());
            if (!comEntity.isPresent()) {
                throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR, "Thông tin request không hợp lệ");
            }
        }
        CommentsEntity commentsEntity = new CommentsEntity();
        commentsEntity.setId(UUID.randomUUID().toString());
        commentsEntity.setParentId(StringUtils.isBlank(request.getParentId()) ? null : request.getParentId());
        commentsEntity.setBookId(request.getBookId());
        commentsEntity.setAccountId(userInfoDto.getUserId());
        commentsEntity.setStatus(StatusType.ACTIVE.name());
        commentsEntity.setValue(request.getValue());
        commentsEntity.setCreatedAt(new Date(System.currentTimeMillis()));
        commentRepository.save(commentsEntity);
        return new ResStatus(Contants.SUCCESS);
    }

    @Override
    public ResStatus postVotes(PostVoteRequest request) {
        UserInfoDto userInfoDto = UserInfoService.getUserInfo(CustomerType.CUSTOMER.name());
        if (StringUtils.isBlank(request.getValue())
                || !RegexUtils.isValid(request.getValue(), Contants.REGEX_VOTE)
                || StringUtils.isBlank(request.getBookId())) {
            throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR, "Thông tin request không hợp lệ");
        }
        Optional<BooksEntity> booksEntity = bookRepository.findById(request.getBookId());
        if (!booksEntity.isPresent()) {
            throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR, "Thông tin request không hợp lệ");
        }
        VotesEntity votesEntity = new VotesEntity();
        votesEntity.setId(UUID.randomUUID().toString());
        votesEntity.setBookId(request.getBookId());
        votesEntity.setAccountId(userInfoDto.getUserId());
        votesEntity.setStatus(TransactionStatus.INIT.name());
        votesEntity.setValue(Float.parseFloat(request.getValue()));
        votesEntity.setCreatedAt(new Date(System.currentTimeMillis()));
        voteRepository.save(votesEntity);
        return new ResStatus(Contants.SUCCESS);
    }
}
