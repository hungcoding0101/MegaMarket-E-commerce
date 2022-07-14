import React, { useState } from 'react';
import { BrowserRouter, Route, Switch, Link, Redirect } from "react-router-dom";
import HomeScreen from './Screens/HomeScreen';
import ProductScreen from './Screens/ProductScreen';
import SignInScreen from './Screens/SignInScreen';
import SignUpScreen from './Screens/SignUpScreen';
import CartScreen from "./Screens/CartScreen";
import CustomerScreen from './Screens/CustomerScreen';
import  SellerHomeScreen  from "./Screens/SellerHomeScreen";
import {
  ShoppingCartOutlined,
  MenuUnfoldOutlined,
  CloseCircleOutlined,
} from "@ant-design/icons";
import { Button, Badge, Result, Row, Col, BackTop, Drawer } from 'antd';
import "antd/dist/antd.css";
import ProtectedRoute from './components/ProtectedRoute';
import { ReactQueryDevtoolsPanel } from "react-query/devtools";
import { useCurrentUser, useLogOut } from './Hooks/UserHooks';
import ProductListScreen from './Screens/ProductListScreen';
import ProductSearchBar from './components/ProductSearchBar';
import SideMenu from './components/SideMenu';
import axios from 'axios';
import { useCookies } from 'react-cookie';



export const appContext = React.createContext();

function App(props) {
  
  const [cookies] = useCookies(["XSRF-TOKEN"]);
  axios.defaults.headers.post["X-XSRF-TOKEN"] = cookies["XSRF-TOKEN"];
  axios.defaults.headers.put["X-XSRF-TOKEN"] = cookies["XSRF-TOKEN"];
  axios.defaults.headers.patch["X-XSRF-TOKEN"] = cookies["XSRF-TOKEN"];
  axios.defaults.headers.delete["X-XSRF-TOKEN"] = cookies["XSRF-TOKEN"];

  console.log('APP CALLED')

  const user = useCurrentUser(
    null,
    null,
    true
  );

  const logOut = useLogOut();

  const [signInVisible, setSignInVisible] = useState(false);
  const [sideMenuVisible, setSideMenuVisible] = useState(false);

  const redirectToSeller = () => {return(<Redirect to="/seller/uploadProduct"></Redirect>)}

      return (
        /*REMEMBER: ONLY USE "BrowserRouter" once, at the root Component of your app.
            Other nested "Route" only need to be inside "Switch", no more "BrowserRouter"*/
        <appContext.Provider
          value={{
            logOut: logOut,
            user: user,
            setSignInVisible: setSignInVisible,
          }}
        >
          <BrowserRouter>
            <header>
              <Row
                justify="space-between"
                align="middle"
                style={{ paddingTop: 10, paddingBottom: 10 }}
              >
                <Col
                  xs={4}
                  sm={4}
                  md={2}
                  lg={2}
                  xl={2}
                  xxl={2}
                  style={{ paddingLeft: 5 }}
                >
                  <Button
                    type="primary"
                    onClick={() => setSideMenuVisible(true)}
                  >
                    <MenuUnfoldOutlined />
                  </Button>
                </Col>
                <Col xs={0} sm={0} md={4} lg={4} xl={4} xxl={4}>
                  <div>
                    <Link className="brand" to={"/"}>
                      MegaMarket
                    </Link>
                  </div>
                </Col>

                <Col xs={12} sm={12} md={15} lg={15} xl={15} xxl={15}>
                  {user?.data?.data.role !== "ROLE_SELLER" && (
                    <ProductSearchBar></ProductSearchBar>
                  )}
                </Col>

                <Col xs={8} sm={8} md={3} lg={3} xl={3} xxl={3}>
                  {user.isSuccess && user?.data?.data != null && (
                    <Link to={"/cart"}>
                      {user?.data?.data.role === "ROLE_CUSTOMER" ? (
                        <Badge
                          count={user.data?.data.cart.length}
                          offset={[3, 1]}
                          style={{ marginRight: "1rem" }}
                          size="default"
                        >
                          <Button type="primary" style={{ paddingLeft: 5 }}>
                            <ShoppingCartOutlined />
                            Cart
                          </Button>
                        </Badge>
                      ) : null}
                    </Link>
                  )}
                </Col>
              </Row>
            </header>

            <main style={{ padding: 0 }}>
              <Switch>
                <Route
                  path="/product/list/:searchType"
                  component={ProductListScreen}
                ></Route>
                <Route
                  path="/product/single/:id"
                  component={ProductScreen}
                ></Route>
                <Route
                  path="/signin"
                  render={(props) => (
                    <SignInScreen
                      redirectLink={window.location.href}
                    ></SignInScreen>
                  )}
                ></Route>
                <Route path="/signup" component={SignUpScreen}></Route>
                <ProtectedRoute path="/customer" component={CustomerScreen} />
                <Route path="/seller" component={SellerHomeScreen}></Route>
                <ProtectedRoute path="/cart" component={CartScreen} />
                <Route
                  path="/"
                  exact
                  component={
                    !user?.isSuccess ||
                    user?.data?.data == null ||
                    user.data.data.role === "ROLE_CUSTOMER"
                      ? HomeScreen
                      : redirectToSeller
                  }
                ></Route>
                <Route path="*">
                  <Result
                    status="404"
                    title="404"
                    subTitle="Sorry, the page you visited does not exist."
                    extra={
                      <Link to={"/"}>
                        <Button type="primary">Back Home</Button>
                      </Link>
                    }
                  />
                </Route>
              </Switch>
              <BackTop duration={100}></BackTop>
              <SignInScreen
                visible={signInVisible}
                setVisible={setSignInVisible}
                onCancel={() => setSignInVisible(false)}
              ></SignInScreen>
              <Drawer
                visible={sideMenuVisible}
                title={<div style={{ marginLeft: 30 }}>Menu</div>}
                drawerStyle={{ maxWidth: 380 }}
                width={280}
                placement="left"
                headerStyle={{ padding: 5 }}
                closable={false}
                onClose={() => setSideMenuVisible(false)}
                bodyStyle={{ padding: 0, fontWeight: "bold" }}
                extra={
                  <Button
                    onClick={() => setSideMenuVisible(false)}
                    type="primary"
                    shape="circle"
                    size="large"
                  >
                    <CloseCircleOutlined></CloseCircleOutlined>
                  </Button>
                }
              >
                <SideMenu
                  setSignInVisible={setSignInVisible}
                  setSideMenuVisible={setSideMenuVisible}
                  logOut={logOut}
                ></SideMenu>
              </Drawer>
            </main>
            <footer className="row center">All right reserved</footer>
          </BrowserRouter>
          {/* <ReactQueryDevtoolsPanel
            style={{ width: "100%" }}
            initialIsOpen={false}
            position="top-right"
          /> */}
        </appContext.Provider>
      );
}

export default App;