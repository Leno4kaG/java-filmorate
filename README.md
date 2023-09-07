# java-filmorate
В данном проекте реализована следующая функциональность: возможность добавления, редактирование, получения и удаления фильма, а также добавление и удаление лаков для фильма.


![Диаграмма с таблицами](https://github.com/Leno4kaG/java-filmorate/blob/main/QuickDBD-filmorate%20.png)


Для получения фильма по id: 
SELECT * FROM Film as f WHERE id = f.id

Для получения пользователя по id:
SELECT * FROM User as u WHERE id = u.id

Для получения списка всех фильмов:
SELECT * FROM Film

Для получения списка пользователей:
SELECT * FROM User
