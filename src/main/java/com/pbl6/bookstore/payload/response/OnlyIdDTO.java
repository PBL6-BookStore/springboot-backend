package com.pbl6.bookstore.payload.response;

import lombok.*;

import java.io.Serializable;

/**
 * @author lkadai0801
 * @since 26/10/2022
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OnlyIdDTO implements Serializable {
    private Long id;
}
