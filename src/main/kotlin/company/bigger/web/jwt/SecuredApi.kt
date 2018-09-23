package company.bigger.web.jwt

import company.bigger.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils

/**
 * Component to test if the authorization token is valid
 */
@Component
class SecuredApi {
    @Autowired
    lateinit var userService: UserService

    companion object {
        private val LOG = LoggerFactory.getLogger(SecuredApi::class.java)
    }

    /**
     * If the [authorization] is JWT-valid, process [execute], otherwise return HttpStatus.UNAUTHORIZED
     */
    fun <T> processAuthorization(authorization: String?, execute: () -> T): ResponseEntity<T?> {
        val apiKey = authorization?.replace("Token ", "")?.replace("Bearer ", "")
        val failedResult = null as T?

        if (StringUtils.isEmpty(apiKey)) {
            val errorMessage = "No Authorization part of the request header/parameters, returning ${HttpStatus.UNAUTHORIZED}."
            LOG.info(errorMessage)

            return ResponseEntity(failedResult, HttpStatus.UNAUTHORIZED)
        }

        // find the user associated to the given api key.
        val user = userService.findByToken(apiKey ?: "")
        LOG.info("user by token: ${user?.loginName}")
        if (user == null) {
            LOG.info("No user with Authorization: {}, returning {}.", apiKey, HttpStatus.UNAUTHORIZED)

            return ResponseEntity(failedResult, HttpStatus.UNAUTHORIZED)
        } else {
            // validate JWT
            try {
                LOG.info("Validating JWT")
                if (!userService.validToken(apiKey ?: "", user)) {
                    LOG.info("JWT invalid")
                    LOG.info("Authorization: {} is an invalid JWT.", apiKey, HttpStatus.UNAUTHORIZED)

                    return ResponseEntity(failedResult, HttpStatus.UNAUTHORIZED)
                }
            } catch (e: Exception) {
                return ResponseEntity(failedResult, HttpStatus.UNAUTHORIZED)
            }
        }

        LOG.info("User is: ${user.loginName}")
        userService.setCurrentUser(user)

        LOG.info("OK accessing resource, proceeding.")
        return ResponseEntity(execute(), HttpStatus.OK)
    }
}