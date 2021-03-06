# slick-example

* `Tutorial`: https://docs.google.com/presentation/d/1pWi8B6cO_8JpxMBy7PVLMbJbRkeMqWH3LJh4lwxnRN8/htmlpresent
* `Repo`: https://github.com/zotvent/slick-example

Цель этого проекта - показать, как можно использовать библиотеку `slick` в качестве *Functional Relational Mapping для Scala*.


---

* `Jesús Martínez-B. H. * Slick 101`: https://docs.google.com/presentation/d/1pWi8B6cO_8JpxMBy7PVLMbJbRkeMqWH3LJh4lwxnRN8/htmlpresent
* `Jan Christopher Vogt * Patterns for slick database applications`: https://www.slideshare.net/skillsmatter/patterns-for-slick-database-applications
* `Rebecca Grenier * Scala Slick-2`: https://www.slideshare.net/ArtemVlasenko1/scala-slick2
* `Scala Tutorial: Create CRUD with Slick and MySQL`: https://codequs.com/p/B1IogRLY
* `Slick 3.2.0 manual - DATABASE I/O ACTIONS`: https://scala-slick.org/doc/3.2.0/dbio.html
  ```scala
    val q = for (c <- coffees) yield c.name
    val a = q.result
    val f: Future[Seq[String]] = db.run(a)
    
    f.onSuccess { case s => println(s"Result: $s") }
  ```
* **(** `Introduction to Slick`: https://www.baeldung.com/scala/slick-intro **)**
* **(** `Scala Tutorial – map, flatMap, flatten and filter – Higher-order Methods`: https://freecontent.manning.com/using-option-in-scala-part-2-map-and-flatmap **)**
* **(** `Scala Future return Type`: https://stackoverflow.com/questions/64585141/scala-future-return-type **)**
  * https://coderoad.ru/23179097/Почему-Scala-Future-возвращает-None-используя-flatMap-и-для-понимания
* **(** `How to read query parameters in akka-http`: https://stackoverflow.com/questions/39763652/how-to-read-query-parameters-in-akka-http **)**

## Features

- Автоматически создает в базе данных таблицу с именем `BOOKS`
- Выполняет CRUD-операции
- REST-API построен на *Akka HTTP*
- *HikaryCP* используется как пул соединений

### Prerequisites

В этом примере используется `postgresql` (убедитесь, что он установлен на компьютере).

1. После установки создайте базу данных (по умолчанию имя базы данных `postgres`).
2. Замените значения user и password в файле `application.conf` самостоятельно (по умолчанию user и password к базе данных `postgres`/`postgres`).
3. В базе данных создайте схему (по умолчанию схема базы данных `public`).
4. Если вам нужно другое имя схемы, вы должны изменить его в `kz.example.database.table.BooksTable` в определении класса` Books`

### Dependencies

Перед запуском проекта загрузите все зависимости.
Сделать это можно например командой с терминала:

```shell
sbt clean compile
```

### Running

И запустите проект, например командой с терминала:

```shell
sbt run
```

Если sbt не может автоматически найти файл `application.conf`, тогда укажите это явно:

```shell
sbt run -Dconfig.resource=application.conf
```

### CRUD operations example

Теперь Вы можете использовать slick через интерфейс REST.

> add book

```shell
curl -X POST \
  http://localhost:8080/books \
  -H 'Content-Type: application/json' \
  -H 'Postman-Token: b2650af6-4348-4681-9ad6-6f760c2124b3' \
  -H 'cache-control: no-cache' \
  -d '{
	"id": 1,
	"name": "test name",
	"author": "test author"
}'
```
```json
{
    "id": 4,
    "name": "test name",
    "author": "test author"
}
```

> get book

```shell
curl -X GET \
  http://localhost:8080/books/1 \
  -H 'Postman-Token: cfea87e8-9f4a-4ab3-914c-3926f0cf5fad' \
  -H 'cache-control: no-cache'
```
```json
{
    "id": 4,
    "name": "test name",
    "author": "test author"
}
```

> update book

```shell
curl -X PUT \
  http://localhost:8080/books \
  -H 'Content-Type: application/json' \
  -H 'Postman-Token: 952b1e75-907f-4ff8-a8a3-4b16bb2f6ecd' \
  -H 'cache-control: no-cache' \
  -d '{
	"id": 1,
	"name": "test update name",
	"author": "test update author"
}'
```
```json
{
    "id": 4,
    "name": "test update name",
    "author": "test update author"
}
```

> delete book

```shell
curl -X DELETE \
  http://localhost:8080/books/1 \
  -H 'Postman-Token: 826f5900-865e-46fc-bb02-ab4023863d3e' \
  -H 'cache-control: no-cache'
```
```text
"Book was deleted"
```
