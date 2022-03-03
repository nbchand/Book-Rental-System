package com.nbchand.brs.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-02-24
 */
@Getter
@AllArgsConstructor
public enum RentType {
    RENT(1, "RENT"),
    RETURN(2, "RETURN");

    private Integer key;
    private String name;

    public static RentType getByKey(Integer key) throws Exception {
        if (key == null) {
            throw new Exception("Key cannot be null");
        }
        RentType[] rentTypes = values();

        for (RentType rentType : rentTypes) {
            if (key.equals(rentType.getKey())) {
                return rentType;
            }
        }
        throw new Exception("Invalid rent-type key");
    }
}
