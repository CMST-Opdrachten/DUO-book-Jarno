package pivotal

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class PerformanceSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl("http://localhost:8080/api")

  val scn = scenario("PerformanceSimulation")
    .repeat(10) {
      exec(http("GET /boeken/1").get("/boeken/1"))
    }

  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpProtocol)
}