
import axios from "axios";
import {AuthInfo} from '../constants/ServerConstant'

import * as qs from 'qs'

const tokenUpdater = {
         updateToken: async function(){
             try {

                const requestBody = {
                   grant_type: AuthInfo.refresh_token_grant_type,
                 };

               const response = await axios.post(
                 "/oauth/token",
                 qs.stringify(requestBody),
                 {
                   headers: {
                     contain_token: "yes",
                   },

                   auth: {
                     username: AuthInfo.username,
                     password: AuthInfo.password,
                   },
                 }
               );
               
               return true;

             } catch (error) {           
               return false;
             }

         }
}

export default tokenUpdater;