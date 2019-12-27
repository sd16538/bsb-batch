package personal.sdm.bsb.batch

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.config.EnableWebFlux
import personal.sdm.bsb.batch.services.BsbRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@SpringBootApplication
@EnableWebFlux
class BsbBatchApplication

fun main(args: Array<String>) {
    runApplication<BsbBatchApplication>(*args)
}

@RestController
class BsbController(
        // TODO : Is it okay to use repo here ?
        @Autowired val bsbRepository: BsbRepository
) {

    @GetMapping("/bsb")
    @ResponseStatus(HttpStatus.OK)
    fun `get all BSBs`(): Flux<Bsb> = bsbRepository.findAll()

    @GetMapping("/bsb/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun `get by bsb number`(@PathVariable("id") id: String): Mono<Bsb> = bsbRepository.findById(id)
}
