package com.nbchand.brs.dto.book;

import com.nbchand.brs.dto.author.AuthorDto;
import com.nbchand.brs.dto.category.CategoryDto;
import com.nbchand.brs.entity.author.Author;
import com.nbchand.brs.entity.category.Category;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-02-27
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDto {

    private Integer id;

    @NotEmpty(message = "Book name must not be empty")
    private String name;

    @NotNull(message = "Number of book pages must not be empty")
    @Min(value = 1, message = "Book must have at least 1 page")
    private Integer numberOfPages;

    @NotEmpty(message = "Book ISBN must not be empty")
    private String isbn;

    @NotNull(message = "Provide a book rating")
    @Min(value = 0, message = "Book rating can't be negative")
    @Max(value = 5, message = "Book rating can't be greater than 5")
    private Double rating;

    @NotNull(message = "Provide book stock count")
    @Min(value = 0, message = "Stock count can't be negative")
    private Integer stockCount;

    @NotEmpty(message = "Please provide a valid date")
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])", message = "Date format must be \"yyyy-MM-dd\" ")
    private String publishedDateString;

    private Date publishedDate;

    private MultipartFile photo;

    private String photoLocation;

    @NotNull(message = "Book category can't be empty")
    private Integer categoryId;

    private CategoryDto categoryDto;

    private Category category;

    @NotEmpty(message = "Book authors can't be empty")
    @NotNull(message = "Book authors can't be empty")
    List<Integer> authorIds;

    private List<AuthorDto> authorDtoList;

    private List<Author> authors;

    public static class BookDtoBuilder {
        public BookDtoBuilder categoryDto(Category category) {

            this.categoryDto = CategoryDto.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .description(category.getDescription())
                    .build();
            return this;
        }

        public BookDtoBuilder authorDtoList(List<Author> authors) {

            this.authorDtoList = authors.stream().map(author ->
                    AuthorDto.builder()
                            .id(author.getId())
                            .name(author.getName())
                            .email(author.getEmail())
                            .mobileNumber(author.getMobileNumber())
                            .build()
            ).collect(Collectors.toList());

            return this;
        }
    }
}
