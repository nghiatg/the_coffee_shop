import encoding from 'k6/encoding';
import http from 'k6/http';
import { check } from 'k6';


// random config for running test
const numberOfShopChainOwner = 2;
const numberOfShopChainPerOwner = 3;
const numberOfShopPerShopChain = 3;
const numberOfDishPerShop = 4;
const numberOfQueuePerShop = 2;
const numberOfContactPerShop = 2;
const maxQueueSize = 100;



const numberOfCustomer = 10;
const numberOfOrderCreatedPerCustomerPerShop = 2;



// config for API endpoints
const authenticationHost = 'http://host.docker.internal:8091/coffee_shop_authentication';
const registerUserUrl = authenticationHost + '/users/register';
const loginUserUrl = authenticationHost + '/users/login';

const shopMngHost = 'http://host.docker.internal:8090/coffee_shop_management';
const addShopChainUrl = shopMngHost + '/shop-chain';
const addShopUrl = shopMngHost + '/shop-chain/#shopchainid/shop';
const addDishUrl = shopMngHost + '/shop/#shopid/dish';
const addQueueUrl = shopMngHost + '/shop/#shopid/order-queue';
const addContactUrl = shopMngHost + '/shop/#shopid/contact';


const orderHost = 'http://host.docker.internal:8092/coffee_shop_order';
const createOrderUrl = orderHost + '/customer/order'
const getOrderDetailUrl = orderHost + '/customer/order/#orderid'

// user type
const shopChainOwnerType = 'SHOP_CHAIN_OWNER';
const shopOperatorType = 'SHOP_OPERATOR';
const customerType = 'CUSTOMER';


// variables that is dynamic when running test
let dishes = [];
let queues = [];
let allCustomerJwts = [];

export default function () {

    for(let c = 1 ; c <= numberOfCustomer ; ++c) {
        let currentTime = Date.now() + '';
        let customerUsername = 'customer_' + currentTime;
        let customerPassword = 'customer_' + currentTime + '_pw';
        createUser(customerUsername, customerPassword, customerType);
        allCustomerJwts.push(loginUser(customerUsername, customerPassword));
    }

    
    for(let c = 1 ; c <= numberOfShopChainOwner ; ++c) {
        let currentTime = Date.now() + '';
        let shopChainOwnerUsername = 'shopchain_owner_' + currentTime;
        let shopChainOwnerPassword = 'shopchain_owner_' + currentTime + '_pw';
        createUser(shopChainOwnerUsername, shopChainOwnerPassword, shopChainOwnerType);
        let chainOwnerJwt = loginUser(shopChainOwnerUsername, shopChainOwnerPassword);
        for(let i = 1 ; i <= numberOfShopChainPerOwner ; ++i) {
            addShopChain(chainOwnerJwt);
        }
    }
}

function createUser(username, password, userType) {

    let data = { 
        username: username,
        password: password,
        userTypes: [userType],
    };
    let params = {
        headers: { 'Content-Type': 'application/json' },
    };
    let res = http.post(registerUserUrl, JSON.stringify(data), params);
    check(res, {
        'status is 200': (r) => r.status === 200
    });
}

function loginUser(username, password) {
    let data = { 
        username: username,
        password: password
    };
    let params = {
        headers: { 'Content-Type': 'application/json' },
    };
    let res = http.post(loginUserUrl, JSON.stringify(data), params);
    check(res, {
        'status is 200': (r) => r.status === 200
    });
    return res.json().data.accessToken;
}

function addShopChain(shopChainOwnerJwt) {
    let currentTime = Date.now() + '';
    let data = { 
        shopChainName: 'shop_chain_name_' + currentTime 
    };
    let params = {
        headers: { 
            'Authorization': 'Bearer ' + shopChainOwnerJwt,
            'Content-Type': 'application/json' 
        },
    };
    let res = http.post(addShopChainUrl, JSON.stringify(data), params);
    check(res, {
        'status is 200': (r) => r.status === 200
    });


    let shopChainId = res.json().data.id;
    for(let i = 1 ; i <= numberOfShopPerShopChain ; i++) {
        addShop(shopChainId, shopChainOwnerJwt);
    }

}

function addShop(shopChainId, shopChainOwnerJwt) {
    let currentTime = Date.now() + '';
    let shopOperatorUsername = 'shop_operator_' + currentTime;
    let shopOperatorPassword = 'shop_operator_' + currentTime + '_pw';
    createUser(shopOperatorUsername, shopOperatorPassword, shopOperatorType);
    let data = { 
        shopOperator: shopOperatorUsername,
        longitude: generateRandomNumber(1, 89) + '',
        latitude: generateRandomNumber(1, 89) + '',
        openTime: (getRandomInt(0,2) + 7) + ':00:00',
        closeTime: (getRandomInt(0,5) + 18) + ':00:00'
    };
    let params = {
        headers: { 
            'Authorization': 'Bearer ' + shopChainOwnerJwt,
            'Content-Type': 'application/json' 
        },
    };
    let res = http.post(addShopUrl.replace('#shopchainid', shopChainId), JSON.stringify(data), params);
    check(res, {
        'status is 200': (r) => r.status === 200
    });

    let shopId = res.json().data.id;

    for(let i = 1 ; i <= numberOfContactPerShop ; i++) {
        addContact(shopId, shopChainOwnerJwt);
    }

    dishes = [];
    queues = [];

    for(let i = 1 ; i <= numberOfDishPerShop ; i++) {
        addDish(shopId, shopChainOwnerJwt);
    }

    for(let i = 1 ; i <= numberOfQueuePerShop ; i++) {
        addQueue(shopId, shopChainOwnerJwt);
    }

    allCustomerJwts.forEach((customerJwt) => {
        for(let i = 1 ; i <= numberOfOrderCreatedPerCustomerPerShop ; ++i) {
            var randomDishId = dishes[getRandomInt(0, dishes.length-1)];
            var randomQueueId = queues[getRandomInt(0, queues.length-1)];
            createOrder(customerJwt, randomDishId, randomQueueId);
        }
    })
}

function generateRandomNumber(min, max) {
    return Math.random() * (max - min) + min;
};

function getRandomInt(min, max) {
    return Math.floor(generateRandomNumber(min, max));
}


function addDish(shopId, shopChainOwnerJwt) {
    let currentTime = Date.now() + '';
    let dishName = 'dish_' + currentTime;
    let data = { 
        name: dishName,
        price: generateRandomNumber(5, 50) + ''
    };
    let params = {
        headers: { 
            'Authorization': 'Bearer ' + shopChainOwnerJwt,
            'Content-Type': 'application/json' 
        },
    };
    let res = http.post(addDishUrl.replace('#shopid', shopId), JSON.stringify(data), params);
    check(res, {
        'status is 200': (r) => r.status === 200
    });
    dishes.push(res.json().data.id);
}

function addQueue(shopId, shopChainOwnerJwt) {
    let data = { 
        maxSize: getRandomInt(30, maxQueueSize)
    };
    let params = {
        headers: { 
            'Authorization': 'Bearer ' + shopChainOwnerJwt,
            'Content-Type': 'application/json' 
        },
    };
    let res = http.post(addQueueUrl.replace('#shopid', shopId), JSON.stringify(data), params);
    check(res, {
        'status is 200': (r) => r.status === 200
    });
    queues.push(res.json().data.id);
}

function addContact(shopId, shopChainOwnerJwt) {
    let currentTime = Date.now() + '';
    let data = { 
        type: 'EMAIL',
        value: 'email_' + currentTime + '@gmail.com'
    };
    let params = {
        headers: { 
            'Authorization': 'Bearer ' + shopChainOwnerJwt,
            'Content-Type': 'application/json' 
        },
    };
    let res = http.post(addContactUrl.replace('#shopid', shopId), JSON.stringify(data), params);
    check(res, {
        'status is 200': (r) => r.status === 200
    });
}

function createOrder(customerJwt, dishId, queueId) {
    let data = { 
        reservedOrderQueueId: queueId,
        dishId: dishId
    };
    let params = {
        headers: { 
            'Authorization': 'Bearer ' + customerJwt,
            'Content-Type': 'application/json' 
        },
    };
    let res = http.post(createOrderUrl, JSON.stringify(data), params);

    if(res.status === 200 && res.json()) {
        let orderId = res.json().data.id;
        let getDetailRes = http.get(getOrderDetailUrl.replace('#orderid', orderId), params);
        check(getDetailRes, {
            'status is 200': (r) => r.status === 200
        });
    }

}