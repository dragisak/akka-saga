package dragisa.akka.saga

import akka.remote.testkit.{MultiNodeSpec, MultiNodeConfig}
import akka.testkit._
import akka.actor._
import org.scalatest._

class PingPongActorSpecMultiJvmNode1 extends MultiNodeSample

class PingPongActorSpecMultiJvmNode2 extends MultiNodeSample

class PingPongActorSpecMultiJvmNode3 extends MultiNodeSample


object PingPongActorSpecConfig extends MultiNodeConfig {
  val node1 = role("node1")
  val node2 = role("node2")
  val node3 = role("node3")
}


class MultiNodeSample extends MultiNodeSpec(PingPongActorSpecConfig)
with SagaMultiNodeSpec with Matchers with ImplicitSender with DefaultTimeout  {

  import PingPongActorSpecConfig._


  "A MultiNodeSample" should {

    "wait for all nodes to enter a barrier" in {
      enterBarrier("startup")
    }

    "send to and receive from a remote node" in {

      runOn(node1) {
        enterBarrier("deployed")
        val pingPong2 = system.actorSelection(node(node2) / "user" / "pingPong")
        val pingPong3 = system.actorSelection(node(node3) / "user" / "pingPong")
        pingPong2 ! "node2"
        pingPong3 ! "node3"
        expectMsgAllOf("Pong node2", "Pong node3")
      }

      runOn(node2) {
        system.actorOf(Props[PingPongActor], "pingPong")
        enterBarrier("deployed")
      }

      runOn(node3) {
        system.actorOf(Props[PingPongActor], "pingPong")
        enterBarrier("deployed")
      }

      enterBarrier("finished")
    }
  }
}
