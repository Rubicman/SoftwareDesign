package repository.impl

import domain.User
import repository.UserRepository
import java.util.concurrent.atomic.AtomicLong

class InMemoryUserRepository: UserRepository {
    private val nextId = AtomicLong(1)
    private val memory = mutableMapOf<String, User>()

    override fun get(id: String): User? = memory[id]

    override fun create(user: User): User {
        val id = nextId.getAndIncrement().toString()
        return user.copy(id = id).also {
            memory[id] = it
        }
    }

    override fun save(user: User) {
        memory[user.id] = user
    }
}