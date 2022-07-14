import { applyMiddleware, combineReducers, compose, createStore } from "redux";
import thunk from "redux-thunk";
import storage from 'redux-persist/lib/storage';
import  storageSession from "redux-persist/lib/storage/session";
import {persistReducer} from 'redux-persist';
import { producListReducer, productUploadReducer } from "./reducers/ProductReducer";
import {productCategoryTreeReducer} from './reducers/ProductCategoryReducer'
import {
  userRememberReducer,
  userSignInReducer,
  userSignUpReducer,
  userSignInReducer_permanent,
} from "./reducers/UserReducer";

    const composeEnhancer =
    (window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ &&
        window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__({
        trace: true,
        traceLimit: 25,
        })) ||
    compose;

    const initialState = {};

    const rootPersistConfig = {
      key: "root",
      storage: storageSession,
      blacklist: ["permanentAuth", "rememberMe_permanent"],
    };

    const permanentAuthConfig = {
      key: "permanentAuth",
      storage,
    };

    const rememberMeConfig = {
      key: "rememberMe_permanent",
      storage,
    };



    const rootReducer = combineReducers({
      productList: producListReducer,
      productCategories: productCategoryTreeReducer,
      uploadProductResult: productUploadReducer,
      signInResult: userSignInReducer,
      signInResult_permanent: persistReducer(
        permanentAuthConfig,
        userSignInReducer_permanent
      ),
      rememberMe: persistReducer(rememberMeConfig, userRememberReducer),
      signUpResult: userSignUpReducer,
    });

    const persistedRootReducer = persistReducer(rootPersistConfig, rootReducer);

    const store = createStore(
      persistedRootReducer,
      initialState,
      composeEnhancer(applyMiddleware(thunk))
    );

export default store;