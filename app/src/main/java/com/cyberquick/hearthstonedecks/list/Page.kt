package com.cyberquick.hearthstonedecks.list

class Page(
    var pageNumber : Int,
    var isPreviousPageExists : Boolean,
    var isNextPageExists : Boolean,
    var linkOnPage : String,
    var linkOnPreviousPage : String,
    var linkOnNextPage : String
)