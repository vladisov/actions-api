package dev.cards.repository

import dev.cards.AbstractTest
import dev.cards.domain.Item
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import reactor.test.StepVerifier
import java.time.Instant


/**
 * @author vladov 2019-03-16
 */
@DataMongoTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemRepositoryTest(@Autowired private val itemRepository: ItemRepository) : AbstractTest() {

    @BeforeAll
    internal fun setup() {
        itemRepository.save(Item(null, "desc", "132sda", Instant.now(), "username")).block()
        itemRepository.save(Item(null, "addescda", "sda321", Instant.now(), "username")).block()
        itemRepository.save(Item(null, "dasda", "das", Instant.now(), "username111")).block()
    }

    @Test
    fun testFindByDescriptionContainingAndUsernameSuccess() {
        val items = itemRepository.findByContentContainingAndUsername("desc", "username")
        StepVerifier
                .create(items)
                .assertNext { item ->
                    assertThat(item).isNotNull
                    assertThat(item.content).isEqualTo("desc")
                }
                .assertNext { item ->
                    assertThat(item.content).isEqualTo("addescda")
                }
                .expectComplete()
                .verify()
    }

    @Test
    fun testFindByDescriptionContainingAndUsernameEmptyResult() {
        val items = itemRepository.findByContentContainingAndUsername("desc", "vasya")
        StepVerifier
                .create(items)
                .expectComplete()
                .verify()
    }

    @Test
    fun testFindByResultContainingAndUsernameSuccess() {
        val items = itemRepository.findByContentContainingAndUsername("desc", "username")
        StepVerifier
                .create(items)
                .assertNext { item ->
                    assertThat(item).isNotNull
                    assertThat(item.content).isEqualTo("desc")
                }
                .assertNext { item ->
                    assertThat(item.content).isEqualTo("addescda")
                }
                .expectComplete()
                .verify()
    }

    @Test
    fun testFindByResultContainingAndUsernameEmptyResult() {
        val items = itemRepository.findByContentContainingAndUsername("desc", "vasya")
        StepVerifier
                .create(items)
                .expectComplete()
                .verify()
    }

    @Test
    fun testFindByIdSuccess() {
        itemRepository.save(Item("123321", "dasda", "das", Instant.now(), "username111")).block()
        val items = itemRepository.findById("123321")
        StepVerifier
                .create(items)
                .assertNext { item ->
                    assertThat(item).isNotNull
                    assertThat(item.id).isEqualTo("123321")
                    assertThat(item.content).isEqualTo("dasda")
                }
                .expectComplete()
                .verify()
    }

    @Test
    fun testFindByIdEmptyResult() {
        val items = itemRepository.findById("1")
        StepVerifier
                .create(items)
                .expectComplete()
                .verify()
    }
}