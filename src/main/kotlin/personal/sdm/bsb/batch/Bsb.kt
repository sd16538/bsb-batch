package personal.sdm.bsb.batch

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

//TODO track creationTimeUTC and sourceFileName
@Document
data class Bsb(
        @Id val hyphenatedNumber: String = "",
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
data class ApcaBsb(var hyphenatedNumber: String = "",
                   var finInstMnemonic: String = "",
                   var name: String = "",
                   var street: String = "",
                   var suburb: String = "",
                   var state: String = "",
                   var postCode: String = "",
                   var paymentFlags: String = ""
)