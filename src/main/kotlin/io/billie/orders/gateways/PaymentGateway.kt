package io.billie.orders.gateways

import io.billie.orders.domain.MerchantId
import io.billie.orders.domain.Shipment
import org.springframework.stereotype.Service

@Service
class PaymentGateway {
    fun payShipment(merchantId: MerchantId, shipment: Shipment) {
        TODO("Not yet implemented")
    }
}