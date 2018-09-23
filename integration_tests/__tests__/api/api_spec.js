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
    .get('http://localhost:8080/user/GardenUser/login/GardenUser')
    .expect('status', 200)
    .expect('json', 'logged', true);
});

it('GardenUser can login and token works', function () {
  return frisby
    .setup({
      request: {
        headers: {
          'Content-Type': 'text/plain; charset=UTF-8',
        }
      }
    })
    .get('http://localhost:8080/user/GardenUser/login/GardenUser')
    .expect('status', 200)
    .expect('json', 'logged', true)
    .then(function (res) { // res = FrisbyResponse object
      let token = res.json.token;
      return frisby
        .setup({
          request: {
            headers: {
              'Content-Type': 'text/plain; charset=UTF-8',
              'Authorization': 'Token ' + token
            }
          }
        })
        .get('http://localhost:8080/user/me')
        .expect('status', 200)
        .expect('json', 'firstName', 'GardenUser')
        .expect('json', 'key', 102);
    });
});

it('GardenUser can login and see users', function () {
  return frisby
    .setup({
      request: {
        headers: {
          'Content-Type': 'text/plain; charset=UTF-8',
        }
      }
    })
    .get('http://localhost:8080/user/GardenUser/login/GardenUser')
    .expect('status', 200)
    .expect('json', 'logged', true)
    .then(function (res) { // res = FrisbyResponse object
      let token = res.json.token;
      return frisby
        .setup({
          request: {
            headers: {
              'Content-Type': 'text/plain; charset=UTF-8',
              'Authorization': 'Token ' + token
            }
          }
        })
        .get('http://localhost:8080/users')
        .expect('status', 200);
    });
});
