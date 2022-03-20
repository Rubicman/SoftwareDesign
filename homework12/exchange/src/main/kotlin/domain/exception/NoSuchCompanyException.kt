package domain.exception

class NoSuchCompanyException(id: String): RuntimeException(
    "Couldn't find company with id '$id'"
)