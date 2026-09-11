# Fix Build Errors: Hilt and Kotlin Compilation

The project is currently failing to build due to two issues:
1. Hilt cannot find the class `com.example.thestudents.StudentsApplication`. The project currently has `BaseApplication` instead.
2. An "Unresolved reference 'h'" error in `CommentsReviewViewModel.kt`.

## Proposed Changes

### [Application Rename]

#### [NEW] [StudentsApplication.kt](file:///C:/Users/david/AndroidStudioProjects/TheStudents-MovilProject/app/src/main/java/com/example/thestudents/StudentsApplication.kt)
- Create new file with the name `StudentsApplication` instead of `BaseApplication`.

#### [DELETE] [BaseApplication.kt](file:///C:/Users/david/AndroidStudioProjects/TheStudents-MovilProject/app/src/main/java/com/example/thestudents/BaseApplication.kt)
- Remove the old application class.

#### [MODIFY] [AndroidManifest.xml](file:///C:/Users/david/AndroidStudioProjects/TheStudents-MovilProject/app/src/main/AndroidManifest.xml)
- Update `android:name` to `.StudentsApplication`.

### [Bug Fixes]

#### [MODIFY] [CommentsReviewViewModel.kt](file:///C:/Users/david/AndroidStudioProjects/TheStudents-MovilProject/app/src/main/java/com/example/thestudents/ui/screens/commentsReview/CommentsReviewViewModel.kt)
- Fix the suspected typo or stray character at line 10.

## Verification Plan

### Automated Tests
- Run `./gradlew clean :app:assembleDebug` to verify the build.
