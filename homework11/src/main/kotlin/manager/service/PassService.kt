package manager.service

import common.domain.Pass
import common.domain.event.Event
import manager.domain.data.CreatePassData
import manager.domain.data.RenewPassData

interface PassService{
    fun getEvents(passId: String): List<Event>
    fun createPass(data: CreatePassData): Pass
    fun renew(passId: String, data: RenewPassData): Pass
}