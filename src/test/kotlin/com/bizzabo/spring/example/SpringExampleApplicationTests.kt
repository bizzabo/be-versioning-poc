package com.bizzabo.spring.example

import org.junit.jupiter.api.Test
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document
import kotlin.test.assertEquals

class SpringExampleApplicationTests: BaseTest() {


  @Test
  fun `hello controller returns message`() {
    val result = webClient.get().uri("/hello")
      .exchange()
      .expectStatus().is2xxSuccessful
      .expectBody()
      .consumeWith(document("get-example"))
      .returnResult()
      .responseBody

    assertEquals("Hello, Spring!", String(result!!))
  }

}
