Weather app using Kotlin Multiplatform and Compose Multiplatform.

## Setup
1. In `local.properties`, add below properties
    1. FLAVOR=uat
    ## Prepaid config
    2. prepaidUatUrl=https://www.api.singtel.com
    3. prepaidProductionUrl=https://www.api.singtel.com
    4. prepaidUatClientId=JBELxykgMGAjJVwHjfqTAi4WWewbgcMO
    5. prepaidProdClientId=pqrs
    6. prepaidUatClientSecret=efgh
    7. prepaidProdClientSecret=efgh


## Video
### Android
[Screen_recording_20230801_123215.webm](artifacts/Screen_recording_20231025_183240.webm)

### iOS
[artifacts/Simulator-Screen-Recording-iPhone_14-2023-10-25_at_18.36.42.mp4)

## Libraries
- [Kamel](https://github.com/Kamel-Media/Kamel) - Used to load remote images.
- [PreCompose](https://github.com/Tlaster/PreCompose/) - Used for compose navigation.
- [moko-resources](https://github.com/icerockdev/moko-resources) - Used to have string resources.
- [moko-mvvm](https://github.com/icerockdev/moko-mvvm) - Used to provide ViewModel implementations.
- [SqlDelight](https://github.com/cashapp/sqldelight) - Used for local database.
- [DataStore](https://developer.android.com/jetpack/androidx/releases/datastore) - Used to save preferences.
- [moko-permissions](https://github.com/icerockdev/moko-permissions) - Handle user permissions in both platforms.
- [ktor](https://ktor.io/) - Used for networking
- [koin](https://insert-koin.io/) - For dependency injection
- [kotlinx-datetime](https://github.com/Kotlin/kotlinx-datetime) - Date-time related operations
- [BuildKonfig](https://github.com/yshrsmz/BuildKonfig) - Read values from gradle files
- [Kermit](https://github.com/touchlab/Kermit) - Used for logging
- [Kotest Assertions](https://kotest.io/docs/assertions/assertions.html) - Testing
- [coroutine-test](https://github.com/Kotlin/kotlinx.coroutines/tree/master/kotlinx-coroutines-test) - Testing
- [turbine](https://github.com/cashapp/turbine) - https://github.com/cashapp/turbine


### TODO

- Add Unit Tests
- Add JVM UI Tests
- Add Integration tests
- Add Lint (Detekt)
- Add Code formatter (Spotless)
- Add Code Coverage (Jacoco)
- Add OSS Scan Plugin
