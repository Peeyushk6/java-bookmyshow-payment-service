package io.github.peeyushkumar.bookmyshow.entity;

import io.github.peeyushkumar.bookmyshow.enums.MerchantStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name="merchants")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Merchant extends BaseEntity{

    @Column(nullable = false, unique = true)
    private String merchantId;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MerchantStatus status;

    @Column(nullable = false, unique = true)
    private String apiKey;

    @Column(nullable = false)
    private String apiSecret;

    @Column(nullable = false)
    private String callbackUrl;

    public boolean isActive() {
        return status == MerchantStatus.ACTIVE;
    }

}
