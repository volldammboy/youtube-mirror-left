# Espejo YouTube para Crochet Zurdo

Aplicación Android que espeja horizontalmente los vídeos de YouTube para que los tutoriales de crochet para diestros puedan seguirse por zurdos. También incluye velocidades de reproducción desde `0.1x` hasta `2.0x`.

[Read in English](README.md)

## Funciones

- Navegador de YouTube integrado con `WebView`, con persistencia de sesión para contenido gratuito y Premium.
- Botón de espejo que voltea el vídeo horizontalmente mediante `scaleX(-1)`, manteniendo los controles del reproductor utilizables.
- Ciclo de velocidad: `0.1x, 0.2x, 0.3x, 0.4x, 0.5x, 0.75x, 1.0x, 1.25x, 1.5x, 2.0x`.
- Interfaz Material 3.
- Soporte básico de pantalla completa, navegación hacia atrás y recarga.

## Requisitos

- Android SDK 36
- JDK 17 o superior
- Gradle Wrapper incluido en el proyecto

## Compilación

Build de depuración:

```sh
./gradlew assembleDebug
```

La build de release requiere un fichero local `keystore.properties`. Créalo en la raíz del proyecto, pero no lo subas al repositorio:

```properties
storeFile=C:/ruta/a/tu-release.jks
storePassword=...
keyAlias=release
keyPassword=...
```

Después:

```sh
./gradlew assembleRelease bundleRelease
```

Salida del APK de depuración:

```text
app/build/outputs/apk/debug/app-debug.apk
```

Salidas de release firmadas:

```text
app/build/outputs/apk/release/app-release.apk
app/build/outputs/bundle/release/app-release.aab
```

## Instalación

Instala el APK mediante sideload en Android. Puede ser necesario permitir la instalación desde tu gestor de archivos (por ejemplo, Mixplorer) en los ajustes de Android.

## Notas

- El inicio de sesión se realiza dentro de la `WebView` de la app, así que el acceso a la cuenta y al contenido Premium depende de la sesión web de YouTube.
- El espejo invierte texto, patrones y otros elementos que aparecen en el vídeo. Sirve bien para el movimiento de manos, pero no para patrones con mucho texto o detalles de orientación lateral.
- La orientación del trabajo cambia: el crochet para diestros suele mostrarse de derecha a izquierda, mientras que el zurdo va de izquierda a derecha.

## Firma de release

Genera un keystore de release y guárdalo fuera del repositorio junto con su contraseña. `keystore.properties` ya está en `.gitignore`.

Ejemplo:

```sh
keytool -genkeypair -v \
  -keystore tu-release.jks \
  -alias release \
  -keyalg RSA -keysize 2048 -validity 10000
```
