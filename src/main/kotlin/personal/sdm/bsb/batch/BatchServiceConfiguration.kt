package personal.sdm.bsb.batch

import org.slf4j.LoggerFactory.getLogger
import org.springframework.batch.item.data.MongoItemWriter
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper
import org.springframework.batch.item.file.mapping.DefaultLineMapper
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.AbstractResource
import org.springframework.core.io.ClassPathResource
import org.springframework.data.mongodb.core.MongoTemplate


@Configuration
class BatchServiceConfiguration(
        @Autowired val mongoTemplate: MongoTemplate
) {
    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val logger = getLogger(javaClass.enclosingClass)
    }

    @Bean
    fun bsbFileResource(@Value("\${apca.bsb.all-records:}") allRecordsFile: String): AbstractResource {

        logger.info("CSV file [$allRecordsFile]")
        return ClassPathResource(allRecordsFile)
    }

    @Bean
    fun apcaBsbReader(@Autowired bsbFileResource: AbstractResource): FlatFileItemReader<ApcaBsb> {
        val reader: FlatFileItemReader<ApcaBsb> = FlatFileItemReader()


        reader.setResource(bsbFileResource)
        reader.setLineMapper(object : DefaultLineMapper<ApcaBsb>() {
            init {
                setLineTokenizer(object : DelimitedLineTokenizer() {
                    init {
                        setNames(*arrayOf(
                                "hyphenatedNumber",
                                "finInstMnemonic",
                                "name",
                                "street",
                                "suburb",
                                "state",
                                "postCode",
                                "paymentFlags"))
                    }
                })
                setFieldSetMapper(object : BeanWrapperFieldSetMapper<ApcaBsb>() {
                    init {
                        setTargetType(ApcaBsb::class.java)
                    }
                })
            }
        })
        return reader
    }


    @Bean
    fun bsbWriter(@Value("\${apca.bsb.collection:bsb}") bsbCollection: String): MongoItemWriter<Bsb> {
        val writer: MongoItemWriter<Bsb> = MongoItemWriter()
        writer.setTemplate(mongoTemplate)
        logger.info("Collection to be used [$bsbCollection]")
        writer.setCollection(bsbCollection)
        return writer
    }

}