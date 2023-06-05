package ru.petkeeper

import io.ktor.client.call.*
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlin.test.*
import io.ktor.server.testing.*
import io.ktor.util.reflect.*
import ru.petkeeper.database.orders_table.OrderDto
import ru.petkeeper.plugins.*
import ru.petkeeper.routes.load_orders.SettingsConfig

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            configureRouting()
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }

        client.post("/load_orders").apply {
            assertEquals(emptyList<OrderDto>(), body())
        }
    }



    @Test fun `login should succeed with token`() = withServer {
        val req = handleRequest {
            method = HttpMethod.Post
            uri = "/login"
            addHeader("Content-Type", "application/json")
            setBody(
                Gson().toJson(UserPasswordCredential("user", "pass"))
            )
        }

        req.requestHandled shouldBe true
        req.response.status() shouldEqual HttpStatusCode.OK
        req.response.content.shouldNotBeNullOrBlank().length shouldBeGreaterThan 6
    }

    @Test fun `request without token should fail`() = withServer {
        val req = handleRequest {
            uri = "/secret"
        }
        req.requestHandled shouldBe true
        req.response.status() shouldEqual HttpStatusCode.Unauthorized
    }

    @Test fun `request with token should pass`() = withServer {
        val req = handleRequest {
            uri = "/secret"
            addJwtHeader()
        }
        req.requestHandled shouldBe true
        req.response.let {
            it.status() shouldEqual HttpStatusCode.OK
            it.content.shouldNotBeNullOrBlank()
        }
    }

    @Test fun `optional route should work without token`() = withServer {
        val req = handleRequest {
            uri = "/optional"
        }
        req.let {
            it.requestHandled.shouldBeTrue()
            it.response.status() shouldEqual HttpStatusCode.OK
            it.response.content.shouldNotBeNullOrBlank() shouldBeEqualTo "optional"
        }
    }

    @Test fun `optional route should work with token`() = withServer {
        val req = handleRequest {
            uri = "/optional"
            addJwtHeader()
        }
        req.let {
            it.requestHandled.shouldBeTrue()
            it.response.status() shouldEqual HttpStatusCode.OK
            it.response.content.shouldNotBeNullOrBlank() shouldBeEqualTo "authenticated!"
        }
    }

    private fun TestApplicationRequest.addJwtHeader() = addHeader("Authorization", "Bearer ${getToken()}")

    private fun getToken() = JwtConfig.makeToken(testUser)

    private fun withServer(block: TestApplicationEngine.() -> Unit) {
        withTestApplication({ module() }, block)
    }

    @Test
    fun testGetAllDog() {
        withTestApplication({ module() }) {
            handleRequest(HttpMethod.Get,"/dogs").apply {
                assertEquals(
                    HttpStatusCode.NotFound,
                    response.status()
                )
            }
        }
    }

    @Test
    fun testGetSpecificDog() {
        withTestApplication({ module() }) {
            handleRequest(HttpMethod.Delete,"/dogs").apply {
                assertEquals(
                    HttpStatusCode.NotFound,
                    response.status()
                )
            }
        }
    }

    @Test
    fun testValidateManually() {
        initMainNode()
        mainNode = false
        assertEquals(false, Logic.generateGenesis()) // only main node can generate genesis
        mainNode = true
        assertEquals(true, Logic.generateGenesis())
        val genesis = Logic.Logic[0]
        assertEquals("000000", genesis.hash.takeLast(6))
        assertEquals("Genesis", genesis.data)
    }

    @Test
    fun testBlockInsertedNotification() {
        initMainNode()
        runBlocking {
            LogicController.generateNewBlock()
        }
        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel("""1"""),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val client = LogicRepo(HttpClient(mockEngine) {
            install(ContentNegotiation) {
                gson()
            }
        })

        runBlocking {
            assertEquals(
                true,
                client.notifyBlockInserted(Logic.Logic.last())
            )
        }
    }

    @Test
    fun testBlockInsertedNotificationFailed() {
        initMainNode()
        Logic.generateGenesis()
        runBlocking {
            LogicController.generateNewBlock()
        }
        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel("""0"""),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val client = LogicRepo(HttpClient(mockEngine) {
            install(ContentNegotiation) {
                gson()
            }
        })

        runBlocking {
            assertEquals(
                false,
                client.notifyBlockInserted(Logic.Logic.last(), BLOCK_INSERTED_ROUTING + node1Port)
            )
        }
    }

    @Test
    fun testAskThirdNode() {
        initMainNode()
        Logic.generateGenesis()
        runBlocking {
            LogicController.generateNewBlock()
        }
        val mockEngine = MockEngine { request ->
            GsonBuilder().create().toJson(Logic.Logic.last())
            respond(
                content = ByteReadChannel(GsonBuilder().create().toJson(Logic.Logic.last())),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val client = LogicRepo(HttpClient(mockEngine) {
            install(ContentNegotiation) {
                gson()
            }
        })

        runBlocking {
            assertEquals(
                Logic.Logic.last(),
                client.askThirdNode(BLOCK_INSERTED_ROUTING + node2Port)
            )
        }
    }

    @Test
    fun testSendLastBlock() {
        initMainNode()
        Logic.generateGenesis()
        runBlocking {
            LogicController.generateNewBlock()
        }
        assertEquals(Logic.Logic.last(), LogicRepo.get().sendLastBlock())
    }

    @Test
    fun testValidateLogic() {
        initMainNode()
        Logic.generateGenesis()
        runBlocking {
            LogicController.generateNewBlock()
        }
        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel(GsonBuilder().create().toJson(Logic.Logic)),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val client = LogicRepo(HttpClient(mockEngine) {
            install(ContentNegotiation) {
                gson()
            }
        })

        runBlocking {
            assertEquals(
                Logic.Logic,
                client.validateLogic(BLOCK_INSERTED_ROUTING + node1Port)
            )
        }
    }

    @Test
    fun testValidateLogicFailed() {
        initMainNode()
        Logic.generateGenesis()
        runBlocking {
            LogicController.generateNewBlock()
        }
        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel(GsonBuilder().create().toJson(listOf(Logic.Logic.first()))),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val client = LogicRepo(HttpClient(mockEngine) {
            install(ContentNegotiation) {
                gson()
            }
        })

        runBlocking {
            assertNotEquals(
                Logic.Logic,
                client.validateLogic(BLOCK_INSERTED_ROUTING + node1Port)
            )
        }
    }

    @Test
    fun testInsertBlock() {
        initMainNode()
        Logic.generateGenesis()
        val blockToInsert: Block?
        runBlocking {
            blockToInsert = LogicController.generateNewBlock()
        }
        Logic.Logic.dropLast(1) // drop generated block to check if it is inserted correctly
        if (blockToInsert != null) {
            LogicRepo.get().insertBlock(blockToInsert)
        }
        assertEquals(blockToInsert, Logic.Logic.last())
    }

    @Test
    fun testHandleReceivedData() {
        initMainNode()
        runBlocking {
            LogicController.generateNewData()
        }
        val receivedData = Data(
            Logic.Logic.size,
            Logic.Logic.last().hash,
            sha256Hash("Received data").dropLast(6).plus("000000"),
            "receivedBlock", 0)

        runBlocking {
            LogicController.handleReceivedData(receivedBlock, node1Port)
        }
        assertEquals(receivedData, Logic.Logic.last())
    }

    private fun initMainNode() {
        Logic.Logic.clear()
        currentNodePort = "8080"
        node1Port = "8081"
        node2Port = "8082"
        mainNode = true
    }
}
