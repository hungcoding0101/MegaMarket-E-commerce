import axios from "axios";
import tokenUpdater from "./updateAuthToken";

const securedRequestHelper = {

        manage: (ajaxCall) => async(dispatch) => {

            axios.interceptors.response.use(
                function(response){

                },

                async function(error){
                    if (error.response.data.error === "invalid_token") {
                        const result = await tokenUpdater.updateToken();
                        if (result) {
                            dispatch(ajaxCall);
                        }
                        
                        else{window.location.href = "/signin";}
                    } 
                }
            )

            dispatch(ajaxCall);
        }
};

export default securedRequestHelper;
