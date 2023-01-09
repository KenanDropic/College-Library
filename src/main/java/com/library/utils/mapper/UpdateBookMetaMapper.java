package com.library.utils.mapper;

import com.library.entity.BookMeta;
import com.library.utils.dto.Book.UpdateMetaDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring")
public interface UpdateBookMetaMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    void updateEntityFromDto(UpdateMetaDto dto, @MappingTarget BookMeta meta);
}
