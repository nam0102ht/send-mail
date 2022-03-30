package com.ntnn.model;

import com.opencsv.bean.CsvBindByPosition;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Data
public class User implements Serializable {
    @CsvBindByPosition(position = 0)
    private String title;

    @CsvBindByPosition(position = 1)
    private String firstName;

    @CsvBindByPosition(position = 2)
    private String lastName;

    @CsvBindByPosition(position = 3)
    private String email;
}
