package com.example.quay_so.presentation.impl;

import com.bookstore.quay_so.model.entity.*;
import com.bookstore.quay_so.model.repository.*;
import com.bookstore.quay_so.model.request.transaction.*;
import com.bookstore.quay_so.utils.*;
import com.bookstore.quay_so.utils.validate.*;
import com.example.quay_so.exception.ErrorCode;
import com.example.quay_so.exception.ProjectException;
import com.example.quay_so.jwt.PasswordService;
import com.example.quay_so.model.dto.TransByTypeDto;
import com.example.quay_so.model.dto.TransDetailDto;
import com.example.quay_so.model.dto.UserInfoDto;
import com.example.quay_so.model.entity.*;
import com.example.quay_so.model.repository.*;
import com.example.quay_so.model.request.accounts.AccountRequest;
import com.example.quay_so.model.request.accounts.UserRequest;
import com.example.quay_so.model.request.books.BookRequest;
import com.example.quay_so.model.request.categories.CategoryRequest;
import com.example.quay_so.model.request.transaction.*;
import com.example.quay_so.model.response.ResStatus;
import com.example.quay_so.model.response.TotalResponse;
import com.example.quay_so.model.response.transaction.TransPendingResponse;
import com.example.quay_so.presentation.service.TransactionService;
import com.example.quay_so.utils.*;
import com.example.quay_so.utils.mapper.CategoryBookMapper;
import com.example.quay_so.utils.mapper.RequestMapper;
import com.example.quay_so.utils.validate.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class TransactionServiceImpl implements TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Resource
    UserValidate userValidate;

    @Resource
    CategoryValidate categoryValidate;

    @Resource
    BookValidate bookValidate;

    @Resource
    AccountValidate accountValidate;

    @Resource
    RequestUtils requestUtils;

    @Resource
    RequestRepository requestRepository;

    @Resource
    BookRepository bookRepository;

    @Resource
    CategoryRepository categoryRepository;

    @Resource
    CategoryBookRepository categoryBookRepository;

    @Resource
    AccountRepository accountRepository;

    @Resource
    UserRepository userRepository;

    @Resource
    RoleRepository roleRepository;

    @Override
    public ResStatus createUser(UserRequest userRequest) {
        UserInfoDto userInfoDto = UserInfoService.getUserInfo(CustomerType.ADMIN.name());
        userValidate.validateRequestUser(userRequest);
        requestUtils.saveRequest(userRequest, userInfoDto, FunctionCode.CREATE_USER);
        return new ResStatus(Contants.SUCCESS);
    }

    @Override
    public ResStatus updateUser(UserRequest userRequest) {
        UserInfoDto userInfoDto = UserInfoService.getUserInfo(CustomerType.ADMIN.name());
        userValidate.validateRequestUser(userRequest);
        requestUtils.saveRequest(userRequest, userInfoDto, FunctionCode.UPDATE_USER);
        return new ResStatus(Contants.SUCCESS);
    }

    @Override
    public ResStatus createAccount(AccountRequest accountRequest) {
        UserInfoDto userInfoDto = UserInfoService.getUserInfo(CustomerType.ADMIN.name());
        accountValidate.validateRequestAccount(accountRequest, FunctionCode.CREATE_ACCOUNT);
        requestUtils.saveRequest(accountRequest, userInfoDto, FunctionCode.CREATE_ACCOUNT);
        return new ResStatus(Contants.SUCCESS);
    }

    @Override
    public ResStatus createCategory(CategoryRequest categoryRequest) {
        UserInfoDto userInfoDto = UserInfoService.getUserInfo(CustomerType.ADMIN.name());
        categoryValidate.validateRequestCategory(categoryRequest);
        requestUtils.saveRequest(categoryRequest, userInfoDto, FunctionCode.CREATE_CATEGORY);
        return new ResStatus(Contants.SUCCESS);
    }

    @Override
    public ResStatus createBook(BookRequest bookRequest) {
        UserInfoDto userInfoDto = UserInfoService.getUserInfo(CustomerType.ADMIN.name());
        bookValidate.validateRequestBook(bookRequest);
        requestUtils.saveRequest(bookRequest, userInfoDto, FunctionCode.CREATE_BOOK);
        return new ResStatus(Contants.SUCCESS);
    }


    @Override
    public ResStatus updateAccount(AccountRequest accountRequest) {
        UserInfoDto userInfoDto = UserInfoService.getUserInfo(CustomerType.ADMIN.name());
        accountValidate.validateRequestAccount(accountRequest, FunctionCode.UPDATE_ACCOUNT);
        requestUtils.saveRequest(accountRequest, userInfoDto, FunctionCode.UPDATE_ACCOUNT);
        return new ResStatus(Contants.SUCCESS);
    }

    @Override
    public ResStatus updateCategory(CategoryRequest categoryRequest) {
        UserInfoDto userInfoDto = UserInfoService.getUserInfo(CustomerType.ADMIN.name());
        categoryValidate.validateRequestCategory(categoryRequest);
        requestUtils.saveRequest(categoryRequest, userInfoDto, FunctionCode.UPDATE_CATEGORY);
        return new ResStatus(Contants.SUCCESS);
    }

    @Override
    public ResStatus updateBook(BookRequest bookRequest) {
        UserInfoDto userInfoDto = UserInfoService.getUserInfo(CustomerType.ADMIN.name());
        bookValidate.validateRequestBook(bookRequest);
        requestUtils.saveRequest(bookRequest, userInfoDto, FunctionCode.UPDATE_BOOK);
        return new ResStatus(Contants.SUCCESS);
    }

    @Override
    public VerifyResponse verify(VerifyRequest verifyRequest) {
        if (CollectionUtils.isEmpty(verifyRequest.getRequestId())) {
            throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        UserInfoDto userInfoDto = UserInfoService.getUserInfo(CustomerType.ADMIN.name());
        VerifyResponse verifyResponse = new VerifyResponse();
        List<RequestEntity> requestEntity = requestRepository.findByIdInAndStatus(verifyRequest.getRequestId(), TransactionStatus.INIT.name());
        if (CollectionUtils.isEmpty(requestEntity) || requestEntity.size() != verifyRequest.getRequestId().size()) {
            throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        String otpId = UUID.randomUUID().toString();
        requestRepository.saveAll(requestEntity.stream().peek(rq -> rq.setOtpId(otpId)).collect(Collectors.toList()));
        verifyResponse.setPhoneNumber(UserInfoService.maskPhoneNumber(userInfoDto.getPhoneNumber()));
        verifyResponse.setRequestId(verifyRequest.getRequestId());
        verifyResponse.setOtpId(otpId);
        return verifyResponse;
    }

    @Override
    public ResStatus cancel(CancelRequest cancelRequest) {
        if (CollectionUtils.isEmpty(cancelRequest.getRequestId())) {
            throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        UserInfoDto userInfoDto = UserInfoService.getUserInfo(CustomerType.ADMIN.name());
        List<RequestEntity> requestEntity = requestRepository.findByIdInAndStatus(cancelRequest.getRequestId(), TransactionStatus.INIT.name());
        if (CollectionUtils.isEmpty(requestEntity) || requestEntity.size() != cancelRequest.getRequestId().size()) {
            throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        requestRepository.saveAll(requestEntity.stream().peek(rq -> rq.setStatus(TransactionStatus.CANCEL.name())).collect(Collectors.toList()));
        return new ResStatus(Contants.SUCCESS);
    }

    @Override
    public ResStatus confirm(ConfirmRequest confirmRequest) {
        UserInfoDto userInfoDto = UserInfoService.getUserInfo(CustomerType.ADMIN.name());
        validatConfirmRequest(confirmRequest);
        List<RequestEntity> requestEntitys = requestRepository.findByIdInAndStatus(confirmRequest.getRequestId(), TransactionStatus.INIT.name());
        if (CollectionUtils.isEmpty(requestEntitys) || requestEntitys.size() != confirmRequest.getRequestId().size()) {
            throw new ProjectException("", "Thông tin request không hợp lệ");
        }
        if (!StringUtils.equalsAnyIgnoreCase(confirmRequest.getOtp(), Contants.OTP)) {
            throw new ProjectException("", "Mã OTP không đúng");
        }
        requestEntitys.forEach(requestEntity -> {
            try {
                FunctionCode functionCode = FunctionCode.valueOf(requestEntity.getRequestType());
                switch (functionCode) {
                    case CREATE_USER:
                    case UPDATE_USER:
                        processUser(userInfoDto, requestEntity, functionCode);
                        break;
                    case CREATE_ACCOUNT:
                    case UPDATE_ACCOUNT:
                        processAccount(userInfoDto, requestEntity, functionCode);
                        break;
                    case CREATE_CATEGORY:
                    case UPDATE_CATEGORY:
                        processCategory(userInfoDto, requestEntity, functionCode);
                        break;
                    case CREATE_BOOK:
                    case UPDATE_BOOK:
                        processBook(userInfoDto, requestEntity, functionCode);
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                logger.error("confirm error: {}", e.getMessage());
                requestEntity.setStatus(TransactionStatus.FAILED.name());
                requestEntity.setUpdatedAt(new Date(System.currentTimeMillis()));
                requestRepository.save(requestEntity);
            }
        });

        return new ResStatus(Contants.SUCCESS);
    }

    private void processBook(UserInfoDto userInfoDto, RequestEntity requestEntity, FunctionCode functionCode) {
        BookRequest rqBody = ConvertUtils.fromJson(requestEntity.getRequestBody(), BookRequest.class);
        BooksEntity booksEntity = new BooksEntity();
        List<CategoryBookEntity> listCategoryBookEntities = new ArrayList<>();
        switch (functionCode) {
            case CREATE_BOOK:
                String id = UUID.randomUUID().toString();
                booksEntity.setId(id);
                booksEntity.setName(rqBody.getName());
                booksEntity.setAuthor(rqBody.getAuthor());
                booksEntity.setPublishYear(rqBody.getPublishYear());
                booksEntity.setDescription(rqBody.getDescription());
                booksEntity.setPrice(Float.valueOf(rqBody.getPrice()));
                booksEntity.setInventory(Integer.valueOf(rqBody.getInventory()));
                booksEntity.setStatus(StringUtils.equalsIgnoreCase(rqBody.getStatus(), StatusType.ACTIVE.name()) ? StatusType.ACTIVE.name() : StatusType.DEACTIVE.name());
                booksEntity.setImage(StringUtils.isBlank(rqBody.getImageUrl()) ? "https://lh4.googleusercontent.com/proxy/8ecGg7bwK9gnQhcaSUphA5Vo--J55LFV_6lm-PMkOjQvpD4xciMz4dHeozsoivmT-lcBpLnP795Ng_Sb3jNtZf5Mb1dt6bnNjDTmAUY-09bNf6KwKUS2h9WKGHymoGxSv7PFeacG_zPcLVNAi05Bza4AvR7IcqLn6djH9ZlUB0GXAorbOQbLubnTxzf-AhSD3jDUpxBTUxwJ4-n26hMv50ebjqSeIIzUhmDK1_nK" : rqBody.getImageUrl());
                booksEntity.setCreatedAt(new Date(System.currentTimeMillis()));
                booksEntity.setUpdatedAt(new Date(System.currentTimeMillis()));
                booksEntity.setCreatedBy(requestEntity.getCreatedBy());
                booksEntity.setUpdatedBy(userInfoDto.getUserName());

                List<CategoriesEntity> categoriesEntities = categoryRepository.findAllByIdInAndStatus(rqBody.getCategoriesId(), StatusType.ACTIVE.name());
                if (CollectionUtils.isEmpty(categoriesEntities) || categoriesEntities.size() != rqBody.getCategoriesId().size()) {
                    throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR);
                }
                listCategoryBookEntities = categoriesEntities.stream()
                        .map(category -> CategoryBookMapper.maptoCategoryBookEntity(category, id))
                        .collect(Collectors.toList());
                break;
            case UPDATE_BOOK:
                Optional<BooksEntity> entity = bookRepository.findById(rqBody.getId());
                if (!entity.isPresent()) {
                    throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR);
                } else {
                    booksEntity = entity.get();
                    booksEntity.setName(rqBody.getName());
                    booksEntity.setAuthor(rqBody.getAuthor());
                    booksEntity.setPublishYear(rqBody.getPublishYear());
                    booksEntity.setDescription(rqBody.getDescription());
                    booksEntity.setPrice(Float.valueOf(rqBody.getPrice()));
                    booksEntity.setInventory(Integer.valueOf(rqBody.getInventory()));
                    booksEntity.setStatus(StringUtils.equalsIgnoreCase(rqBody.getStatus(), StatusType.ACTIVE.name()) ? StatusType.ACTIVE.name() : StatusType.DEACTIVE.name());
                    booksEntity.setUpdatedAt(new Date(System.currentTimeMillis()));
                    booksEntity.setUpdatedBy(userInfoDto.getUserName());

                    List<CategoriesEntity> list = categoryRepository.findAllByIdInAndStatus(rqBody.getCategoriesId(), StatusType.ACTIVE.name());
                    if (CollectionUtils.isEmpty(list) || list.size() != rqBody.getCategoriesId().size()) {
                        throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR);
                    }
                    List<CategoryBookEntity> bookEntityList = categoryBookRepository.findAllByBookId(booksEntity.getId());
                    BooksEntity finalBooksEntity = booksEntity;
                    listCategoryBookEntities = list.stream().map(categoryEntity -> {
                        CategoryBookEntity catE = bookEntityList.stream()
                                .filter(categoryBookEntity -> StringUtils.equalsIgnoreCase(categoryBookEntity.getCategoryId(), categoryEntity.getId()))
                                .findFirst().orElse(null);
                        if (ObjectUtils.isEmpty(catE)) {
                            return CategoryBookMapper.maptoCategoryBookEntity(categoryEntity, finalBooksEntity.getId());
                        }
                        catE.setStatus(StatusType.ACTIVE.name());
                        return catE;
                    }).collect(Collectors.toList());
                    List<String> ids = listCategoryBookEntities.stream().map(CategoryBookEntity::getBookId).collect(Collectors.toList());
                    listCategoryBookEntities.addAll(
                            bookEntityList.stream()
                                    .filter(b -> !ids.contains(b.getBookId()) && StringUtils.equalsIgnoreCase(b.getStatus(), StatusType.ACTIVE.name()))
                                    .peek(b -> {
                                        b.setStatus(StatusType.DEACTIVE.name());
                                        b.setUpdatedAt(new Date(System.currentTimeMillis()));
                                    })
                                    .collect(Collectors.toList()));
                }
                break;
        }
        requestEntity.setStatus(TransactionStatus.SUCCESS.name());
        categoryBookRepository.saveAll(listCategoryBookEntities);
        bookRepository.save(booksEntity);
        requestRepository.save(requestEntity);
    }

    private void processCategory(UserInfoDto userInfoDto, RequestEntity requestEntity, FunctionCode functionCode) {
        CategoryRequest rqBody = ConvertUtils.fromJson(requestEntity.getRequestBody(), CategoryRequest.class);
        CategoriesEntity categoriesEntity = new CategoriesEntity();
        switch (functionCode) {
            case CREATE_CATEGORY:
                categoriesEntity.setId(UUID.randomUUID().toString());
                categoriesEntity.setName(rqBody.getName());
                categoriesEntity.setStatus(rqBody.getStatus());
                categoriesEntity.setDescription(rqBody.getDescription());
                categoriesEntity.setCreatedAt(new Date(System.currentTimeMillis()));
                categoriesEntity.setUpdatedAt(new Date(System.currentTimeMillis()));
                categoriesEntity.setCreatedBy(requestEntity.getCreatedBy());
                categoriesEntity.setUpdatedBy(userInfoDto.getUserName());
                break;
            case UPDATE_CATEGORY:
                Optional<CategoriesEntity> entity = categoryRepository.findById(rqBody.getId());
                if (!entity.isPresent()) {
                    throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR);
                } else {
                    categoriesEntity = entity.get();
                    categoriesEntity.setName(rqBody.getName());
                    categoriesEntity.setStatus(rqBody.getStatus());
                    categoriesEntity.setDescription(rqBody.getDescription());
                    categoriesEntity.setUpdatedAt(new Date(System.currentTimeMillis()));
                    categoriesEntity.setUpdatedBy(userInfoDto.getUserName());
                }
                break;
        }
        requestEntity.setStatus(TransactionStatus.SUCCESS.name());
        categoryRepository.save(categoriesEntity);
        requestRepository.save(requestEntity);
    }

    private void processAccount(UserInfoDto userInfoDto, RequestEntity requestEntity, FunctionCode functionCode) {
        AccountRequest rqBody = ConvertUtils.fromJson(requestEntity.getRequestBody(), AccountRequest.class);
        AccountsEntity accountsEntity = new AccountsEntity();
        switch (functionCode) {
            case CREATE_ACCOUNT:
                accountsEntity.setId(UUID.randomUUID().toString());
                accountsEntity.setUserName(rqBody.getUserName());
                accountsEntity.setFullName(rqBody.getFullName());
                accountsEntity.setEmail(rqBody.getEmail());
                accountsEntity.setPhoneNumber(rqBody.getPhoneNumber());
                accountsEntity.setAddress(rqBody.getAddress());
                accountsEntity.setStatus(rqBody.getStatus());
                String password = PasswordService.encodePassword(StringUtils.isBlank(rqBody.getPassword()) ? Contants.PASSWORD_DEFAULT : rqBody.getPassword());
                accountsEntity.setPassword(password);
                accountsEntity.setBirthday(DateUtils.convertStringToDate(rqBody.getBirthday()));
                accountsEntity.setCreatedAt(new Date(System.currentTimeMillis()));
                accountsEntity.setUpdatedAt(new Date(System.currentTimeMillis()));
                break;
            case UPDATE_ACCOUNT:
                Optional<AccountsEntity> entity = accountRepository.findById(rqBody.getId());
                if (!entity.isPresent()) {
                    throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR);
                } else {
                    accountsEntity = entity.get();
                    accountsEntity.setFullName(rqBody.getFullName());
                    accountsEntity.setEmail(rqBody.getEmail());
                    accountsEntity.setPhoneNumber(rqBody.getPhoneNumber());
                    accountsEntity.setAddress(rqBody.getAddress());
                    accountsEntity.setStatus(rqBody.getStatus());
                    accountsEntity.setBirthday(DateUtils.convertStringToDate(rqBody.getBirthday()));
                    accountsEntity.setUpdatedAt(new Date(System.currentTimeMillis()));
                }
                break;
        }
        requestEntity.setStatus(TransactionStatus.SUCCESS.name());
        accountRepository.save(accountsEntity);
        requestRepository.save(requestEntity);
    }

    private void processUser(UserInfoDto userInfoDto, RequestEntity requestEntity, FunctionCode functionCode) {
        UserRequest rqBody = ConvertUtils.fromJson(requestEntity.getRequestBody(), UserRequest.class);
        UsersEntity usersEntity = new UsersEntity();
        Optional<RolesEntity> rolesEntity = roleRepository.findByIdAndStatus(rqBody.getRoleId(), StatusType.ACTIVE.name());
        try {
            userValidate.validateRequestUser(rqBody);
        } catch (Exception e) {
            logger.error("processUser error: {}", e.getMessage());
            throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        if (!rolesEntity.isPresent()) {
            throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        switch (functionCode) {
            case CREATE_USER:
                usersEntity.setId(UUID.randomUUID().toString());
                usersEntity.setRoleId(rqBody.getRoleId());
                usersEntity.setUserName(rqBody.getUserName());
                usersEntity.setFullName(rqBody.getFullName());
                usersEntity.setEmail(rqBody.getEmail());
                usersEntity.setPhoneNumber(rqBody.getPhoneNumber());
                usersEntity.setAddress(rqBody.getAddress());
                usersEntity.setStatus(rqBody.getStatus());
                String password = PasswordService.encodePassword(StringUtils.isBlank(rqBody.getPassword()) ? Contants.PASSWORD_DEFAULT : rqBody.getPassword());
                usersEntity.setPassword(password);
                usersEntity.setCreatedAt(new Date(System.currentTimeMillis()));
                usersEntity.setUpdatedAt(new Date(System.currentTimeMillis()));
                break;
            case UPDATE_USER:
                Optional<UsersEntity> entity = userRepository.findById(rqBody.getId());
                if (!entity.isPresent()) {
                    throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR);
                } else {
                    usersEntity = entity.get();
                    usersEntity.setRoleId(rqBody.getRoleId());
                    usersEntity.setUserName(rqBody.getUserName());
                    usersEntity.setFullName(rqBody.getFullName());
                    usersEntity.setEmail(rqBody.getEmail());
                    usersEntity.setPhoneNumber(rqBody.getPhoneNumber());
                    usersEntity.setAddress(rqBody.getAddress());
                    usersEntity.setStatus(rqBody.getStatus());
                    usersEntity.setUpdatedAt(new Date(System.currentTimeMillis()));
                }
                break;
        }
        requestEntity.setStatus(TransactionStatus.SUCCESS.name());
        userRepository.save(usersEntity);
        requestRepository.save(requestEntity);
    }

    @Override
    public TransPendingResponse getTransPending(TransPendingRequest request) {
        Pageable pageable;
        Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");
        try {
            pageable= PageRequest.of(request.getPageRequest().getPage(), request.getPageRequest().getSize(), sort);
        } catch (Exception e) {
            pageable = PageRequest.of(1, 10, sort);
        }
        List<String> requestType = CollectionUtils.isEmpty(request.getRequestType()) ? null : request.getRequestType();
        TransPendingResponse transPendingResponse = new TransPendingResponse();
        List<TransByTypeDto> transDetailResponseList = new ArrayList<>();
        Page<RequestEntity> list = requestRepository.findTransPendingWithRequest(TransactionStatus.INIT.name(), requestType, request.getCreatedBy(), pageable);
        Map<String, List<RequestEntity>> map = list.stream().collect(Collectors.groupingBy(RequestEntity::getRequestType));
        map.forEach((rqType, requestEntities) -> {
            TransByTypeDto transByTypeDto = new TransByTypeDto();
            transByTypeDto.setRequestType(rqType);
            List<TransDetailDto> transDetailDtos = requestEntities.stream().map(RequestMapper::mapRequestEntityToTransDetailDto).collect(Collectors.toList());
            transByTypeDto.setTransDetailDtos(transDetailDtos);
            transDetailResponseList.add(transByTypeDto);
        });
        TotalResponse totalResponse = new TotalResponse();
        totalResponse.setTotalPages(String.valueOf(list.getTotalPages()));
        totalResponse.setTotalElements(String.valueOf(list.getTotalElements()));
        transPendingResponse.setTotalResponse(totalResponse);
        transPendingResponse.setTransDetailResponseList(transDetailResponseList);
        return transPendingResponse;
    }

    private void validatConfirmRequest(ConfirmRequest request) {
        if (CollectionUtils.isEmpty(request.getRequestId()) || StringUtils.isBlank(request.getOtp()) ||
                StringUtils.isBlank(request.getOtpId())) {
            throw new ProjectException("", "Thông tin request không hợp lệ");
        }
    }
}
