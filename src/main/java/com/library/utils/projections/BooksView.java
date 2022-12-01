package com.library.utils.projections;


import java.util.List;

public interface BooksView {
    Long getBook_Id();
    String getSource_Title();
    List<String> getAuthors();
    String getLanguage();
    Integer getRelease_Year();
    String getIsbn();
    String getBook_Type();
}
