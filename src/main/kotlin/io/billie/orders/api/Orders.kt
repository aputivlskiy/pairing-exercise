package io.billie.orders.api

import io.billie.orders.domain.*
import io.billie.orders.service.OrderService
import io.billie.organisations.data.UnableToFindCountry
import io.billie.organisations.viewmodel.*
import org.jetbrains.annotations.NotNull
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.*
import javax.validation.Valid


@RestController
@RequestMapping("orders")
class Orders(val service: OrderService) {

    @GetMapping("/{orderId}")
    fun index(@NotNull @RequestHeader("x-merchant") merchantId: MerchantId, @NotNull @PathVariable orderId:Entity): List<Order>
        = service.findOrdersByMerchant(merchantId)

    @PostMapping()
    fun post(@NotNull @RequestHeader("x-merchant") merchantId: MerchantId, @Valid orderRequest: CreateOrderRequest): ResponseEntity<Order> {
        val order = service.createOrder(orderRequest.orderId, merchantId, orderRequest.amount, orderRequest.invoiceDetails)

        val location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .buildAndExpand(order.id)
            .toUri()

        return ResponseEntity.created(location).body(order)
    }

    @PostMapping("/{orderId}/shipments")
    fun post(@NotNull @RequestHeader("x-merchant") merchantId: MerchantId, @Valid shipmentRequest: CreateShipmentRequest): ResponseEntity<Shipment> {
        try {
            val shipment = service.addShipment(shipmentRequest.orderId, shipmentRequest.id, shipmentRequest.amount)

            val location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .buildAndExpand(shipment.id)
                .toUri()

            return ResponseEntity.created(location).body(shipment)
        } catch (e: OrderNotFoundException) {
            throw ResponseStatusException(NOT_FOUND, e.message)
        }
    }
}

data class CreateOrderRequest(@NotNull val orderId: OrderId, @NotNull val amount:Int, @NotNull @Valid val invoiceDetails: InvoiceDetails)
data class CreateShipmentRequest(@NotNull val id: ShipmentId, @NotNull val orderId: OrderId, @NotNull val amount:Int)
