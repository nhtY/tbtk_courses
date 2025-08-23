# Spring ve Spring Boot eğitimi notlar

Temel kavramlar:
* Tight Coupling and Loose Coupling
* IOC Container
* Application Context
* Component Scan
* Dependency Injection
* Spring Beans
* Auto Wiring

> Coupling: Bir şeyi değiştirmeknin ne kadar iş yükü gerektirdiğiyle ilgilidir. Tightly coupling bir sistemde en ufak bir değişiklik çok zaman alabilir, çalışan sistemi bozabiliriz. Loosly coupling bir sistemde bir parçanın değişmesi diğerlerini daha az etkileyeceğinden değişikliğin etkisi ve gerektireceği iş yükü az olacaktır.

### Tight Coupling
Class'ların instance'larını developer oluşturur.
Instance'ları kullanılacakları class'a yine developer verir. Class'lar birbirlerine doğrudan bağlıdır. Yani class A içinde class B kullanılır. Aynı işi yapmak için farklı bir tercih mesela class C kullanılacaksa class B'ye olan doğrudan bağımlılık nedeniyle kod düzenlemesi zaman alacaktır. Çözüm: interface kullanmak.

Class A içinde kullanılacak class B'nin instance'ını, A'nın içinde oluşturmak da tight coupling'dir. Hatta en alâsıdır. Eğer bağımlılık dışardan verilmezse farklı bir implementasyon lazım olduğunda yukarıda anlatıldığı gibi zorluk yaşanır. Ayrıza A class'ının test edilmesi çok zor olur. Çünkü bağımlılıkları mock'lanamaz ve A class'ını izole ederek test etmek zorlaşır. Örneğin bağımlılık bir veritabanı bağlantısı olsa o bağlantı için gerekli yığınla konfigürasyon da test sırasında hazır olmalı, yazılmalı. Yani bağımlılığın da bağımlılıkları varsa işler çorbaya döner. Her şeyi bir yerde yazmak, tanımlamak gerekebilir.

### Loose Coupling via Interfaces
Bir class'ın kullanacağı diğer class interface ile ifade edilir. Böylece sadece belirli bir concrete class'a bağımlı kalmak yerine interface'i implement eden birden çok class kullanılabilir kılınır. Yani kullanılacak class'a doğrudan bir bağımlılık söz konusu olmaz ve esneklik kazanırız.

### Loose Coupling via Spring - level 1
Spring beans (@Bean) kullanarak bir metotun bizim oluşturduğumuz instance'ı return etmesi ve uygulama içinde kullanabilmemiz.

Nesnelerin oluşturulması -> developer

Nesnelerin yönetilmesi ve birbirine bağlanması -> Spring

### Loose Coupling via Spring - level 2
Class'ların instance'larının oluşturulması işini de Spring'in yaptığı bir aşama. İlgili annotasyonlarla işaretlenen class'ın instance'ı Spring tarafından oluşturulur ve uygulamamızda kullanılabilir.

Nesnelerin oluşturulması -> Spring

Nesnelerin yönetilmesi ve birbirine otomatik bağlanması -> Spring

*Wiring*: nesnelerin birbirine bağlanması. Yani bir nesneye ihtiyacı olan diğer nesneyi vermek. Setter ya da constructor ile verilebilir. Örneğin:

```java
DbConnection myConnection = new DbConnection();

Repository myRepo = new Repository(myConnection); // Repository'ye ihtiyacı olan DbConnection Nesnesini verdik. 

```
---


> Maven: Özellikle java için kullanılan bir build(derleme) otomasyon aracıdır. Yazılım geliştirmenin iki yöniyle ilgilidir: yazılımın nasıl derleneceği ve bağımlılıklar.

> pom.xml (Project Object Model): Maven için gerekli. An XML file describes the software project being built, its dependencies on other external modules and components, the build order, directories, and required plug-ins. 

* Bir java maven projesinde group id ve artifact id'nin işlevi maven ekosistemindeki projeleri birbirinden ayırt etmeye yarayan değerlerdir.

> Unique Identification: The combination of Group ID and Artifact ID ensures that your project or dependency is uniquely identified across the Maven ecosystem.

> The Group ID is like a unique namespace for your project. It represents the organization or the broader package that the project belongs to, similar to a company or a domain name. A Group ID helps distinguish your project from other projects that may have similar Artifact IDs (names) but belong to different organizations.

> The Artifact ID is the name of the project or module itself. It is unique within the scope of the Group ID. Think of it as the specific identifier for a particular project or library that you’re working on or referring to. The Artifact ID is what Maven uses to download and manage dependencies in your project.


---

### @Configuration
Bir class'ı bean tanımlarının kaynağı olarak işaretler ve böylece Spring Container, tanımlanan bean'ları yönetebilir. Örnek kod:

Instance'ının oluşturulması istenen class.
```java
public class MessageService {

    public String getMessage() {
        return "Merhaba, Spring'in annotation konfigürasyon dünyasına hoş geldin!";
    }
}
```

*@Bean* ile ilgili class'ın instance'ının oluşturuluması için kullanılacak metot işaretlenir. Biz Spring'ten o class'ın bir instance'ını istediğimizde buraya bakacaktır.
```java
@Configuration
public class AppConfig {

    @Bean
    public MessageService messageService() {
        return new MessageService();
    }
}
```
Context oluşturma ve Spring'in context'te oluşturduğu bean'a ulaşma:
```java
public class MainApp {

    public static void main(String[] args) {
        // Annotation tabanlı konfigürasyonu yüklemek için context oluşturma
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // Bean'i context'ten alma
        MessageService messageService = context.getBean(MessageService.class);

        // Bean'in metodunu kullanma
        System.out.println(messageService.getMessage());
    }
}
```

Özetle süreç şöyle:

Uygulama Başlangıcı: ApplicationContext oluşturulduğunda (new AnnotationConfigApplicationContext(AppConfig.class);), Spring, AppConfig sınıfını okur. *AnnotationConfigApplicationContext*, belirtilen konfigürasyon class'ını kullanarak bir Spring AApplication Context oluşturmada kullanılır.

Bean Tanımlama: AppConfig içindeki @Bean annotation'lı messageService() metodu sayesinde, Spring bu metodun döndürdüğü MessageService nesnesini oluşturur ve bunu IoC konteynerine kaydeder.

getBean() Çağrısı: context.getBean(MessageService.class) satırına geldiğinizde, Spring konteyneri içinde MessageService tipinde bir bean olup olmadığına bakar.

Var Olanı Döndürme: Spring bu bean'i bulur ve size o an hali hazırda bellekte bulunan nesnenin referansını verir.
---

### @Bean
Spring context içinde oluşturulacak nesnenin tanımlandığı ve oluşturulma kodunu içeren metotları işaretler. Böylece Spring o türden bir nesneye ihtiyaç olursa bize context içinden verir.

```java
@Bean
public Person person() {
    return new Person("Ali Yılmaz", 32);
}
```

* Context içindeki bean'ları unique kılan isimleridir. Burada bean'ın varsayılan ismi metot ismidir: 'person'
```java
Person myInstance = context.getBean("person");
```

* istersek bean adını metot isminden farklı kılabiliriz:
```java
@Bean(name = "myPerson")
public Person person() {
    return new Person("Ali Yılmaz", 32);
}
```

Bean'ın adı myPErson oldu:
```java
Person myInstance = context.getBean("myPerson");
```

### Context'teki Bean'lara erişmede alternatif
Bean'lara isimleriyle erişmek mümkün:
```java
Person myInstance = context.getBean("myPerson");
```

Diğer yandan tipine/class'ına göre de erişebiliriz:
```java
Person myInstance = context.getBean(Person.class);
```

Peki context içinde Person türünde birden çok bean varsa? Eğer isimle erişiyorsak zaten isimleri farklı olacağından sıkıntı çıkmayacaktır ama tür üzerinden erişeceksek, ki uygulama genelinde böyle kullanımlar olacaktır, o zaman hangi bean'ın istenildiği ya da varsayılan olarak hangisinin getirileceği de yapılandırılabilir. Yoksa o türden birden fazla bean olduğuna dair hata firlatılacaktır.

### Bean'ları autowire etme
Bir Bean metodu içinde context'teki diğer bean'ları kullanmamız gerekebilir. Bu durumda iki yol var: 

* diğer bean metotlarını çağırmak.

* İlgili bean metoduna diğer bean'ları isim ve türlerine dikkat ederek *parametre olarak* geçmek. Burada dikkat edilecek husus metotta @Bean annotasyonu olmazsa, metot herhangi bir metot gibi muamele görür ve parametreleri context'ten getirilmez. Ayrıca o parametrelerin daha öncesinde bean olarak tanımlanmış olmaları gerekir ki context'ten gelebilsinler.

Aşağıda örnekleri mevcut:

```java
ecord Person (String name, int age, Address address) { };

//Address - firstLine & city
record Address(String firstLine, String city){ };

@Configuration
public class HelloWorldConfiguration {
	
	@Bean
	public String name() {
		return "Ranga";
	}
	
	@Bean
	public int age() {
		return 15;
	}
	
	@Bean
	public Person person() {
		return new Person("Ravi", 20, new Address("Main Street", "Utrecht"));		
	}

    // METOT ÇAĞRIMI ile kullanım 
	@Bean
	public Person person2MethodCall() {
		return new Person(name(), age(), address()); //name, age		
	}

    // PARAMETRE olarak kullanım
	@Bean
	public Person person3Parameters(String name, int age, Address address3) {
		//name,age,address2
		return new Person(name, age, address3); //name, age		
	}

	@Bean(name = "address2")
	public Address address() {
		return new Address("Baker Street", "London");		
	}

	@Bean(name = "address3")
	public Address address3() {
		return new Address("Motinagar", "Hyderabad");		
	}

}
```

### Bazı cevaplanası sorular:
* Spring Container vs Spring Context vs IoC Container vs Application Context bunlar nedir ve farkları nelerdir?

Spring Container = Spring Context = IoC Container: Spring Bean'larını ve onların yaşam döngülerini yönetir: oluşturulması(construction), yapılandırılması(configuration) ve silinmesi (deconstruction).
Örnekleri ise şunlardır: Bean Factory ve Application Context.

Bean factory basit bir Spring Container'dır. Application Context ise daha çok kurumsal uygulamalara hitab eder.

Aşağıdaki fotoda görüleceği üzere Spring Container yazdığımız Java class'larını ve Configuration class'larını alıp birer instance'larını oluşturur. Ayrıca bu instance'ların yaşam döngüsünü yönetir.
![Spring_IoC](./photos/Spring_IoC.png)

* JPOJO vs Java Bean vs Spring Bean farkı nedir?

POJO, herhangi bir java class'ı gibi düşünülebilir, kural yok.

Java Bean (Enterprise Java Bean) ise  bazı kurallara tabi olarak yazdığımız POJO'dur: implements Serializable, public varsayılan no-args constructor, private fields, getter ve setters olmalı.

Spring Bean ise Spring (IoC) tarafından yönetilen herhangi bir nesnedir.

* Spring Framework tarafından yönetilen tün Bean'ları listelemek istersen bunu nasıl yaparız?

```java
context.getBeanDefinition(beanName); // adı verilen Bean'ın tanımını verir

context.getBeanDefinitionCount() // IoC'de tanımlı bean sayısını verir

context.getDefinitionNames(); // IoC'de tanımlı tün Bean'ların isimlerini String[] olarak verir.
```

* Birden fazla aynı türden Bean varsa ne olur?
DbConnection türünden bir bağımlılığı spring auto-wire ile bize sağlayacak olsun. Ancak biz mySqlConnnection ve mongoConnection adında iki tane bu türden Bean tanımlamış olalım.

``` java
@Bean
public DbConnection mySqlConnection() {
    return new DbConnection("mySql");
}

@Bean
public DbConnection mongoConnection() {
    return new DbConnection("mongo");
}
```
Spring DbConnection türünden iki aday instance olduğundan hangisini sana vereceğini bilemeyecek. Öyleyse içlerinden birini öncelikli olarak belirleyelim:

```java
@Bean
@Primary
public DbConnection mySqlConnection() {
    return new DbConnection("mySql");
}

@Bean
public DbConnection mongoConnection() {
    return new DbConnection("mongo");
}
```
Şimdi DbConnection türünden bir dependency auto-wire edilecekse bize mySqlConnection isimli bean gelecektir.

Bir diğer seçenek ise bean için onu ayırt edici bir isim verip ihtiyaç duyulan yerde o isim ile erişmek. Yani ihtiyaç duyulan mongo ise Spring'e bana mongoConnection'ı getir diyebiliyoruz:

```java
@Bean
@Primary
public DbConnection mySqlConnection() {
    return new DbConnection("mySql");
}

@Bean
@Qualifier("mongoQualifier")
public DbConnection mongoConnection() {
    return new DbConnection("mongo");
}

@Bean
public DbConnection sqlServerConnection() {
    return new DbConnection("sqlServer");
}
```

kullanırken de belirlenen değerle ilgili bean'ı getirmesini spring'e söyleyebiliyoruz:

Örnek 1
```java
context.getBean("mongoQualifier") // mongo için DbConnection bean'ını getirir.
```

Örnek 2
```java
// parametredeki bean auto-wire edilirken hangi instance'ın getirileceğini spring'e söyleriz
@Bean
public Repository myRepo(@Qualifier(mongoQualifier) DbConnection dbConnection) {
    return new Repository(dbConnection);
}
```


* Spring nesneleri yönetiyor ve birbirine bağlıyor (auto-wiring) ama nesneleri biz kodla oluşturmuyor muyduk? Spring nesneleri bizim yerimize nasıl oluşturuyor?


* BeanFactory vs ApplicationContext:

> BeanFactory loads beans on-demand, while ApplicationContext loads all beans at startup. Thus, BeanFactory is lightweight as compared to ApplicationContext. kaynak: https://www.baeldung.com/spring-beanfactory-vs-applicationcontext

* Default scope of a spring bean?

The Singleton scope is the default scope in Spring. In this scope, the Spring container creates a single instance of the bean, and this instance is shared across the entire application.

--- 

## Section 7-8

### @Component
Class'ların instance'larını bir konfigurasyon class'ı içinde metotlarla tanımlamak yerine Spring'in otomarik olarak oluşturmasını sağlayabiliriz.

*@Component* annotasyonu ile işaretlenen class uygulamamızın bir komponent'idir. Yani Spring'in annotasyon bazlı konfigurasyonu ve classpath taraması için bir aday class olurlar.

Yani Spring'in bir class'ın instacance'ını bizim yerimize otomatik oluşturmasını istersek o class'ı @Component ile işaretleriz.

Önce:
```java
public class PacmanGame implements GamingConsole {
	// ... some methods
}


@Configuration
public class GameConfig {

	@Bean 
	public GamingConsole pacman() {
		return new PacmanGame();
	}

	@Bean 
	public GameRunner(GamingConsole pacman) {
		return new GameRunner(pacman);
	}
}

public class Main {
	public static void main(String[] args) {
		var context = new AnnotationConfigApplicationContext(GameConfig.class);

		var gameRuner = context.getBean(GameRunner.class);
		gameRunner.run();
	}
}
```

Sonra:
```java
@Component
public class PacmanGame implements GamingConsole {
	// ... some methods
}


@Configuration
@ComponentScan
public class GameConfig {

	@Bean 
	public GameRunner(GamingConsole pacman) {
		return new GameRunner(pacman);
	}
}

public class Main {
	public static void main(String[] args) {
		var context = new AnnotationConfigApplicationContext(GameConfig.class);

		var gameRuner = context.getBean(GameRunner.class);
		gameRunner.run();
	}
}
```

PacmanGame class'ı @Component olarak işaretlendi ve böylece instance oluşturma işi spring'e verildi. Ancak uygulama içindeki component'leri nerede bulacağını @ComponentScan ile Spring'e söylemezsek 'NoSuchBeanDefinitionException' alırdık.

@ComponentScan herhangi bir parametre verilmezse kullanıldıği package ve altındaki yerlerde Component'leri arar. Parametreler vererek birden fazla package'a bakmasını ve bazen de bakmaması gereken konumları belirtebiliriz.

* Kodu Main class'ında toplayalım:

```java
@Component
public class PacmanGame implements GamingConsole {
	// ... some methods
}

@Configuration
@ComponentScan
public class Main {

	@Bean 
	public GameRunner(GamingConsole pacman) {
		return new GameRunner(pacman);
	}

	public static void main(String[] args) {
		var context = new AnnotationConfigApplicationContext(Main.class);

		var gameRuner = context.getBean(GameRunner.class);
		gameRunner.run();
	}
}
```

Artık konfigurasyonu Main içinde yaptığımızdan Context'i Maindeki yapılandırmaya göre hazırlıyoruz. Böylece daha az class yazdık denebilir.

* Şimdi GameRunner'ı da @Component ile oluşturalım:
```java
@Component
public class PacmanGame implements GamingConsole {
	// ... some methods
}

@Component
public class GameRunner {
	private GamingConsole game;

	public GameRunner(GamingConsole game) {
		this.game = game;
	}

	// ... some methods
}

@Configuration
@ComponentScan
public class Main {

	public static void main(String[] args) {
		var context = new AnnotationConfigApplicationContext(Main.class);

		var gameRuner = context.getBean(GameRunner.class);
		gameRunner.run();
	}
}
```

Böylece Spring class'larımız için hem instance oluşturdu hem de bağımlılılarını yönetti.

### @Component ile işaretli aynı türden birden fazla class varsa ve inject edilmesi gerekiyorsa
Böyle bir durumda spring bize 'NoUniqueBeanDefinitionException' hatasını firlatacaktır. Çünkü o türden oluşturulmuş hangi instance'ı kullanmak/inject etmek istediğin belirsiz.

Örneğin:
```java
@Component
public class PacmanGame implements GamingConsole {
	// ... some methods
}

@Component
public class MarioGame implements GamingConsole {
	// ... some methods
}

@Component
public class GameRunner {
	private GamingConsole game;

	public GameRunner(GamingConsole game) {
		this.game = game;
	}

	// ... some methods
}

@Configuration
@ComponentScan
public class Main {

	public static void main(String[] args) {
		var context = new AnnotationConfigApplicationContext(Main.class);

		var gameRuner = context.getBean(GameRunner.class);
		gameRunner.run();
	}
}
```

* Hatırlayalım: bu gibi durumlarda @Primary ya da @Qualifier gibi annotasyonlar kullanarak Spring'e hangi bean'ı getirmesi gerektiği söylenebilir.

Öyleyse MarioGame'i @Primary ile işaretleyelim:

```java
@Component
public class PacmanGame implements GamingConsole {
	// ... some methods
}

@Component
@Primary
public class MarioGame implements GamingConsole {
	// ... some methods
}

@Component
public class GameRunner {
	private GamingConsole game;

	public GameRunner(GamingConsole game) {
		this.game = game;
	}

	// ... some methods
}

@Configuration
@ComponentScan
public class Main {

	public static void main(String[] args) {
		var context = new AnnotationConfigApplicationContext(Main.class);

		var gameRuner = context.getBean(GameRunner.class);
		gameRunner.run();
	}
}
```

GameRunner'ın ihtiyacı olan GamingConsole türünden bean artık MarioGame olarak verilir. Çünkü spring'e GamingConsole türünden aday bean'lardan hangisnin öncelikli olarak kullanılacağı @Primary ile söylendi.

* @Qualifier ile istediğimiz Bean'ı kullanalım:

```java
@Component
@Qualifier("PacmanGameQualifier")
public class PacmanGame implements GamingConsole {
	// ... some methods
}

@Component
@Primary
public class MarioGame implements GamingConsole {
	// ... some methods
}

@Component
public class GameRunner {
	private GamingConsole game;

	public GameRunner(@Qualifier("PacmanGameQualifier") GamingConsole game) {
		this.game = game;
	}

	// ... some methods
}

@Configuration
@ComponentScan
public class Main {

	public static void main(String[] args) {
		var context = new AnnotationConfigApplicationContext(Main.class);

		var gameRuner = context.getBean(GameRunner.class);
		gameRunner.run();
	}
}
```

Spring @Qualifier("PacmanGameQualifier") ile işaretlenmiş class'ın instance'ını GameRunner'a verir ve böylece PacmanGame kullanılır.

> Diyelim ki bir A class'ını @Qualifier ile işaretlemedik ancak onu B class'ında kullanacağız. Diğer adaylardan ayrıştırmak için A class'ına qualifier verilmese bile B içinde class adıyla belirtilebilir:
```java
@Component
public class A implements MyType {

}

@Component
@Primary
public class X implements MyType {

}

@Component
@Qualifier("qualifierY")
public class Y implements MyType {

}

@Component
public class B {
	private MyType dependency;

	public B(@Qualifier("A") MyType dependency) {
		this.dependency = dependency;
	}
}
```

---

### Dependency Injection Türleri
3 tür vardır: Constructor based, Setter based ve Field.

* Constructor-based: Dependencies are set by craeting the Bean using its Constructor.

* Setter-based: Dependencies are set by calling setter methods on your behalf.

* Field: No setter or constructor. Dependency is injected using *reflection*.

**Field Injection**: reflection ile bağımlılıkların Spring tarafından oluşturulup ilgili class'a verildiği tür.

Örnek:
```java
@Component
class BusinessService {
    Dependency1 dependency1;
    Dependency2 dependency2;

    public String toString() {
        return "BusinessService{" +
                "dependency1=" + dependency1 +
                ", dependency2=" + dependency2 +
                '}';
    }
}

@Component
class Dependency1 {
    // Simulating a dependency
}

@Component
class Dependency2 {
    // Simulating another dependency
}


@Configuration
@ComponentScan
public class FieldInjectionExample {

    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext(FieldInjectionExample.class)) {
            Arrays.stream(context.getBeanDefinitionNames())
                    .forEach(System.out::println);

            BusinessService businessService = context.getBean(BusinessService.class);
            System.out.println(businessService);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

Bu Örnekte çıktı olarak @Component ile işaretlenmiş class'lar için Bean oluşturulduğunu görürüz ancak BusinessService'in toString metodu çağrılınca bağımlılılarının null olduğunu görürüz:

```bash
fieldInjectionExample
businessService
dependency1
dependency2
BusinessService{dependency1=null, dependency2=null}
```

* @Autowired annotasyonu ile Spring'e bu bağımlılıkları ilgili class'a inject etmesi gerektiği söylenmeli:

```java
@Component
class BusinessService {
    @Autowired
    Dependency1 dependency1;

    @Autowired
    Dependency2 dependency2;

    public String toString() {
        return "BusinessService{" +
                "dependency1=" + dependency1 +
                ", dependency2=" + dependency2 +
                '}';
    }
}

```

Bunun sonucunda çıktımız şöyle görünecektir:
```bash
fieldInjectionExample
businessService
dependency1
dependency2
BusinessService{dependency1=com.nht.dependencyinjection.exapmle.e1.Dependency1@3cd3e762, dependency2=com.nht.dependencyinjection.exapmle.e1.Dependency2@1fa121e2}

```

**Setter Injection**: Spring'e bizim yazdığımız setter metotlarını kullanarak bağımlılıkları inject etmsini söyleyeceğiz:

Örneğin
```java
@Component
class BusinessService {
    Dependency1 dependency1;
    Dependency2 dependency2;

    @Autowired
    public void setDependency1(Dependency1 dependency1) {
        this.dependency1 = dependency1;
    }

    @Autowired
    public void setDependency2(Dependency2 dependency2) {
        this.dependency2 = dependency2;
    }

    public String toString() {
        return "BusinessService{" +
                "dependency1=" + dependency1 +
                ", dependency2=" + dependency2 +
                '}';
    }
}
```

Burada setter metotları @Autowired ile işaretlenmeseydi Spring injection'ı yapamazdı ve toString kısmında bağımlılıklar null görünecekti. Biz @Autowired kullandık ve çıktımız:

```bash
setterInjectionExample
businessService
dependency1
dependency2
BusinessService{dependency1=com.nht.dependencyinjection.example.e2.Dependency1@131ef10, dependency2=com.nht.dependencyinjection.example.e2.Dependency2@55b0dcab}
```

**Constructor Injection** Spring ilgili class'ın Constructor'ında gördüğü bağımlılıkları auto-wire eder/inject eder. Constuctor yardımıyla injection yapılırken @Autowired kullanmasak bile Spring bunu anlar ve kendi yapar.

```java
@Component
class BusinessService {
    Dependency1 dependency1;
    Dependency2 dependency2;

    public BusinessService(Dependency1 dependency1, Dependency2 dependency2) {
        this.dependency1 = dependency1;
        this.dependency2 = dependency2;
    }

    public String toString() {
        return "BusinessService{" +
                "dependency1=" + dependency1 +
                ", dependency2=" + dependency2 +
                '}';
    }
}
```

@Autowired kullanmadık ama kullanabilirdik.

Çıkıtısı:
```bash
constructorInjectionExample
businessService
dependency1
dependency2
BusinessService{dependency1=com.nht.dependencyinjection.example.e3.Dependency1@7eac9008, dependency2=com.nht.dependencyinjection.example.e3.Dependency2@4116aac9}
```

> Hangi tür injection kullanılmalı? Cevap constructor-based injection. Çünkü tek bit metotta class initialize ediliriken bağımlılıklarına sahip olur ve böylece kullanıma hazırdır.

---
### Terminoloji

* @Component: Bir class'ın nesnesidir ve Springframework tarafından yönetilir.

* Dependency: Bir class'ın ihtiyaç duyduğu diğer class. Eğer interface olarak tanımlandıysa, o türden bir implementastona ihtiyaç duyar.

* Component Scan: @Component ile işaretlenmiş class'ları tanımlandıkları paketlerde bulabilmesi için Spring'e yardımcı oluyoruz. Mesela @ComponentScan(com.nht.myproject). varsayılan olarak @ComponentScan annotasyonuyla işaretlenmiş class'ın bulunduğu paket ve altındaki paketleri tarar.

* Dependency Injection: Bean'ları tanıyıp ihtiyaç duydukları bağımlılıkları onlara oluşturup vermektir. İhtiyaç duyulan bağılılıkların oluşturulması ve bağlanması/ilgili class'a verilmesi işi framework tarafından yapılınca buna Inversion Of Control (IoC) diyoruz. Yani bu süreçte kontrol framework'tedir.

* Spring Beans: Yaşam döngüsü Spring Framework taradından yönetilen herhangi bir class'ın herhangi bir instance'ı.

* IoC container: Bean'ların yaşam döngüsü ve bağımlılıklarını yönetir. Türleri: ApplicationContext (daha karmaşık ve eager), BeanFactory(basit ve lazy).

* Autowiring: Bir Spring Bean'ı için bağımlılıkların bağlanması sürecidir.

---

### @Component vs @Bean

| Özellik | @Component | @Bean |
| --- | --- | --- |
| Where? | Can be used on any Java class | Typically used on methods in Spring Configuration classes |
| Ease of Use | Very easy. Just add annotation | Write all the code |
| Autowiring | Yes; Field, Setter, Constructor injection | Yes; method call or method parameters |
| Who creates beans? | Spring Framework | Spring Framework, but you write the creation code |
| Recommended for | Instantiating Beans for your own application code: @Component | 1- Custom Business Logic. 2- Instantiating Beans from 3rd party libraries @Ban |