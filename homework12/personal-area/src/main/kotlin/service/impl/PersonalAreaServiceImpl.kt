package service.impl

import client.ExchangeClient
import domain.Stocks
import domain.User
import domain.data.AddUserData
import domain.data.BuyOrSellData
import domain.data.ChangeBalanceData
import domain.exception.NoSuchUserException
import domain.exception.NotEnoughAmountOnBalance
import repository.UserRepository
import service.PersonalAreaService

class PersonalAreaServiceImpl(
    private val userRepository: UserRepository,
    private val exchangeClient: ExchangeClient,
) : PersonalAreaService {
    override fun addUser(data: AddUserData): String =
        userRepository.create(User(
            name = data.name,
            balance = data.startBalance
        )).id

    override fun changeBalance(userId: String, data: ChangeBalanceData) {
        val user = user(userId)
        if (user.balance < data.amount) throw NotEnoughAmountOnBalance()
        user.balance += data.amount
        userRepository.save(user)
    }

    override fun getUser(userId: String): User? =
        userRepository.get(userId)

    override fun getUserStocks(userId: String): List<Stocks> {
        val stocks = user(userId).stocks
        return stocks.mapNotNull { (companyId, stocksCount) ->
            exchangeClient.getInfo(companyId)?.let { company ->
                Stocks(company.name, company.id, stocksCount, company.price)
            }
        }
    }

    override fun getUserSummaryBalance(userId: String): Double {
        val user = user(userId)
        return user.balance + user.stocks.map { (companyId, stocksCount) ->
            (exchangeClient.getInfo(companyId)?.price ?: 0.0) * stocksCount
        }.sum()
    }

    override fun buyStocks(userId: String, data: BuyOrSellData) =
        buyOrSell(userId, data.companyId, data.amount)

    override fun sellStocks(userId: String, data: BuyOrSellData) =
        buyOrSell(userId, data.companyId, -data.amount)

    private fun buyOrSell(userId: String, companyId: String, amount: Int) {
        val user = user(userId)
        val company =
            exchangeClient.getInfo(companyId) ?: throw IllegalArgumentException("No company with id '$companyId'")
        if (user.stocks.getOrDefault(companyId, 0) < -amount || user.balance < amount * company.price)
            throw NotEnoughAmountOnBalance()
        exchangeClient.changeStocks(companyId, -amount)
        user.stocks[companyId] = user.stocks.getOrDefault(companyId, 0) + amount
        user.balance -= amount * company.price
        userRepository.save(user)
    }

    private fun user(userId: String) =
        userRepository.get(userId) ?: throw NoSuchUserException(userId)
}