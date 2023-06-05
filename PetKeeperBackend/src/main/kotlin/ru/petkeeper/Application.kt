package ru.petkeeper

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.cio.*
import org.jetbrains.exposed.sql.Database
import ru.petkeeper.routes.auto_login.configureAutoLoginRouting
import ru.petkeeper.routes.close_order.configureCloseOrderRouting
import ru.petkeeper.routes.code_validation.configureCodeValidationRouting
import ru.petkeeper.routes.create_comment.configureCreateCommentRouting
import ru.petkeeper.routes.create_order.configureCreateOrderRouting
import ru.petkeeper.routes.create_response.configureCreateResponseRouting
import ru.petkeeper.routes.login.configureLoginRouting
import ru.petkeeper.routes.register.configureRegisterRouting
import ru.petkeeper.routes.email_validation.configureEmailValidationRouting
import ru.petkeeper.routes.fetch_user_notifications.configureFetchUserNotificationsRouting
import ru.petkeeper.routes.load_comments_by_email.configureLoadCommentsByEmailRouting
import ru.petkeeper.routes.load_order_by_id.configureLoadOrderByIdRouting
import ru.petkeeper.routes.load_orders.configureLoadOrdersRouting
import ru.petkeeper.routes.load_pet_by_id.configureLoadPetByIdRouting
import ru.petkeeper.routes.load_responses.configureLoadResponsesByOrderIdRouting
import ru.petkeeper.routes.load_user_by_email.configureLoadUserByEmailRouting
import ru.petkeeper.routes.load_user_info.configureLoadUserInfoRouting
import ru.petkeeper.routes.load_user_orders.configureLoadUserOrdersRouting
import ru.petkeeper.routes.login_with_google.configureLoginWithGoogleRouting
import ru.petkeeper.routes.post_notification.configurePostNotificationRouting
import ru.petkeeper.routes.post_pet_image.configurePostPetImageRouting
import ru.petkeeper.plugins.*

fun main() {

    Database.connect("jdbc:postgresql://localhost:5432/petkeeper",
        "org.postgresql.Driver",
        "postgres",
        "1337")

    embeddedServer(CIO, port = 8080, host = "127.0.0.1", module = Application::module)
        .start(wait = true)

}

fun Application.module() {
    configureSerialization()
    configureRouting()
    configureRegisterRouting()
    configureLoginRouting()
    configureAutoLoginRouting()
    configureEmailValidationRouting()
    configureCodeValidationRouting()
    configureLoadUserInfoRouting()
    configureCreateOrderRouting()
    configureLoadOrdersRouting()
    configureLoadUserByEmailRouting()
    configureLoadPetByIdRouting()
    configurePostPetImageRouting()
    configureLoadOrderByIdRouting()
    configureLoginWithGoogleRouting()
    configureLoadUserOrdersRouting()
    configureCreateResponseRouting()
    configureLoadResponsesByOrderIdRouting()
    configureCreateCommentRouting()
    configureLoadCommentsByEmailRouting()
    configureCloseOrderRouting()
    configurePostNotificationRouting()
    configureFetchUserNotificationsRouting()

//    install(DataConversion) {
//        convert<Date> { // this: DelegatingConversionService
//            val format = SimpleDateFormat.getInstance()
//
//            decode { values -> // converter: (values: List<String>, type: Type) -> Any?
//                values.single().let { format.parse(it) }
//            }
//
//            encode { value -> // converter: (value: Any?) -> List<String>
//                when (value) {
//                    null -> listOf()
//                    is Date -> listOf(SimpleDateFormat.getInstance().format(value))
//                    else -> throw DataConversionException("Cannot convert $value as Date")
//                }
//            }
//        }
//    }
}
