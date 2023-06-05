package com.morphylix.android.petkeeper.data

import android.util.Log
import com.morphylix.android.petkeeper.data.api.PetKeeperApi
import com.morphylix.android.petkeeper.data.shared_pref.SharedPrefTokenStorage
import com.morphylix.android.petkeeper.domain.PetKeeperRepository
import com.morphylix.android.petkeeper.domain.model.domain.*
import com.morphylix.android.petkeeper.domain.model.domain.settings.SettingsConfig
import com.morphylix.android.petkeeper.domain.model.network.NetworkMappers
import com.morphylix.android.petkeeper.domain.model.network.auto_login.UserAutoLoginDto
import com.morphylix.android.petkeeper.domain.model.network.auto_login.UserAutoLoginDtoReceive
import com.morphylix.android.petkeeper.domain.model.network.load_user_info.UserInfoDto
import com.morphylix.android.petkeeper.domain.model.network.login.UserLoginDto
import com.morphylix.android.petkeeper.domain.model.network.login.UserLoginDtoReceive
import com.morphylix.android.petkeeper.domain.model.network.order.LoadUserOrdersDto
import com.morphylix.android.petkeeper.domain.model.network.order.OrderNetworkEntity
import com.morphylix.android.petkeeper.domain.model.network.register.UserRegisterDto
import com.morphylix.android.petkeeper.domain.model.network.register.UserRegisterDtoReceive
import com.morphylix.android.petkeeper.domain.model.network.submit_validation.UserSubmitValidationDto
import com.morphylix.android.petkeeper.domain.model.network.submit_validation.UserSubmitValidationDtoReceive
import com.morphylix.android.petkeeper.domain.model.network.validate_email.UserValidateEmailDto
import com.morphylix.android.petkeeper.domain.model.network.validate_email.UserValidateEmailDtoReceive
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.Multipart
import java.io.InputStream
import javax.inject.Inject

private const val TAG = "PetKeeperRepositoryImpl"

class PetKeeperRepositoryImpl
@Inject constructor(
    private val petKeeperApi: PetKeeperApi,
    private val sharedPrefTokenStorage: SharedPrefTokenStorage,
    private val networkMappers: NetworkMappers
) : PetKeeperRepository {

    override suspend fun loginAndFetchToken(userLoginDto: UserLoginDto): UserLoginDtoReceive {
        return petKeeperApi.loginAndFetchToken(userLoginDto)
    }

    override suspend fun registerAndFetchToken(userRegisterDto: UserRegisterDto): UserRegisterDtoReceive {
        return petKeeperApi.registerAndFetchToken(userRegisterDto)
    }

    override suspend fun autoLoginWithToken(userAutoLoginDto: UserAutoLoginDto): UserAutoLoginDtoReceive {
        return petKeeperApi.autoLogin(userAutoLoginDto)
    }

    override fun getCurrentToken(): String? {
        return sharedPrefTokenStorage.loadToken()
    }

    override fun saveToken(token: String) {
        sharedPrefTokenStorage.saveToken(token)
    }

    override suspend fun getValidationCodeOnEmail(userValidateEmailDto: UserValidateEmailDto): UserValidateEmailDtoReceive {
        return petKeeperApi.getValidationCodeOnEmail(userValidateEmailDto)
    }

    override suspend fun submitValidationCode(userSubmitValidationDto: UserSubmitValidationDto): UserSubmitValidationDtoReceive {
        return petKeeperApi.submitValidationCode(userSubmitValidationDto)
    }

    override suspend fun loadUserInfo(userInfoDto: UserInfoDto): User {
        val userNetworkEntity = petKeeperApi.loadUserInfo(userInfoDto)
        return networkMappers.userNetworkMapper.mapFromEntity(userNetworkEntity)
    }

    override suspend fun postOrder(order: Order): Order {
        val orderNetworkEntity = networkMappers.orderNetworkMapper.mapToEntity(order)
        val token = sharedPrefTokenStorage.loadToken()
        val postedOrderNetworkEntity = petKeeperApi.postOrder(token!!, orderNetworkEntity)
        Log.i(TAG, "posted order is $postedOrderNetworkEntity")
        return networkMappers.orderNetworkMapper.mapFromEntity(postedOrderNetworkEntity)
    }

    override suspend fun fetchOrders(settingsConfig: SettingsConfig): List<Order> {
        val orderNetworkEntityList = petKeeperApi.fetchOrders(settingsConfig)
        //Log.i(TAG, "fetched orderList is $orderNetworkEntityList")
        Log.i(TAG, "fetched an orderList")

        val orderList = networkMappers.orderNetworkMapper.mapFromEntityList(orderNetworkEntityList)
        for (i in orderList.indices) {
            val currentOrder = orderList[i]
            currentOrder.pet = orderNetworkEntityList[i].pet
            currentOrder.user = fetchUserByEmail(orderNetworkEntityList[i].userEmail)
        }
        return orderList
    }

    override suspend fun fetchPetById(id: Int): Pet {
        Log.i(TAG, "fetched pet is ${petKeeperApi.fetchPetById(id)}")
        return networkMappers.petNetworkMapper.mapFromEntity(petKeeperApi.fetchPetById(id))
    }

    override suspend fun fetchUserByEmail(email: String): User {
        return networkMappers.userNetworkMapper.mapFromEntity(petKeeperApi.fetchUserByEmail(email))
    }

    override suspend fun uploadPetImage(inputStream: InputStream, petId: Int) {
        val part = MultipartBody.Part.createFormData(
            "pic", "myPic", inputStream.readBytes()
                .toRequestBody(
                    "/drawable/pet_pic_placeholder.png".toMediaTypeOrNull(),
                    0
                )
        )
        Log.i(TAG, "part is ${part} ${part.body}")
        petKeeperApi.uploadPetImage(
            petId,
            part
        )
    }

    override suspend fun fetchOrderById(orderId: Int): Order {
        val orderNetworkEntity = petKeeperApi.fetchOrderById(orderId)
        val currentOrder = networkMappers.orderNetworkMapper.mapFromEntity(orderNetworkEntity)
        currentOrder.pet = orderNetworkEntity.pet
        currentOrder.user = fetchUserByEmail(orderNetworkEntity.userEmail)
        return currentOrder
    }

    override suspend fun loginWithGoogleAndFetchToken(userRegisterDto: UserRegisterDto): UserLoginDtoReceive {
        return petKeeperApi.loginWithGoogleAndFetchToken(userRegisterDto)
    }

    override suspend fun fetchOrdersByEmail(loadUserOrdersDto: LoadUserOrdersDto): List<Order> {
        val orderNetworkEntityList = petKeeperApi.loadOrdersByEmail(loadUserOrdersDto)
        val orderList = networkMappers.orderNetworkMapper.mapFromEntityList(orderNetworkEntityList)
        for (i in orderList.indices) {
            val currentOrder = orderList[i]
            currentOrder.pet = orderNetworkEntityList[i].pet
            currentOrder.user = fetchUserByEmail(orderNetworkEntityList[i].userEmail)
        }
        return orderList
    }

    override suspend fun fetchOrderResponses(orderId: Int): List<Response> {
        val responseNetworkEntityList = petKeeperApi.loadResponsesByOrderId(orderId)
        return networkMappers.responseNetworkMapper.mapFromEntityList(responseNetworkEntityList)
    }

    override suspend fun postResponse(response: Response) {
        petKeeperApi.postResponse(networkMappers.responseNetworkMapper.mapToEntity(response))
    }

    override suspend fun postComment(comment: Comment) {
        petKeeperApi.postComment(networkMappers.commentNetworkMapper.mapToEntity(comment))
    }

    override suspend fun fetchUserComments(userEmail: String): List<Comment> {
        val commentsNetwork = petKeeperApi.fetchUserComments(userEmail)
        val comments = networkMappers.commentNetworkMapper.mapFromEntityList(commentsNetwork)
        comments.map { comment ->
            val user = fetchUserByEmail(comment.senderEmail)
            comment.senderName = user.name
            comment.senderSurname = user.surname
        }
        return comments
    }

    override suspend fun closeOrder(orderId: Int) {
        petKeeperApi.closeOrder(orderId)
    }

    override fun saveSettingsConfig(settingsConfig: SettingsConfig) {
        sharedPrefTokenStorage.saveSettingsConfig(settingsConfig)
    }

    override fun getSettingsConfig(): SettingsConfig {
        return sharedPrefTokenStorage.loadSettingsConfig()
    }

    override fun saveLastOrderString(order: Order) {
        sharedPrefTokenStorage.saveLastOrderString(order)
    }

    override fun getLastOrderString(): String {
        return sharedPrefTokenStorage.getLastOrderString()
    }

    override suspend fun postNotification(notification: Notification) {
        petKeeperApi.postNotification(networkMappers.notificationNetworkMapper.mapToEntity(notification))
    }

    override suspend fun fetchUserNotifications(email: String): List<Notification> {
        val notificationsNetwork = petKeeperApi.fetchUserNotifications(email)
        val notifications = networkMappers.notificationNetworkMapper.mapFromEntityList(notificationsNetwork)
        notifications.forEach {
            val user = fetchUserByEmail(it.senderEmail)
            it.senderName = user.name
            it.senderSurname = user.surname
        }
        return notifications
    }

}