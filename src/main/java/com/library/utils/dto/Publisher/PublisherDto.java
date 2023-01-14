package com.library.utils.dto.Publisher;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class PublisherDto {
    @Length(min = 3, max = 30)
    private String publisherName;
}
