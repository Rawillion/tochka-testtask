# Test Task for Tochka
В application.properties находятся настройки проекта:
порт по умолчанию 8008, база на postgresql, базу необходимо создать ручками перед запуском приложения, схему создаст hibernate

Схема создаётся заново при каждом запуске приложения, для удобства отладки, внимание на параметр spring.jpa.hibernate.ddl-auto

POST запрос на http://localhost:8008/subscribe добавляет в приложение новую подписку.
Из необходимых параметров: в хедерах source с адресом источника новостей и type - html или rss - с типом источника.
В body в json необходимо передать правила для парсинга, news - путь в верстке к набору новостей, title и description - пути соответственно для заголовка и описания новости относительно набора новостей.

Данные подписки можно исправить если направить новый POST запрос с исправленными правилами - приложение найдет подписку по полю source и исправит её.

GET запрос на http://localhost:8008/news вернёт все спарсенные приложением новости. Необязательный параметр title в запросе указывает подстроку в заголовке, по которой приложение отфильтрует выдачу новостей, регистр значения не имееет.
***
Пара примеров правил:  
Сайт   
https://habr.com/ru/all/  
Правило  
{  
    "news": "body > div:class(layout) > div:class(%layout__row_body) > div > section > div:class(content_left%) > div:class(posts_list) > ul > li:id(post%)",  
    "title": "article > h2 > a",  
    "description": "article > div > div"  
}  

Его RSS  
https://habr.com/ru/rss/all/all/  
{  
    "news": "channel > item",  
    "title": "title",  
    "description": "description"  
}  

Сайт  
https://amurmedia.ru/news/  
Правило  
{  
    "news": "body > section:class(tp-s) > section > div > div > div > div > div > div:class(col-md-8 ) > div > div > div:class(item)",  
    "title": "div > a:class(news-for-copy)",  
    "description": "div > span:class()"  
}  

Его RSS  
https://habr.com/ru/rss/all/all/  
{  
    "news": "channel > item",  
    "title": "title",  
    "description": "description"  
}  
Стандарт RSS один на всех, но правило указывать всё равно нужно, с параметрами по умолчанию для RSS не заморачивался.  
