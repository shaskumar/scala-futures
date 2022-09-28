import akka.actor.ActorSystem
import akka.pattern.CircuitBreaker
import play.api.libs.ws.WSClientConfig
import play.api.libs.ws.ahc.{AhcWSClientConfig, StandaloneAhcWSClient}

import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success}

class CircuitBreakerClass(implicit actorSystem: ActorSystem, implicit val ec: ExecutionContextExecutor) {

  val wsClientConfig = WSClientConfig(requestTimeout = 1000.milliseconds)
  val ahcWSClientCOnfig = AhcWSClientConfig(wsClientConfig, maxConnectionLifetime = 10.seconds)
  val standAloneAhcClient = StandaloneAhcWSClient(ahcWSClientCOnfig)

  def makeCall(cb: CircuitBreaker) = {
    val headers: ListBuffer[(String, String)] = ListBuffer[(String, String)]()
    headers.addOne(s"Accept" -> s"application/json")
    headers.addOne(s"Content-Type" -> s"application/json")

    cb.withCircuitBreaker {
        val request = standAloneAhcClient
          .url(s"http://localhost:8091/info")
          .addHttpHeaders(headers.toSeq: _*)
      request.get().map(_.body)

      }
      .transform {
        case Success(value) =>
          Success(value)
        case Failure(exception) =>
          Failure(exception)
      }
  }
}

object FutureApiCall extends App {

  implicit lazy val actorSystem: ActorSystem = ActorSystem("test-scala")
  implicit val executionContext = actorSystem.dispatcher
  val circuitBreakerClass = new CircuitBreakerClass

  val cb = new CircuitBreaker(
    scheduler = actorSystem.scheduler,
    50: Int,
    callTimeout = 500.milliseconds,
    resetTimeout = 1000.milliseconds,
    maxResetTimeout = 1000.milliseconds,
    exponentialBackoffFactor = 1.0
  )

  val res = circuitBreakerClass.makeCall(cb)
  for {
    r <- res
  } yield {
    println(r)
  }
}
