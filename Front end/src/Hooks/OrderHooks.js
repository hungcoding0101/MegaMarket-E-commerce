import { useMutation, useQueryClient } from "react-query";
import { cancelOrder, placeOrder } from "../actions/OrderActions";
import { user_keys } from "../QueryKeys/User_keys";


export const usePlaceOrder = () => {
    const queryClient = useQueryClient();
    return useMutation(placeOrder, {
        onSuccess: (data) => {
            queryClient.invalidateQueries(user_keys.current_user);
        },
        
        
    })
}

export const useCancelOrder = () => {
  const queryClient = useQueryClient();
  return useMutation(cancelOrder, {
    onSuccess: () => queryClient.invalidateQueries(user_keys.current_user),
  })
}