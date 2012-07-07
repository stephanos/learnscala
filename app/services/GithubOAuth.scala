package services

import play.api.libs.ws.WS

// see http://developer.github.com/v3/oauth/
object GithubOAuth {

    private val clientId =
        "72acdf509f713a5ce711"

    private val clientSecret =
        "9ee135c0099cb20c465a14d9f088d0d0342ec978"


    def getAuthorizeUrl =
        "https://github.com/login/oauth/authorize?client_id=%s" // &scope=%s
            //.format(clientId, "user") // TODO: user,public_repo

    def getAccessTokenEndpoint(code: String) =
        WS.url("https://github.com/login/oauth/access_token")
            //.withHeaders(("Accept", "application/json"))
            .post(Map("client_id" -> Seq(clientId), "client_secret" -> Seq(clientSecret), "code" -> Seq(code)))
}
