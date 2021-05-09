package com.github.crazytosser46.chameleon.model

enum class MatcherOperation {
    ANY,
    IS_ONE_OF,
    EQUALS,
    NOT_EQUALS
}

class MatcherOperationComparator: Comparator<MatcherOperation> {
    companion object {
        val map = mutableMapOf(
            MatcherOperation.EQUALS to 1,
            MatcherOperation.IS_ONE_OF to 2,
            MatcherOperation.NOT_EQUALS to 3,
            MatcherOperation.ANY to 4
        ).toMap()
    }

    override fun compare(o1: MatcherOperation?, o2: MatcherOperation?): Int {
        val valueOperation1 = map[o1] ?: 0
        val valueOperation2 = map[o2] ?: 0

        return valueOperation1 - valueOperation2
    }
}