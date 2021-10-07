## Android Studio
Nothing to do.

## Eclipse
Recomiendo que instales [m2e-apt](https://marketplace.eclipse.org/content/m2e-apt) ya que aplica el annotation processing de forma automática.

Para indicarle al plugin que se ejecute lo más fácil es agregar la siguiente propiedad al pom.xml de tu proyecto

``` xml
	<properties>
      <m2e.apt.activation>jdt_apt</m2e.apt.activation>
    </properties>
```

## IntelliJ
Para maven y gradle ya obtiene la configuración automaticamente.

## Netbeans
Si usas maven no hay nada más que hacer.