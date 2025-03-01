package ru.download.prepodavatel_online

data class ProfileData (
    var name: String? = null,
    var lastname: String? = null,
    var avaUrl: String? = null,
    var numOfVoters: Int? = null,
    var sumOfVoters: Int? = null,
    var vkId: String? = null,
    var tgId: String? = null,
    var subject: String? = null,
    var rating: Int? = null,
    var about: String? = null,
    var age: String? = null,
    var exp: String? = null
    )