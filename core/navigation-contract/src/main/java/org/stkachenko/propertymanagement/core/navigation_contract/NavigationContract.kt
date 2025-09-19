package org.stkachenko.propertymanagement.core.navigation_contract


object PropertiesDestinations {
    const val PROPERTIES_ROUTE = "properties_route"

    const val PROPERTY_ID_ARG = "propertyId"
    const val PROPERTY_DETAILS_ROUTE_BASE = "property_details_route"
    const val PROPERTY_DETAILS_ROUTE =
        "$PROPERTY_DETAILS_ROUTE_BASE/{$PROPERTY_ID_ARG}"
}

object ChatDestination {
    const val CHAT_ROUTE = "chat_route"
}

object StatisticsDestination {
    const val STATISTICS_ROUTE = "statistics_route"
}

object ProfileDestination {
    const val PROFILE_ROUTE = "profile_route"
}

