# BSMP Plushies (Fabric 1.20.1)

Mod que invoca un peluche pequeño con **tu skin ("kin")** como textura y **tu nombre** como nametag.

## Qué hace
- Item **"Plushie Tag"** (`bsmpplushies:plushie_tag`): clic derecho en el suelo -> invoca un peluche con TU kin.
- **Menú**: clic derecho directamente sobre un peluche ya invocado (sin correa en la mano) abre una
  pantalla donde puedes escribir el nombre de otro jugador para cambiarle el kin, o pulsar
  "Usar mi skin" para volver a la tuya.
- El peluche copia la skin del jugador elegido: si está online se usa su skin en tiempo real
  (tablist); si no, se busca su UUID en la caché del servidor (`UserCache`) para al menos poner
  el nombre correcto (ver limitación abajo).
- Nametag siempre visible con el nombre del jugador/kin elegido.
- Tamaño reducido (40% de un jugador normal) para que se vea como un peluche.
- Puedes atarlo con correa (leash) porque `canBeLeashedBy` está activado (si tienes una correa en
  la mano al hacer clic, se usa para atar en vez de abrir el menú).

## Cómo compilar desde GitHub
1. Sube esta carpeta a un repo de GitHub (el `.gitignore` ya excluye `build/`, `.gradle/`, etc).
2. El workflow en `.github/workflows/build.yml` compila automáticamente en cada push a
   `main`/`master` y deja el `.jar` como **artifact** descargable en la pestaña "Actions".
3. Si además subes un tag `vX.Y.Z` (ej. `git tag v1.0.0 && git push origin v1.0.0`), se crea
   automáticamente un **Release** de GitHub con el `.jar` adjunto, listo para descargar.

No necesitas tener Gradle instalado en tu PC para esto: todo corre en los servidores de GitHub.

## Cómo compilar
Necesitas: **JDK 17**, conexión a internet (para que Gradle descargue Minecraft/Fabric la primera vez).

```bash
cd bsmp-plushies
./gradlew build
```

El .jar final queda en `build/libs/bsmp-plushies-1.0.0.jar`.
Instálalo en la carpeta `mods` de un cliente/servidor Fabric 1.20.1 con Fabric API instalado también.

Si no tienes el wrapper de gradle (`gradlew`), genera uno con:
```bash
gradle wrapper --gradle-version 8.5
```

## Estructura
```
.github/workflows/build.yml     -> compila el mod solo en GitHub Actions (y crea Releases con tags)

src/main/java/net/bsmp/plushies/
 ├─ BsmpPlushiesMod.java        -> entrypoint común
 ├─ entity/
 │   ├─ PlushieEntity.java      -> la entidad peluche (UUID del dueño + abre el menú al interactuar)
 │   └─ ModEntities.java        -> registro de la entidad
 ├─ item/
 │   ├─ PlushieTagItem.java     -> item que invoca al peluche
 │   └─ ModItems.java           -> registro de items + pestaña creativa
 ├─ network/
 │   ├─ ModNetworking.java      -> ID del paquete cliente->servidor
 │   └─ ServerNetworking.java   -> recibe el paquete del menú y cambia el kin
 └─ client/
     ├─ BsmpPlushiesClient.java -> entrypoint de cliente (registra renderer)
     ├─ gui/
     │   ├─ KinScreenOpener.java-> helper client-only para abrir la pantalla
     │   └─ KinSelectScreen.java-> el MENÚ: escribe un nombre y aplica el kin
     └─ render/
         ├─ PlushieModel.java   -> modelo humanoide (usa el mismo UV que una skin)
         └─ PlushieRenderer.java-> busca la skin real del jugador y escala el modelo
```

## ⚠️ Limitación conocida (importante)
Si el jugador elegido **no está conectado** en ese momento, el peluche va a mostrar su **nombre
correcto** pero con una **skin por defecto (tipo Steve/Alex)**, no su skin real — porque descargar
la textura real de un jugador offline requiere hablar con los servidores de Mojang desde el
cliente, y esa parte (`PlayerSkinProvider`/`SkinProvider` de Minecraft) cambia de nombre entre
versiones y no pude verificarla contra el compilador real aquí (no tengo internet en este
entorno). Dejé el código listo para que sea fácil de agregar:

- Mira `PlushieRenderer.getTexture(...)` — ahí es donde deberías añadir la consulta asíncrona
  a `MinecraftClient.getInstance().getSkinProvider()` (o el nombre equivalente en tu IDE; usa
  autocompletado/Ctrl+Click, ya que Loom te genera los sources con los nombres reales de tu
  versión de Yarn) y cachear el resultado en un `Map<UUID, Identifier>`.
- Como fallback ya funciona: nombre correcto + skin real si el jugador está online, o skin
  default + nombre correcto si está offline.

## Próximos pasos / ideas para seguir mejorando
1. **Skins offline reales** (ver limitación arriba).
2. **Modelo propio**: el modelo actual es un humanoide simplificado (cabeza/cuerpo/brazos/piernas)
   reescalado. Se puede hacer un modelo más "peluche" (redondeado, con costuras) en Blockbench
   exportando a Java (Fabric API) — solo hay que respetar el UV de la skin si quieres que se
   siga viendo el kin correctamente, o crear una textura propia tipo peluche.
3. **Animaciones** de "abrazo" o "squish" al hacer clic sobre el peluche.
4. **Modelo de item en 3D** (actualmente el ítem usa una textura plana placeholder de 16x16,
   reemplázala en `textures/item/plushie_tag.png`).
5. **Permisos**: ahora mismo cualquiera puede clic-derecho un peluche ajeno y cambiarle el kin;
   si no quieres eso, guarda el UUID de quien lo invocó originalmente y compáralo en
   `ServerNetworking` antes de aplicar el cambio.

## Notas
- Escrito para **Yarn mappings 1.20.1+build.10**, **Fabric Loader 0.15.11**, **Fabric API 0.92.2+1.20.1**.
  Si al compilar Gradle se queja de versión de Fabric API, revisa la versión más reciente
  compatible con 1.20.1 en https://modrinth.com/mod/fabric-api y actualiza `gradle.properties`.
