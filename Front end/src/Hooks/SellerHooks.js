import { useMutation, useQuery, useQueryClient } from "react-query";
import { getOwnedProducts, updateOrders, uploadProduct } from "../actions/SellerActions";
import { seller_keys, user_keys } from "../QueryKeys/User_keys";


export const useUploadProduct = () => {
    const queryClient = useQueryClient();
    return useMutation(uploadProduct, {
        onSuccess: () => {
            queryClient.invalidateQueries(user_keys.current_user);
            queryClient.invalidateQueries(seller_keys.owned_products);
        },
    })
}

export const useOwnedProducts = (onSuccess, onError, enabled) => {
  return useQuery(seller_keys.owned_products, getOwnedProducts, {
    onError: (error) => (onError ? onError(error) : {}),
    onSuccess: (data) => (onSuccess ? onSuccess(data) : {}),
    enabled: enabled,
    staleTime: 120000,
    notifyOnChangeProps: "tracked",
    isDataEqual: (oldData, newData) =>
        JSON.stringify(oldData) === JSON.stringify(newData),
  });
};

export const usePrefetchOwnedProducts = async () => {
  const queryClient = useQueryClient();
  await queryClient.prefetchQuery(
    seller_keys.owned_products,
    getOwnedProducts,
    {
      staleTime: 120000,
    }
  );
}

export const useUpdateOrder = (onSuccess, onError, enabled) => {
  const queryClient = useQueryClient();
  return useMutation(updateOrders, {
    onSuccess: () => {
      queryClient.invalidateQueries(user_keys.current_user);
      queryClient.invalidateQueries(seller_keys.owned_products);
    },
  });
};