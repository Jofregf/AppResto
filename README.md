# Restaurant reservation application

## Spring Boot Dependencies/Starters
* Spring Data JPA
* PostgreSQL Driver
* Spring Web
* Spring Boot DevTools
* Hibernate Validator

# Entidad Restaurant
1. Restaurant
2. RestaurantRepository
3. RestaurantController

## FLUJO
1. Controller recibe petición web.
2. Se llama un Servicio
3. Se ejecuta la lógica del repositorio que se conecta a la base de datos
4. Se usan los modelos

# ASOCIACIONES
## DIRECCIONALIDAD
Nos indica si desde una entidad, podemos obtener la otra. Una relación puede ser de dos tipos:
1. Unidireccional: cuando desde la entidad origen se puede obtener la entidad destino.
2. Bidireccional cuando desde ambas partes de la relación se puede obtener la otra parte.

## TIPOS DE ASOCIACIONES 

1. ONE TO MANY (Bidireccional)
Una biblioteca (ONE) puede tener muchos libros (MANY). Y es bidireccional porque muchos libros (MANY) pueden pertenecer
a una biblioteca (ONE).


VIDEO 01:32:00 explica sortBY!!!!! hasta el 01:45:00

Video 02:11:22 Empezar a ver desde acá!!!! ERRORES SOLUXIONADOS!!!!

Video 03:22:00 Empieza seguridad