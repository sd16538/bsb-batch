package personal.sdm.bsb.batch

import org.springframework.batch.item.ItemProcessor

class BsbLineItemProcessor : ItemProcessor<Bsb, Bsb> {
    override fun process(item: Bsb): Bsb {
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

/*
* "123-456","ABC","XYZ Company","109 Test Street","Sydney","NSW","2000","PEH"
* BSB Number (as 000-000)
* Three Character Financial Institution Mnemonic
* BSB Name or Closed
* BSB Street
* BSB Suburb
* BSB State
* BSB Postcode
* BSB Payments Flags
 */
data class Bsb(val hyphenatedNumber: String = "",
               val finInstMnemonic: String = "",
               val name: String = "",
               val street: String = "",
               val suburb: String = "",
               val state: String = "",
               val postCode: String = "",
               val paymentFlags: String = "",
               val nonHyphenated: String = "",
               val paper: Boolean = false,
               val electronic: Boolean = false,
               val highValue: Boolean = false,
               val closed: Boolean = true
)