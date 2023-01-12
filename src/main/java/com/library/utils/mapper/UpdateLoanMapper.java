package com.library.utils.mapper;

import com.library.entity.Loan;
import com.library.utils.dto.Loan.UpdateLoanDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring")
public interface UpdateLoanMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "loanExtended",source = "dto.loanExtended")
    void updateEntityFromDto(UpdateLoanDto dto, @MappingTarget Loan loan);
}
