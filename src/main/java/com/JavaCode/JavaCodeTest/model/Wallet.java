package com.JavaCode.JavaCodeTest.model;

import lombok.Getter;
import lombok.Setter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wallet {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(nullable = false, unique = true)
    private UUID walletId;

    @NotNull
    @Column(nullable = false)
    @Setter
    private BigDecimal balance;

    public Wallet(BigDecimal balance) {
        this.balance = balance;
    }
    public Wallet(BigDecimal balance, int mock) { // for tests
        this.walletId = UUID.randomUUID();
        this.balance = balance.add(BigDecimal.valueOf(mock));
    }
}
