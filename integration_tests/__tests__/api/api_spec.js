const frisby = require("frisby");
const Joi = frisby.Joi; // Frisby exports Joi for convenience on type assersions

function randStr(len) {
  let s = '';
  while (s.length < len) s += Math.random().toString(36).substr(2, len - s.length);
  return s;
}

it("iDempiere login should work", function () {
  return frisby
    .setup({
      request: {
        headers: {
          "Content-Type": "text/plain; charset=UTF-8",
        }
      }
    })
    .get("http://localhost:8080/session/GardenUser/login/GardenUser")
    .expect("status", 200)
    .expect("json", "logged", true);
});

it("GardenUser can login and see countries using GraphQL", function () {
  return frisby
    .setup({
      request: {
        headers: {
          "Content-Type": "text/plain; charset=UTF-8",
        }
      }
    })
    .get("http://localhost:8080/session/GardenUser/login/GardenUser")
    .expect("status", 200)
    .expect("json", "logged", true)
    .then(function (res) { // res = FrisbyResponse object
      let token = res.json.token;
      return frisby
      .setup({
        request: {
          headers: {
            "Content-Type": "application/json",
            "Accept": "application/json",
            "Authorization": "Token " + token
          }
        }
      })
      .post("http://localhost:8080/graphql", {query: "{ countries {id name} }" })
      .expect("status", 200)
      .expect("jsonTypes", "data.countries.*", { // Assert *each* object in "items" array
        "id": Joi.string().required(),
        "name": Joi.string().required()
       });
    });
});
it("GardenUser can see version using GraphQL", function () {
  return frisby
    .setup({
      request: {
        headers: {
          "Content-Type": "text/plain; charset=UTF-8",
        }
      }
    })
    .get("http://localhost:8080/session/GardenUser/login/GardenUser")
    .expect("status", 200)
    .expect("json", "logged", true)
    .then(function (res) { // res = FrisbyResponse object
      let token = res.json.token;
      return frisby
        .setup({
          request: {
            headers: {
              "Content-Type": "application/json",
              "Accept": "application/json",
              "Authorization": "Token " + token
            }
          }
        })
        .post("http://localhost:8080/graphql", {query: "{ version {v} }" })
        .expect("status", 200);
    });
});
it("GardenUser can create crm category using GraphQL", function () {
  var categoryName = randStr(10);

  return frisby
    .setup({
      request: {
        headers: {
          "Content-Type": "text/plain; charset=UTF-8",
        }
      }
    })
    .get("http://localhost:8080/session/GardenUser/login/GardenUser")
    .expect("status", 200)
    .expect("json", "logged", true)
    .then(function (res) { // res = FrisbyResponse object
      let token = res.json.token;
      return frisby
        .setup({
          request: {
            headers: {
              "Content-Type": "application/json",
              "Accept": "application/json",
              "Authorization": "Token " + token
            }
          }
        })
        .post("http://localhost:8080/graphql", {query: "mutation createCategory {  createCategory(name:\"" + categoryName + "\", value:\"" + categoryName + "\") {name id}}" })
        .expect("status", 200)
        .then( function (res) {
          return frisby
          .setup({
            request: {
              headers: {
                "Content-Type": "application/json",
                "Accept": "application/json",
                "Authorization": "Token " + token
              }
            }
          })
          .post("http://localhost:8080/graphql", {query: "{ categories {name} }" })
          .expect("status", 200)
          .expect("bodyContains", categoryName);
        });
    });
});