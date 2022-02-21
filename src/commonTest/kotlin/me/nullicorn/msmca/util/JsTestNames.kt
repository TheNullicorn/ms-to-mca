package me.nullicorn.msmca.util

/*
 * Nondescript names for use in the [JsName] annotation on tests named using back-ticks (`).
 *
 * Kotlin/JS doesn't allow functions with backticks to include special characters (spaces,
 * punctuation, symbols) unless you annotate the function with @JsName and a normal name.
 *
 * However, the IDE (or at least IntelliJ) still displays the test's correct name in the testing UI,
 * so the `@JsName` doesn't need to be anything meaningful. Hence, the values included here can be
 * used to give tests unique [JsName] values.
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