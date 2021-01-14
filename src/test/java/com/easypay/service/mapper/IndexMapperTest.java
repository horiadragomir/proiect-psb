package com.easypay.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class IndexMapperTest {

    private IndexMapper indexMapper;

    @BeforeEach
    public void setUp() {
        indexMapper = new IndexMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(indexMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(indexMapper.fromId(null)).isNull();
    }
}
