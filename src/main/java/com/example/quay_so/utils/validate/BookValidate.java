package com.example.quay_so.utils.validate;

import com.example.quay_so.exception.ErrorCode;
import com.example.quay_so.exception.ProjectException;
import com.example.quay_so.model.entity.BooksEntity;
import com.example.quay_so.model.entity.CategoriesEntity;
import com.example.quay_so.model.repository.BookRepository;
import com.bookstore.quay_so.model.repository.CategoryRepository;
import com.example.quay_so.model.request.books.BookRequest;
import com.example.quay_so.utils.StatusType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Component
public class BookValidate {
    @Resource
    private BookRepository bookRepository;

    @Resource
    private CategoryRepository categoryRepository;

    public void validateRequestBook(BookRequest bookRequest) {
        if (StringUtils.isBlank(bookRequest.getName())
                || StringUtils.isBlank(bookRequest.getAuthor())
                || StringUtils.isBlank(bookRequest.getPublishYear())
                || StringUtils.isBlank(bookRequest.getDescription())
                || StringUtils.isBlank(bookRequest.getPrice())
                || StringUtils.isBlank(bookRequest.getInventory())
                || StringUtils.isBlank(bookRequest.getStatus())
                || CollectionUtils.isEmpty(bookRequest.getCategoriesId())
        ) {
            throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        if (checkExistName(bookRequest.getName(), bookRequest.getId())) {
            throw new ProjectException("", "Tên sách đã tồn tại");
        }
        if (checkExistCategories(bookRequest.getCategoriesId())) {
            throw new ProjectException("", "Tên sách đã tồn tại");
        }

    }

    public boolean checkExistName(String name, String id) {
        Optional<BooksEntity> entity = bookRepository.findByName(name);
        return entity.isPresent() && checkId(entity.get().getId(), id);
    }

    public boolean checkExistCategories(List<String> categoiesId) {
        List<CategoriesEntity> entity = categoryRepository.findAllByIdInAndStatus(categoiesId, StatusType.ACTIVE.name());
        return CollectionUtils.isEmpty(entity) || entity.size() != categoiesId.size();
    }

    private boolean checkId(String id, String requestId) {
        return StringUtils.isBlank(requestId) || !StringUtils.equalsIgnoreCase(id, requestId);
    }
}
