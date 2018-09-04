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
      .get('http://localhost:8080/user/GardenUser/login/GardenUser')
      .expect('status', 200)
      .expect('json', 'logged', true);
});
