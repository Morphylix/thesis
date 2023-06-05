package com.morphylix.android.petkeeper.domain

import com.morphylix.android.petkeeper.domain.model.domain.*
import com.morphylix.android.petkeeper.domain.model.domain.settings.SettingsConfig
import com.morphylix.android.petkeeper.domain.model.network.auto_login.UserAutoLoginDto
import com.morphylix.android.petkeeper.domain.model.network.auto_login.UserAutoLoginDtoReceive
import com.morphylix.android.petkeeper.domain.model.network.load_user_info.UserInfoDto
import com.morphylix.android.petkeeper.domain.model.network.load_user_info.UserNetworkEntity
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
import java.io.InputStream

interface PetKeeperRepository {

    suspend fun loginAndFetchToken(userLoginDto: UserLoginDto): UserLoginDtoReceive

    suspend fun registerAndFetchToken(userRegisterDto: UserRegisterDto): UserRegisterDtoReceive

    suspend fun autoLoginWithToken(userAutoLoginDto: UserAutoLoginDto): UserAutoLoginDtoReceive

    fun getCurrentToken(): String?

    fun saveToken(token: String)

    suspend fun getValidationCodeOnEmail(userValidateEmailDto: UserValidateEmailDto): UserValidateEmailDtoReceive

    suspend fun submitValidationCode(userSubmitValidationDto: UserSubmitValidationDto): UserSubmitValidationDtoReceive

    suspend fun loadUserInfo(userInfoDto: UserInfoDto): User

    suspend fun postOrder(order: Order): Order

    suspend fun fetchOrders(settingsConfig: SettingsConfig): List<Order>

    suspend fun fetchPetById(id: Int): Pet

    suspend fun fetchUserByEmail(email: String): User

    suspend fun uploadPetImage(inputStream: InputStream, petId: Int)

    suspend fun fetchOrderById(orderId: Int): Order

    suspend fun loginWithGoogleAndFetchToken(userRegisterDto: UserRegisterDto): UserLoginDtoReceive

    suspend fun fetchOrdersByEmail(loadUserOrdersDto: LoadUserOrdersDto): List<Order>

    suspend fun fetchOrderResponses(orderId: Int): List<Response>

    suspend fun postResponse(response: Response)

    suspend fun postComment(comment: Comment)

    suspend fun fetchUserComments(userEmail: String): List<Comment>

    suspend fun closeOrder(orderId: Int)

    fun saveSettingsConfig(settingsConfig: SettingsConfig)

    fun getSettingsConfig(): SettingsConfig

    fun saveLastOrderString(order: Order)

    fun getLastOrderString(): String

    suspend fun postNotification(notification: Notification)

    suspend fun fetchUserNotifications(email: String): List<Notification>
}