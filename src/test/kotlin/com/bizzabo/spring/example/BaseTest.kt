package com.bizzabo.spring.example

import capital.scalable.restdocs.AutoDocumentation
import capital.scalable.restdocs.SnippetRegistry
import capital.scalable.restdocs.section.SectionBuilder
import capital.scalable.restdocs.section.SectionSnippet
import capital.scalable.restdocs.webflux.WebTestClientInitializer
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.http.HttpDocumentation
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest
@ContextConfiguration
@ExtendWith(RestDocumentationExtension::class, SpringExtension::class)
@AutoConfigureWebTestClient
@AutoConfigureRestDocs
class BaseTest {

  lateinit var webClient: WebTestClient

  @Autowired
  lateinit var objectMapper: ObjectMapper

  val section: SectionSnippet = SectionBuilder()
    .snippetNames(
      SnippetRegistry.AUTO_AUTHORIZATION,
      SnippetRegistry.AUTO_PATH_PARAMETERS,
      SnippetRegistry.AUTO_REQUEST_PARAMETERS,
      SnippetRegistry.AUTO_REQUEST_FIELDS,
      SnippetRegistry.AUTO_RESPONSE_FIELDS,
      SnippetRegistry.HTTP_RESPONSE
    )
    .skipEmpty(true)
    .build()

  @BeforeEach
  fun prepareWebClient(applicationContext: ApplicationContext, restDocumentation: RestDocumentationContextProvider) {
    val filter = WebTestClientRestDocumentation.documentationConfiguration(restDocumentation)
      .snippets()
      .withDefaults(
        WebTestClientInitializer.prepareSnippets(applicationContext),
        HttpDocumentation.httpRequest(),
        HttpDocumentation.httpResponse(),
        AutoDocumentation.requestFields(),
        AutoDocumentation.responseFields(),
        AutoDocumentation.pathParameters(),
        AutoDocumentation.requestParameters(),
        AutoDocumentation.description(),
        section,
        AutoDocumentation.methodAndPath()
      )
      .and().operationPreprocessors()
      .withRequestDefaults(Preprocessors.removeHeaders("Authorization"), Preprocessors.prettyPrint())
      .withResponseDefaults(Preprocessors.prettyPrint())

    webClient = WebTestClient
      .bindToApplicationContext(applicationContext)
      .configureClient()
      .filter(filter)
      .build()
  }

}

fun <T> WebTestClient.BodyContentSpec.mapResult(objectMapper: ObjectMapper, clazz: Class<T>): T
  = objectMapper.readValue(this.returnResult().responseBody, clazz)
