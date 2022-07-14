/* import {Buffer} from 'buffer' */
export const SECURITY_UPDATE_TOKEN = "SECURITY_UPDATE_TOKEN";


class AuthInfo{

    static username = ''
    static password = ''
    static scope = ''
    static password_grant_type = 'password'
    static refresh_token_grant_type = 'refresh_token'
    static bearer_prefix = 'Bearer '
}

export {AuthInfo}