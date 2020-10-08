import io.gatling.commons.util.TypeHelper.TypeValidator
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import org.graalvm.compiler.hotspot.nodes.MonitorCounterNode.counter
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

import scala.concurrent.duration._
import scala.math.random
import scala.util.Random


class PerformanceTest extends Simulation {

  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl("http://192.168.99.100:31722")

  val scn: ScenarioBuilder = scenario("PerformanceSimulation")
    .repeat(1, "count")
    {
      exec(
        http("POST /api/boeken")
          .post("/api/boeken")
          .body( StringBody( """{ "titel": "ik ben een geupdate titel", "uitgever": "Ik ben de geupdate uitgever" }""")).asJson
          .check(jsonPath("$.id").saveAs("BookId"))
      )
    }

    .repeat(1 ) {
      exec(
        http("GET /api/boeken")
          .get("/api/boeken")
      )
    }
    .repeat(1){
      exec(
        http("GET /api/boeken/id")
          .get("/api/boeken/${BookId}")
          .check(jsonPath("$.titel").is("ik ben een geupdate titel"))
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
        http("PUT /api/boeken/id")
          .put("/api/boeken/${BookId}")
          .body( StringBody( """{ "titel": "ik ben een geupdate titellll", "uitgever": "Ik ben de geupdate uitgeverrrr" }""")).asJson
      )
    }

    .repeat(1){
      exec(
        http("DELETE /api/boeken/id")
          .delete("/api/boeken/${BookId}")
      )
    }


  setUp(
    scn.inject(
      nothingFor(4 seconds), // 1
      //atOnceUsers(500), // 2
      rampUsers(1000) during (60 seconds), // 3
      //constantUsersPerSec(10) during (15 seconds), // 4
      //constantUsersPerSec(20) during (15 seconds) randomized, // 5
    ).protocols(httpProtocol))
}