package com.library.utils.mapper;

import com.library.entity.Book;
import com.library.utils.dto.Book.UpdateBookDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring")
public interface UpdateBookMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    void updateEntityFromDto(UpdateBookDto dto, @MappingTarget Book book);
}
