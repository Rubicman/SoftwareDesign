package pass.service

interface EnterService {
    fun enter(passId: String): Boolean
    fun exit(passId: String): Boolean
}