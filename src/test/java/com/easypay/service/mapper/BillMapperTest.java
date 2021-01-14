package com.easypay.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class BillMapperTest {

    private BillMapper billMapper;

    @BeforeEach
    public void setUp() {
        billMapper = new BillMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(billMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(billMapper.fromId(null)).isNull();
    }
}
