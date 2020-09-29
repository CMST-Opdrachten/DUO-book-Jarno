package pivotal

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class PerformanceSimulation extends Simulation {

  var a = 0

  val httpProtocol = http
    .baseUrl("http://localhost:8080/api")

  val scn = scenario("PerformanceSimulation")
    .repeat(1) {
      exec(http("GET /boeken").get("/boeken"))
    }
    .repeat(1){
      for( a <- 1 to 10) {
        exec(http("GET /boeken/2").get("/boeken/" + a))
      }
    }
    .repeat(1){
      exec(http("DELETE /boeken/3").delete("/boeken/3"))
    }
    .repeat(1){
      exec(http("POST /boeken").post("/boeken").body( StringBody( """{ "titel": "ik ben een geupdate titel", "uitgever": "Ik ben de geupdate uitgever" }""")).asJson)
    }
    .repeat(1){
      exec(http("PUT /boeken/4").put("/boeken/4").body( StringBody( """{ "titel": "ik ben een geupdate titellll", "uitgever": "Ik ben de geupdate uitgeverrrr" }""")).asJson)
    }

  setUp(
    scn.inject(
      nothingFor(4 seconds), // 1
      atOnceUsers(1), // 2
      //rampUsers(10) during (5 seconds), // 3
      //constantUsersPerSec(20) during (15 seconds), // 4
      //constantUsersPerSec(20) during (15 seconds) randomized, // 5
    ).protocols(httpProtocol))


}