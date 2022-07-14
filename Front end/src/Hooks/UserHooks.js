import { useMutation, useQuery, useQueryClient } from 'react-query'
import {addToCart, deleteCart, fetchUser, logOutUser, requestPassResetCode, resetPasswords, signInUser, signUpUser, updateBasicInfo} from '../actions/UserAction'
import {customer_key, user_keys} from '../QueryKeys/User_keys'
import tokenUpdater from '../Util/updateAuthToken';


export const useCurrentUser = (onSuccess, onError, enabled) => {
    return useQuery(user_keys.current_user, fetchUser, {
      onSuccess: (data) => (onSuccess ? onSuccess(data) : {}),
      onError: (error) => (onError ? onError(error) : {}),
      enabled: enabled,
      staleTime: 120000,
      notifyOnChangeProps: "tracked",
      isDataEqual: (oldData, newData) =>
        JSON.stringify(oldData) === JSON.stringify(newData),
    });
}

export const usePrefetCurrentUser = async () => {
  const queryClient = useQueryClient();
  await queryClient.prefetchQuery(user_keys.current_user, fetchUser, {
    staleTime: 120000,
  });
};

export const useSignUp = (onSuccess, onError) => {
  return useMutation(user_keys.signup, signUpUser, {
    onSuccess: (data) => (onSuccess ? onSuccess(data) : {}),
    onError: (error) => (onError ? onError(error) : {}),
  });
};

export const useLogIn = (onSuccess, onError) => {
    return useMutation(user_keys.login, signInUser, {
      onSuccess: (data) => (onSuccess ? onSuccess(data) : {}),
      onError: (error) => (onError ? onError(error) : {}),
    });
}

export const useLogOut = () => {
    const queryClient = useQueryClient();
    return useMutation(user_keys.logout, logOutUser, {
      onSuccess:(data) => {
        console.log('logged out')
        queryClient.removeQueries(user_keys.current_user, {exact: true});
      },
    })
}

export const useUpdateToken = () => {
  return useMutation(user_keys.update_token, tokenUpdater.updateToken, {
  });
};

export  const useRequestResetCode = () => {
  return useMutation(user_keys.request_resetcode, requestPassResetCode,)
}

export const useResetPasswords = () => {
  return useMutation(user_keys.resetpass, resetPasswords,)
}

export const useUpdateBasicInfos = () => {
    const queryClient = useQueryClient();
    return useMutation(updateBasicInfo, {
       /* onSuccess: (data) => {
        queryClient.removeQueries(user_keys.current_user, { exact: true });
      },  */
    });
}
