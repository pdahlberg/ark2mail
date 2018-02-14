package com.raglic.ark2mail

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestMapping


@Controller
class WebController {

    @RequestMapping(value = ["/"], method = [(RequestMethod.GET)])
    fun homepage(): String {
        return "index"
    }

}