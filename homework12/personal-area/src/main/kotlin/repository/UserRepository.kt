package repository

import domain.User

interface UserRepository {
    fun get(id: String): User?
    fun create(user: User): User
    fun save(user: User)
}
