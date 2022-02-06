package com.github.crazytosser46.chameleon.util

import com.github.crazytosser46.chameleon.entity.RequestDocument
import com.github.crazytosser46.chameleon.model.MatcherOperation
import org.springframework.stereotype.Component

@Component
class MatcherOperationComparator: Comparator<RequestDocument> {
    companion object {
        val map = mutableMapOf(
            MatcherOperation.EQUALS to 1,
            MatcherOperation.IS_ONE_OF to 2,
            MatcherOperation.NOT_EQUALS to 3,
            MatcherOperation.ANY to 4
        ).toMap()
    }

    override fun compare(o1: RequestDocument?, o2: RequestDocument?): Int {
        val valueOperation1 = o1?.matcherOperation?.let { map[it] } ?: 0
        val valueOperation2 = o2?.matcherOperation?.let { map[it] } ?: 0

        return valueOperation1 - valueOperation2
    }
}