package com.karrel.zipsample.extensions

import java.util.*

/**
 * ex)
 * (1..10) -> Random().nextInt(10 - 1) + 1
 */
fun ClosedRange<Int>.random() =
        Random().nextInt(endInclusive - start) +  start