# todays-gif-service

## Описание
Проект представляет собой сервис, который обращается к внешнему сервису курсов валют,
и отдает gif в ответ: если курс выбранной валюты за сегодня стал выше вчерашнего,
то сервис возвращает рандомную gif отсюда: https://giphy.com/search/rich, а если ниже -
отсюда https://giphy.com/search/broke.

Сервис написан на `Spring Boot 2 + Java`. Для взаимодействия с внешними сервисами
используется `Feign`. Запросы приходят на `HTTP endpoint`, туда передается код валюты.

## Параметры

Некоторые параметры вынесены в отдельный файл настроек `src/main/resources/application.properties`.
```txt
server.port = 8080 - основной порт, на котором запускается приложение

CURRENCY_SERVICE_API_KEY = *insert your currency service api key here* - API ключ сервиса с курсами валют
GIF_SERVICE_API_KEY = *insert your gif service api key here* - API ключ сервиса gif картинок

GIFKEYWORD_GOOD = rich - слово или фраза, по которой ищется gif в случае превышения курса
GIFKEYWORD_BAD = broke - слово или фраза, по которой ищется gif в случае провала курса

CURRENCY_SERVICE_URL = https://openexchangerates.org/api - URL сервиса с курсами валют
GIF_SERVICE_URL = https://api.giphy.com/v1/gifs - URL сервиса с gif

CURRENCY_BASE_DEFAULT = USD - код валюты по умолчанию, относительно которой производится расчёт курса 
```
Также для тестирования существует отдельный файл настроек `src/test/resources/application.properties`.
Данный файл практически идентичен предыдущему файлу настроек, за исключением следующих параметров:
```txt
testing.server.port = 8089 - порт Mock'а внешних сервисов
```
Следующие два параметра также отличаются от основного файла настроек. Их значения могут быть произвольными,
так как и вызывающий код и код тестов содержат ссылки на них.
```txt
CURRENCY_SERVICE_API_KEY = SUPER_PUPER_CURRENCY_SERVICE_API_KEY
GIF_SERVICE_API_KEY = SUPER_PUPER_GIF_SERVICE_API_KEY
```

## Запуск и тестирование
Для сборки проекта используется Gradle.

### Подготовка
Перед началом работы необходимо получить ключи API для [сервиса Gif картинок](адрес ссылки) и
[сервиса курсов валют](адрес ссылки). Для получения ключей API достаточно зарегистрироваться на соответствующих
ресурсах. Ключ представляет собой последовательность шестнадцатеричных символов вида `8ac74ff008dd573cdea237eaf70bf92a`.

После получения ключей API, или в случае если у Вас они уже есть, необходимо прописать их в файле настроек
`src/main/resources/application.properties`:
```txt
CURRENCY_SERVICE_API_KEY = *insert your currency service api key here* - API ключ сервиса с курсами валют
GIF_SERVICE_API_KEY = *insert your gif service api key here* - API ключ сервиса gif картинок
```
Также необходимо удостовериться, что у вас установлен `Java SE Development Kit`. Скачать его можно
[здесь](https://www.oracle.com/java/technologies/downloads/). Рекоммендуемая версия - `17`. Проверить,
установлен ли JDK и его версию можно следующей командой в `Консоли Windows (cmd)` или `Windows PowerShell`:
```txt
javac -version
```

### Запуск сервиса
Для того, чтобы запустить сервис, необходимо выполнить его сборку. Для этого откройте `Консоль Windows (cmd)` или
`Windows PowerShell` и перейдите в папку проекта. Далее выполните сборку проекта:

```txt
cd D:\...\todays-gif-service
gradlew bootRun
```
Обратите внимание, что для запуска через `Windows PowerShell` необходимо использовать последовательность `.\ `:
```txt
.\gradlew bootRun
```
В случае успешного запуска на экране отобразятся логотип Spring и несколько информационных сообщений. 

Сервис имеет статическую страницу `src/main/resources/static/Index.html` для быстрого тестирования работоспособности
приложения после запуска. Обратиться к данной странице можно по следующему адресу (где `port` - порт, указанный в
файле настроек):
```txt
localost:port/
```
Пробуйте и тестируйте ***:)***

### Остановка сервиса
Для остановки сервиса необходимо в окне консоли, через которую Вы производили запуск, нажать сочетание клавиш
`Ctrl+C`, а на появившийся вопрос `Завершить выполнение пакетного файла [Y(да)/N(нет)]?` ответить `y`.

### Тестирование
На сервис написаны тесты. Для Mock'а внешних сервисов используется WireMock. Для тестирование необходимо выполнить
следующую команду в папке проекта:
```txt
gradlew test
```

## Известные проблемы
* ### Жёсткая привязка к курсу доллара США (`USD`) как к базовой валюте.

Согласно заданию:
> ... если курс по отношению к `рублю` за сегодня стал выше вчерашнего ...

Однако, ввиду того, что бесплатная подписка используемого сервиса курсов валют позволяет использовать
только доллар США (`USD`) в качестве базовой валюты, невозможно использовать другие валюты в качестве базовой.
Тем не менее, в приложении реализована поддержка других валют в качестве базовой, но сервис будет выдавать ошибку.
Если заменить URL сервиса в файле настроек и скорректировать соответствующим образом Feign, данный сервис будет работать.

* ### Не определённое в задании поведение сервиса в случае неизменности курса выбранной валюты.

Согласно заданию:
> ... если курс по отношению к рублю за сегодня стал выше вчерашнего ...

> ... если ниже ...

Получается, что если текущий курс выбранной валюты не отличается от вчерашнего, поведение сервиса в задании не определено.
В данный момент реализована следующая логика: неизменность курса является положительным случаем, как случай превышения.
***Денежки-то мы не потеряли :)***


## Дальнейшее развитие
* Планируется переход на сборку и запуск Docker контейнера с данным сервисом.