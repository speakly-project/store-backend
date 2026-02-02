package es.speakly.store_backend.controller;

import es.speakly.store_backend.domain.dto.LanguageDto;
import es.speakly.store_backend.domain.model.Page;
import es.speakly.store_backend.domain.service.LanguageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LanguageController.class)
class LanguageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LanguageService languageService;

    @BeforeEach
    void resetMocks() {
        Mockito.reset(languageService);
    }

    @Test
    void findAllLanguages_DefaultParams_ShouldReturnPage() throws Exception {
        LanguageDto l1 = new LanguageDto(1L, "English", "en");
        LanguageDto l2 = new LanguageDto(2L, "Spanish", "es");

        Page<LanguageDto> page = new Page<>(
                List.of(l1, l2),
                1,
                20,
                2L
        );

        when(languageService.getAll(eq(1), eq(20))).thenReturn(page);

        mockMvc.perform(get("/api/speakly/languages"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].name").value("English"))
                .andExpect(jsonPath("$.data[0].code").value("en"))
                .andExpect(jsonPath("$.pageNumber").value(1))
                .andExpect(jsonPath("$.pageSize").value(20))
                .andExpect(jsonPath("$.totalElements").value(2));

        verify(languageService).getAll(1, 20);
    }

    @Test
    void findAllLanguages_CustomParams_ShouldPassParamsToService() throws Exception {
        Page<LanguageDto> page = new Page<>(
                List.of(),
                2,
                5,
                0L
        );

        when(languageService.getAll(eq(2), eq(5))).thenReturn(page);

        mockMvc.perform(get("/api/speakly/languages")
                        .param("pageNumber", "2")
                        .param("pageSize", "5"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.pageNumber").value(2))
                .andExpect(jsonPath("$.pageSize").value(5))
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.data").isArray());

        verify(languageService).getAll(2, 5);
    }

    @Test
    void findLanguageById_ShouldReturnDto() throws Exception {
        LanguageDto dto = new LanguageDto(1L, "English", "en");
        when(languageService.getById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/speakly/languages/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("English"))
                .andExpect(jsonPath("$.code").value("en"));

        verify(languageService).getById(1L);
    }


    @Test
    void getLanguagesAmount_ShouldReturnNumber() throws Exception {
        when(languageService.count()).thenReturn(12L);

        mockMvc.perform(get("/api/speakly/languages/amount"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$").value(12));

        verify(languageService).count();
    }
}
