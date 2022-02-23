package domain

import actor.BingActor
import actor.GoogleActor
import actor.YandexActor
import akka.actor.UntypedAbstractActor
import kotlin.reflect.KClass

enum class SearchSystem(val actorClazz: KClass<out UntypedAbstractActor>) {
    BING(BingActor::class),
    GOOGLE(GoogleActor::class),
    YANDEX(YandexActor::class)
}