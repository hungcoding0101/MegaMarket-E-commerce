import { useMutation, useQuery, useQueryClient } from "react-query";
import { updateNotificationStatus } from "../actions/NotificationActions";
import { user_keys } from "../QueryKeys/User_keys";

export const useUpdateNotification = () => {
    const queryClient = useQueryClient();
    return useMutation(updateNotificationStatus, {
        onSuccess: () => queryClient.invalidateQueries(user_keys.current_user),
    })
}