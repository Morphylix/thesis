package ru.petkeeper.database.orders_table

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.`java-time`.datetime
import org.jetbrains.exposed.sql.transactions.transaction
import ru.petkeeper.database.pets_table.PetsTable
import ru.petkeeper.database.users_table.UsersTable
import ru.petkeeper.routes.load_orders.SettingsConfig
import ru.petkeeper.routes.load_orders.SortType
import ru.petkeeper.util.converters.convertDateTimeToDate
import ru.petkeeper.util.converters.convertDateToLocalDateTime

object OrdersTable : Table("orders") {

    var idCounter = 0
    val orderId = OrdersTable.integer("orderId")
    private val petId = OrdersTable.integer("petId").references(PetsTable.petId)
    private val createDate = OrdersTable.datetime("createDate")
    private var isCancelled = OrdersTable.bool("isCancelled")
    private val recs = OrdersTable.text("recs")
    private val about = OrdersTable.text("about")
    private val userEmail = OrdersTable.varchar("userEmail", 50).references(UsersTable.email)
    private val isTemporary = OrdersTable.bool("isTemporary")
    private val startDate = OrdersTable.datetime("startDate").nullable()
    private val endDate = OrdersTable.datetime("endDate").nullable()

    fun insertOrder(orderDto: OrderDto): OrderDto {
        transaction {
            println("inserting order with is cancelled = ${orderDto.isCancelled}")
            val newId = OrdersTable.selectAll().count().toInt()
            idCounter = newId
            OrdersTable.insert {
                it[orderId] = newId
                it[petId] = orderDto.pet?.id ?: -1
                it[createDate] = convertDateToLocalDateTime(orderDto.createDate)
                it[isCancelled] = orderDto.isCancelled
                it[recs] = orderDto.recs
                it[about] = orderDto.about
                it[userEmail] = orderDto.userEmail
                it[isTemporary] = orderDto.isTemporary
                it[startDate] = if (orderDto.startDate != null) convertDateToLocalDateTime(orderDto.startDate) else null
                it[endDate] = if (orderDto.endDate != null) convertDateToLocalDateTime(orderDto.endDate) else null
            }
        }
        return fetchOrder(idCounter)!!
    }

    fun fetchOrder(id: Int): OrderDto? {
        return try {
            transaction {
                val orderModel = OrdersTable.select {
                    orderId.eq(id)
                }.single()
                val petId = orderModel[petId]
                val pet = PetsTable.fetchPet(petId)
                OrderDto(
                    orderId = id,
                    pet = pet,
                    createDate = convertDateTimeToDate(orderModel[createDate]),
                    isCancelled = orderModel[isCancelled],
                    recs = orderModel[recs],
                    about = orderModel[about],
                    userEmail = orderModel[userEmail],
                    isTemporary = orderModel[isTemporary],
                    startDate = if (orderModel[startDate] != null) convertDateTimeToDate(orderModel[startDate]!!) else null,
                    endDate = if (orderModel[endDate] != null) convertDateTimeToDate(orderModel[endDate]!!) else null
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    fun cancelOrder(orderId: Int) {
        try {
            transaction {

                println("inside cancelOrder fun")
                val orderModel = OrdersTable.select {
                    OrdersTable.orderId.eq(orderId)
                }.single()
                println("after orderModel")
                val petId = orderModel[petId]
                val pet = PetsTable.fetchPet(petId)
                val orderDto = OrderDto(
                    orderId = orderId,
                    pet = pet,
                    createDate = convertDateTimeToDate(orderModel[createDate]),
                    isCancelled = true,
                    recs = orderModel[recs],
                    about = orderModel[about],
                    userEmail = orderModel[userEmail],
                    isTemporary = orderModel[isTemporary],
                    startDate = if (orderModel[startDate] != null) convertDateTimeToDate(orderModel[startDate]!!) else null,
                    endDate = if (orderModel[endDate] != null) convertDateTimeToDate(orderModel[endDate]!!) else null
                )
                println("before deleting")
                OrdersTable.deleteWhere { OrdersTable.orderId.eq(orderId) }
                OrdersTable.insertOrder(orderDto)
            }
        } catch (e: Exception) {
            println("exception! $e")
        }
    }

    fun fetchAllOrders(settingsConfig: SettingsConfig): List<OrderDto> {
        return try {
            var resList = mutableListOf<OrderDto>()
            transaction {
                val orderModelList = OrdersTable.selectAll()
                for (orderModel in orderModelList) {
                    val petId = orderModel[petId]
                    val pet = PetsTable.fetchPet(petId)
                    resList.add(
                        OrderDto(
                            orderId = orderModel[orderId],
                            pet = pet,
                            createDate = convertDateTimeToDate(orderModel[createDate]),
                            isCancelled = orderModel[isCancelled],
                            recs = orderModel[recs],
                            about = orderModel[about],
                            userEmail = orderModel[userEmail],
                            isTemporary = orderModel[isTemporary],
                            startDate = if (orderModel[startDate] != null) convertDateTimeToDate(orderModel[startDate]!!) else null,
                            endDate = if (orderModel[endDate] != null) convertDateTimeToDate(orderModel[endDate]!!) else null
                        )
                    )
                }
            }
            when (settingsConfig.sortBy) {
                SortType.CREATED_DATE_NEWEST -> {
                    println("newest")
                    resList = resList.filter { !it.isCancelled }.sortedBy { it.createDate }.toMutableList()
                }
                SortType.CREATED_DATE_OLDEST -> {
                    println("oldest")
                    resList = resList.filter { !it.isCancelled }.sortedBy { it.createDate }.reversed().toMutableList()
                }
                SortType.USER_RATING_ASC -> {
                    resList = resList.filter { !it.isCancelled }.toMutableList()
                }
                SortType.USER_RATING_DESC -> {
                    resList = resList.filter { !it.isCancelled }.toMutableList()
                }
                else -> {
                    resList = resList.filter { !it.isCancelled }.toMutableList()
                }
            }
            if (settingsConfig.filterBy?.petType != null) {
                println("resList size before filter is ${resList.size}")
                resList = resList.filter { it.pet?.type == settingsConfig.filterBy.petType }.toMutableList()
                println("resList size after filter is ${resList.size}")
            }
            if (settingsConfig.filterBy?.petBreed != null) {
                resList = resList.filter { it.pet?.breed == settingsConfig.filterBy.petBreed }.toMutableList()
            }
            resList
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun fetchUserOrders(email: String): List<OrderDto> {
        return try {
            val resList = mutableListOf<OrderDto>()
            transaction {
                val orderModelList = OrdersTable.select {
                    userEmail.eq(email)
                }.toList()
                for (orderModel in orderModelList) {
                    val petId = orderModel[petId]
                    val pet = PetsTable.fetchPet(petId)
                    resList.add(
                        OrderDto(
                            orderId = orderModel[orderId],
                            pet = pet,
                            createDate = convertDateTimeToDate(orderModel[createDate]),
                            isCancelled = orderModel[isCancelled],
                            recs = orderModel[recs],
                            about = orderModel[about],
                            userEmail = orderModel[userEmail],
                            isTemporary = orderModel[isTemporary],
                            startDate = if (orderModel[startDate] != null) convertDateTimeToDate(orderModel[startDate]!!) else null,
                            endDate = if (orderModel[endDate] != null) convertDateTimeToDate(orderModel[endDate]!!) else null
                        )
                    )
                }
            }
            resList
        } catch (e: Exception) {
            emptyList()
        }
    }

}