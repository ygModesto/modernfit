## Android Studio
No need to do anything 

## Eclipse
I recommend that you install [m2e-apt](https://marketplace.eclipse.org/content/m2e-apt) as it applies annotation processing automatically.

To tell the plugin to run, the easiest way is to add the following property to your project's pom.xml

``` xml
	<properties>
      <m2e.apt.activation>jdt_apt</m2e.apt.activation>
    </properties>
```

## IntelliJ
For maven and gradle you already get the configuration automatically.

## Netbeans
If you use maven there is nothing else to do.