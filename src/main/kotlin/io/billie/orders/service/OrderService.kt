package io.billie.orders.service

import io.billie.orders.data.OrderRepository
import io.billie.orders.domain.*
import io.billie.orders.gateways.InvoiceGateway
import io.billie.orders.gateways.PaymentGateway
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService(
    private val repository: OrderRepository,
    val paymentGateway: PaymentGateway,
    val invoiceGateway: InvoiceGateway
) {

    fun findOrdersByMerchant(merchantId: MerchantId): List<Order> = repository.findOrdersByMerchant(merchantId)

    @Transactional
    fun createOrder(orderId: OrderId, merchantId: MerchantId, amount: Int, invoiceDetails: InvoiceDetails): Order {
        var order = Order(orderId, merchantId, amount, invoiceDetails)

        val inserted = repository.saveNewOrder(order)
        if (inserted == 0) {
            order = repository.findOrderById(orderId)!!
        }

        return order
    }

    @Transactional
    fun addShipment(orderId: OrderId, shipmentId: ShipmentId, shipmentAmount: Int): Shipment {

        val order = repository.findOrderById(orderId) ?: throw OrderNotFoundException()

        if (order.status == OrderStatus.Closed) {
            throw OrderStateException("Can't modify closed order")
        }

        val shipment = Shipment(shipmentId, orderId, shipmentAmount)

        order.addShipment(shipment)

        repository.saveShipment(shipment)

        paymentGateway.payShipment(order.merchantId, shipment)

        if (order.status == OrderStatus.Closed) {
            invoiceGateway.sendInvoice(order)
        }

        return shipment
    }
}