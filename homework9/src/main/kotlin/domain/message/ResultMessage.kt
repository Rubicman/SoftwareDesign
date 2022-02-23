package domain.message

import domain.Link

data class ResultMessage(
    val links: List<Link>
)
