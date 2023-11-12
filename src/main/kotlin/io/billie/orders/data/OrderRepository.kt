package io.billie.orders.data

import io.billie.orders.domain.MerchantId
import io.billie.orders.domain.Order
import io.billie.orders.domain.OrderId
import io.billie.orders.domain.Shipment
import org.springframework.stereotype.Repository

@Repository
class OrderRepository {
    fun findOrdersByMerchant(merchantId: MerchantId): List<Order> {
        TODO("Not yet implemented")
    }

    fun saveNewOrder(order: Order): Int {
        val sql = "insert into order (...) value (...) on conflict do nothing"
        return 1
    }

    fun findOrderById(orderId: OrderId): Order? {
        TODO("Not yet implemented")
    }

    fun saveShipment(shipment: Shipment) {
        TODO("Not yet implemented")
    }
}