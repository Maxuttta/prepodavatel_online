package ru.download.prepodavatel_online

data class CardData(
    var id: String? = null,
    var name: String? = null,
    var lastname: String? = null,
    var type: String? = null,
    var subject: String? = null,
    var title: String? = null,
    var subtitle: String? = null,
    var avaUrl: String? = null,
    var rating: Int? = null,
    var numOfVoters: Int? = null,
    var sumOfVoters: Int? = null,
    var vkId: String? = null
)
