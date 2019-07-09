package io.glitchlib.api.model

/**
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
enum class AnalyticsReportType {
    OVERWIEV_V1,
    OVERWIEV_V2
}

/**
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @see [Code Statuses](https://dev.twitch.tv/docs/api/code-redemption-api/.code-statuses)
 *
 * @since 1.0
 */
enum class CodeStatus {
    /**
     * Request successfully redeemed this code to the authenticated user’s account.
     *
     *
     * This status will only ever be encountered when calling the POST API to redeem a key.
     */
    SUCCESSFULLY_REDEEMED,

    /**
     * Code has already been claimed by a Twitch user.
     */
    ALREADY_CLAIMED,

    /**
     * Code has expired and can no longer be claimed.
     */
    EXPIRED,

    /**
     * User is not eligible to redeem this code.
     */
    USER_NOT_ELIGIBLE,

    /**
     * Code is not valid and/or does not exist in our database.
     */
    NOT_FOUND,

    /**
     * Code is not currently active.
     */
    INACTIVE,

    /**
     * Code has not been claimed.
     *
     *
     * This status will only ever be encountered when calling the GET API to get a keys status.
     */
    UNUSED,

    /**
     * Code was not properly formatted.
     */
    INCORRECT_FORMAT,

    /**
     * Indicates some internal and/or unknown failure handling this code.
     */
    INTERNAL_ERROR
}

enum class ExtensionType {
    OVERLAY,
    COMPONENT,
    PANEL,
    MOBILE
}

enum class Period {
    DAY,
    WEEK,
    MONTH,
    YEAR,
    ALL
}

/**
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
enum class VideoSort {
    TIME,
    TRENDING,
    VIEWS
}

/**
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
enum class ProductType {
    BITS_IN_EXTENSION
}

/**
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
enum class DropReason {
    WATCHING
}