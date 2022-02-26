package handlers

import com.fasterxml.jackson.databind.ObjectMapper
import domain.data.UserRegistrationData
import rx.Observable
import service.UserService

class UserRegistrationHandler(
    private val userService: UserService,
    private val mapper: ObjectMapper,
) : AbstractHandler() {
    override fun handle(json: String): Observable<String> =
        Observable
            .just(json)
            .map { mapper.readValue(it, UserRegistrationData::class.java) }
            .flatMap { userService.registration(it) }
            .map { it.toViewDto() }
            .map { mapper.writeValueAsString(it) }

}