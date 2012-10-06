package controllers

import controllers.base.MyController
import views.html.comp.gimage

object Images extends MyController {

    def xmlHierarchy(h: Int = 700) =
        gimage("1HeZy0FkgF5KooDV6SgWc96GU-HpRaacFFriO_soySIo", height = h)

    def tools1(h: Int = 700) =
        gimage("1X-TUMbxK4vkC96NLMAQsqPn51lr5RL_ZGQn4_RGtpTo", height = h)

    def tools2(h: Int = 700) =
        gimage("1eoWjkxbFpM_9jvHgcER4S40PoJqrWGGvyg4FOCKxaJg", height = h)

    def webTools(h: Int = 700) =
        gimage("1gNRfFTYbRkbpnrH2be0k2II2AWW0Y_d9kcJOaRL4pak", height = h)

    def types(p: Int = 0, h: Int = 700) =
        p match {
            case 0 => gimage("1rohDVf9bLpnQOdnrA-ODUQXHt1BWXUFvvP70bkyLlsU", height = h)
            case 1 => gimage("19k3tuSEN-J5VTHvJJb-3ObAfj8P57cAkhBKIEgJJ7WI", height = h)
            case 2 => gimage("1BUr9qK8XaZ_GbMztJqmY7iOmuepP1qoCas7brZ0XJWA", height = h)
            case 3 => gimage("1DT7OetZ3WkNsUa-rGlJwbBdbPOGXuzdmK1rks6DCqBc", height = h)
            case 4 => gimage("1TTuoy1DRDc5o7DcBu8zuK4vXQUXawnaAzLfP29kMR-E", height = h)
        }

    def testTools(h: Int = 700) =
        gimage("1Mk7YZ3L1zkpevoWf5MPe6GKMdeFaRJ78nbtElhQQMVc", height = h)
}
