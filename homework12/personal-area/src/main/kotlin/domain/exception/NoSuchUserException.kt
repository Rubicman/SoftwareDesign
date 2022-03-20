package domain.exception

class NoSuchUserException(id: String): IllegalArgumentException("No user with id '$id'")