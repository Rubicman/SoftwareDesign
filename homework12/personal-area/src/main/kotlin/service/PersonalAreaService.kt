package service

import domain.Stocks
import domain.User
import domain.data.AddUserData
import domain.data.BuyOrSellData
import domain.data.ChangeBalanceData

interface PersonalAreaService {
    fun addUser(data: AddUserData): String
    fun changeBalance(userId: String, data: ChangeBalanceData)
    fun getUser(userId: String): User?
    fun getUserStocks(userId: String): List<Stocks>
    fun getUserSummaryBalance(userId: String): Double
    fun buyStocks(userId: String, data: BuyOrSellData)
    fun sellStocks(userId: String, data: BuyOrSellData)
}