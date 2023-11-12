package io.billie.orders.domain

import io.billie.organisations.viewmodel.Entity
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.*

typealias ShipmentId = Entity

@Table("order_shipment")
data class Shipment(@Id val id: ShipmentId, val orderId: OrderId, val amount: Int)

class ShipmentException(message: String) : RuntimeException(message)