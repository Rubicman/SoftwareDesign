package domain

import java.net.URI

data class Link(
    val searchSystem: SearchSystem,
    val url: URI
) {
    constructor(searchSystem: SearchSystem, url: String): this(searchSystem, URI(url))
}
