package actor

import akka.actor.ActorRef
import akka.actor.PoisonPill
import akka.actor.Props
import akka.actor.UntypedAbstractActor
import domain.message.ResultMessage
import domain.message.SearchMessage
import domain.message.TaskMessage
import java.time.Duration

class MainActor : UntypedAbstractActor() {

    private val masterToClient = mutableMapOf<ActorRef, ActorRef>()

    companion object {
        val timeout = Duration.ofSeconds(3L)!!
    }


    override fun onReceive(message: Any?) {
        when (message) {
            is SearchMessage -> search(message.searchQuery)
            is ResultMessage -> returnResult(message)
        }
    }

    private fun search(query: String) {
        val master = context.system.actorOf(Props.create(MasterActor::class.java))
        masterToClient[master] = sender
        master.tell(TaskMessage(query, timeout), self)
    }

    private fun returnResult(message: ResultMessage) {
        val client = masterToClient[sender]!!
        sender.tell(PoisonPill.getInstance(), self)
        client.tell(message, self)
    }
}