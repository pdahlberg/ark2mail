package com.raglic.ark2mail.model

class PayedFeature() {

    companion object {
        val DELIVER_AFTER: Int = 1 // Sender selects future time
        val OWN_PASSWORD: Int = 2 // Sender adds an extra password which is required to /accept the forward
        /*
        * Pay extra features [receiver]:
          - Pay X ARK/year to register a "direct forward wallet address"
          - Allow direct forward wallet address to be visible if someone tries to send to this email, telling them to transfer the ARK directly is preferred
         */
    }

}