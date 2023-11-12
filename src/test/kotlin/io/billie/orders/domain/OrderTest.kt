package io.billie.orders.domain

import io.billie.functional.data.Fixtures
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

class OrderTest {

    @Test
    fun shipmentAmountCantExceedOpenAmount() {
        val order = Fixtures.createOrder()
        order.addShipment(Shipment(UUID.randomUUID(), order.id, 500))

        assertEquals(500, order.openAmount)

        val exception: Exception = assertThrows(ShipmentException::class.java) {
            order.addShipment(Shipment(UUID.randomUUID(), order.id, 1000))
        }

        assertTrue(exception.message!!.contains("Shipment amount exceeds open order amount"))
    }
}