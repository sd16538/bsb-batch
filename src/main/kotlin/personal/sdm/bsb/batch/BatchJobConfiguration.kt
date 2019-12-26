package personal.sdm.bsb.batch

import org.slf4j.LoggerFactory.getLogger
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.data.MongoItemWriter
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import personal.sdm.bsb.batch.services.BsbLineItemProcessor


@EnableBatchProcessing
@Configuration
class BatchJobConfiguration(
        @Autowired val jobBuilderFactory: JobBuilderFactory,
        @Autowired val stepBuilderFactory: StepBuilderFactory,
        @Autowired val apcaBsbReader: FlatFileItemReader<ApcaBsb>,
        @Autowired val bsbWriter: MongoItemWriter<Bsb>,
        @Autowired val processor: BsbLineItemProcessor,
        @Value("\${apca.bsb.batchSize:10}") val batchSize: Int
) {
    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val logger = getLogger(javaClass.enclosingClass)
    }

    @Bean
    fun bsbLoader(): Job {
        return jobBuilderFactory["bsbLoader"]
                .incrementer(RunIdIncrementer())
                .start(step())
                .build()
    }

    @Bean
    fun step(): Step {
        logger.info("Batching in sizes of [$batchSize]")
        return stepBuilderFactory["read, parse and write BSBs"]
                .chunk<ApcaBsb, Bsb>(batchSize)
                .reader(apcaBsbReader)
                .processor(processor)
                .writer(bsbWriter)
                .build()
    }

}