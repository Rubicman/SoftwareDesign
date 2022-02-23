package actor

import akka.actor.ActorRef
import akka.actor.PoisonPill
import akka.actor.Props
import akka.actor.UntypedAbstractActor
import domain.Link
import domain.SearchSystem
import domain.message.ResultMessage
import domain.message.SearchMessage
import domain.message.TaskMessage
import domain.message.TimeoutMessage
import java.time.Duration

class MasterActor : UntypedAbstractActor() {

    private val links = mutableListOf<Link>()
    private var childrenCount = SearchSystem.values().size
    private lateinit var parent: ActorRef
    private val children = mutableListOf<ActorRef>()

    override fun onReceive(message: Any?) {
        when (message) {
            is TaskMessage -> search(message.query, message.timeout)
            is ResultMessage -> addResult(message.links)
            is TimeoutMessage -> complete()
        }
    }

    private fun search(query: String, timeout: Duration) {
        parent = sender
        for (searchSystem in SearchSystem.values()) {
            val actor = context.system.actorOf(Props.create(searchSystem.actorClazz.java))
            children.add(actor)
            actor.tell(SearchMessage(query), self)
        }
        context.system.apply {
            scheduler.scheduleOnce(timeout, { self.tell(TimeoutMessage(), ActorRef.noSender()) }, dispatcher)
        }
    }

    private fun addResult(result: List<Link>) {
        links.addAll(result)
        if (--childrenCount == 0) {
            complete()
        }
    }

    private fun complete() {
        if (childrenCount >= 0) {
            parent.tell(ResultMessage(links), self)
            children.forEach { child ->
                child.tell(PoisonPill.getInstance(), self)
            }
            childrenCount = -1
        }
    }
}