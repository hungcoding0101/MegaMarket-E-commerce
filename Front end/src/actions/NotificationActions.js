import axios from "axios"

export const updateNotificationStatus = async (ids) => {
    return await axios.patch("/notification/updateStatus", ids, {
      headers: {
        contain_token: "yes",
      },
    });
}