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

> Maven: Özellikle java için kullanılan bir build(derleme) otomasyon aracıdır. Yazılım geliştirmenin iki yöniyle ilgilidir: yazılımın nasıl derleneceği ve bağımlılıklar.

> pom.xml (Project Object Model): Maven için gerekli. An XML file describes the software project being built, its dependencies on other external modules and components, the build order, directories, and required plug-ins. 

* Bir java maven projesinde group id ve artifact id'nin işlevi maven ekosistemindeki projeleri birbirinden ayırt etmeye yarayan değerlerdir.

> Unique Identification: The combination of Group ID and Artifact ID ensures that your project or dependency is uniquely identified across the Maven ecosystem.
> The Group ID is like a unique namespace for your project. It represents the organization or the broader package that the project belongs to, similar to a company or a domain name. A Group ID helps distinguish your project from other projects that may have similar Artifact IDs (names) but belong to different organizations.
> The Artifact ID is the name of the project or module itself. It is unique within the scope of the Group ID. Think of it as the specific identifier for a particular project or library that you’re working on or referring to. The Artifact ID is what Maven uses to download and manage dependencies in your project.


