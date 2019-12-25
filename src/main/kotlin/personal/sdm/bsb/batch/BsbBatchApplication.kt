package personal.sdm.bsb.batch

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BsbBatchApplication

fun main(args: Array<String>) {
	runApplication<BsbBatchApplication>(*args)
}
