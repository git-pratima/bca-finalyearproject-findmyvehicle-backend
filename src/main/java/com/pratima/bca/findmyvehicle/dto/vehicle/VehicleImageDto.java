package com.pratima.bca.findmyvehicle.dto.vehicle;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleImageDto {

    private MultipartFile image;
}
