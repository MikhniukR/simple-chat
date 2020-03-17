Нужно сделать групповой чат, где люди могут обмениваться только текстовыми сообщениями. 
Группа может состоять из неограниченного количества людей.
У группы может быть только один админ чата, в случае если админ удаляет чат,
 все сообщения удаляются и чат перестаёт быть активным. 

На задание два дня, нужно сделать в максимально простом виде, без выкрутасов.    

Если есть по заданию вопросы, с радостью все поясню. Я всегда на связи. 

P.S: не усложняйте задание, нужно сделать самый простой вариант чата, просто его «скелет».
Важно проверить, как сам код и логика организованы. 

Да, это должно быть какое-то API с простенький рабочим интерфейсом и базой данных, 
интерфейс не нужен и хватит только API. 

База не нужна 

##

Примеры запросов 

##Получить всех пользователей

    curl -X GET http://localhost:8080/user
    
Пример результата
    
    200 OK
    [
        {
            "nick": "test"
        },
        {
            "nick": "user"
        }
    ]
    
## Получить пользователя по нику
    
    curl -X GET http://localhost:8080/user/nick
    
Пример результата

    200 OK
    {"nick":"nick"}
    
##Создать пользователя
    
    curl -X POST http://localhost:8080/user -F nick=someUser

Примеры результатов

    200 OK
    {"nick":"someUser"} 
    
    400 Bad Request
    User with nick someUser already exist, nick should be uniq.
    
##Удаление пользователя

    curl -X DELETE \
      http://localhost:8080/user/test 
      
Примеры результатов

    200 OK
    
##Получить все чаты
    
    curl -X GET http://localhost:8080/chat
    
Пример результата
    
    200 OK
    [
        {
            "admin": {
                "nick": "test"
            },
            "name": "kek",
            "participants": [
                {
                    "nick": "test"
                },
                {
                    "nick": "pidor"
                }
            ],
            "messages": [
                {
                    "author": {
                        "nick": "pidor"
                    },
                    "text": "Hello, World!",
                    "time": "2020-03-17T18:23:25.758929Z"
                }
            ]
        }
    ]
    
##Создание чата

    curl -X POST \
      http://localhost:8080/chat \
      -F admin=test \
      -F name=chat \
      -F 'participants=test, user'
    
Пример результата
    
    200 OK
     {
        "admin": {
            "nick": "test"
        },
        "name": "chat",
        "participants": [
            {
                "nick": "test"
            },
            {
                "nick": "user"
            }
        ],
        "messages": []
     }
    
##Получить чат по имени
    
    curl -X GET http://localhost:8080/chat/chat -F author=user1

##Получить сообщения чата

    curl -X GET http://localhost:8080/chat/chat/messages -F author=user1
     
##Удаление чата

    curl -X DELETE \
      http://localhost:8080/chat/chat \
      -F user=test
      
Пример ответа 
    
    200 OK
    
##Добавление сообщения 

    curl -X POST \
      http://localhost:8080/chat/char \
      -F author=user \
      -F 'text=Hello, World!'
      
Пример результата 
    
    200 OK
    
