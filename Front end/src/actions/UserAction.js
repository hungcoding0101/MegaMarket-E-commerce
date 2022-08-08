import axios from 'axios';
import {AuthInfo} from '../constants/ServerConstant'
import qs from'qs'



export const signUpUser = async (credentials) => {
    const signUpRequest = {
      ...credentials,
      grant_type: AuthInfo.password_grant_type,
      scope: AuthInfo.scope,
    }; 
    
    return await axios.post(`/user/signup`, signUpRequest, {
      auth: {
        username: AuthInfo.username,
        password: AuthInfo.password,
      },
    });
};

export const signInUser = async (credentials) => {
 
        const signInRequest = {...credentials,
                                grant_type: AuthInfo.password_grant_type,
                                scope: AuthInfo.scope
                            };

          return await axios.post(
            `/oauth/token?rememberme=${credentials.rememberme}`,
            qs.stringify(signInRequest),
            {
              headers: {
                "Content-Type": "application/x-www-form-urlencoded",
              },

              auth: {
                username: AuthInfo.username,
                password: AuthInfo.password,
              },
            }
          ); 
}


export const fetchUser = async () => {
     return await axios.get(
       "/user/login",
       {
         headers: {
           contain_token:'yes',
         },
       } /* { timeout: 5000 } */
     );
}

export const logOutUser = async () => {
      console.log("LOGGEG OUT!");
      return await axios.get("user/logout", {
        headers: {
          contain_token: "yes",
        },
      });

}

export const addToCart = async (request) => {
  return await axios.post("/cart/add", request, {
    headers: {
      contain_token: "yes",
    },
  });
};

export const deleteCart = async (id) => {
        return await axios.delete(`/cart/delete/${id}`, {
          headers: {
            contain_token: "yes",
          },
        });
}

export const requestPassResetCode = async (email) => {
    return await axios.get(`/user/reset/passwords/require`, {
        params:{
            email: email,
        }
    });
}

export const resetPasswords = async (request) => {
    return await axios.post(`/user/reset/passwords/handle`, request,);
}

export const updateBasicInfo = async (request) => {
    return await axios.patch(`/user/update/basicInfo`, request, {
      headers: {
        contain_token: "yes",
      },
    });
}

export const updateAddresses = async (request) => {
  return await axios.patch(`/customer/updateAddresses`, request, {
    headers: {
      contain_token: "yes",
    },
  });
};

