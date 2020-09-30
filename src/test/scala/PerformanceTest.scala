import io.gatling.commons.util.TypeHelper.TypeValidator
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import org.graalvm.compiler.hotspot.nodes.MonitorCounterNode.counter

import scala.concurrent.duration._
import scala.math.random
import scala.util.Random

class PerformanceTest extends Simulation {

  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl("http://localhost:8080")

  val scn: ScenarioBuilder = scenario("PerformanceSimulation")
    .repeat(10, "count")
    {
      exec(
        http("POST /api/boeken")
          .post("/api/boeken")
          .body( StringBody( """{ "titel": "ik ben een geupdate titel${count}", "uitgever": "Ik ben de geupdate uitgever" }""")).asJson
      )
    }
    .repeat(1 ) {
      exec(
        http("GET /api/boeken")
          .get("/api/boeken")
      )
    }
    .repeat(2, "count"){
      exec(
        http("GET /api/boeken/")
          .get("/api/boeken/${count}")
      )
    }
    .repeat(1){
      exec(
        http("GET /getBookPdf")
          .get("/getBookPdf")
      )
    }
    .repeat(1){
      exec(
        http("POST /api/boeken")
          .post("/api/boeken")
          .body( StringBody( """{ "titel": "ik ben een geupdate titel", "uitgever": "Ik ben de geupdate uitgever" }""")).asJson
      )
    }
    .pause(5 seconds)
    .repeat(1){
      exec(
        http("DELETE /api/boeken/11")
          .delete("/api/boeken/11")
      )
    }
    .repeat(1){
      exec(
        http("PUT /api/boeken/4")
          .put("/api/boeken/4")
          .body( StringBody( """{ "titel": "ik ben een geupdate titellll", "uitgever": "Ik ben de geupdate uitgeverrrr" }""")).asJson
      )
    }


  setUp(
    scn.inject(

      nothingFor(4 seconds), // 1
      atOnceUsers(10), // 2
      //rampUsers(10) during (5 seconds), // 3
      //constantUsersPerSec(20) during (15 seconds), // 4
      //constantUsersPerSec(20) during (15 seconds) randomized, // 5
    ).protocols(httpProtocol))
}