# Java Temelleri Eğitimi Notlar

## JShell
Java kodlarını hızlıca çalıştırmaya yarar. Yani bir java uzantılı dosya oluşturup içerinde bir class tanımlayıp ardından main metodu yazmaya gerek olmadan hızlıca denemeler yapmaya imkan verir.

Java 9 ve sonrası için mevcut. JDK kurulu ise kullanabilirsin.

Java için bir REPL(Read Eval Print Loop) aracıdır ve komut satırında işimizi hallederiz.

REPL komut satırında gerçekleşen şu işlemleri ifade eder:
1- Read (Oku) → Senin girdiğin ifadeyi okur.
2- Eval (Değerlendir) → İfadeyi yorumlayıp çalıştırır.
3- Print (Yazdır) → Sonucu ekrana bastırır.
4- Loop (Döngü) → Senden tekrar yeni bir ifade bekler.

Bu döngü sürekli tekrar eder.
```cmd
nihat>jshell
|  Welcome to JShell -- Version 17.0.12
|  For an introduction type: /help intro

jshell> 5+6
$1 ==> 11

jshell>
```

