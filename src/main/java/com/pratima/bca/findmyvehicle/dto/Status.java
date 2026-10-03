package com.pratima.bca.findmyvehicle.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor   // generates public Status(int, String)
@NoArgsConstructor
public class Status {
    private int status;

    private String message;
}
