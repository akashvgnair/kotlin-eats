plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "kotlin-eats"

include("shared-kernel")
include("restaurant-domain")
include("restaurant-application")
include("restaurant-infrastructure")
include("restaurant-cli")
include("ordering-domain")
include("ordering-application")
include("ordering-infrastructure")
include("ordering-cli")
include("kitchen-domain")
include("kitchen-application")
include("kitchen-infrastructure")
include("kitchen-cli")
include("delivery-domain")
include("delivery-application")
include("delivery-infrastructure")
include("delivery-cli")
include("payment-domain")
include("payment-application")
include("payment-infrastructure")
include("payment-cli")
include("app")