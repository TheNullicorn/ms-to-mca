package me.nullicorn.msmca.util

import kotlin.js.JsName

/**
 * Nondescript names for the [@JsName][JsName] annotation, particularly on tests whose function names are
 * wrapped in backticks, e.g. &#96;...&#96;.
 *
 * Usage:
 * ```kotlin
 * @Test
 * @JsName(ONE)
 * fun `should do something really cool`() {
 *     // Your awesome test here...
 * }
 * ```
 *
 * # Explanation
 *
 * Kotlin/JS doesn't allow functions named with backticks to include special characters (spaces,
 * punctuation, symbols) unless you annotate those functions with @JsNames that include valid
 * function names.
 *
 * However, the IDE (or at least IntelliJ) still displays the test's correct name in the testing UI,
 * so the `@JsName` doesn't need to be anything meaningful. Hence, the values included here can be
 * used to give tests unique [@JsName][JsName] values.
 *
 * If you're contributing tests and need more unique names, feel free to add them as-needed.
 */

const val ONE =
    "_"

const val TWO =
    "__"

const val THREE =
    "___"

const val FOUR =
    "____"

const val FIVE =
    "_____"

const val SIX =
    "______"

const val SEVEN =
    "_______"

const val EIGHT =
    "________"

const val NINE =
    "_________"

const val TEN =
    "__________"

const val ELEVEN =
    "___________"

const val TWELVE =
    "____________"