# Generar Previews de Dark Mode para Register Screen y sus componentes

El objetivo es proporcionar previsualizaciones tanto en modo claro como en modo oscuro para la pantalla de registro y todos sus componentes individuales, solucionando el problema de renderizado donde no se encontraba el método de preview.

## User Review Required

> [!IMPORTANT]
> Se agregarán funciones de @Preview a varios archivos en el paquete `com.example.thestudents.ui.screens.register`. Por favor, verifique que esto coincida con su estructura de diseño.

## Proposed Changes

### Register Screen Component
Se agregarán o actualizarán las previsualizaciones para asegurar que el modo oscuro se visualice correctamente.

#### [MODIFY] [RegisterScreen.kt](file:///C:/Users/jaunp/StudioProjects/TheStudents-MovilProject/app/src/main/java/com/example/thestudents/ui/screens/register/RegisterScreen.kt)
- Asegurar que `RegisterScreenDarkPreview` esté correctamente definido y visible para las herramientas de Android Studio.

### Register Components
Se agregarán previsualizaciones a cada componente individual.

#### [MODIFY] [RegisterHeader.kt](file:///C:/Users/jaunp/StudioProjects/TheStudents-MovilProject/app/src/main/java/com/example/thestudents/ui/screens/register/components/RegisterHeader.kt)
- Agregar `RegisterHeaderPreview` y `RegisterHeaderDarkPreview`.

#### [MODIFY] [RegisterFooter.kt](file:///C:/Users/jaunp/StudioProjects/TheStudents-MovilProject/app/src/main/java/com/example/thestudents/ui/screens/register/components/RegisterFooter.kt)
- Agregar `RegisterFooterPreview` y `RegisterFooterDarkPreview`.

#### [MODIFY] [RegisterTextField.kt](file:///C:/Users/jaunp/StudioProjects/TheStudents-MovilProject/app/src/main/java/com/example/thestudents/ui/screens/register/components/RegisterTextField.kt)
- Agregar `RegisterTextFieldPreview` y `RegisterTextFieldDarkPreview`.

#### [MODIFY] [TermsAndConditions.kt](file:///C:/Users/jaunp/StudioProjects/TheStudents-MovilProject/app/src/main/java/com/example/thestudents/ui/screens/register/components/TermsAndConditions.kt)
- Agregar `TermsAndConditionsPreview` y `TermsAndConditionsDarkPreview`.

#### [MODIFY] [SocialRegisterOptions.kt](file:///C:/Users/jaunp/StudioProjects/TheStudents-MovilProject/app/src/main/java/com/example/thestudents/ui/screens/register/components/SocialRegisterOptions.kt)
- Agregar `SocialRegisterOptionsPreview` y `SocialRegisterOptionsDarkPreview`.

## Verification Plan

### Manual Verification
- Abrir la pestaña de "Preview" en Android Studio para cada archivo modificado.
- Verificar que aparezcan dos previsualizaciones: una para "Light Mode" y otra para "Dark Mode".
- Confirmar que los colores cambien adecuadamente según el tema.
