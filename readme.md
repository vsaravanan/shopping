Also please get the api help from

http://saravan-js.com:10300/swagger-ui.html

[Master Card Question Paper](./doc/mc-api-team-test.docx)

#  Challenge
## In promotional items (method 1), 2nd item would be 50% discount
## In promotional items (method 2), 3rd item would be free

===========================================================

Populate catalog 

 /api/catalogs
    
    [
     {
       "itemName": "A",
       "itemPrice": 10,
       "promo1": false,
       "promo2": true
     },
     {
       "itemName": "B",
       "itemPrice": 5,
       "promo1": false,
       "promo2": true
     },
     {
       "itemName": "C",
       "itemPrice": 4,
       "promo1": false,
       "promo2": true
     },
     {
       "itemName": "D",
       "itemPrice": 3,
       "promo1": false,
       "promo2": true
     },
     {
       "itemName": "E",
       "itemPrice": 8,
       "promo1": false,
       "promo2": true
     },
     {
       "itemName": "F",
       "itemPrice": 2,
       "promo1": false,
       "promo2": false
     },
     {
       "itemName": "G",
       "itemPrice": 10,
       "promo1": true,
       "promo2": false
     },
     {
       "itemName": "H",
       "itemPrice": 9,
       "promo1": true,
       "promo2": false
     },
     {
       "itemName": "I",
       "itemPrice": 7,
       "promo1": false,
       "promo2": false
     }
    ]

# Catalog
![Catalog](./doc/catalog.jpg?raw=true)

Use case 1 : As per Promo1, second item would be 50% discount

    /api/shopping
    
    [
      {
        "itemName": "I",     "counts": 1
      },
      {
        "itemName": "G",     "counts": 1
      },
      {
        "itemName": "H",     "counts": 2
      }
    ]


# use case 1
![use case 1](./doc/usecase1.jpg?raw=true)


Use case 2 : As per Promo2,  third item will be free, but third item should be cheaper from the group

    /api/shopping
    
    [
      {
        "itemName": "C",     "counts": 2
      },
      {
        "itemName": "A",     "counts": 2
      },
      {
        "itemName": "B",     "counts": 2
      },
      {
        "itemName": "E",     "counts": 1
      },
      {
        "itemName": "F",     "counts": 2
      }
    ]


# use case 2
![use case 2](./doc/usecase2.jpg?raw=true)

Use Case 3 :  Promo 2 third item will be free

    /api/shopping

    
    [
    
      {
        "itemName": "A",     "counts": 3
      },
      {
        "itemName": "B",     "counts": 2
      }
      
    ]


# use case 3
![use case 3](./doc/usecase3.jpg?raw=true)

Use Case 4 :  Promo 2 third item will be free

    /api/shopping
    
    [
    
      {
        "itemName": "A",     "counts": 3
      }
      
    ]


# use case 4
![use case 4](./doc/usecase4.jpg?raw=true)

