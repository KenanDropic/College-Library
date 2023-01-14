package com.library.utils.dto.Pressman;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

@Data
public class SearchPressmanDto {
    @Nullable
    @Length(min = 3, max = 45)
    private String pressmanName;

    private String field = "pressman_id"; // id or pressman name
    private String direction = "ASC";

}
