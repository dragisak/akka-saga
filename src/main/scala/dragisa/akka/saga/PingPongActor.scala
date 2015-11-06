package dragisa.akka.saga

import akka.actor._

class PingPongActor extends Actor {

  override def receive: Receive = {
    case m: String => sender() ! s"Pong $m"
  }
}
