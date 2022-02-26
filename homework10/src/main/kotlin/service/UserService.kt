package service

import domain.User
import domain.data.UserRegistrationData
import repository.UserRepository
import rx.Observable

class UserService(
    private val userRepository: UserRepository,
) {
    fun registration(data: UserRegistrationData): Observable<User> =
        Observable
            .just(data)
            .flatMap { userRepository.save(it) }

    fun get(userId: String): Observable<User> =
        userRepository.get(userId)
}