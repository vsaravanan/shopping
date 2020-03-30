Also please get the api help from


http://saravan-js.com:10300/swagger-ui.html


For example 1:

I have renamed A to G and B to H. 

            catalog = new Catalog("G", new BigDecimal(10), true, false);
            catalogService.priceItem(catalog);
            catalog = new Catalog("H", new BigDecimal(9), true, false);
            catalogService.priceItem(catalog);

Rest of the them has more clarity from TestMasterCardServices

Please run the test and find the output log from console.

Shopping 1:

    G promo1 newPrice : 10.00
    G : cart.getTotalPrice() : 10.00, order.getTotalSum() : 10.00
    H promo1 newPrice : 9.00
    H promo1 newPrice : 4.500
    H : cart.getTotalPrice() : 13.500, order.getTotalSum() : 23.500

Shopping 2:

    A promo2 newPrice : 10.00
    A : cart.getTotalPrice() : 10.00, order.getTotalSum() : 10.00
    B promo2 newPrice : 5.00
    B : cart.getTotalPrice() : 5.00, order.getTotalSum() : 15.00
    C promo2 newPrice : 0
    C : cart.getTotalPrice() : 0, order.getTotalSum() : 15.00
    F no promo newPrice : 2.00
    F : cart.getTotalPrice() : 2.00, order.getTotalSum() : 17.00
    
Also, I have added apis    

localhost:10041/api/catalogs    to add inventory


    [
        {
            "itemName" : "G",
            "itemPrice" : "10",
            "promo1" : true,
            "promo2" : false
            
        }
        ,
        {
            "itemName" : "H",
            "itemPrice" : "9",
            "promo1" : true,
            "promo2" : false
        
        }
	]
    
    
localhost:10041/api/shopping  to test promo1

    [
        {
            "itemName" : "G",
            "counts" : 1
        }
        ,
        {
            "itemName" : "H",
            "counts" : 2
        
        }
    ]

Response would be

    {
        "status": "OK",
        "message": "shopping checkout was successfully finished ",
        "result": {
            "orderId": 1,
            "totalSum": 23.500,
            "carts": null,
            "orderItems": [
                {
                    "itemName": "G",
                    "counts": 1,
                    "originalPrice": 10.00,
                    "discountedPrice": 10.00,
                    "total": 10.00
                },
                {
                    "itemName": "H",
                    "counts": 2,
                    "originalPrice": 9.00,
                    "discountedPrice": 4.500,
                    "total": 13.500
                }
            ],
            "customerName": null
        }
    }    
    

Also you can try shopping2 to test promo2

    [
        {
            "itemName" : "A",
            "counts" : 1
        }
        ,
        {
            "itemName" : "B",
            "counts" : 1
        
        }
        ,
        {
            "itemName" : "C",
            "counts" : 1
        
        }
        ,
        {
            "itemName" : "F",
            "counts" : 1
        
        }
    
    ]    
    
    
Response would be
    
    {
        "status": "OK",
        "message": "shopping checkout was successfully finished ",
        "result": {
            "orderId": 2,
            "totalSum": 17.00,
            "carts": null,
            "orderItems": [
                {
                    "itemName": "A",
                    "counts": 1,
                    "originalPrice": 10.00,
                    "discountedPrice": 10.00,
                    "total": 10.00
                },
                {
                    "itemName": "B",
                    "counts": 1,
                    "originalPrice": 5.00,
                    "discountedPrice": 5.00,
                    "total": 5.00
                },
                {
                    "itemName": "C",
                    "counts": 1,
                    "originalPrice": 4.00,
                    "discountedPrice": 0,
                    "total": 0
                },
                {
                    "itemName": "F",
                    "counts": 1,
                    "originalPrice": 2.00,
                    "discountedPrice": 0,
                    "total": 2.00
                }
            ],
            "customerName": null
        }
    }

    
