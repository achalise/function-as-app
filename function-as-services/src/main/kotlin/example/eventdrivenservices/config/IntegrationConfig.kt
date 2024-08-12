package example.eventdrivenservices.config

import example.eventdrivenservices.functions.receiveMessageFn
import example.eventdrivenservices.repository.ApplicationRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.expression.common.LiteralExpression
import org.springframework.integration.annotation.InboundChannelAdapter
import org.springframework.integration.annotation.Poller
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.integration.channel.DirectChannel
import org.springframework.integration.config.EnableIntegration
import org.springframework.integration.core.MessageSource
import org.springframework.integration.dsl.IntegrationFlow
import org.springframework.integration.dsl.IntegrationFlowBuilder
import org.springframework.integration.dsl.MessageChannels
import org.springframework.integration.dsl.Pollers
import org.springframework.integration.dsl.SourcePollingChannelAdapterSpec
import org.springframework.integration.file.FileReadingMessageSource
import org.springframework.integration.file.FileWritingMessageHandler
import org.springframework.integration.file.filters.SimplePatternFileListFilter
import org.springframework.integration.file.support.FileExistsMode
import org.springframework.integration.mongodb.dsl.MongoDb
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.MessageHandler
import java.io.File
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

@Configuration
    @EnableIntegration
    @Profile("integration")
    class BasicIntegrationConfig {
        var INPUT_DIR: String = "the_source_dir"
        var OUTPUT_DIR: String = "the_dest_dir"
        var FILE_PATTERN: String = "*.txt"

        @Bean
        fun fileChannel(): MessageChannel {
            return DirectChannel()
        }

        @Bean
        @InboundChannelAdapter(value = "fileChannel", poller = Poller(fixedDelay = "1000"))
        fun fileReadingMessageSource(): MessageSource<File> {
            println("Polling for input files")
            val sourceReader: FileReadingMessageSource = FileReadingMessageSource()
            sourceReader.setDirectory(File(INPUT_DIR))
            sourceReader.setFilter(SimplePatternFileListFilter(FILE_PATTERN))
            return sourceReader
        }

        @Bean
        @ServiceActivator(inputChannel = "fileChannel")
        fun fileWritingMessageHandler(): MessageHandler {
            val handler: FileWritingMessageHandler = FileWritingMessageHandler(File(OUTPUT_DIR))
            handler.setFileExistsMode(FileExistsMode.REPLACE)
            handler.setExpectReply(false)
            return handler
        }
    }

    @Configuration
    @EnableIntegration
    @Profile("integration")
    class DbIntegration {
        // importPeriodicallyFromDB => sourceChannel => chain of handlers => completionChannel => completion handler
        @Bean
        fun sourceChannel(): MessageChannel {
            return MessageChannels.direct().`object`
        }

        @Bean
        fun integrationFlowFromDB(mongoTemplate: MongoOperations): IntegrationFlow {
            val flow = pollFromDbIntoIntegrationFlow(mongoTemplate)
            return flow
                .channel(sourceChannel())
                .get()
        }

        private fun pollFromDbIntoIntegrationFlow(mongoTemplate: MongoOperations): IntegrationFlowBuilder {
            val fromDateTime = OffsetDateTime.now().minus(60L, ChronoUnit.SECONDS)
            val queryExpression =
                LiteralExpression("{status: 'APPLICATION_STATE_NEW', dateCreated: { \$gt: ISODate('${fromDateTime}')}}").value
            val flow = IntegrationFlow.from(
                MongoDb.inboundChannelAdapter(mongoTemplate, queryExpression).collectionName("claim")
            ) { c: SourcePollingChannelAdapterSpec ->
                c.poller(
                    Pollers.fixedRate(30000).maxMessagesPerPoll(1)
                )
            }
            return flow
        }

        @Bean
        fun messageProcessorsIntegrationFlow(mongoTemplate: MongoOperations): IntegrationFlow {
            return IntegrationFlow.from(sourceChannel())
                .handle(eligibilityHandler())
                .handle(fraudCheckHandler())
                .handle(paymentHandler())
                .channel("next-channel")
                .get()
        }

        @Bean
        fun completeProcessFlow(mongoTemplate: MongoOperations): IntegrationFlow {
            return IntegrationFlow.from("next-channel")
                .handle(completeFlowHandler())
                .get()
        }

        @Bean
        fun receiveMessage(repository: ApplicationRepository, sourceChannel: MessageChannel): (String) -> String {
            return receiveMessageFn(repository, sourceChannel)
        }

        fun eligibilityHandler(): (Message<*>) -> Message<*> {
            return {
                val payload = it.payload
                println("elgibility check completed for ${payload}\n")
                it
            }
        }

        fun fraudCheckHandler(): (Message<*>) -> Message<*> {
            return {
                val payload = it.payload
                println("fraud check completed for ${payload}\n")
                it
            }
        }

        fun paymentHandler(): (Message<*>) -> Message<*> {
            return {
                val payload = it.payload
                println("payment completed for ${payload}\n")
                it
            }
        }

        fun completeFlowHandler(): (Message<*>) -> Unit {
            return {
                println("Completed application processing")
            }
        }
    }