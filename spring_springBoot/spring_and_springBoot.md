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

---

## Section 9

### Lazy Initialization vs Eager Intialization
Spring @Component ile işaretlenmiş herhangi bir class'ın instance'ını varsayılan olarak Application context başladığında oluşturur. Tüm bean'lar varsayılan olarak uygulama ayağa kalkerken initialize edildiğinden bazen initialize edilmesi uzun sürebilecek bean'ları ilk kullanıldıkları anda oluşturmak isteyebiliriz. Bunun için elimizde @Lazy annotasyonu var.

@Lazy, @Component ile işaretlenmiş class ya da @Bean ile işaretlenmiş metotlarla kullanılabilir.

> Lazy olarak initialize edilecek bean'ın yerine **Proxy** bir obje oluşturulur. 

Bu mekanizmanın temel mantığı şöyle çalışır:

Bir bean'i @Lazy anotasyonu ile işaretlediğinizde, Spring bu bean'i uygulamanın başlangıcında (startup) oluşturmaz. Bunun yerine, ilgili bean'e ihtiyaç duyulduğu an (yani bir metot çağrısı yapıldığında) gerçek bean'i oluşturan bir proxy objesi enjekte eder. Bu proxy, gerçek bean'in vekilidir.

Siz bu proxy üzerinden herhangi bir metot çağırdığınızda, proxy ilk olarak gerçek bean'in henüz oluşturulup oluşturulmadığını kontrol eder. Eğer bean oluşturulmamışsa, onu o anda oluşturur ve metodu bu yeni oluşturulan gerçek bean üzerinde çağırır. Böylece, başlangıç süresini kısaltmış olur ve belleği daha verimli kullanırsınız.

Örnek, varsayılan EAGER:

```java
@Component
public class SomeDependency {

}

@Component
public class TakesTooLongToInitialize {
	SomeDependency someDependency;

	public TakesTooLongToInitialize(SomeDependency someDependency) {
		System.out.println("TakesTooLongToInitialize: Initializing...");
		// some inititalization logic here ...
		this.someDependency = someDependency;
	}

	public void doSomething() {
		System.out.println("Doing something...")
	}
}


@Configuration
@ComponentScan
public class InitializationExample {

    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext(InitializationExample.class)) {

        	  System.out.println("Context initialized successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

Bean'lar uygulama ayağa kalkarken oluşturulur ve hepsi hazır olunca context de hazırdır.

```bash
TakesTooLongToInitialize: Initializing...
Context initialized successfully.
 ```

 Örnek, LAZY

 ```java
@Component
public class SomeDependency {

}

@Component
@Lazy
public class TakesTooLongToInitialize {
	SomeDependency someDependency;

	public TakesTooLongToInitialize(SomeDependency someDependency) {
		// some inititalization logic here ...
		this.someDependency = someDependency;
	}

	public void doSomething() {
		System.out.println("Doing something...");
	}
}


@Configuration
@ComponentScan
public class InitializationExample {

    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext(InitializationExample.class)) {

        	  System.out.println("Context initialized successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

TakesTooLongToInitialize class'ı @Lazy ile işaretlendiğinden kullanılacağı zaman oluşturulur. Dolayısıyla çıktıda initilize edildiğine dair bir şey yazmadı:
```bash
Context initialized successfully.
```

Şimdi @Lazy ile işaretli class'ı kullanalım:
```java
@Configuration
@ComponentScan
public class InitializationExample {

    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext(InitializationExample.class)) {

        	  System.out.println("Context initialized successfully.");

        	  var myBean = context.getBean(TakesTooLongToInitialize.class);
        	  myBean.doSomething();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

Artık o bean'a erişmek istediğimizden initialize edilir ve instance'ını kullanabiliriz. Önce application context oluşur ve ardından eriştiğimiz bean initialize edilir.
```bash
Context initialized successfully.
TakesTooLongToInitialize: Initializing...
Doing something...
```

* Eager vs Lazy initialization karşılaştırma:

| Özellik | Lazy | Eager |
| --- | --- | --- |
| Initialization Time | Bean initialized when it is first made use of in the application | Bean is initialized at the startup of the application |
| Deafault | NOT Default | Default |
| Code Snippet | @Lazy or @Lazy(value=true) | @Lazy(value=false) or Absance of the annotation |
| Error while initializing | Errors will result in runtime exception | Errors will prevent application from starting up |
| Usage | Rarely | Frequently |
| Memory Consumption | Less(until bean is initialized) | More, all beans are initialized at startup |
| Recommended Scenario | Beans very rarely used in your app | most of your beans |


Eğer eager ise uygulama ayağa kalkarken bean oluşturulamazsa hata alınır ve process sonlanır. Ama lazy ise adece exception fırlatılır ve process kaldığı yerden devam eder, eğer bu durum başka hatalara neden olmazsa.

### Scope of a Bean
Scope, herhangi bir class için oluşturulacak instance'ın uygulama genelinde bir tane mi yoksa birden fazla ve ayrı birer instance olarak mı oluşturulacağına dairdir.

Örneğin Singleton ise ApplicationContext içinde o bean'dan bir tane vardır ve context'ten ne zaman o türden bean istesen sana hep aynısını verir, yenisini oluşturmaz.

Ancak scope Prototype ise context'ten her istediğinde yeni bir bean initialize edilir ve onu kullanırsın.

Örnek:
```java
@Component
class NormalClass {

}


@Scope(value= ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
class PrototypeClass {

}

@Configuration
@ComponentScan
public class BeanScopeExampleApllication {

    public static void main(String[] args) {
        try (var context = new org.springframework.context.annotation.AnnotationConfigApplicationContext(BeanScopeExampleApllication.class)) {
            System.out.println("Context initialized successfully.");

            NormalClass normalBean1 = context.getBean(NormalClass.class);
            NormalClass normalBean2 = context.getBean(NormalClass.class);

            System.out.println("NormalBean1 and NormalBean2: " + normalBean1 + " = " + normalBean2);
            System.out.println("Normal beans are the same instance: " + (normalBean1 == normalBean2));

            PrototypeClass prototypeBean1 = context.getBean(PrototypeClass.class);
            PrototypeClass prototypeBean2 = context.getBean(PrototypeClass.class);

            System.out.println("PrototypeBean1 and PrototypeBean2: " + prototypeBean1 + " != " + prototypeBean2);
            System.out.println("Prototype beans are DIFFERENT instances: " + (prototypeBean1 != prototypeBean2));

        }
    }
}
```

Çıktısı şöyle olacaktır:
```bash
Context initialized successfully.

NormalBean1 and NormalBean2: com.nht.scope.NormalClass@22ff4249 = com.nht.scope.NormalClass@22ff4249
Normal beans are the same instance: true

PrototypeBean1 and PrototypeBean2: com.nht.scope.PrototypeClass@7586beff != com.nht.scope.PrototypeClass@3b69e7d1
Prototype beans are DIFFERENT instances: true
```

Spring Bean'ları için belirli scope'ların tanımı şöyledir:

* **Singleton**: One object instance per Spring IoC Container.
* **Prototype**: Possibly many object instances per Spring IoC Container.

Yalnızca Web uygulamasına yönelik Spring Application Context'deki scope'lar ise:

* Request: One object instance per single HTTP request.
* Session: One object instance per single HTTP session. (Örnek Aynı kullanıcıya ait birden fazla request bir session'a ait olabilir.)
* Application: One object insatence per web application runtime.
* Websocket: One object instance per WebSocker instance.

> Spring Singleton ile design pattern olarak yazılan Singleton arasında ufak bir fark var!

* Spring Singleton: One object instance per Spring IoC Container iken
* Java Singleton: One objet instance per JVM.

Yani aynı JVM'de birden fazla Spring IoC Container çalıştırırsak, Spring Singleton nesnelerimizden birden fazlasına sahip olabilriz. Ancak bu genelde yapılan bir şey olmadığından (eğitimde öyle söyleniyor) Spring Singleton ile Java Singleton çoğu zaman aynı anlam ve işlevde olabiliyor.

### Prototype vs Singleton
# Prototype vs Singleton Bean Scope

| Özellik      | Prototype                                                                 | Singleton                                                        |
|--------------|---------------------------------------------------------------------------|------------------------------------------------------------------|
| Instances    | Possibly many per Spring IOC Container                                    | One per Spring IOC Container                                     |
| Beans        | New bean instance created every time bean is referred to                  | Same bean instance reused                                        |
| Default      | NOT Default                                                               | Default                                                          |
| Code Snippet | `@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)`                 | `@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)` OR Default |
| Usage        | Rarely used                                                               | Very frequently used                                             |
| Recommended Scenario  | Stateful beans                                                            | Stateless beans                                         |

Statefull'a örnek olarak uygulamanın kullanıcı bilgileri olabilir. Eğer kullanıcı bilgilerini tutan bir nesne varsa onun tüm uygulamada kullanılmasını istemeyiz ve her kullanıcı için kullanıcı bilgisini tutan yeni bir instance'a ihtiyaç duyarız. Dolayısıyla bilgide devamlılık aranıyorsa Prototype kullanılabilir.

Aksi takdirde daha uygulama genelinde kullanılabilir, generic class'lar varsa bunlar için aynı instance kullanılabilir. Dolayısıyla stateless diyebiliriz. 

---

### @PostConstruct ve @PreDestroy
Bir bean oluşturukduktan ve bağımlılıkları inject edildikten hemen sonra yapılacak işler için bir metodumuz olsun. Bu metodu @PostConstruct ile işaretlersek Spring bizim yerimize bu metodu **constructor çağrıldıktan ve dependency inject edildikten sonra** çağıracaktır.

@PreDestroy ise Spring tarafından yönetilen Bean destroy edilmeden önce yapılacakların bulunduğu metodu işaretlemede kullanılır ve böylece varsa kullanılan kaynakları serbest bırakabiliriz.

Örnek
```java
@Component
class A {
    private Dependency dependency;
    private Dependency2 dependency2;

    @Autowired
    public void setDependency2(Dependency2 dependency2) {
        System.out.println("A: Dependency2 injected.");
        this.dependency2 = dependency2;
    }


    public A(Dependency dependency) {
        System.out.println("A: Constructor called.");

        this.dependency = dependency;

        System.out.println("A: Dependency injected.");
    }

    @PostConstruct
    public void init() {
        System.out.println("A: Init method called. After constructor and dependency injection.");
        dependency.doSomething();
        dependency2.doSomethingElse();
    }

    @PreDestroy
    public void destroy() {
        System.out.println("A: Destroy method called. Releasing resources...");
    }
}

@Component
class Dependency {
    public void doSomething() {
        System.out.println("Dependency: Doing something.");
    }
}

@Component
class Dependency2 {
    public void doSomethingElse() {
        System.out.println("Dependency2: Doing something else.");
    }
}

@Configuration
@ComponentScan
public class PrePostAnnotationsApplication {

    public static void main(String[] args) {
        try (var context = new org.springframework.context.annotation.AnnotationConfigApplicationContext(PrePostAnnotationsApplication.class)) {
            System.out.println("Context initialized successfully.");
        }
    }
}
```

Çıktısı:
```bash
A: Constructor called.
A: Dependency injected.
A: Dependency2 injected.
A: Init method called. After constructor and dependency injection.
Dependency: Doing something.
Dependency2: Doing something else.
Context initialized successfully.
A: Destroy method called. Releasing resources...
```

Yani constructor çağrılır, varsa constructor ile dependency inject edilir. Devamında varsa setter ile dependency inject edilir. Tüm dependency'ler inject edilip bean hazır hale gelince @PostConstruct ile işaretlenen metot çağrılır. Daha sonra bean destroy edilmeden önce yapılacak işler varsa @PreDestroy ile işaretlenen metot içinde yapılır. Bu annotasyon ile destroy öncesi metodun çağrılması için Spring'e sinyal verilir.

---

### J2EE --> Java EE --> Jakarta EE
* Kurumsal uygulama geliştirmede kullanılan özellikler önceleri JDK ile geliyordu. Sonra bunu ayırdılar ve J2EE (Java 2 Enterprise) adı altında harici olarak sunuldu

* Yeniden markalama kapsamında J2EE adı yerini Java EE (Java Platform Enterprise Edition) a bıraktı.

* Daha sonra Oracle, Java EE'nin haklarını Eclipse Vakfına verdi. Ve vakıf da yapılan bir anket sonucu Jakarta EE adını kullandı.

* Jakarta EE ile
	* Jakarta Server Pages (JSP)
	* Jakarta Standad Tag Library (JSTL)
	* Jakarta Enterprise Beans (EJB)
	* Jakarta RESTful Web Services (JAX-RS)
	* Jakarta Bean Validation
	* **Jakarta Contexts and Dependency Injection (CDI)**
	* Jakarta Persistence API (JPA)

(burada Jakarta yazan yerlere önceden Java getiriyorduk denebilir.)

> Spring 6 ve Spring Boot 3 ile birlikte jakarta desteklenmeye başladı ve paket isimlerinde gördüğümüz 'javax' yerine 'jakarta' geliyor.

#### Jakarta Contexts and Dependency Injection (CDI)
* CDI specification Java EE 6 ile 2009'da hayatımıza girdi. Şimdi ise Jakarta Contexts and Dependency Injection (CDI) adıyla biliniyor.
* CDI bir specification (interface) yani yapılacak işlerin tanımlandığı bir arayüzdür. Bu arayüzün implementasyonu çeşitlilik gösterebilir. Spring Framework de CDI'ı implemente eder. Aynı Spring Data JPA'in, JPA'i implemente etmesi gibi.
* CDI içinde Inject API mevcut ve oradan bazı annotasyonlar şunlardır:
	* Inject (Autowired in Spring)
	* Named (Component in Spring)
	* Qualifier
	* Scope
	* Singleton

Örnek
```java
// @Component
@Named
class MyClass {

	private Dependency dependency;

	// @Autowired
	@Inject
	public void setDependency(Dependency dependency) {
		this.dependency = dependency;
	}
}

// @Component
@Named
class Dependency {

}
```

> Bu tarz annotasyonları kullanabilmek için projeye
groupId: jakarta.inject
artifactId: jakarta.inject-api
ile tanımlı bağımlılık pom ile eklendi.