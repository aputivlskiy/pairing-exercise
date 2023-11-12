package io.billie.orders.domain

import io.billie.organisations.viewmodel.Entity
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.*

typealias MerchantId = Entity
typealias OrderId = Entity

@Table("order")
class Order(
    @Id val id: OrderId,
    val merchantId: MerchantId,
    val amount: Int,
    val invoiceDetails: InvoiceDetails,
    shipments: List<Shipment> = emptyList()
) {
    private var shipments = shipments
        get() = shipments
    val openAmount: Int
        get() = amount - shipments.sumOf { it.amount }

    val status: OrderStatus
        get() = if (openAmount < amount) OrderStatus.Open else OrderStatus.Closed

    fun addShipment(shipment: Shipment) {
        if (status == OrderStatus.Closed) {
            throw OrderStateException("Can't modify closed order")
        }

        if (shipments.any { it.id == shipment.id }) return

        if (openAmount + shipment.amount > amount) {
            throw ShipmentException("Shipment amount exceeds open order amount")
        }

        shipments = shipments + shipment
    }
}

enum class OrderStatus {
    Open, Closed
}

data class InvoiceDetails(val name: String, val iban: String)

class OrderNotFoundException() : RuntimeException()
class OrderStateException(message: String) : RuntimeException(message)
