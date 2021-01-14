package com.easypay.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.easypay.web.rest.TestUtil;

public class IndexTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Index.class);
        Index index1 = new Index();
        index1.setId(1L);
        Index index2 = new Index();
        index2.setId(index1.getId());
        assertThat(index1).isEqualTo(index2);
        index2.setId(2L);
        assertThat(index1).isNotEqualTo(index2);
        index1.setId(null);
        assertThat(index1).isNotEqualTo(index2);
    }
}
