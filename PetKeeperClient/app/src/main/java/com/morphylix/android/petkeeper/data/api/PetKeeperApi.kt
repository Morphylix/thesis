package com.morphylix.android.petkeeper.data.api

import com.morphylix.android.petkeeper.domain.model.domain.settings.SettingsConfig
import com.morphylix.android.petkeeper.domain.model.network.auto_login.UserAutoLoginDto
import com.morphylix.android.petkeeper.domain.model.network.auto_login.UserAutoLoginDtoReceive
import com.morphylix.android.petkeeper.domain.model.network.comment.CommentNetworkEntity
import com.morphylix.android.petkeeper.domain.model.network.comment.CommentNetworkMapper
import com.morphylix.android.petkeeper.domain.model.network.load_user_info.UserInfoDto
import com.morphylix.android.petkeeper.domain.model.network.load_user_info.UserNetworkEntity
import com.morphylix.android.petkeeper.domain.model.network.login.UserLoginDto
import com.morphylix.android.petkeeper.domain.model.network.login.UserLoginDtoReceive
import com.morphylix.android.petkeeper.domain.model.network.notification.NotificationNetworkEntity
import com.morphylix.android.petkeeper.domain.model.network.order.LoadUserOrdersDto
import com.morphylix.android.petkeeper.domain.model.network.order.OrderNetworkEntity
import com.morphylix.android.petkeeper.domain.model.network.pet.PetNetworkEntity
import com.morphylix.android.petkeeper.domain.model.network.register.UserRegisterDto
import com.morphylix.android.petkeeper.domain.model.network.register.UserRegisterDtoReceive
import com.morphylix.android.petkeeper.domain.model.network.response.ResponseNetworkEntity
import com.morphylix.android.petkeeper.domain.model.network.submit_validation.UserSubmitValidationDto
import com.morphylix.android.petkeeper.domain.model.network.submit_validation.UserSubmitValidationDtoReceive
import com.morphylix.android.petkeeper.domain.model.network.validate_email.UserValidateEmailDto
import com.morphylix.android.petkeeper.domain.model.network.validate_email.UserValidateEmailDtoReceive
import okhttp3.MultipartBody
import retrofit2.http.*

interface PetKeeperApi {

    @POST("/login")
    suspend fun loginAndFetchToken(@Body userLoginDto: UserLoginDto): UserLoginDtoReceive

    @POST("/register")
    suspend fun registerAndFetchToken(@Body userRegisterDto: UserRegisterDto): UserRegisterDtoReceive

    @POST("/auto_login")
    suspend fun autoLogin(@Body userAutoLoginDto: UserAutoLoginDto): UserAutoLoginDtoReceive

    @POST("/email_validation")
    suspend fun getValidationCodeOnEmail(@Body userValidateEmailDto: UserValidateEmailDto): UserValidateEmailDtoReceive

    @POST("/code_validation")
    suspend fun submitValidationCode(@Body userSubmitValidationDto: UserSubmitValidationDto): UserSubmitValidationDtoReceive

    @POST("/load_user_info")
    suspend fun loadUserInfo(@Body userInfoDto: UserInfoDto): UserNetworkEntity

    @POST("/create_order")
    suspend fun postOrder(@Query("token") token: String, @Body orderNetworkEntity: OrderNetworkEntity): OrderNetworkEntity

    @POST("/load_orders")
    suspend fun fetchOrders(@Body settingsConfig: SettingsConfig): List<OrderNetworkEntity>

    @GET("/load_pet_by_id")
    suspend fun fetchPetById(@Query("petId") id: Int): PetNetworkEntity

    @GET("/load_user_by_email")
    suspend fun fetchUserByEmail(@Query("email") email: String): UserNetworkEntity

    @Multipart
    @POST("/upload_pet_image")
    suspend fun uploadPetImage(
        @Query("petId") petId: Int,
        @Part image: MultipartBody.Part
    )

    @GET("/load_order_by_id")
    suspend fun fetchOrderById(
        @Query("orderId") orderId: Int
    ): OrderNetworkEntity

    @POST("/login_with_google")
    suspend fun loginWithGoogleAndFetchToken(@Body userRegisterDto: UserRegisterDto): UserLoginDtoReceive

    @POST("/load_user_orders")
    suspend fun loadOrdersByEmail(@Body loadUserOrdersDto: LoadUserOrdersDto): List<OrderNetworkEntity>

    @GET("/load_order_responses")
    suspend fun loadResponsesByOrderId(@Query("orderId") orderId: Int): List<ResponseNetworkEntity>

    @POST("/create_response")
    suspend fun postResponse(@Body responseNetworkEntity: ResponseNetworkEntity)

    @POST("/create_comment")
    suspend fun postComment(@Body commentNetworkEntity: CommentNetworkEntity)

    @GET("load_user_comments")
    suspend fun fetchUserComments(@Query("email") email: String): List<CommentNetworkEntity>

    @GET("/close_order")
    suspend fun closeOrder(@Query("orderId") orderId: Int)

    @POST("/create_notification")
    suspend fun postNotification(@Body notificationNetworkEntity: NotificationNetworkEntity)

    @GET("/load_user_notifications")
    suspend fun fetchUserNotifications(@Query("email") email: String): List<NotificationNetworkEntity>
}