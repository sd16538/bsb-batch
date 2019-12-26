package personal.sdm.bsb.batch.services

import org.springframework.batch.item.ItemProcessor
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Service
import personal.sdm.bsb.batch.ApcaBsb
import personal.sdm.bsb.batch.Bsb

@Service
class BsbLineItemProcessor : ItemProcessor<ApcaBsb, Bsb> {
    override fun process(item: ApcaBsb): Bsb {
        val nonHyphenated = item.hyphenatedNumber.replace("-", "")
        val paper = item.paymentFlags.contains("P")
        val electronic = item.paymentFlags.contains("E")
        val highValue = item.paymentFlags.contains("H")
        val closed = item.paymentFlags.isBlank()

        return Bsb(
                item.hyphenatedNumber,
                item.finInstMnemonic,
                item.name,
                item.street,
                item.suburb,
                item.state,
                item.postCode,
                item.paymentFlags,
                nonHyphenated,
                paper,
                electronic,
                highValue,
                closed
        )
    }
}

interface BsbRepository : MongoRepository<Bsb, String>