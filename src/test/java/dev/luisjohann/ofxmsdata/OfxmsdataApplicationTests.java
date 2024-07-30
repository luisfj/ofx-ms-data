package dev.luisjohann.ofxmsdata;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

// @ExtendWith(SpringExtension.class)
// We create a `@SpringBootTest`, starting an actual server on a `RANDOM_PORT`
// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// @AutoConfigureWebTestClient
@SpringBootTest
@ActiveProfiles("dev")
class OfxmsdataApplicationTests {

	// @Autowired
	// private WebTestClient webTestClient;

	// @Test
	// public void testHello() {
	// webTestClient
	// // Create a GET request to test an endpoint
	// .get().uri("/hello")
	// .accept(MediaType.APPLICATION_JSON)
	// .exchange()
	// // and use the dedicated DSL to test assertions against the response
	// .expectStatus().isOk()
	// .expectBody(Greeting.class).value(greeting -> {
	// assertThat(greeting.getMessage()).isEqualTo("Hello, Spring!");
	// });
	// }

	@Test
	void teste() {
		Assertions.assertTrue(Boolean.TRUE);
	}

}
