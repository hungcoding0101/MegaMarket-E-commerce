import { Skeleton } from 'antd';
import React, { useContext } from 'react'
import { Route, Redirect } from "react-router-dom";
import { appContext } from '../App';
import { useCurrentUser } from '../Hooks/UserHooks';


export default function ProtectedRoute({component: Component, ...rest}) {
   const user = useCurrentUser(null, null, true);

   console.log(`HERE: REST: ${JSON.stringify(rest)}`);

  return (
    <Skeleton active={true} loading={user.isLoading}>
      <Route
        {...rest}
        render={(props) =>
          user.isSuccess && user.data?.data != null ? (
            <Component {...props}></Component>
          ) : (
            <Redirect
              to={{
                pathname: "/",
                state: { ...rest },
              }}
            ></Redirect>
          )
        }
      />
    </Skeleton>
  );
}

