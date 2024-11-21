package com.ssafy.enjoycamping.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@Test
	@DisplayName("시큐리티 적용 테스트")
	void SecurityTest() throws Exception{
		mockMvc.perform(get("/swagger-ui.html"))
			.andExpect(status().isOk());
		
	}
}
