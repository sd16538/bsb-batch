package personal.sdm.bsb.batch.services

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import personal.sdm.bsb.batch.ApcaBsb

internal class BsbLineItemProcessorTest {

    private val processor = BsbLineItemProcessor()

    @Test
    fun `hyphenated`() {
        val processedBsb = processor.process(ApcaBsb("803-233"))
        assertThat(processedBsb.nonHyphenated).isEqualTo("803233")
    }

    @Test
    fun `closed`() {
        val processedBsb = processor.process(ApcaBsb(
                "803-233",
                "CRU", "Closed",
                "Refer to BSB 803-158 09/97",
                "Melbourne", "VIC", "3000",
                ""

        ))
        assertThat(processedBsb.closed).isTrue()
        assertThat(processedBsb.paper).isFalse()
        assertThat(processedBsb.electronic).isFalse()
        assertThat(processedBsb.highValue).isFalse()
    }

    @Test
    fun `flags as all whitespace`() {
        val processedBsb = processor.process(ApcaBsb(
                "803-233",
                "CRU", "Closed",
                "Refer to BSB 803-158 09/97",
                "Melbourne", "VIC", "3000",
                "    "

        ))
        assertThat(processedBsb.closed).isTrue()
        assertThat(processedBsb.paper).isFalse()
        assertThat(processedBsb.electronic).isFalse()
        assertThat(processedBsb.highValue).isFalse()
    }

    @ParameterizedTest
    @ValueSource(strings = ["P", " P", " P ", "P "])
    fun `only paper allowed`(paymentFlag: String) {
        val processedBsb = processor.process(ApcaBsb(
                "814-999",
                "CUA", "Credit Union Australia Ltd",
                "175 Eagle Street",
                "Brisbane", "QLD", "4000",
                paymentFlag

        ))
        assertThat(processedBsb.closed).isFalse()
        assertThat(processedBsb.paper).isTrue()
        assertThat(processedBsb.electronic).isFalse()
        assertThat(processedBsb.highValue).isFalse()
    }

    @ParameterizedTest
    @ValueSource(strings = ["E", " E", " E ", "E "])
    fun `only electronic allowed`(paymentFlag: String) {
        val inputBsb = ApcaBsb(
                "823-000",
                "ENC", "AMB Endeavour Mutual Bank",
                "59 Buckingham Street",
                "Surry Hills", "NSW", "2010",
                paymentFlag

        )
        val processedBsb = processor.process(inputBsb)
        assertThat(processedBsb.closed).isFalse()
        assertThat(processedBsb.paper).isFalse()
        assertThat(processedBsb.electronic).isTrue()
        assertThat(processedBsb.highValue).isFalse()
    }

    @ParameterizedTest
    @ValueSource(strings = ["H", " H", " H ", "H "])
    fun `only high value allowed`(paymentFlag: String) {
        val inputBsb = ApcaBsb(
                "013-217",
                "ANZ", "Closed",
                "Refer to BSB 013-352 - 03/11",
                "Mentone", "VIC", "3194",
                paymentFlag

        )
        val processedBsb = processor.process(inputBsb)
        assertThat(processedBsb.closed).isFalse()
        assertThat(processedBsb.paper).isFalse()
        assertThat(processedBsb.electronic).isFalse()
        assertThat(processedBsb.highValue).isTrue()
    }


    @ParameterizedTest
    @ValueSource(strings = ["PEH", "HEP", "EHP", " P E H "])
    fun `all three payment types allowed`(paymentFlag: String) {
        val inputBsb = ApcaBsb(
                "013-220",
                "ANZ", "Bentleigh",
                "413-415 Centre Road",
                "Bentleigh", "VIC", "3204",
                paymentFlag

        )
        val processedBsb = processor.process(inputBsb)
        assertThat(processedBsb.closed).isFalse()
        assertThat(processedBsb.paper).isTrue()
        assertThat(processedBsb.electronic).isTrue()
        assertThat(processedBsb.highValue).isTrue()
    }

    @Test
    fun `no hyphen in hyphenated number`() {
        val inputBsb = ApcaBsb(
                "814999",
                "ABC", "La la la",
                "123",
                "Brisbane", "QLD", "4000",
                "PEH"

        )
        val processedBsb = processor.process(inputBsb)
        assertThat(processedBsb.nonHyphenated).isEqualTo("814999")
    }

    @ParameterizedTest
    @ValueSource(strings = ["OWG", " *&^", " 123 "])
    fun `none of the allowed payment types`(paymentFlag: String) {
        val inputBsb = ApcaBsb(
                "000-000",
                "0", "La la la",
                "123",
                "Brisbane", "QLD", "4000",
                paymentFlag

        )
        val processedBsb = processor.process(inputBsb)
        assertThat(processedBsb.closed).isFalse()
        assertThat(processedBsb.paper).isFalse()
        assertThat(processedBsb.electronic).isFalse()
        assertThat(processedBsb.highValue).isFalse()
    }

    @ParameterizedTest
    @ValueSource(strings = ["PE", " P E", " PE ", "PE "])
    fun `paper and electronic`(paymentFlag: String) {
        val inputBsb = ApcaBsb(
                "013-947",
                "ANZ", "CBRE (V) PTY LTD ESTATE AGENCY",
                "(NBFI Agency to 013-000)",
                "Brisbane", "QLD", "4000",
                paymentFlag

        )
        val processedBsb = processor.process(inputBsb)
        assertThat(processedBsb.closed).isFalse()
        assertThat(processedBsb.paper).isTrue()
        assertThat(processedBsb.electronic).isTrue()
        assertThat(processedBsb.highValue).isFalse()
    }

    @ParameterizedTest
    @ValueSource(strings = ["PH", " P H ", " PH", "PH "])
    fun `paper and high value`(paymentFlag: String) {
        val inputBsb = ApcaBsb(
                "014-033",
                "ANZ", "Markets Division",
                "8/324 Queen Street",
                "Brisbane", "QLD", "4000",
                paymentFlag

        )
        val processedBsb = processor.process(inputBsb)
        assertThat(processedBsb.closed).isFalse()
        assertThat(processedBsb.paper).isTrue()
        assertThat(processedBsb.electronic).isFalse()
        assertThat(processedBsb.highValue).isTrue()
    }

    @ParameterizedTest
    @ValueSource(strings = ["EH", " EH", " EH ", "EH ", " E H "])
    fun `electronic and high value`(paymentFlag: String) {
        val inputBsb = ApcaBsb(
                "014-993",
                "ANZ", "Lactalis Australia Pty Ltd",
                "(NBFI Agency to 014-000)",
                "South Brisbane", "QLD", "4101",
                paymentFlag

        )
        val processedBsb = processor.process(inputBsb)
        assertThat(processedBsb.closed).isFalse()
        assertThat(processedBsb.paper).isFalse()
        assertThat(processedBsb.electronic).isTrue()
        assertThat(processedBsb.highValue).isTrue()
    }
}
