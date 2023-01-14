package com.library.utils.dto.Pressman;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class PressmanDto {
    @Length(min = 3, max = 45)
    private String pressmanName;
}
