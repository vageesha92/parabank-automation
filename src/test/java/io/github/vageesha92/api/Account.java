package io.github.vageesha92.api;

import java.math.BigDecimal;

public record Account(
        long id,
        long customerId,
        String type,
        BigDecimal balance
) {}