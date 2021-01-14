package com.easypay.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.easypay.web.rest.TestUtil;

public class IndexDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IndexDTO.class);
        IndexDTO indexDTO1 = new IndexDTO();
        indexDTO1.setId(1L);
        IndexDTO indexDTO2 = new IndexDTO();
        assertThat(indexDTO1).isNotEqualTo(indexDTO2);
        indexDTO2.setId(indexDTO1.getId());
        assertThat(indexDTO1).isEqualTo(indexDTO2);
        indexDTO2.setId(2L);
        assertThat(indexDTO1).isNotEqualTo(indexDTO2);
        indexDTO1.setId(null);
        assertThat(indexDTO1).isNotEqualTo(indexDTO2);
    }
}
