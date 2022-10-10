# droidjam-managing-state-compose
Managing State with Jetpack compose

## Tech Stack
```
- UI        : Compose UI, Swift UI, Compose Material 3
- Database  : SQLDelight 
```

## Module:

```
- android             // Android app
- desktop             // Desktop app
- ios                 // Entry point for iOS's KMM
- ios-native          // iOS App
- js                  // Kotlin js + Web Assembly app
- shared              // Shared module kmm code between all platforms
-- core               // Core module, all module dependes on this
-- core-ui            // Place for shared ui code like themes, module that renders ui will always depends from this
-- core-app           // App entry point for KMM, this will initialize all dependency injection manager from it's submodule
-- core-ios           // Extra layer for iOS, usually iOS require additinal translation layer such as converting suspend function or flow
-- core-molecule      // Presenter that want to use molecule will depends on this
-- booking
--- booking-app               // Implementation detail of booking's feature
--- booking-presentation-api  // Presentation API that expose presenter, allowing all platform to share presentation & business logic
--- booking-ui                // Module for booking's compose ui
-- locale
--- locale-app                // Implementation detail of locale's feature
--- locale-presentation-api   // Presentation API that expose presenter, allowing all platform to share presentation & business logic
--- locale-ui                 // Module for locale's compose ui
```

https://user-images.githubusercontent.com/19620593/194906030-e56d525c-2ac3-44e6-8585-4cbe739dd7ae.mp4

