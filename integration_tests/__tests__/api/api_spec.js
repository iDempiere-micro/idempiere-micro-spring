const frisby = require('frisby');
const Joi = frisby.Joi; // Frisby exports Joi for convenience on type assersions

it ('iDempiere login should work ', function () {
    return frisby
      .setup({
        request: {
          headers: {
            'Content-Type': 'text/plain; charset=UTF-8',
          }
        }
      })
      .get('http://localhost:8080/idempiere/api/authentication?username=GardenUser&password=GardenUser')
      .expect('status', 200)
      .expect('json', 'logged', true);
});

it ('iDempiere login should work when orgId is sent too', function () {
    return frisby
      .setup({
        request: {
          headers: {
            'Content-Type': 'text/plain; charset=UTF-8',
          }
        }
      })
      .get('http://localhost:8080/idempiere/api/authentication?username=GardenUser&password=GardenUser&orgId=11')
      .expect('status', 200)
      .expect('json', 'logged', true);
});
