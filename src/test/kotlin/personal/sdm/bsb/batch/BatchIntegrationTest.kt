package personal.sdm.bsb.batch

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.batch.core.ExitStatus.COMPLETED
import org.springframework.batch.item.data.MongoItemWriter
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.batch.test.context.SpringBatchTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Primary
import org.springframework.core.io.AbstractResource
import org.springframework.core.io.FileSystemResource
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import personal.sdm.bsb.batch.BatchIntegrationTest.Companion.BSB_LINE
import java.util.Collections.singletonList

@ExtendWith(SpringExtension::class)
@SpringBatchTest
@EnableAutoConfiguration
@ContextConfiguration(classes = [
    BatchJobConfiguration::class,
    BatchServiceConfiguration::class,
    TestConfig::class
])
@TestPropertySource(
        properties = ["apca.bsb.all-records=test.csv"]
)
class BatchIntegrationTest {

    companion object {
        const val BSB = "012-002"
        const val NON_HYPHENATED = "012002"
        const val MNEMONIC = "MBL"
        const val BRANCH = "We all bank with Macquarie"
        const val STREET = "1 Every Street Corner"
        const val SUBURB = "Timbuktu"
        const val STATE = "MDE"
        const val POSTCODE = "2000"
        const val PEH_FLAG = "PEH"
        const val BSB_LINE: String = """
            "$BSB","$MNEMONIC","$BRANCH","$STREET","$SUBURB","$STATE","$POSTCODE","$PEH_FLAG"
        """
    }

    @Autowired
    lateinit var jobLauncherTestUtils: JobLauncherTestUtils

    @MockBean
    lateinit var bsbWriter: MongoItemWriter<Bsb>

    @Test
    fun `job should read, parse and write`() {

        val launchedJob = jobLauncherTestUtils.launchJob()

        assertThat(launchedJob.jobInstance.jobName).isEqualTo("bsbLoader")
        assertThat(launchedJob.exitStatus).isEqualTo(COMPLETED)


        verify(bsbWriter, times(1)).write(singletonList(
                Bsb(
                        BSB,
                        MNEMONIC, BRANCH,
                        STREET,
                        SUBURB, STATE, POSTCODE,
                        PEH_FLAG,
                        NON_HYPHENATED,
                        paper = true, electronic = true, highValue = true,
                        closed = false
                )
        ))
    }

}

@ComponentScan(basePackages = ["personal.sdm.bsb.batch.services"])
class TestConfig {

    @Bean
    @Primary
    fun bsbFileResource(): AbstractResource {
        val tempFile = createTempFile("test", ".csv")
        tempFile.writeText(BSB_LINE.trimIndent())

        return FileSystemResource(tempFile)
    }

}
