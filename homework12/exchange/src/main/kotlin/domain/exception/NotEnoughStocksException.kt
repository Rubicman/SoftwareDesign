package domain.exception

import domain.Company

class NotEnoughStocksException(company: Company, wants: Int) : RuntimeException(
    "Couldn't buy $wants stock of '${company.name}' because only ${company.stocks} remain"
)