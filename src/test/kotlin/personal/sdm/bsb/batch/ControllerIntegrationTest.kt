package personal.sdm.bsb.batch

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.EntityExchangeResult
import org.springframework.test.web.reactive.server.WebTestClient
import personal.sdm.bsb.batch.services.BsbRepository
import reactor.core.publisher.Flux.fromIterable
import reactor.core.publisher.Mono.just

@ExtendWith(SpringExtension::class)
@WebFluxTest(BsbController::class)
class ControllerIntegrationTest {

    companion object {
        val BSB_1 = Bsb("001-001")
        val BSB_2 = Bsb("002-002")
    }

    @MockBean
    private lateinit var bsbRepository: BsbRepository

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Test
    fun `get all BSBs`() {

        `when`(bsbRepository.findAll()).thenReturn(fromIterable(listOf(BSB_2, BSB_1)))

        webTestClient.get().uri("/bsb")
                .exchange()
                .expectStatus().is2xxSuccessful
                .expectBodyList(Bsb::class.java)
                .hasSize(2)
                .contains(BSB_1, BSB_2)

    }

    @Test
    fun `get by hyphenated number`() {

        `when`(bsbRepository.findById("001-001")).thenReturn(just(BSB_1))

        val returnResult: EntityExchangeResult<Bsb> = webTestClient.get().uri("/bsb/001-001")
                .exchange()
                .expectStatus().is2xxSuccessful
                .expectBody<Bsb>(Bsb::class.java)
                .returnResult()

        assertThat(returnResult.responseBody).isEqualTo(BSB_1)
    }
}