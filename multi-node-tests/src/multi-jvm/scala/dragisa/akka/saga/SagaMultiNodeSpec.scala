package dragisa.akka.saga

import akka.remote.testkit.{MultiNodeSpec, MultiNodeSpecCallbacks}
import org.scalatest._


trait SagaMultiNodeSpec extends MultiNodeSpecCallbacks
with WordSpecLike with BeforeAndAfterAll {
  self: MultiNodeSpec =>

  override def initialParticipants = roles.size

  override def beforeAll() = multiNodeSpecBeforeAll()

  override def afterAll() = multiNodeSpecAfterAll()
}
