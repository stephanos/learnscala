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

  def actorModel(h: Int = 650) =
    gimage("1A9ZZjzFCkYDzQtq39jlO3w-pMbNCziwVVZZ5k7yMqG4", height = h)

  def users(h: Int = 600) =
    gimage("1ZHbV8KNtKConB-jYx-Wk4KJBBb074MP_BZcp-zoxmJQ", height = h)

  def dbTools(h: Int = 700) =
    gimage("1rCk7wFprPGcJTwahZtWR70HQMdYl7km2Z8AuVfjSjcI", height = h)

  def list(h: Int = 400) =
    gimage("1ZVCCd_-oEDMptkgmVZPvCJwFPHZol-PGo7sHKkFwNfQ", height = h)

  def option(h: Int = 400) =
    gimage("1aBpqhm7Vud4C2rlNNyj6jpeFJOR1hce4YpyOFf9YH-o", height = h)

  def blocking1(h: Int = 400) =
    gimage("1T7wyV-jrqE_I3172EQMiZUm-ALwhkPSkFtXmrbdxJ4w", height = h)

  def blocking2(h: Int = 400) =
    gimage("1GOsKL-eqdxVWuYZa_05wDMSdf2UfCuhkcnRnsqbIkdM", height = h)

  def types(p: Int = 0, h: Int = 700) =
    p match {
      case 0 => gimage("1rohDVf9bLpnQOdnrA-ODUQXHt1BWXUFvvP70bkyLlsU", height = h)
      case 1 => gimage("19k3tuSEN-J5VTHvJJb-3ObAfj8P57cAkhBKIEgJJ7WI", height = h)
      case 2 => gimage("1BUr9qK8XaZ_GbMztJqmY7iOmuepP1qoCas7brZ0XJWA", height = h)
      case 3 => gimage("1DT7OetZ3WkNsUa-rGlJwbBdbPOGXuzdmK1rks6DCqBc", height = h)
      case 4 => gimage("1TTuoy1DRDc5o7DcBu8zuK4vXQUXawnaAzLfP29kMR-E", height = h)
    }

  def collectpckg(h: Int = 600) =
    gimage("1kRUpQWsq_eIhXBq3vk18o094FoN4ERSA8kZv-iG6Jb4", height = h)

  def collection(name: String, h: Int = 600) =
    gimage(name.toLowerCase match {
      case "traversable" => "1_LSD_bFqReAiIJ_Xj-Mf54MwfrjqFqvTFUcjDCIJHUA"
      case "iterable" => "18hUCwlgzHjlqSjhoZucpRy6-JNRGCfssi_ruOcsToM0"
      case "seq" => "1fuA53lFmZ9Swblb5Fk-O7X3ar0C2lt7ipPnw_t3PNDU"
      case "map" => "1Nb4FMBcvdszJTMGpGgLS0oJrcyBzcXyD0FNBPPUH70k"
      case "set" => "1MFPRX-Oim6jP3IWB4M3nHzMQrPq2kjDHj1GKm6kJRqE"
    }, height = h)

  def testTools(h: Int = 700) =
    gimage("1Mk7YZ3L1zkpevoWf5MPe6GKMdeFaRJ78nbtElhQQMVc", height = h)
}
