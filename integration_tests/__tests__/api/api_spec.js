const frisby = require('frisby');
const Joi = frisby.Joi; // Frisby exports Joi for convenience on type assersions

it('iDempiere login should work', function () {
  return frisby
    .setup({
      request: {
        headers: {
          'Content-Type': 'text/plain; charset=UTF-8',
        }
      }
    })
    .get('http://localhost:8080/session/GardenUser/login/GardenUser')
    .expect('status', 200)
    .expect('json', 'logged', true);
});

it('GardenUser can login and see countries using GraphQL', function () {
  return frisby
    .setup({
      request: {
        headers: {
          'Content-Type': 'text/plain; charset=UTF-8',
        }
      }
    })
    .get('http://localhost:8080/session/GardenUser/login/GardenUser')
    .expect('status', 200)
    .expect('json', 'logged', true)
    .then(function (res) { // res = FrisbyResponse object
      let token = res.json.token;
      return frisby
      .setup({
        request: {
          headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
            'Authorization': 'Token ' + token
          }
        }
      })
      .post("http://localhost:8080/graphql", {query: "{ countries {id name} }" })
      .expect('status', 200)
      .expect('jsonTypes', 'data.countries.*', { // Assert *each* object in 'items' array
        "id": Joi.string().required(),
        "name": Joi.string().required()
       });
    });
});
it('GardenUser can see version using GraphQL', function () {
  return frisby
    .setup({
      request: {
        headers: {
          'Content-Type': 'text/plain; charset=UTF-8',
        }
      }
    })
    .get('http://localhost:8080/session/GardenUser/login/GardenUser')
    .expect('status', 200)
    .expect('json', 'logged', true)
    .then(function (res) { // res = FrisbyResponse object
      let token = res.json.token;
      return frisby
        .setup({
          request: {
            headers: {
              'Content-Type': 'application/json',
              'Accept': 'application/json',
              'Authorization': 'Token ' + token
            }
          }
        })
        .post('http://localhost:8080/graphql', {query: "{ version {v} }" })
        .expect('status', 200);
    });
});
